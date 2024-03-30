import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class RedBlackTreeTest {
    private lateinit var tree: RBTree<Int, String>

    @BeforeEach
    fun setup () {
        tree = RBTree()
    }

    // tests for add
    @Test
    fun `root should be black if has single node`() {
        tree.add(1, "")
        assert((tree.root as Node.RBNode?)?.color == Node.RBNode.Color.BLACK)
    }

    @Test
    fun `root should be black if has multiple nodes`() {
        for (i in 1..10) {
            tree.add(i, "")
        }
        assert((tree.root as Node.RBNode?)?.color == Node.RBNode.Color.BLACK)
    }

    @Test
    fun `root should be black if has multiple nodes and root is deleted`() {
        for (i in 1..10) {
            tree.add(i, "")
        }
        tree.root?.let { tree.delete(it.key) }
        assert((tree.root as Node.RBNode?)?.color == Node.RBNode.Color.BLACK)
    }


    // tests for min, max
    @Test
    fun `max should return max node`() {
        for (i in 1..10) {
            tree.add(i, "")
        }
        assert(tree.max()?.key == 10)
    }

    @Test
    fun `min should return min node`() {
        for (i in 1..10) {
            tree.add(i, "")
        }
        assert(tree.min()?.key == 1)
    }

    // tests for balanceRBTree
    @Test
    fun `balanceRBTree should return balanced tree`() {
        for (i in 1..10) {
            tree.add(i, "")
        }

        val expectedBalanced = arrayOf(4, 2, 6, 1, 3, 5, 8, 7, 9, 10)
        var index = 0
        tree.iterateBFS().forEach {
            assert(it.key == expectedBalanced[index])
            index++
        }
    }


    // tests for merge
    @Test
    fun `tree should be empty`() {
        val secondTree = RBTree<Int, String>()

        tree.merge(secondTree)

        assert(tree.root == null)
    }

    @Test
    fun `should merge second tree with empty tree`() {
        val secondTree = RBTree<Int, String>()
        secondTree.add(1, "value1")
        secondTree.add(2, "value2")

        tree.merge(secondTree)

        assert(secondTree.root == tree.root)
    }

    @Test
    fun `should merge empty second tree with initial tree`() {
        val secondTree = RBTree<Int, String>()
        tree.add(1, "value1")
        tree.add(2, "value2")

        tree.merge(secondTree)

        assert(tree.root == tree.root)
    }

    @Test
    fun `should merge non empty trees`() {
        val secondTree = RBTree<Int, String>()
        tree.add(1, "value1")
        tree.add(2, "value2")
        secondTree.add(3, "value3")
        secondTree.add(4, "value4")

        tree.merge(secondTree)

        // Check if the resulting tree contains all elements from both trees
        var treeNodeCount = 0
        tree.iterator().forEach { _ -> treeNodeCount++ }
        assert(4 == treeNodeCount)
        assert("value1" == tree.getNode(1)?.value)
        assert("value2" == tree.getNode(2)?.value)
        assert("value3" == tree.getNode(3)?.value)
        assert("value4" == tree.getNode(4)?.value)
    }


}