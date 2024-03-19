fun main() {
    var tree = BinaryTree<Int, String>()

    tree.add(1, "B")
    tree.add(0, "A")
    tree.add(2, "C")
    tree.add(-1, "OK")
    tree.add(3, "D")
    tree.add(-2, "E")
    tree.add(4, "F")
    tree.add(-3, "G")

    println(tree.toStringBeautifulWidth())

    println("\n\n")
    println("RB Tree:")

    val rbTree = RBTree<Int, String>()

    val num = 40
    val num2 = 5
    for (i in 0..num) {
        val value = "$i"
        rbTree[i] = value
    }
    println(rbTree.toStringBeautifulWidth())


    val secondRBTree = RBTree<Int, String>()
    for (i in 1..num2) {
        val value = "${num+i}"
        secondRBTree[num+i] = value
    }
    println(secondRBTree.toStringBeautifulWidth())


    println("\n\n")
    println("Merged:")
    rbTree.merge(secondRBTree)
    println(rbTree.toStringBeautifulWidth())

}