
fun main() {
    var tree = BinaryTree<Int, String>()

    for (i in 1..10) tree.add(i * i * (i - 10), "")

    println(tree.toStringBeautifulWidth())
    for (i in tree){
        tree.delete(i.key)
        println(tree.toStringBeautifulWidth())
    }
}