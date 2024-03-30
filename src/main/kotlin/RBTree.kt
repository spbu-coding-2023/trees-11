class RBTree<K : Comparable<K>, V : Any>(root: Node.RBNode<K, V>? = null) : Tree<K, V, Node.RBNode<K, V>>(root) {

    override fun add(key: K, value: V) {
        val node: Node<K, V, Node.RBNode<K, V>> = Node.RBNode(key, value)
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

        if (node.color == Node.RBNode.Color.RED) {
            if (node.left == null && node.right == null) {
                deleteRedNodeWithNoChildren(node)
            } else if (node.left != null && node.right != null) {
                deleteNodeWithTwoChildren(node)
            }
        } else {
            if (node.left == null && node.right == null) {
                deleteBlackNodeWithNoChildren(node)
            } else if (node.left != null && node.right != null) {
                deleteNodeWithTwoChildren(node)
            } else {
                deleteBlackNodeWithOneChild(node)
            }
        }
    }

    private fun deleteRedNodeWithNoChildren(node: Node.RBNode<K, V>) {
        if (node == node.parent?.left) {
            node.parent?.left = null
        } else {
            node.parent?.right = null
        }
    }

    private fun deleteNodeWithTwoChildren(node: Node.RBNode<K, V>) {
        val successor = (node.right as Node.RBNode<K, V>).min()
        val tmpValue = node.value
        val tmpKey = node.key
        node.key = successor.key
        node.value = successor.value
        successor.key = tmpKey
        successor.value = tmpValue
        if (successor.color == Node.RBNode.Color.RED) {
            if (successor.left != null && successor.right != null) {
                deleteNodeWithTwoChildren(successor)
            } else {
                if (successor.left != null || successor.right != null) {
                    throw IllegalStateException("Successor should have two children or None")
                }
                deleteRedNodeWithNoChildren(successor)
            }
        } else {
            if (successor.left == null && successor.right == null) {
                deleteBlackNodeWithNoChildren(successor)
            } else if (successor.left != null && successor.right != null) {
                deleteNodeWithTwoChildren(successor)
            } else {
                deleteBlackNodeWithOneChild(successor)
            }
        }

//        if ((successor.parent as Node.RBNode<K, V>).color == Node.RBNode.Color.RED) {
//            (successor.parent as Node.RBNode<K, V>).leftRotate()
//        }
    }

    private fun deleteBlackNodeWithOneChild(node: Node.RBNode<K, V>) {
        if (node.parent == null) {
            root = node.left ?: node.right
            (root as Node.RBNode<K, V>).color = Node.RBNode.Color.BLACK
            return
        }
        val child = node.left as Node.RBNode<K, V>? ?: node.right as Node.RBNode<K, V>
        val tmpValue = node.value
        val tmpKey = node.key
        node.key = child.key
        node.value = child.value
        child.key = tmpKey
        child.value = tmpValue

        if (child.color == Node.RBNode.Color.BLACK) {
            deleteBlackNodeWithNoChildren(child)
        } else {
            deleteRedNodeWithNoChildren(child)
        }
    }

    private fun deleteBlackNodeWithNoChildren(node: Node.RBNode<K, V>) {
        if (node.parent == null) {
            // If the node is the root, set the root to null
            root = null
        } else {
            var fixNode: Node.RBNode<K, V>? = null
            var sibling =
                if (node == node.parent!!.left) node.parent!!.right as Node.RBNode<K, V>? else node.parent!!.left as Node.RBNode<K, V>?
            if (sibling?.color == Node.RBNode.Color.RED) {
                // If the sibling is red, recolor and perform rotations
                sibling.color = Node.RBNode.Color.BLACK
                (node.parent as Node.RBNode<K, V>).color = Node.RBNode.Color.RED
                if (node == node.parent!!.left) {
                    (node.parent as Node.RBNode<K, V>).leftRotate()
                } else {
                    (node.parent as Node.RBNode<K, V>).rightRotate()
                }
                // Update sibling after rotation
                fixNode =
                    if (node == node.parent!!.left) node.parent!!.right as Node.RBNode<K, V>?
                    else node.parent!!.left as Node.RBNode<K, V>?
            } else {
                // If the sibling is black
                if (((sibling?.left as Node.RBNode<K, V>?)?.color == Node.RBNode.Color.BLACK || sibling?.left == null)
                    && ((sibling?.right as Node.RBNode<K, V>?)?.color == Node.RBNode.Color.BLACK || sibling?.right == null)
                ) {
                    // If both children of sibling are black, recolor sibling and move fixNode to parent
                    sibling?.color = Node.RBNode.Color.RED
                    fixNode = node.parent as Node.RBNode<K, V>?
                } else {
                    if (node.parent != null) {
                        val parentNode = node.parent!! as Node.RBNode<K, V>
                        val isLeftChild = node == parentNode.left

                        if ((isLeftChild && (sibling.right == null || (sibling.right as Node.RBNode<K, V>?)?.color == Node.RBNode.Color.BLACK))
                            || (!isLeftChild && (sibling.left == null || (sibling.left as Node.RBNode<K, V>?)?.color ==
                                    Node.RBNode.Color.BLACK))
                        ) {
                            // If node is left child and sibling's right child is black or null,
                            // or if node is right child and sibling's left child is black or null,
                            // perform rotation and recoloring
                            val siblingChild = if (isLeftChild) sibling.left else sibling.right
                            (siblingChild as Node.RBNode<K, V>?)?.color = Node.RBNode.Color.BLACK
                            sibling.color = Node.RBNode.Color.BLACK
                            if (isLeftChild) sibling.rightRotate() else sibling.leftRotate()
                            // Update sibling after rotation
                            fixNode =
                                if (isLeftChild) parentNode.right as Node.RBNode<K, V> else parentNode.left as Node.RBNode<K, V>
                        } else if ((isLeftChild && ((sibling.right as Node.RBNode<K, V>?)?.color == Node.RBNode.Color.RED))
                            || (!isLeftChild && ((sibling.left as Node.RBNode<K, V>?)?.color == Node.RBNode.Color.RED))) {
                            // If node is left child and sibling's right child is red,
                            // or if node is right child and sibling's left child is red,
                            // perform rotation and additional checks for recoloring
                            if (isLeftChild) parentNode.leftRotate() else parentNode.rightRotate()
                            if ((isLeftChild && (sibling.right as Node.RBNode<K, V>?)?.color == Node.RBNode.Color.RED
                                        && (sibling.left as Node.RBNode<K, V>?)?.color == Node.RBNode.Color.BLACK
                                        && (sibling.right as Node.RBNode<K, V>?)?.right == null && sibling.right?.left == null)
                                || (!isLeftChild && (sibling.left as Node.RBNode<K, V>?)?.color == Node.RBNode.Color.RED
                                        && (sibling.right as Node.RBNode<K, V>?)?.color == Node.RBNode.Color.BLACK
                                        && sibling.left?.left == null && sibling.left?.right == null)) {
                                val siblingChild = if (isLeftChild) sibling.right else sibling.left
                                (siblingChild as Node.RBNode<K, V>?)?.color = Node.RBNode.Color.BLACK
                            }
                        }
                    }
                    // sometimes it helps to balance the tree, else it does nothing
                    balanceRBTree(node)
                    // Perform additional fix-up operations if needed
                    if (fixNode != null) {
                        if (fixNode == fixNode.parent!!.left) {
                            (fixNode.parent as Node.RBNode<K, V>).leftRotate()
                        } else {
                            (fixNode.parent as Node.RBNode<K, V>).rightRotate()
                        }
                        fixNode.color = (fixNode.parent as Node.RBNode<K, V>).color
                        (fixNode.parent as Node.RBNode<K, V>).color = Node.RBNode.Color.BLACK
                    }
                }
            }
            // Transplant node with null and fix deletion
            transplant(node, null)
            if (fixNode != null) {
                fixDeletion(fixNode)
            }
        }
    }

    private fun transplant(u: Node.RBNode<K, V>?, v: Node.RBNode<K, V>?) {
        if (u?.parent == null) {
            // If u is the root, set v as the new root
            root = v
        } else if (u == u.parent?.left) {
            // If u is the left child of its parent, set v as the new left child
            u.parent?.left = v
        } else {
            // Otherwise, u is the right child of its parent, set v as the new right child
            u.parent?.right = v
        }
        // Update v's parent to be u's parent
        v?.parent = u?.parent
    }

    private fun fixDeletion(node: Node.RBNode<K, V>?) {
        var fixNode = node

        while (fixNode != null && fixNode != root && fixNode.color == Node.RBNode.Color.BLACK) {
            val parentNode = fixNode.parent as Node.RBNode<K, V>? ?: break
            val isLeftChild = fixNode == parentNode.left
            var sibling =
                if (isLeftChild) parentNode.right as Node.RBNode<K, V>? else parentNode.left as Node.RBNode<K, V>?

            if (sibling == null) {
                // No sibling, continue fixup from parent
                fixNode.color = Node.RBNode.Color.RED

            }
            if (fixNode == parentNode.left) {

                if (sibling?.color == Node.RBNode.Color.RED) {
                    // Case 1: Sibling is red
                    sibling.color = Node.RBNode.Color.BLACK
                    parentNode.color = Node.RBNode.Color.RED
                    parentNode.leftRotate()
                    sibling = parentNode.right as Node.RBNode<K, V>?
                }

                val siblingLeft = sibling?.left as Node.RBNode<K, V>?
                val siblingRight = sibling?.right as Node.RBNode<K, V>?
                if ((siblingLeft?.color == Node.RBNode.Color.BLACK ||
                            siblingLeft == null) && (siblingRight?.color == Node.RBNode.Color.BLACK || siblingRight == null)) {
                    // Case 2: Both children of sibling are black
                    sibling?.color = Node.RBNode.Color.RED
                    fixNode = parentNode
                } else {
                    if ((siblingRight?.color == Node.RBNode.Color.BLACK || siblingRight == null)) {
                        // Case 3: Left child of sibling is red
                        siblingLeft?.color = Node.RBNode.Color.BLACK
                        sibling?.color = Node.RBNode.Color.RED
                        sibling?.rightRotate()
                        sibling = parentNode.right as Node.RBNode<K, V>?
                    }

                    // Case 4: Right child of sibling is red
                    sibling?.color = parentNode.color
                    parentNode.color = Node.RBNode.Color.BLACK
                    siblingRight?.color = Node.RBNode.Color.BLACK
                    parentNode.leftRotate()
                    fixNode = root as Node.RBNode<K, V>?
                }
            } else {

                if (sibling?.color == Node.RBNode.Color.RED) {
                    // Case 1: Sibling is red
                    sibling.color = Node.RBNode.Color.BLACK
                    parentNode.color = Node.RBNode.Color.RED
                    parentNode.rightRotate()
                    sibling = parentNode.left as Node.RBNode<K, V>?
                }

                val siblingLeft = sibling?.left as Node.RBNode<K, V>?
                val siblingRight = sibling?.right as Node.RBNode<K, V>?
                if ((siblingRight?.color == Node.RBNode.Color.BLACK || siblingRight == null)
                    && (siblingLeft?.color == Node.RBNode.Color.BLACK || siblingLeft == null)) {
                    // Case 2: Both children of sibling are black
                    sibling?.color = Node.RBNode.Color.RED
                    fixNode = parentNode
                } else {
                    if ((siblingLeft?.color == Node.RBNode.Color.BLACK || siblingLeft == null)) {
                        // Case 3: Right child of sibling is red
                        siblingRight?.color = Node.RBNode.Color.BLACK
                        sibling?.color = Node.RBNode.Color.RED
                        sibling?.leftRotate()
                        sibling = parentNode.left as Node.RBNode<K, V>?
                    }

                    // Case 4: Left child of sibling is red
                    sibling?.color = parentNode.color
                    parentNode.color = Node.RBNode.Color.BLACK
                    siblingLeft?.color = Node.RBNode.Color.BLACK
                    parentNode.rightRotate()
                    fixNode = root as Node.RBNode<K, V>?
                }
            }
        }

        fixNode?.color = Node.RBNode.Color.BLACK
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