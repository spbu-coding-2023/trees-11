import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AVLNodeTest {
    private lateinit var tree: AVLTree<Int, String>

    @BeforeEach
    fun setup() {
        tree = AVLTree()
    }

    // max and min tests
    @Test
    fun `max should return if empty`() {
        assert(tree.max()?.key == null)
    }

    @Test
    fun `min should return if empty`() {
        assert(tree.min()?.key == null)
    }

    @Test
    fun `max should return max key`() {
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

        assert(tree.max()?.key == 10)
    }

    @Test
    fun `min should return min key`() {
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

        assert(tree.min()?.key == 1)
    }
}
