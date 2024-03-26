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

        assert(
            tree.getNode(firstKey) == clonedTree.getNode(firstKey)
            && tree.getNode(firstKey) !== clonedTree.getNode(firstKey)
            && tree.getNode(secondKey) == clonedTree.getNode(secondKey)
            && tree.getNode(secondKey) !== clonedTree.getNode(secondKey)
            && tree.getNode(thirdKey) == clonedTree.getNode(thirdKey)
            && tree.getNode(thirdKey) !== clonedTree.getNode(thirdKey)
        )
    }

    @Test
    fun `Delete from empty tree if key isn't in tree should work correctly(do nothing)`(){
        tree.delete(15)
        assert(true)
    }

    @Test
    fun `Delete should work correctly no children root`() {
        val key = 1

        tree.add(key, key.toString())
        tree.delete(key)

        assert(tree.root == null)
    }

    @Test
    fun `Delete should work correctly no children not-root left`() {
        val keyRoot = 0
        val key = -1
        tree.add(keyRoot, keyRoot.toString())
        tree.add(key, key.toString())
        tree.delete(key)

        val root = tree.root
        assert(tree.getNode(keyRoot) === root && root != null && root.left == null)
    }

    @Test
    fun `Delete should work correctly no children not-root right`() {
        val keyRoot = 0
        val key = 1
        tree.add(keyRoot, keyRoot.toString())
        tree.add(key, key.toString())
        tree.delete(key)

        val root = tree.root
        assert(tree.getNode(keyRoot) === root && root != null && root.right == null)
    }

    @Test
    fun `Delete should work correctly left child root`() {
        val keyRoot = 0
        val key = -1
        tree.add(keyRoot, keyRoot.toString())
        tree.add(key, key.toString())

        tree.delete(keyRoot)

        assert(tree.getNode(key) === tree.root)
    }

    @Test
    fun `Delete should work correctly right child root`() {
        val keyRoot = 0
        val key = 1
        tree.add(keyRoot, keyRoot.toString())
        tree.add(key, key.toString())
        tree.delete(keyRoot)

        val root = tree.root
        assert(tree.getNode(key) === root)
    }

    @Test
    fun `Delete should work correctly left child not-root left`() {
        val keyRoot = 0
        val key = -1
        val childKey = -2
        tree.add(keyRoot, keyRoot.toString())
        tree.add(key, key.toString())
        tree.add(childKey, childKey.toString())

        val root = tree.root
        tree.delete(key)

        assert(tree.getNode(keyRoot) === root
                && root != null
                && root.left === tree.getNode(childKey)
                && tree.getNode(childKey)?.parent === root
        )
    }

    @Test
    fun `Delete should work correctly right child not-root left`() {
        val keyRoot = 0
        val key = -2
        val childKey = -1
        tree.add(keyRoot, keyRoot.toString())
        tree.add(key, key.toString())
        tree.add(childKey, childKey.toString())

        val root = tree.root
        tree.delete(key)

        assert(tree.getNode(keyRoot) === root
                && root != null
                && root.left === tree.getNode(childKey)
                && tree.getNode(childKey)?.parent === root
        )
    }

    @Test
    fun `Delete should work correctly left child not-root right`() {
        val keyRoot = 0
        val key = 2
        val childKey = 1
        tree.add(keyRoot, keyRoot.toString())
        tree.add(key, key.toString())
        tree.add(childKey, childKey.toString())

        val root = tree.root
        tree.delete(key)

        assert(tree.getNode(keyRoot) === root
                && root != null
                && root.right === tree.getNode(childKey)
                && tree.getNode(childKey)?.parent === root
        )
    }

    @Test
    fun `Delete should work correctly right child not-root right`() {
        val keyRoot = 0
        val key = 1
        val childKey = 2
        tree.add(keyRoot, keyRoot.toString())
        tree.add(key, key.toString())
        tree.add(childKey, childKey.toString())

        val root = tree.root
        tree.delete(key)

        assert(tree.getNode(keyRoot) === root
                && root != null
                && root.right === tree.getNode(childKey)
                && tree.getNode(childKey)?.parent === root
        )
    }

    // Madness is following...

    @Test
    fun `Delete should work correctly 2 child root where next haven't children`() {
        val keyRoot = 0
        val keyRight = 1
        val keyLeft = -1
        tree.add(keyRoot, keyRoot.toString())
        tree.add(keyRight, keyRight.toString())
        tree.add(keyLeft, keyLeft.toString())

        tree.delete(keyRoot)

        val root = tree.root
        assert(tree.getNode(keyRight) === root
                && root != null
                && root.left === tree.getNode(keyLeft)
                && tree.getNode(keyLeft)?.parent === root
        )
    }

    @Test
    fun `Delete should work correctly 2 child root where next has not left children`() {

        // scenario explanation on picture
        //
        // ┌──  3                    ┌── 3
        // |    |   ┌── 2            |   └── 2
        // |    └── 1       ──>      1
        // 0             (0 delete)  └── -1
        // └── -1

        val keys = arrayOf(0, -1, 3, 1, 2)

        keys.forEach { tree.add(it, it.toString()) }

        val next = tree.getNext(tree.root!!)

        tree.delete(keys[0])

        val root = tree.root
        assert(root == next
                && root != null
                && root.left === tree.getNode(keys[1])
                && tree.getNode(keys[1])?.parent === root
                && root.right === tree.getNode(keys[2])
                && tree.getNode(keys[2])?.parent === root
                && root.right != null
                && root.right?.left === tree.getNode(keys[4])
                && tree.getNode(keys[4])?.parent === root.right
        )
    }

    @Test
    fun `delete should work correctly 2 child not-root`() {
        // scenario explanation on picture
        //      ┌── 3              ┌──  3
        // ┌──  2                  |    └── 1
        // |    └── 1      ──>     0
        // 0           (2 delete)  └── -1
        // └── -1

        val keys = arrayOf(0, -1, 2, 1, 3)
        keys.forEach { tree.add(it, it.toString()) }

        val node = tree.getNode(keys[4])
        tree.delete(keys[2])

        val root = tree.root
        assert(root != null
                && root.left === tree.getNode(keys[1])
                && tree.getNode(keys[1])?.parent === root
                && node == root.right
                && root.right?.left === tree.getNode(keys[3])
        )
    }

    @Test
    fun `delete should work correctly 2 child not-root but next is left child of its parent`() {
        // scenario explanation on picture
        // 0                            0
        // |         ┌── -1             |    ┌── -1
        // |    ┌── -2       ──>        └── -2
        // └── -3          (-2 delete)       └── -4
        //      └── -4

        val keys = arrayOf(0, -3, -2, -4, -1)
        keys.forEach { tree.add(it, it.toString()) }

        tree.delete(keys[1])

        val root = tree.root
        val node = tree.getNode(keys[2])
        assert(root != null
                && root.left === node
                && node?.parent === root
                && node.left === tree.getNode(keys[3])
                && tree.getNode(keys[3])?.parent === node
                && node.right === tree.getNode(keys[4])
                && tree.getNode(keys[4])?.parent === node
        )
    }

}