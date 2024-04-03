class AVLTree<K : Comparable<K>, V : Any>(root: Node.AVLNode<K, V>? = null) : Tree<K, V, Node.AVLNode<K, V>>(root) {

    override fun add(key: K, value: V) {
        this.root = insert(this.root as Node.AVLNode<K, V>?, key, value)
    }

    private fun height(node: Node.AVLNode<K, V>?): Int {
        if (node == null) return 0

        return node.height
    }

    private fun max(a: Int, b: Int): Int {
        return if ((a > b)) a else b
    }

    private fun getBalance(node: Node.AVLNode<K, V>?): Int {
        if (node == null) return 0

        return height(node.left as Node.AVLNode<K, V>?) - height(node.right as Node.AVLNode<K, V>?)
    }

    private fun insert(node: Node.AVLNode<K, V>?, key: K, value: V): Node.AVLNode<K, V>? {

        if (node == null) return Node.AVLNode(key, value)

        if (key < node.key) node.left = insert(node.left as Node.AVLNode<K, V>?, key, value)
        else if (key > node.key) node.right = insert(node.right as Node.AVLNode<K, V>?, key, value)
        else return node

        node.height = 1 + max(height(node.left as Node.AVLNode<K, V>?), height(node.right as Node.AVLNode<K, V>?))

        val balance = getBalance(node)

        if (balance > 1 && key < node.left!!.key) return rightRotate(node)

        if (balance < -1 && key > node.right!!.key) return leftRotate(node)

        if (balance > 1 && key > node.left!!.key) {
            node.left = leftRotate(node.left as Node.AVLNode<K, V>?)
            return rightRotate(node)
        }

        if (balance < -1 && key < node.right!!.key) {
            node.right = rightRotate(node.right as Node.AVLNode<K, V>?)
            return leftRotate(node)
        }

        return node
    }

    private fun deleteNode(node: Node.AVLNode<K, V>?, key: K): Node.AVLNode<K, V>? {
        var root = node
        if (root == null) return null

        if (key < root.key) root.left = deleteNode(root.left as Node.AVLNode<K, V>?, key)
        else if (key > root.key) root.right = deleteNode(root.right as Node.AVLNode<K, V>?, key)
        else {
            if ((root.left == null) || (root.right == null)) {
                var temp: Node.AVLNode<K, V>? = null
                temp = if (temp === root.left) root.right as Node.AVLNode<K, V>?
                else root.left as Node.AVLNode<K, V>?

                if (temp == null) {
                    temp = root
                    root = null
                } else root = temp

            } else {
                val temp = minValueNode(root.right as Node.AVLNode<K, V>?)

                root.key = temp!!.key

                root.right = deleteNode(root.right as Node.AVLNode<K, V>?, temp.key)
            }
        }

        if (root == null) return root

        (root as Node.AVLNode<K, V>?)?.height =
            max(height(root.left as Node.AVLNode<K, V>?), height(root.right as Node.AVLNode<K, V>?)) + 1

        val balance = getBalance(root)

        if (balance > 1 && getBalance(root.left as Node.AVLNode<K, V>?) >= 0) return rightRotate(root)

        if (balance > 1 && getBalance(root.left as Node.AVLNode<K, V>?) < 0) {
            root.left = leftRotate(root.left as Node.AVLNode<K, V>?)
            return rightRotate(root)
        }

        if (balance < -1 && getBalance(root.right as Node.AVLNode<K, V>?) <= 0) return leftRotate(root)

        if (balance < -1 && getBalance(root.right as Node.AVLNode<K, V>?) > 0) {
            root.right = rightRotate(root.right as Node.AVLNode<K, V>?)
            return leftRotate(root)
        }

        return root
    }

    private fun minValueNode(node: Node.AVLNode<K, V>?): Node.AVLNode<K, V>? {
        var current = node

        /* loop down to find the leftmost leaf */
        while (current!!.left != null) current = current.left as Node.AVLNode<K, V>?

        return current
    }

    private fun rightRotate(node: Node.AVLNode<K, V>?): Node.AVLNode<K, V> {
        val x = node!!.left as Node.AVLNode<K, V>?
        val T2 = x!!.right as Node.AVLNode<K, V>?

        // Perform rotation
        x.right = node
        node.left = T2

        // Update heights
        node.height = max(height(node.left as Node.AVLNode<K, V>?), height(node.right as Node.AVLNode<K, V>?)) + 1
        x.height = max(height(x.left as Node.AVLNode<K, V>?), height(x.right as Node.AVLNode<K, V>?)) + 1

        // Return new root
        return x
    }

    private fun leftRotate(node: Node.AVLNode<K, V>?): Node.AVLNode<K, V>? {
        val y = node!!.right as Node.AVLNode<K, V>?
        val T2 = y!!.left as Node.AVLNode<K, V>?

        y.left = node
        node.right = T2

        node.height = max(height(node.left as Node.AVLNode<K, V>?), height(node.right as Node.AVLNode<K, V>?)) + 1
        y.height = max(height(y.left as Node.AVLNode<K, V>?), height(y.right as Node.AVLNode<K, V>?)) + 1

        return y
    }

    override fun merge(tree: Tree<K, V, Node.AVLNode<K, V>>) {
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

    override fun max(): Node.AVLNode<K, V>? {
        return if (root == null) null else (root as Node.AVLNode<K, V>).max()
    }

    override fun min(): Node.AVLNode<K, V>? {
        return if (root == null) null else (root as Node.AVLNode<K, V>).min()
    }

    override fun delete(key: K) {
        val node = this.getNode(key) as Node.AVLNode<K, V>? ?: return
        deleteNode(node, key)
    }
}