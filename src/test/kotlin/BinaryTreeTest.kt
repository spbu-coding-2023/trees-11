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
    fun `value should added correctly left-left`() {
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(thirdKey, thirdKey.toString())
        tree.add(secondKey, secondKey.toString())
        tree.add(firstKey, firstKey.toString())

        assert( tree.root?.key == thirdKey &&
                tree.root?.left?.key == secondKey &&
                tree.root?.left?.left?.key == firstKey
        )
    }

    @Test
    fun `value should added correctly left-right`() {
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(thirdKey, thirdKey.toString())
        tree.add(firstKey, firstKey.toString())
        tree.add(secondKey, secondKey.toString())

        assert( tree.root?.key == thirdKey &&
                tree.root?.left?.key == firstKey &&
                tree.root?.left?.right?.key == secondKey
        )
    }

    @Test
    fun `value should added correctly right-left`() {
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(firstKey, firstKey.toString())
        tree.add(thirdKey, thirdKey.toString())
        tree.add(secondKey, secondKey.toString())

        assert( tree.root?.key == firstKey &&
                tree.root?.right?.key == thirdKey &&
                tree.root?.right?.left?.key == secondKey
        )
    }

    @Test
    fun `value should added correctly right-right`() {
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(firstKey, firstKey.toString())
        tree.add(secondKey, secondKey.toString())
        tree.add(thirdKey, thirdKey.toString())

        assert( tree.root?.key == firstKey &&
                tree.root?.right?.key == secondKey &&
                tree.root?.right?.right?.key == thirdKey
        )
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

    @Test
    fun `Get should return value left-left`() {
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(thirdKey, thirdKey.toString())
        tree.add(secondKey, secondKey.toString())
        tree.add(firstKey, firstKey.toString())

        assert(tree[firstKey] == firstKey.toString())
    }

    @Test
    fun `Get should return value left-right`() {
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(thirdKey, thirdKey.toString())
        tree.add(firstKey, firstKey.toString())
        tree.add(secondKey, secondKey.toString())

        assert( tree[secondKey] == secondKey.toString() )
    }

    @Test
    fun `Get should return value right-left`() {
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(firstKey, firstKey.toString())
        tree.add(thirdKey, thirdKey.toString())
        tree.add(secondKey, secondKey.toString())

        assert(tree[secondKey] == secondKey.toString())
    }

    @Test
    fun `Get should return value right-right`() {
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(firstKey, firstKey.toString())
        tree.add(secondKey, secondKey.toString())
        tree.add(thirdKey, thirdKey.toString())

        assert(tree[thirdKey] == thirdKey.toString())
    }
    @Test
    fun `GetOrDefault should return value if it exist`() {
        val key = 1
        val expectedResult = "A"

        tree.add(key, expectedResult)
        assert(tree.getOrDefault(key, "B") == "A")
    }

    @Test
    fun `GetOrDefault should return default if it's not exist`() {
        val key = 1
        val fakeKey = 2
        val expectedResult = "A"

        tree.add(key, expectedResult)
        assert(tree.getOrDefault(fakeKey, "B") == "B")
    }

    @Test
    fun `IsKey should return true if key is exist (zig-zag)`() {
        val keys = arrayOf(5, 1, 4, 2, 3)
        keys.forEach { tree.add(it, it.toString()) }

        assert(tree.isKey(3))
    }
    @Test
    fun `IsKey should return false if key isn't exist (zig-zag)`() {
        val keys = arrayOf(6, 1, 5, 2, 4)
        keys.forEach { tree.add(it, it.toString()) }

        assert(!tree.isKey(3))
    }

    @Test
    fun `Min should return null from empty tree`() {
        assert(tree.min() == null)
    }

    @Test
    fun `Min should return min from nonempty tree`() {
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(thirdKey, thirdKey.toString())
        tree.add(secondKey, secondKey.toString())
        tree.add(firstKey, firstKey.toString())

        assert(tree.min()?.key == firstKey)
    }

    @Test
    fun `Max should return null from empty tree`() {
        assert(tree.max() == null)
    }

    @Test
    fun `Max should return min from nonempty tree`() {
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(thirdKey, thirdKey.toString())
        tree.add(secondKey, secondKey.toString())
        tree.add(firstKey, firstKey.toString())

        assert(tree.max()?.key == thirdKey)
    }

    @Test
    fun `getNext should return min of right child if it exist`(){
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(firstKey, firstKey.toString())
        tree.add(secondKey, secondKey.toString())
        tree.add(thirdKey, thirdKey.toString())

        val root = tree.root
        val right = root?.right
        if (right != null) {
            assert(tree.getNext(root) === tree.min(right))
        } else {
            assert(false)
        }
    }

    @Test
    fun `getNext should return parent if right child isn't exist`(){
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(thirdKey, thirdKey.toString())
        tree.add(firstKey, firstKey.toString())
        tree.add(secondKey, secondKey.toString())

        val secondNode = tree.getNode(secondKey)
        val thirdNode = tree.getNode(thirdKey)

        if (secondNode != null) {
            assert(tree.getNext(secondNode) === thirdNode)
        } else {
            assert(false)
        }
    }

    @Test
    fun `getPrev should return max of left child if it exist`(){
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(thirdKey, thirdKey.toString())
        tree.add(secondKey, secondKey.toString())
        tree.add(firstKey, firstKey.toString())

        val root = tree.root
        val left = root?.left
        if (left != null) {
            assert(tree.getPrev(root) === tree.max(left))
        } else {
            assert(false)
        }
    }

    @Test
    fun `getPrev should return parent if left child isn't exist`(){
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(firstKey, firstKey.toString())
        tree.add(thirdKey, thirdKey.toString())
        tree.add(secondKey, secondKey.toString())

        val secondNode = tree.getNode(secondKey)
        val firstNode = tree.getNode(firstKey)

        if (secondNode != null) {
            assert(tree.getPrev(secondNode) === firstNode)
        } else {
            assert(false)
        }
    }

    @Test
    fun `Merge should make other root to this root if this root is null`() {
        val secondTree = BinaryTree<Int, String>()

        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(firstKey, firstKey.toString())
        tree.add(thirdKey, thirdKey.toString())
        tree.add(secondKey, secondKey.toString())

        secondTree.merge(tree)

        assert(secondTree.root === tree.root)
    }

    @Test
    fun `Merge should make other root to this max if this root isn't null`() {
        val secondTree = BinaryTree<Int, String>()
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(firstKey, firstKey.toString())
        tree.add(thirdKey, thirdKey.toString())
        tree.add(secondKey, secondKey.toString())

        secondTree.add(firstKey - 3, firstKey.toString())
        secondTree.add(secondKey - 3, secondKey.toString())
        secondTree.add(thirdKey - 3, thirdKey.toString())

        val max = secondTree.max()
        secondTree.merge(tree)

        assert(max?.right === tree.root)
    }

    @Test
    fun `Split should work correctly`() {
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(firstKey, firstKey.toString())
        tree.add(thirdKey, thirdKey.toString())

        val (small, big) = tree.split(secondKey)

        assert(small.root === tree.getNode(firstKey) && big.root === tree.getNode(thirdKey))
    }

    @Test
    fun `Clone should clone only content not nodes`() {
        val firstKey = -1
        val secondKey = 0
        val thirdKey = 1

        tree.add(firstKey, firstKey.toString())
        tree.add(thirdKey, thirdKey.toString())
        tree.add(secondKey, secondKey.toString())

        val clonedTree = tree.clone()
        var isCorrect = true

        assert(
            tree.getNode(firstKey) == clonedTree.getNode(firstKey)
            && tree.getNode(firstKey) !== clonedTree.getNode(firstKey)
            && tree.getNode(secondKey) == clonedTree.getNode(secondKey)
            && tree.getNode(secondKey) !== clonedTree.getNode(secondKey)
            && tree.getNode(thirdKey) == clonedTree.getNode(thirdKey)
            && tree.getNode(thirdKey) !== clonedTree.getNode(thirdKey)
        )
    }
}