import kotlin.math.ceil
import kotlin.math.floor

class RBTree<K : Comparable<K>, V : Any>(root: Node.RBNode<K, V>? = null) : Tree<K, V, Node.RBNode<K, V>>(root) {

    override fun add(key: K, value: V) {
        val node: Node.RBNode<K, V> = Node.RBNode(key, value)
        this.addNode(node)
    }

    override fun addNode(node: Node<K, V, Node.RBNode<K, V>>) {
        super.addNode(node)
        balanceRBTree(node as Node.RBNode<K, V>)
    }

    override fun merge(tree: Tree<K, V, Node.RBNode<K, V>>) {
        if (this.root != null && tree.root != null) {
            require(this.max()!!.key < tree.min()?.key!!) {
                "Merge operation is defined only when attachable tree's keys is always bigger than base tree's keys"
            }
        }
        if (this.root == null) this.root = tree.root
        else {
            tree.forEach {
                this.add(it.key, it.value)
            }
        }
    }

    override fun delete(key: K) {
        val node = this.getNode(key) as Node.RBNode<K, V>? ?: return

        deleteNode(node)
    }

    private fun deleteNode(nodeToDelete: Node.RBNode<K, V>) {
        val replacementNode = BSTreplace(nodeToDelete)

        // Determine if both 'replacementNode' and 'nodeToDelete' are black
        val areBothBlack =
            (replacementNode == null || replacementNode.color == Node.RBNode.Color.BLACK) && (nodeToDelete.color == Node.RBNode.Color.BLACK)

        val parentNode = nodeToDelete.parent

        // Case 1: 'nodeToDelete' has no replacement node
        if (replacementNode == null) {
            if (nodeToDelete == root) {
                root = null
            } else {
                if (areBothBlack) {
                    // Both 'replacementNode' and 'nodeToDelete' are black, so fix double black at 'nodeToDelete'
                    fixDoubleBlack(nodeToDelete)
                } else if (nodeToDelete.sibling() != null) {
                    nodeToDelete.sibling()!!.color = Node.RBNode.Color.RED
                }
                // Delete 'nodeToDelete' from the tree
                if (nodeToDelete.isOnLeft) parentNode!!.left = null
                else parentNode!!.right = null
            }
            return
        }

        // Case 2: 'nodeToDelete' has only one child
        if (nodeToDelete.left == null || nodeToDelete.right == null) {
            if (nodeToDelete == root) {
                // 'nodeToDelete' is the root, so assign the value of 'replacementNode' to 'nodeToDelete' and delete 'replacementNode'
                nodeToDelete.key = replacementNode.key
                nodeToDelete.right = null
                nodeToDelete.left = nodeToDelete.right
                // Delete 'replacementNode'
            } else {
                // Detach 'nodeToDelete' from the tree and move 'replacementNode' up
                if (nodeToDelete.isOnLeft) parentNode!!.left = replacementNode
                else parentNode!!.right = replacementNode

                replacementNode.parent = parentNode

                if (areBothBlack) {
                    // Both 'replacementNode' and 'nodeToDelete' are black, so fix double black at 'replacementNode'
                    fixDoubleBlack(replacementNode)
                } else {
                    // Either 'replacementNode' or 'nodeToDelete' is red, so color 'replacementNode' black
                    replacementNode.color = Node.RBNode.Color.BLACK
                }
            }
            return
        }

        // Case 3: 'nodeToDelete' has two children, swap values with successor and recurse
        swapValues(replacementNode, nodeToDelete)
        deleteNode(replacementNode)
    }

    private fun swapValues(nodeA: Node.RBNode<K, V>, nodeB: Node.RBNode<K, V>) {
        val tempKey = nodeA.key
        val tempValue = nodeA.value

        nodeA.key = nodeB.key
        nodeA.value = nodeB.value

        nodeB.key = tempKey
        nodeB.value = tempValue
    }

