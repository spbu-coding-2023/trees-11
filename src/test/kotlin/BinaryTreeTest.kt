import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BinaryTreeTest {
    private lateinit var tree: BinaryTree<Int, String>

    @BeforeEach
    fun setup () {
        tree = BinaryTree<Int, String>()
    }

    @Test
    fun `value should be added to empty tree`() {
        val expectedResult = "A"
        val key = 1

        tree.add(key, expectedResult)

        assert(tree[key] contentEquals expectedResult)
    }

    @Test
    fun `root shouldn't be null after adding element to empty tree`() {
        val expectedResult = "A"
        val key = 1

        tree.add(key, expectedResult)

        assert(tree.root != null)
    }
    @Test
    fun `value should be added to not empty tree`() {
        val expectedResult = "A"
        val key = 1

        tree.add(0, "")
        tree.add(key, expectedResult)

        assert(tree[key] contentEquals expectedResult)
    }

    @Test
    fun `Value should be added after set if it not in tree`() {
        val expectedResult = "A"
        val key = 1

        tree[key] = expectedResult

        assert(tree[key] contentEquals expectedResult)
    }

    @Test
    fun `Value should be changed after set if it in tree`() {
        val firstValue = "B"
        val expectedResult = "A"
        val key = 1
        tree[key] = firstValue
        tree[key] = expectedResult

        assert(tree[key] contentEquals expectedResult)
    }

    @Test
    fun `Get should return value`() {
        val key = 1
        val expectedResult = "A"
        tree[key] = expectedResult
        assert(tree[key] contentEquals expectedResult)
    }
}