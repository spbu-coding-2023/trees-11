class AVLTree<K : Comparable<K>, V : Any>(root: Node.AVLNode<K, V>? = null) : Tree<K, V, Node.AVLNode<K, V>>(root) {

    override fun add(key: K, value: V){
        val node : Node.AVLNode<K, V> = Node.AVLNode(key, value)
        this.addNode(node)
    }
    override fun addNode(node : Node<K, V, Node.AVLNode<K, V>>){
        super.addNode(node)
        balanceAVLTree(node as Node.AVLNode<K, V>)
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

    override fun delete(key : K){
        val node = this.getNode(key) as Node.AVLNode<K, V>? ?: return
        deleteNode(node, key)
    }

    private fun deleteNode(node : Node.AVLNode<K, V>?, key : K) : Node.AVLNode<K, V>? {
        if (node == null) return node;
        if (key < node.key){
            node.left = deleteNode(node.left as Node.AVLNode<K, V>, key)
        }
        else if (key > node.key){
            node.right = deleteNode(node.right as Node.AVLNode<K, V>, key)
        }
        else{
           if (node.left == null || node.right == null){
               var tmp : Node.AVLNode<K, V>? = null
               tmp = node.left as Node.AVLNode<K, V> ?: node.right as Node.AVLNode<K, V>
               if (tmp == null){
                   tmp = node
                   node = null
               } else {
                   node = tmp
               }
           }
            else {
               val tmp = findMin(node.right as Node.AVLNode<K, V>)
               node.key = tmp.key
               node.right = deleteNode(node.right as Node.AVLNode<K, V>, tmp.key)
            }
        }
        if (node == null) return node
        fixHeight(node)
        val balance = balanceFactor(node)
        if (balance > 1){
            if (balanceFactor(node.left as Node.AVLNode<K, V>) >= 0){
                return rightRotate(node)
            } else {
                node.left = leftRotate(node.left as Node.AVLNode<K, V>)
                return rightRotate(node)
            }
        }
        if (balance < -1){
            if (balanceFactor(node.right as Node.AVLNode<K, V>) <= 0){
                return leftRotate(node)
            } else {
                node.right = rightRotate(node.right as Node.AVLNode<K, V>)
                return leftRotate(node)
            }
        }
        return node
    }

    private fun findMin(node : Node.AVLNode<K, V>) : Node.AVLNode<K, V>{
        return if (node.left != null) findMin(node.left as Node.AVLNode<K, V>) else node
    }

    private fun removeMin(node : Node.AVLNode<K, V>) : Node.AVLNode<K, V>{
        if (node.left == null){
            return node.right as Node.AVLNode<K, V>
        }
        node.left = removeMin(node.left as Node.AVLNode<K, V>)
        return balanceAVLTree(node)
    }

    private fun balanceAVLTree(node : Node.AVLNode<K, V>) : Node.AVLNode<K, V>{
        fixHeight(node)
        if (balanceFactor(node) == 2){
            if (balanceFactor(node.right as Node.AVLNode<K, V>?) < 0){
                node.right = rightRotate(node.right as Node.AVLNode<K, V>);
            }
            return leftRotate(node);
        }
        if (balanceFactor(node) == -2){
            if (balanceFactor(node.left as Node.AVLNode<K, V>?) > 0){
                node.left = leftRotate(node.left as Node.AVLNode<K, V>)
            }
            return rightRotate(node)
        }
        return node;
    }

    private fun fixHeight(node : Node.AVLNode<K, V>?) {
        node?.height = 1 + maxOf(height(node?.left as Node.AVLNode<K, V>?), height(node?.right as Node.AVLNode<K, V>?))
    }

    private fun height(node : Node.AVLNode<K, V>?) : Int{
        return node?.height ?: 0
    }

    private fun balanceFactor(node : Node.AVLNode<K, V>?) : Int{
        if (node == null) return 0;
        return (height(node.right as Node.AVLNode<K, V>?) - height(node.left as Node.AVLNode<K, V>?));
    }

    private fun leftRotate(a : Node.AVLNode<K, V>) : Node.AVLNode<K, V>{
       if (a.height != 2) return a;
       val b : Node.AVLNode<K,V> = a.right as Node.AVLNode<K, V>;
       if (b.height == -1) return a;

       a.right = b.left;
       if (b.left != null) b.left!!.parent = a;

       b.parent = a.parent
       if(a.parent != null){
           if (a.parent!!.left == a){
               a.parent!!.left = b;
           }
           else{
               a.parent!!.right = b;
           }
       }
       else{
           root = b
       }

       b.left = a;
       a.parent = b;

       if (b.height == 1){
           a.height = 0
           b.height = 0
       }
       else{
           a.height = 1
           b.height = -1
       }
       return b;
   }

    private fun rightRotate(b : Node.AVLNode<K, V>) : Node.AVLNode<K, V>{
        if (b.height != -2) return b;
        val a : Node.AVLNode<K,V> = b.left as Node.AVLNode<K, V>;
        if (a.height == -1) return b;

        b.left = a.right;
        if (a.right != null) a.right!!.parent = b;

        a.parent = b.parent
        if(b.parent != null){
            if (b.parent!!.left == b){
                b.parent!!.left = a;
            }
            else{
                b.parent!!.right = a;
            }
        }
        else{
            root = a
        }

        a.right = b;
        b.parent = a;

        if (a.height == -1){
            a.height = 0
            b.height = 0
        }
        else{
            a.height = 1
            b.height = -1
        }
        return a;
    }
}