    private fun fixDoubleBlack(doubleBlackNode: Node.RBNode<K, V>?) {
        if (doubleBlackNode == null || doubleBlackNode == root) return

        // Retrieve the sibling and parent of the double black node
        val sibling = doubleBlackNode.sibling()
        val parent = doubleBlackNode.parent as Node.RBNode<K, V>?

        // If there is no sibling, the double black is pushed up to its parent
        if (sibling == null) fixDoubleBlack(parent)
        else {
            if (parent == null) return
            if (sibling.color == Node.RBNode.Color.RED) {
                // If the sibling is red, perform rotation and color changes
                parent.color = Node.RBNode.Color.RED
                sibling.color = Node.RBNode.Color.BLACK

                if (sibling.isOnLeft) parent.rightRotate() // Right case
                else parent.leftRotate() // Left case

                fixDoubleBlack(doubleBlackNode) // Recursively fix double black
            } else {
                // If the sibling is black
                if (sibling.hasRedChild()) {
                    // If at least one child of the sibling is red
                    val siblingLeft = sibling.left as Node.RBNode<K, V>?
                    val siblingRight = sibling.right as Node.RBNode<K, V>?
                    if (sibling.isOnLeft) {
                        // Left subtree of the sibling
                        if (siblingLeft?.color == Node.RBNode.Color.RED) {
                            // Left-left case
                            siblingLeft.color = sibling.color
                            sibling.color = parent.color
                            parent.rightRotate()
                        } else {
                            // Left-right case
                            siblingRight?.color = parent.color
                            sibling.leftRotate()
                            parent.rightRotate()
                        }
                    } else {
                        // Right subtree of the sibling
                        if (siblingRight?.color == Node.RBNode.Color.RED) {
                            // Right-right case
                            siblingRight.color = sibling.color
                            sibling.color = parent.color
                            parent.leftRotate()
                        } else {
                            // Right-left case
                            siblingLeft?.color = parent.color
                            sibling.rightRotate()
                            parent.leftRotate()
                        }
                    }
                    parent.color = Node.RBNode.Color.BLACK
                } else {
                    // If both children of the sibling are black
                    sibling.color = Node.RBNode.Color.RED
                    if (parent.color == Node.RBNode.Color.BLACK) fixDoubleBlack(parent)
                    else parent.color = Node.RBNode.Color.BLACK
                }
            }
        }
    }

    private fun successor(x: Node.RBNode<K, V>): Node.RBNode<K, V> {
        var temp = x
        while (temp.left != null) temp = temp.left as Node.RBNode<K, V>
        return temp
    }

    // find node that replaces a deleted node in BST
    private fun BSTreplace(node: Node.RBNode<K, V>): Node.RBNode<K, V>? {
        // If the node has two children
        if (node.left != null && node.right != null) {
            return successor(node.right as Node.RBNode<K, V>)
        }

        if (node.left == null && node.right == null) {
            return null // There is no replacement
        }

        return if (node.left != null) {
            node.left as Node.RBNode<K, V>?
        } else {
            node.right as Node.RBNode<K, V>?
        }
    }

    override fun max(): Node.RBNode<K, V>? {
        return if (root == null) null else (root as Node.RBNode<K, V>).max()
    }

    override fun min(): Node.RBNode<K, V>? {
        return if (root == null) null else (root as Node.RBNode<K, V>).min()
    }

    fun setColored(value: Boolean) {
        Node.RBNode.colored = value
    }

    private fun balanceRBTree(node: Node.RBNode<K, V>) {
        if (node.parent == null) {
            node.color = Node.RBNode.Color.BLACK
            return
        }

        val parentNode = node.parent as? Node.RBNode<K, V> ?: return

        if (parentNode.color == Node.RBNode.Color.BLACK) {
            return
        }
        val grandparentNode = parentNode.parent as? Node.RBNode<K, V> ?: return

        if (parentNode == grandparentNode.left) {
            val uncle = grandparentNode.right as? Node.RBNode<K, V>
            if (uncle?.color == Node.RBNode.Color.RED) {
                parentNode.color = Node.RBNode.Color.BLACK
                uncle.color = Node.RBNode.Color.BLACK
                grandparentNode.color = Node.RBNode.Color.RED
                balanceRBTree(grandparentNode)
            } else {
                if (node == parentNode.right) {
                    parentNode.leftRotate()
                    balanceRBTree(node.left as Node.RBNode<K, V>)
                } else {
                    grandparentNode.rightRotate()
                    parentNode.color = Node.RBNode.Color.BLACK
                    (parentNode.right as Node.RBNode<K, V>?)?.color = Node.RBNode.Color.RED
                }
            }
        } else {
            val uncle = grandparentNode.left as? Node.RBNode<K, V>
            if (uncle?.color == Node.RBNode.Color.RED) {
                parentNode.color = Node.RBNode.Color.BLACK
                uncle.color = Node.RBNode.Color.BLACK
                grandparentNode.color = Node.RBNode.Color.RED
                balanceRBTree(grandparentNode)
            } else {
                if (node == parentNode.left) {
                    parentNode.rightRotate()
                    balanceRBTree((node.right as Node.RBNode<K, V>))
                } else {
                    grandparentNode.leftRotate()
                    parentNode.color = Node.RBNode.Color.BLACK
                    (parentNode.left as Node.RBNode<K, V>?)?.color = Node.RBNode.Color.RED
                }
            }
        }
    }

