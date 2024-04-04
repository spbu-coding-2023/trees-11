import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AVLTreeTest {
    private lateinit var tree: AVLTree<Int, String>

    @BeforeEach
    fun setup() {
        tree = AVLTree()
    }

    @Nested
    inner class AddTests {
        @Test
        fun `Inserting a node into an empty tree`(){
            val key = 1
            val value = "0"

            tree.add(key, value)
            assertEquals(tree[key], value)
        }

        @Test
        fun `Inserting a duplicate node`(){
            tree.add(1, "0")
            tree.add(1, "0")

            val root = tree.root
            assert(root == tree.getNode(1))
            assert(root?.right == null)
            assert(root?.left == null)
        }

        @Test
        fun `Inserting with left rotate`(){
            val key1 = 0
            val key2 = 1
            val key3 = 2

            tree.add(key1, key1.toString())
            tree.add(key2, key2.toString())
            tree.add(key3, key3.toString())

            val root = tree.root
            assert(root?.key == key2)
            assert(root?.left!!.key == key1)
            assert(root.right!!.key == key3)
        }

        @Test
        fun `Inserting with right rotate`(){
            val key1 = 2
            val key2 = 1
            val key3 = 0

            tree.add(key1, key1.toString())
            tree.add(key2, key2.toString())
            tree.add(key3, key3.toString())

            val root = tree.root
            assert(root?.key == key2)
            assert(root?.left!!.key == key3)
            assert(root.right!!.key == key1)
        }

        @Test
        fun `Inserting with big left rotate (last node)`(){
            val key1 = 2
            val key2 = 1
            val key3 = 5
            val key4 = 3
            val key5 = 6
            val key6 = 4

            tree.add(key1, key1.toString())
            tree.add(key2, key2.toString())
            tree.add(key3, key3.toString())
            tree.add(key4, key4.toString())
            tree.add(key5, key5.toString())
            tree.add(key6, key6.toString())

            val root = tree.root
            assert(root == tree.getNode(3))
        }

        @Test
        fun `Inserting with big right rotate (last node)`(){
            val key1 = 5
            val key2 = 6
            val key3 = 4
            val key4 = 2
            val key5 = 3
            val key6 = 1

            tree.add(key1, key1.toString())
            tree.add(key2, key2.toString())
            tree.add(key3, key3.toString())
            tree.add(key4, key4.toString())
            tree.add(key5, key5.toString())
            tree.add(key6, key6.toString())

            val root = tree.root
            assert(root == tree.getNode(3))
        }
    }

    @Nested
    inner class MinMaxTests(){
        @Test
        fun `Max should return max node`() {
            for (i in 1..10) {
                tree.add(i, "")
            }
            assert(tree.max()?.key == 10)
        }

        @Test
        fun `Max from null tree returns null`(){
            assert(tree.max() == null)
        }

        @Test
        fun `Min should return min node`() {
            for (i in 1..10) {
                tree.add(i, "")
            }
            assert(tree.min()?.key == 1)
        }

        @Test
        fun `Min from null tree returns null`(){
            assert(tree.min() == null)
        }
    }

    @Nested
    inner class DeleteTests{
        @Test
        fun `Deletion of the non-existent node should do nothing`(){
            tree.add(1, "0")
            tree.add(2, "890")

            tree.delete(4)

            assert(tree.getNode(1) != null)
            assert(tree.getNode(2) != null)
        }

        @Test
        fun `Deletion of tree's root`(){
            val keyLeft = 0
            val keyRoot = 1
            val keyRight = 2

            tree.add(keyLeft, keyLeft.toString())
            tree.add(keyRoot, keyRoot.toString())
            tree.add(keyRight, keyRight.toString())

            tree.delete(1)

            val root = tree.root
            assert(tree.getNode(1) == null)
            assert(tree.getNode(keyRight) == root)
            assert(root?.right == null)
        }

        @Test
        fun `Deletion of leaf(node with no children)`(){
            val keyRoot = 0
            val key = 1
            val childKey = 2
            tree.add(keyRoot, keyRoot.toString())
            tree.add(key, key.toString())
            tree.add(childKey, childKey.toString())

            tree.delete(2)

            val root = tree.root
            assert(tree.getNode(childKey) == null)
            assert(root?.right == null)
            assert(root?.left == tree.getNode(0))
        }

        @Test
        fun `Deletion of node with one child`(){
            val key1 = 3
            val key2 = 2
            val key3 = 1
            val key4 = 0

            tree.add(key1, key1.toString())
            tree.add(key2, key2.toString())
            tree.add(key3, key3.toString())
            tree.add(key4, key4.toString())

            tree.delete(1)

            val root = tree.root
            assert(tree.getNode(1) == null)
            assert(tree.getNode(0) == root?.left)
        }

        @Test
        fun `Deletion of node with two children`(){
            val key1 = 2
            val key2 = 1
            val key3 = 5
            val key4 = 3
            val key5 = 6

            tree.add(key1, key1.toString())
            tree.add(key2, key2.toString())
            tree.add(key3, key3.toString())
            tree.add(key4, key4.toString())
            tree.add(key5, key5.toString())

            tree.delete(5)

            val root = tree.root
            assert(tree.getNode(5) == null)
            assert(root?.right == tree.getNode(key5))

            val newNode = tree.getNode(key5)
            val childNewNode = newNode?.left
            assert(newNode?.key!! > childNewNode?.key!!)
        }

        @Test
        fun `Deletion of node with two children, which also have child`(){
            val key1 = 2
            val key2 = 1
            val key3 = 5
            val key4 = 3
            val key5 = 6
            val key6 = 0
            val key7 = 4

            tree.add(key1, key1.toString())
            tree.add(key2, key2.toString())
            tree.add(key3, key3.toString())
            tree.add(key4, key4.toString())
            tree.add(key5, key5.toString())
            tree.add(key6, key6.toString())
            tree.add(key7, key7.toString())

            tree.delete(5)

            val newNode = tree.getNode(key7)
            val root = tree.root

            assert(tree.getNode(5) == null)
            assert(newNode == root?.right)
            assert(newNode?.right != null && newNode.left != null)
        }
    }

    @Nested
    inner class MergeTests{
        @Test
        fun `Merge should work correctly on 2 empty trees`() {
            val secondTree = AVLTree<Int, String>()

            tree.merge(secondTree)

            assert(tree.root == null)
        }

        @Test
        fun `should merge second tree with empty tree`() {
            val secondTree = AVLTree<Int, String>()
            secondTree.add(1, "value1")
            secondTree.add(2, "value2")

            tree.merge(secondTree)

            assert(secondTree.root == tree.root)
        }

        @Test
        fun `should merge empty second tree with initial tree`() {
            val secondTree = AVLTree<Int, String>()
            tree.add(1, "value1")
            tree.add(2, "value2")

            tree.merge(secondTree)

            assert(tree.root == tree.root)
        }

        @Test
        fun `should merge non empty trees`() {
            val secondTree = AVLTree<Int, String>()
            tree.add(1, "value1")
            tree.add(2, "value2")
            secondTree.add(3, "value3")
            secondTree.add(4, "value4")

            tree.merge(secondTree)

            // Check if the resulting tree contains all elements from both trees
            var treeNodeCount = 0
            tree.iterator().forEach { _ -> treeNodeCount++ }
            assert("value1" == tree.getNode(1)?.value)
            assert("value2" == tree.getNode(2)?.value)
            assert("value3" == tree.getNode(3)?.value)
            assert("value4" == tree.getNode(4)?.value)
        }

        @Test
        fun `Merge should throw IllegalArgumentException on attempt to merge tree that isn't bigger than this`() {
            val secondTree = AVLTree<Int, String>()
            val keys = arrayOf(0, 1, -1)
            val secondKeys = arrayOf(3, 4, 0)

            keys.forEach { tree.add(it, it.toString()) }
            secondKeys.forEach { secondTree.add(it, it.toString()) }

            val message =
                "Merge operation is defined only when attachable tree's keys is always bigger than base tree's keys"

            val exception = assertFailsWith<IllegalArgumentException> {
                tree.merge(secondTree)
            }
            assert(exception.message == message)
        }
    }
}