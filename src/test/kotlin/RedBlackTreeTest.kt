import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertFailsWith

class RedBlackTreeTest {
    private lateinit var tree: RBTree<Int, String>

    @BeforeEach
    fun setup() {
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

        @Test
        fun `should right rotate when add, then change root`() {
            tree.add(85, "A")
            tree.add(56, "B")
            tree.add(27, "C")

            val root = tree.root as Node.RBNode?
            val left = root?.left as Node.RBNode?
            val right = root?.right as Node.RBNode?

            assert(root != null && root.key == 56 && root.color == Node.RBNode.Color.BLACK)
            assert(left?.key == 27 && left.color == Node.RBNode.Color.RED)
            assert(right?.key == 85 && right.color == Node.RBNode.Color.RED)
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

            val exception =
                assertFailsWith<IllegalArgumentException> {
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

        tree.setColored(true)
        assert(tree.getNode(1)?.toString() == "\u001B[31m(1: A)\u001B[0m")

        tree.setColored(false)
        assert(tree.getNode(1)?.toString() == "(1: A)")
    }

    @Nested
    inner class BalanceTreeTests {
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

            assert(root != null && root.color == Node.RBNode.Color.BLACK)
            assert(parent?.color == Node.RBNode.Color.BLACK)
            assert(uncle?.color == Node.RBNode.Color.BLACK)
            assert(addedNode?.color == Node.RBNode.Color.RED)
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

            assert(root != null && root.color == Node.RBNode.Color.BLACK)
            assert(parent?.color == Node.RBNode.Color.BLACK)
            assert(uncle?.color == Node.RBNode.Color.BLACK)
            assert(addedNode?.color == Node.RBNode.Color.RED)
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

            assert(root != null && root.color == Node.RBNode.Color.BLACK)
            assert(addedNode?.color == Node.RBNode.Color.BLACK)
            assert(leftChild?.color == Node.RBNode.Color.RED)
            assert(rightChild?.color == Node.RBNode.Color.RED)
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

            assert(root != null && root.color == Node.RBNode.Color.BLACK)
            assert(addedNode?.color == Node.RBNode.Color.BLACK)
            assert(leftChild?.color == Node.RBNode.Color.RED)
            assert(rightChild?.color == Node.RBNode.Color.RED)
        }
    }

    /**
     * WARNING:
     *  because Red-Black tree is self-balancing, it balances the nodes automatically, that is why for the sake of
     *  the test we need to set the color of the nodes manually. The cases are totally possible in the real world, I
     *  set colors manually to make easier to understand the test cases.
     */
    @Nested
    inner class DeleteNodeTests {
        @Test
        fun `should not find node and return`() {
            tree.add(1, "A")
            tree.add(2, "B")
            tree.add(3, "C")

            tree.delete(4)

            assert(tree.getNode(1) != null)
            assert(tree.getNode(2) != null)
            assert(tree.getNode(3) != null)
        }

        @Test
        fun `should delete root with no children`() {
            tree.add(1, "A")

            tree.delete(1)

            assert(tree.root == null)
        }

        @Test
        fun `should delete right red node with no children`() {
            tree.add(1, "A")
            tree.add(2, "B")

            tree.delete(2)
            val root = tree.root as Node.RBNode?

            assert(root != null && root.color == Node.RBNode.Color.BLACK)
            assert(root!!.key == 1)
            assert(root.left == null)
            assert(root.right == null)
            assert(tree.getNode(2) == null)
        }

        @Test
        fun `should delete left red node with no children`() {
            tree.add(2, "B")
            tree.add(1, "A")

            tree.delete(1)
            val root = tree.root as Node.RBNode?

            assert(root != null && root.color == Node.RBNode.Color.BLACK)
            assert(root!!.key == 2)
            assert(root.left == null)
            assert(root.right == null)
            assert(tree.getNode(1) == null)
        }

        @Test
        fun `should delete root with one child`() {
            tree.add(1, "A")
            tree.add(2, "B")

            tree.delete(1)
            val root = tree.root as Node.RBNode?

            assert(root != null && root.color == Node.RBNode.Color.BLACK)
            assert(root!!.key == 2)
            assert(root.left == null)
            assert(root.right == null)
            assert(tree.getNode(1) == null)
        }

        @Test
        fun `should delete node with 2 children and red successor`() {
            tree.add(1, "A")
            tree.add(2, "B")
            tree.add(3, "C")

            tree.delete(2)
            val root = tree.root as Node.RBNode?

            assert(root != null && root.color == Node.RBNode.Color.BLACK)
            assert(root!!.key == 3)
            assert(root.right == null)
            assert(root.left?.key == 1)
            assert(tree.getNode(2) == null)
        }

        @Test
        fun `should delete black node with one red child`() {
            tree.add(1, "A")
            tree.add(2, "B")
            tree.add(3, "C")
            tree.add(4, "D")

            tree.delete(3)

            val root = tree.root as Node.RBNode?

            assert(root != null && root.color == Node.RBNode.Color.BLACK)
            assert(root!!.key == 2)
            assert(root.left?.key == 1)
            assert(root.right?.key == 4)
            assert(tree.getNode(3) == null)
        }

        @Test
        fun `should delete black node with one black child`() {
            // scenario explanation on picture
            // |       ┌── (6 Black)                     |       ┌── (6 Black)
            // |   ┌── (5 Black)                         |   ┌── (5 Red)
            // |   |   └── (4 Black)   ----------->      |   |   └── (4 Black)
            // └── (3 Black)           (delete 2)        └── (3 Black)
            //     └── (2 Black)                              └── (1 Black)
            //         └── (1 Black)
            tree.add(3, "C")
            tree.add(2, "B")
            tree.add(4, "D")
            tree.add(1, "A")
            tree.add(5, "E")
            tree.add(6, "F")

            (tree.getNode(1) as Node.RBNode<Int, String>?)?.color = Node.RBNode.Color.BLACK
            (tree.getNode(4) as Node.RBNode<Int, String>?)?.color = Node.RBNode.Color.BLACK
            (tree.getNode(6) as Node.RBNode<Int, String>?)?.color = Node.RBNode.Color.BLACK

            tree.delete(2)

            val root = tree.root as Node.RBNode?
            val rootRight = root?.right as Node.RBNode?
            val rootRightRight = rootRight?.right as Node.RBNode?
            val rootRightLeft = rootRight?.left as Node.RBNode?
            // check if the tree is balanced
            assert(root != null && root.color == Node.RBNode.Color.BLACK)
            assert(root!!.key == 3)
            assert(root.left?.key == 1)
            assert(root.right?.key == 5)
            assert(rootRight?.color == Node.RBNode.Color.RED)
            assert(rootRightLeft?.key == 4)
            assert(rootRightLeft!!.color == Node.RBNode.Color.BLACK)
            assert(rootRightRight?.key == 6)
            assert(rootRightRight!!.color == Node.RBNode.Color.BLACK)
            assert(tree.getNode(2) == null)
        }

        @Test
        fun `should delete black node with 2 black children`() {
            // scenario explanation on picture
            // |       ┌── (6 Red)                       |         ┌── (6 Red)
            // |   ┌── (5 Black)                         |    ┌── (5 Black)
            // |   |   └── (4 Red)       ------------>   └── (4 Black)
            // └── (3 Black)                                  └── (2 Black)
            //     └── (2 Black)                                   └── (1 Red)
            //         └── (1 Red)

            tree.add(3, "C")
            tree.add(2, "B")
            tree.add(4, "D")
            tree.add(1, "A")
            tree.add(5, "E")
            tree.add(6, "F")

            tree.delete(3)

            val root = tree.root as Node.RBNode?
            val rootRight = root?.right as Node.RBNode?
            val rootLeft = root?.left as Node.RBNode?
            val rootLeftLeft = rootLeft?.left as Node.RBNode?
            val rootRightRight = rootRight?.right as Node.RBNode?

            // check if the tree is balanced
            assert(root != null && root.color == Node.RBNode.Color.BLACK)
            assert(root!!.key == 4)
            assert(rootLeft?.key == 2)
            assert(rootRight?.key == 5)
            assert(rootRight!!.color == Node.RBNode.Color.BLACK)
            assert(rootLeft!!.color == Node.RBNode.Color.BLACK)
            assert(rootLeftLeft?.key == 1)
            assert(rootLeftLeft!!.color == Node.RBNode.Color.RED)
            assert(rootRightRight?.key == 6)
            assert(rootRightRight!!.color == Node.RBNode.Color.RED)
            assert(tree.getNode(3) == null)
        }

        @Test
        fun `should delete red node with 2 children and fixDoubleBlack sibling on left`() {
            // scenario explanation on picture
            // |       ┌── (93 Black)                           |       ┌── (93 Black)
            // |   ┌── (90 Red)                                 |   ┌── (90 Red)
            // |   |   |   ┌── (81 Red)                         |   |   |   ┌── (81 Red)
            // |   |   └── (69 Black)                           |   |   └── (69 Black)
            // |   |       └── (62 Red)                         |   |       └── (62 Red)
            // └── (53 Black)                  ----------->     └── (53 Black)
            //     |   ┌── (43 Black)           (delete 32)         |   ┌── (43 Black)
            //     └── (32 Red)                                     └── (27 Red)
            //         |   ┌── (27 Red)                                 └── (17 Black)
            //         └── (17 Black)
            val array =
                arrayOf(
                    32,
                    81,
                    17,
                    90,
                    93,
                    43,
                    27,
                    53,
                    69,
                    62,
                )

            for (num in array) {
                tree.add(num, "$num")
            }
            tree.delete(32)

            val root = tree.root as Node.RBNode?
            val rootLeft = root?.left as Node.RBNode?
            val rootLeftLeft = rootLeft?.left as Node.RBNode?
            val rootLeftRight = rootLeft?.right as Node.RBNode?

            // check if the tree is balanced
            assert(root != null && root.color == Node.RBNode.Color.BLACK)
            assert(root!!.key == 53)
            assert(rootLeft?.key == 27)
            assert(rootLeft!!.color == Node.RBNode.Color.RED)
            assert(rootLeftLeft?.key == 17)
            assert(rootLeftLeft!!.color == Node.RBNode.Color.BLACK)
            assert(rootLeftRight?.key == 43)
            assert(rootLeftRight!!.color == Node.RBNode.Color.BLACK)
            assert(tree.getNode(32) == null)
        }

        @Test
        fun `should delete black node with no children, and fixDoubleBlack sibling on left and sibling's left child is red`() {
            // scenario explanation on picture
            // |       ┌── (93 Black)                           |       ┌── (90 Black)
            // |   ┌── (90 Red)                                 |       |   └── (81 Red)
            // |   |   |   ┌── (81 Red)                         |   ┌── (69 Red)
            // |   |   └── (69 Black)                           |   |   └── (62 Black)
            // |   |       └── (62 Red)      ----------->       └── (53 Black)
            // └── (53 Black)                 (delete 93)            |   ┌── (43 Black)
            //     |   ┌── (43 Black)                                └── (27 Red)
            //     └── (27 Red)                                          └── (17 Black)
            //         └── (17 Black)
            val array =
                arrayOf(
                    32,
                    81,
                    17,
                    90,
                    93,
                    43,
                    27,
                    53,
                    69,
                    62,
                )

            for (num in array) {
                tree.add(num, "$num")
            }
            tree.delete(32)
            tree.delete(93)

            val root = tree.root as Node.RBNode?
            val rootRight = root?.right as Node.RBNode?
            val rootRightLeft = rootRight?.left as Node.RBNode?
            val rootRightRight = rootRight?.right as Node.RBNode?
            val rootRightRightLeft = rootRightRight?.left as Node.RBNode?

            // check if the tree is balanced
            assert(root != null && root.color == Node.RBNode.Color.BLACK)
            assert(root!!.key == 53)
            assert(rootRight?.key == 69)
            assert(rootRight!!.color == Node.RBNode.Color.RED)
            assert(rootRightLeft?.key == 62)
            assert(rootRightLeft!!.color == Node.RBNode.Color.BLACK)
            assert(rootRightRight?.key == 90)
            assert(rootRightRight!!.color == Node.RBNode.Color.BLACK)
            assert(rootRightRightLeft?.key == 81)
            assert(rootRightRightLeft!!.color == Node.RBNode.Color.RED)
            assert(tree.getNode(93) == null)
        }

        @Test
        fun `should delete black node with no children, fixDoubleBlack sibling on right`() {
            // scenario explanation on picture
            // |       ┌── (93 Black)                       |       ┌── (93 Black)
            // |   ┌── (84 Red)                             |   ┌── (84 Red)
            // |   |   └── (71 Black)                       |   |   └── (71 Black)
            // |   |       └── (68 Red)                     |   |       └── (68 Red)
            // └── (60 Black)               ----------->    └── (60 Black)
            //     |   ┌── (59 Black)        (delete 23)        |   ┌── (59 Black)
            //     |   |   └── (49 Red)                         └── (49 Red)
            //     └── (42 Red)                                      └── (42 Black)
            //         └── (23 Black)
            val array =
                arrayOf(
                    60,
                    84,
                    23,
                    71,
                    93,
                    59,
                    68,
                    42,
                    49,
                )
            for (num in array) {
                tree.add(num, "$num")
            }
            tree.delete(23)

            val root = tree.root as Node.RBNode?
            val rootRight = root?.right as Node.RBNode?
            val rootLeft = root?.left as Node.RBNode?
            val rootRightLeft = rootRight?.left as Node.RBNode?
            val rootRightRight = rootRight?.right as Node.RBNode?
            val rootRightLeftLeft = rootRightLeft?.left as Node.RBNode?
            val rootLeftRight = rootLeft?.right as Node.RBNode?
            val rootLeftLeft = rootLeft?.left as Node.RBNode?

            // check if the tree is balanced
            assert(root != null && root.color == Node.RBNode.Color.BLACK)
            assert(root!!.key == 60)
            assert(rootLeft?.key == 49)
            assert(rootLeft!!.color == Node.RBNode.Color.RED)
            assert(rootLeftLeft?.key == 42)
            assert(rootLeftLeft!!.color == Node.RBNode.Color.BLACK)
            assert(rootLeftRight?.key == 59)
            assert(rootLeftRight!!.color == Node.RBNode.Color.BLACK)
            assert(rootRight?.key == 84)
            assert(rootRight!!.color == Node.RBNode.Color.RED)
            assert(rootRightLeft?.key == 71)
            assert(rootRightLeft!!.color == Node.RBNode.Color.BLACK)
            assert(rootRightRight?.key == 93)
            assert(rootRightRight!!.color == Node.RBNode.Color.BLACK)
            assert(rootRightLeftLeft?.key == 68)
            assert(rootRightLeftLeft!!.color == Node.RBNode.Color.RED)
            assert(tree.getNode(23) == null)
        }

        @Test
        fun `should delete black node with 2 children, child's sibling - red, fixDoubleBlack sibling on right`() {
            // scenario explanation on picture
            // |           ┌── (88 Red)
            // |       ┌── (78 Black)                         |       ┌── (88 Black)
            // |   ┌── (69 Red)                               |   ┌── (78 Red)
            // |   |   └── (45 Black)                         |   |   └── (69 Black)
            // └── (28 Black)                 ----------->    └── (45 Black)
            //     |   ┌── (21 Red)           (delete 28)         |   ┌── (21 Red)
            //     └── (17 Black)                                 └── (17 Black)
            //         └── (1 Red)                                    └── (1 Red)
            val array =
                arrayOf(
                    21,
                    69,
                    28,
                    17,
                    42,
                    1,
                    78,
                    88,
                    45,
                )
            for (num in array) {
                tree.add(num, "$num")
            }
            tree.delete(42)
            tree.delete(28)

            val root = tree.root as Node.RBNode?
            val rootRight = root?.right as Node.RBNode?
            val rootLeft = root?.left as Node.RBNode?
            val rootRightLeft = rootRight?.left as Node.RBNode?
            val rootRightRight = rootRight?.right as Node.RBNode?
            val rootLeftRight = rootLeft?.right as Node.RBNode?
            val rootLeftLeft = rootLeft?.left as Node.RBNode?

            // check if the tree is balanced
            assert(root != null && root.color == Node.RBNode.Color.BLACK)
            assert(root!!.key == 45)
            assert(rootLeft?.key == 17)
            assert(rootLeft!!.color == Node.RBNode.Color.BLACK)
            assert(rootLeftLeft?.key == 1)
            assert(rootLeftLeft!!.color == Node.RBNode.Color.RED)
            assert(rootLeftRight?.key == 21)
            assert(rootLeftRight!!.color == Node.RBNode.Color.RED)
            assert(rootRight?.key == 78)
            assert(rootRight!!.color == Node.RBNode.Color.RED)
            assert(rootRightLeft?.key == 69)
            assert(rootRightLeft!!.color == Node.RBNode.Color.BLACK)
            assert(rootRightRight?.key == 88)
            assert(rootRightRight!!.color == Node.RBNode.Color.BLACK)
            assert(tree.getNode(28) == null)
            assert(tree.getNode(42) == null)
        }
    }

    @Test
    fun `toStringBeautifulHeight should work correctly on empty tree`() {
        assert(tree.toStringBeautifulHeight() == "")
    }

    @Test
    fun `toStringBeautifulHeight should work correctly on sample`() {
        // no-child, left, right and 2 child node example
        // because it works similar on all this scenarios
        val keys = arrayOf(0, 1, -1, 2, -2)
        keys.forEach { tree.add(it, it.toString()) }

        val strTree = tree.toStringBeautifulHeight()
        val expectedResult = """────────────────────────┐
                     (0: 0)                     
            ┌───────────┴───────────┐           
        (-1: -1)                 (1: 1)         
      ┌─────┘                       └─────┐     
  (-2: -2)                             (2: 2)   
"""
        assert(strTree == expectedResult)
    }
}
