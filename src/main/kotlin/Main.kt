
fun main() {
    var tree = BinaryTree<Int, String>()

    tree.add(102, "")
    tree.add(101, "")
    tree.add(104, "")
    tree.add(103, "")
    tree.add(105, "")

    println(tree.toStringBeautifulWidth())

    for (i in tree) {
        println(i)
        tree.delete(i.key)
        println(tree.toStringBeautifulWidth())

    }
}