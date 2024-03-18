
fun main() {
    var tree = BinaryTree<Int, String>()
    tree.add(1, "B")
    tree.add(0, "A")
    tree.add(2, "C")
    tree.add(-1, "OK")

    tree[0] = "A_BUT_OTHER"

    var secondTree = BinaryTree<Int, String>()
    secondTree[5] = "E"
    secondTree.add(4, "D")
    secondTree.add(6, "F")
    tree.merge(secondTree)

    tree.delete(1)
    tree.delete(13)

    tree.forEach( { println(it)} )

    println(tree.toStringBeautifulWidth())
    println(tree.toStringBeautifulHeight())

}