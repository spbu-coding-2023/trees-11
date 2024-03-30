class BinaryTree<K: Comparable<K>, V: Any>(root: Node.BinaryNode<K, V>? = null): Tree<K, V, Node.BinaryNode<K, V>>(root)

// Not sure, that this file is needed, because BinaryTree is just Tree.