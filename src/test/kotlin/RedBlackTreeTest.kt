import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertFailsWith

class RedBlackTreeTest {
    private lateinit var tree: RBTree<Int, String>

    @BeforeEach
    fun setup () {
        tree = RBTree()
    }
    @Nested
    inner class AddTests {
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
    }

    @Nested
    inner class MinimumMaximumTests {
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
    }

    @Nested
    inner class MergeTests {
        @Test
        fun `Merge should work correctly on 2 empty trees`() {
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

        @Test
        fun `Merge should throw IllegalArgumentException on attempt to merge tree that isn't bigger than this`() {
            val secondTree = RBTree<Int, String>()
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

    // Tests for setColored
    @Test
    fun `setColored should set color of node`() {
        tree.add(1, "A")
        tree.add(2, "A")
        tree.add(3, "A")

        tree.setColored( true )
        assert(tree.getNode(1)?.toString() == "\u001B[31m(1: A)\u001B[0m")

        tree.setColored( false )
        assert(tree.getNode(1)?.toString() == "(1: A)")
    }

    @Nested
    inner class BalanceTreeTests{
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

        @Test
        fun `should convert red node and it's sibling node to black if added new red node on right of grandparent`() {
            // scenario explanation on picture
            // |   ┌── (93 Red)                     |   ┌── (93 Black)
            // └── (83 Black)       ----------->    └── (83 Black)
            //    |                 (add 71 Red)        |   ┌── (71 Red )
            //    └── (47 Red)                          └── (47 Black)

            tree.add(47, "47")
            tree.add(83, "83")
            tree.add(93, "93")
            tree.add(71, "71")

            val root = tree.root as Node.RBNode?
            val addedNode = tree.getNode(71) as Node.RBNode?
            val parent = addedNode?.parent as Node.RBNode?
            val uncle = root?.right as Node.RBNode?

            assert(
                root != null
                        && root.color == Node.RBNode.Color.BLACK
                        && parent?.color == Node.RBNode.Color.BLACK
                        && uncle?.color == Node.RBNode.Color.BLACK
                        && addedNode?.color == Node.RBNode.Color.RED
            )
        }

        @Test
        fun `should convert red node and it's sibling node to black if added new red node on left of grandparent`() {
            // scenario explanation on picture
            // |   ┌── (93 Red)                     |   ┌── (93 Black)
            // |   |                ----------->    |   |   ┌── (90 Red)
            // └── (83 Black)       (add 71 Red)    └── (83 Black)
            //     └── (47 Red)                          └── (47 Black)

            tree.add(47, "47")
            tree.add(83, "83")
            tree.add(93, "93")
            tree.add(90, "90")

            val root = tree.root as Node.RBNode?
            val addedNode = tree.getNode(90) as Node.RBNode?
            val parent = addedNode?.parent as Node.RBNode?
            val uncle = root?.right as Node.RBNode?

            assert(
                root != null
                        && root.color == Node.RBNode.Color.BLACK
                        && parent?.color == Node.RBNode.Color.BLACK
                        && uncle?.color == Node.RBNode.Color.BLACK
                        && addedNode?.color == Node.RBNode.Color.RED
            )
        }

        @Test
        fun `should right rotate if added red node after red node on left of parent`() {
            // scenario explanation on picture
            // |       ┌── (95 Red)                         |       ┌── (95 Red)
            // |   ┌── (93 Black )                          |   ┌── (94 Black )
            // └── (83 Black )           ----------->       |   |   └── (93 Red)
            //     └── (47 Black )       (add 94 Red)       └── (83 Black )
            //                                                  └── (47 Black )

            tree.add(47, "")
            tree.add(83, "")
            tree.add(93, "")
            tree.add(95, "")
            tree.add(94, "")

            val root = tree.root as Node.RBNode?
            val addedNode = tree.getNode(94) as Node.RBNode?
            val leftChild = addedNode?.left as Node.RBNode?
            val rightChild = addedNode?.right as Node.RBNode?

            assert(
                root != null
                        && root.color == Node.RBNode.Color.BLACK
                        && addedNode?.color == Node.RBNode.Color.BLACK
                        && leftChild?.color == Node.RBNode.Color.RED
                        && rightChild?.color == Node.RBNode.Color.RED
            )
        }

        @Test
        fun `should left rotate if added red node after red node on right of parent`() {
            // scenario explanation on picture
            // |                                            |       ┌── (93 Red)
            // |   ┌── (93 Black )                          |   ┌── (92 Black )
            // |   |   └── (90 Red)                         |   |   └── (90 Red)
            // └── (83 Black )           ----------->       └── (83 Black )
            //     └── (47 Black )       (add 92 Red)           └── (47 Black )
            //

            tree.add(47, "")
            tree.add(83, "")
            tree.add(93, "")
            tree.add(90, "")
            tree.add(92, "")

            val root = tree.root as Node.RBNode?
            val addedNode = tree.getNode(92) as Node.RBNode?
            val leftChild = addedNode?.left as Node.RBNode?
            val rightChild = addedNode?.right as Node.RBNode?

            assert(
                root != null
                        && root.color == Node.RBNode.Color.BLACK
                        && addedNode?.color == Node.RBNode.Color.BLACK
                        && leftChild?.color == Node.RBNode.Color.RED
                        && rightChild?.color == Node.RBNode.Color.RED
            )
        }


    }

}