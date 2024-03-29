import org.junit.jupiter.api.Test

class RBNodeTest {

    @Test
    fun `max should return max value`() {
        val tree = RBTree<Int, String>()
        tree.add(1, "A")
        tree.add(2, "B")
        tree.add(3, "C")
        tree.add(4, "D")
        tree.add(5, "E")
        tree.add(6, "F")
        tree.add(7, "G")
        tree.add(8, "H")
        tree.add(9, "I")
        tree.add(10, "J")

        assert(tree.max()?.value == "J")
    }

    @Test
    fun `min should return min value`() {
        val tree = RBTree<Int, String>()
        tree.add(1, "A")
        tree.add(2, "B")
        tree.add(3, "C")
        tree.add(4, "D")
        tree.add(5, "E")
        tree.add(6, "F")
        tree.add(7, "G")
        tree.add(8, "H")
        tree.add(9, "I")
        tree.add(10, "J")

        assert(tree.min()?.value == "A")
    }

}