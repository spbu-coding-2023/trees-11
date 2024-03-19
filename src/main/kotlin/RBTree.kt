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
        val node = this.getNode(key) ?: return
        if (node.left != null && node.right != null) {
            val successor = (node.right as Node.RBNode).min()
            node.key = successor.key
            node.value = successor.value
            this.deleteNode(successor)
        } else {
            this.deleteNode(node as Node.RBNode<K, V>)
        }
    }

    private fun deleteNode(node: Node.RBNode<K, V>) {
        val child = if (node.right != null) node.right else node.left
        if (node.color == Node.RBNode.Color.BLACK) {
            if ((child as Node.RBNode?)?.color == Node.RBNode.Color.RED) {
                (child as Node.RBNode).color = Node.RBNode.Color.BLACK
            } else {
                balanceRBTree(node)
            }
        }
        if (node.parent == null) {
            root = child
        } else if (node == node.parent?.left) {
            node.parent?.left = child
        } else {
            node.parent?.right = child
        }
        child?.parent = node.parent
    }

    override fun max(): Node.RBNode<K, V>? {
        return if (root == null) null else (root as Node.RBNode<K, V>).max()
    }

    override fun min(): Node.RBNode<K, V>? {
        return if (root == null) null else (root as Node.RBNode<K, V>).min()
    }

//    override fun toStringBeautifulWidth(): String {
//        return if (this.root == null) ""
//        else this.newToStringBeautifulWidth(StringBuilder(), true, StringBuilder(), this.root!! as Node.RBNode<K, V>)
//            .toString()
//    }

    private fun newToStringBeautifulWidth(
        prefix: StringBuilder, isTail: Boolean, buffer: StringBuilder, current: Node.RBNode<K, V>
    ): StringBuilder {
        if (current.right != null) {
            var newPrefix = StringBuilder()
            newPrefix.append(prefix)
            newPrefix.append(if (isTail) "|   " else "    ")
            this.newToStringBeautifulWidth(newPrefix, false, buffer, current.right as Node.RBNode<K, V>)
        }
        buffer.append(prefix)
        buffer.append(if (isTail) "└── " else "┌── ")
        buffer.append("${current.key}(${current.color})")
        buffer.append("\n")
        if (current.left != null) {
            var newPrefix = StringBuilder()
            newPrefix.append(prefix)
            newPrefix.append(if (!isTail) "|   " else "    ")
            this.newToStringBeautifulWidth(newPrefix, true, buffer, current.left as Node.RBNode<K, V>)
        }

        return buffer
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