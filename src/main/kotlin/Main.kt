fun main() {
    var tree = BinaryTree<Int, String>()
    tree.add(102, "")
    tree.add(101, "")
    tree.add(104, "")
    tree.add(103, "")
    tree.add(105, "")

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

    for (i in tree) {
        println(i)
        tree.delete(i.key)
        println(tree.toStringBeautifulWidth())

    }
}