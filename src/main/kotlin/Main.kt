
fun main() {
    val binaryTree = BinaryTree<Int, String>()
    val keys = arrayOf(3, 2, 1, 0, 4, 5)

    keys.forEach { binaryTree.add(it, it.toString()) }

    // Стандартно iterateDFS работает с mode=Tree.ModeDFS.PREORDER
    binaryTree.iterateDFS().forEach { print(it.key.toString() + " ") }
    println()
    binaryTree.iterateDFS(mode=Tree.ModeDFS.INORDER).forEach { print(it.key.toString() + " ") }
    println()
    binaryTree.iterateDFS(mode=Tree.ModeDFS.POSTORDER).forEach { print(it.key.toString() + " ") }
}