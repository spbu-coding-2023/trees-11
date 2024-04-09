import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ByKeyIteratorTest {
    private lateinit var tree: BinaryTree<Int, String>

    @BeforeEach
    fun setup() {
        tree = BinaryTree<Int, String>()
    }

    @Test
    fun `BFS should work correctly empty tree`() {
        var counter = 0
        for (i in tree) counter++
        assert(counter == 0)
    }

    @Test
    fun `DFS should work correctly simple tree sample`() {
        val keys = arrayOf(2, 1, 3, 0, 4)
        val result = mutableListOf<Int>()
        val expectedResult = arrayOf(0, 1, 2, 3, 4)
        keys.forEach { tree.add(it, it.toString()) }
        for (i in tree) result.add(i.key)

        for (i in keys.indices) assert(result[i] == expectedResult[i])
    }
}