    override fun toStringBeautifulHeight(ofSide: Int): String {
        if (this.root == null) return ""
        else {
            val buffer: StringBuilder = StringBuilder()

            val lines: MutableList<MutableList<String?>> = mutableListOf()

            var level: MutableList<Node.RBNode<K, V>?> = mutableListOf()
            var next: MutableList<Node.RBNode<K, V>?> = mutableListOf()

            level.add(this.root as Node.RBNode<K, V>?)

            var nodeNumber = 1
            var widtest = 0

            while (nodeNumber != 0) {
                val line: MutableList<String?> = mutableListOf()

                nodeNumber = 0

                for (node in level) {
                    if (node == null) {
                        line.add(null)

                        next.add(null)
                        next.add(null)
                    } else {
                        val strNode: String = node.toString()
                        line.addLast(strNode)

                        val extra = if (node.color == Node.RBNode.Color.RED) 18 else 0

                        if (strNode.length > widtest + extra) widtest = strNode.length

                        next.add(node.left as Node.RBNode<K, V>?)
                        next.add(node.right as Node.RBNode<K, V>?)

                        if (node.left != null) nodeNumber++
                        if (node.right != null) nodeNumber++
                    }
                }

                widtest += widtest % 2

                lines.add(line)
                val swap = level
                level = next
                next = swap
                next.clear()
            }

            var perpiece: Int = lines[lines.size - 1].size * (widtest + ofSide)

            for (i in 1..perpiece / 2) buffer.append("─")
            buffer.append("┐\n")

            for (i in 0..<lines.size) {
                val line: MutableList<String?> = lines[i]

                val hpw: Int = floor(perpiece / 2f - 1).toInt()

                if (i > 0) {
                    for (j in 0..<line.size) {
                        var c: Char = ' '

                        if (j % 2 == 1) {
                            if (line[j - 1] != null) {
                                c = if (line[j] != null) '┴' else '┘'
                            } else {
                                if (j < line.size && line[j] != null) c = '└'
                            }
                        }
                        buffer.append(c)

                        if (line[j] == null) {
                            repeat(perpiece - 1) {
                                buffer.append(" ")
                            }
                        } else {
                            repeat(hpw) {
                                buffer.append(if (j % 2 == 0) " " else "─")
                            }
                            buffer.append(if (j % 2 == 0) "┌" else "┐")
                            repeat(hpw) {
                                buffer.append(if (j % 2 != 0) " " else "─")
                            }
                        }
                    }
                    buffer.append("\n")
                }
                for (j in 0..<line.size) {
                    var f: String? = line[j]
                    if (f == null) f = ""
                    val extra = if (f.contains("\u001B[31m")) 9 else 0
                    val gap1: Int = ceil(perpiece / 2f - (f.length - extra) / 2f).toInt()
                    val gap2: Int = floor(perpiece / 2f - (f.length - extra) / 2f).toInt()

                    repeat(gap1) {
                        buffer.append(" ")
                    }
                    buffer.append(f)

                    repeat(gap2) {
                        buffer.append(" ")
                    }
                }
                buffer.append("\n")

                perpiece /= 2
            }
            return buffer.toString()
        }
    }

    private fun Node.RBNode<K, V>.leftRotate() {
        val newRoot = this.right
        this.right = newRoot?.left
        if (newRoot?.left != null) {
            newRoot.left!!.parent = this
        }
        newRoot?.parent = this.parent
        if (this.parent == null) {
            root = newRoot
        } else if (this == this.parent?.left) {
            this.parent?.left = newRoot
        } else {
            this.parent?.right = newRoot
        }
        newRoot?.left = this
        this.parent = newRoot
    }

    private fun Node.RBNode<K, V>.rightRotate() {
        val newRoot = this.left
        this.left = newRoot?.right
        if (newRoot?.right != null) {
            newRoot.right!!.parent = this
        }
        newRoot?.parent = this.parent
        if (this.parent == null) {
            root = newRoot
        } else if (this == this.parent?.right) {
            this.parent?.right = newRoot
        } else {
            this.parent?.left = newRoot
        }
        newRoot?.right = this
        this.parent = newRoot
    }

}