import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RBNodeTest {
    private lateinit var tree: RBTree<Int, String>

    @BeforeEach
    fun setup() {
        tree = RBTree()
    }

    // max and min tests
    @Test
    fun `max should return if empty`() {
        assert(tree.max()?.value == null)
    }

    @Test
    fun `min should return if empty`() {
        assert(tree.min()?.value == null)
    }

    @Test
    fun `max should return max value`() {
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


    // toString tests
    @Test
    fun `should return null if tree is empty`() {
        assert(tree.root?.toString() == null)
    }

    @Test
    fun `should return node with black color`() {
        tree.add(1, "A")
        tree.add(2, "B")
        tree.add(3, "C")

        assert(tree.getNode(1)?.toString() == "(1: A)")
    }

    @Test
    fun `should return node with red color`() {
        tree.add(1, "A")
        tree.add(2, "B")
        tree.add(3, "C")
        tree.setColored(true)

        assert(tree.getNode(1)?.toString() == "\u001B[31m(1: A)\u001B[0m")
    }

    // isOnLeft tests
    @Test
    fun `isOnLeft should return false to root node`() {
        tree.add(1, "A")
        assert(!(tree.root as Node.RBNode<Int, String>).isOnLeft)
    }

    @Test
    fun `isOnLeft should return true`() {
        tree.add(1, "A")
        tree.add(2, "D")
        tree.add(3, "C")

        assert((tree.getNode(1) as Node.RBNode<Int, String>).isOnLeft)
    }

    @Test
    fun `isOnLeft should return false`() {
        tree.add(1, "A")
        tree.add(2, "D")
        tree.add(3, "C")

        assert(!(tree.getNode(3) as Node.RBNode<Int, String>).isOnLeft)
    }


    // sibling tests
    @Test
    fun `sibling should return null if parent is null`() {
        tree.add(1, "A")
        assert((tree.root as Node.RBNode<Int, String>).sibling() == null)
    }

    @Test
    fun `sibling should return right node`() {
        tree.add(1, "A")
        tree.add(2, "D")
        tree.add(3, "C")

        assert((tree.getNode(1) as Node.RBNode<Int, String>).sibling()?.value == "C")
    }

    @Test
    fun `sibling should return left node`() {
        tree.add(1, "A")
        tree.add(2, "D")
        tree.add(3, "C")

        assert((tree.getNode(3) as Node.RBNode<Int, String>).sibling()?.value == "A")
    }


    // hasRedChild tests
    @Test
    fun `should return false if has no child`() {
        tree.add(1, "A")

        assert(!(tree.getNode(1) as Node.RBNode<Int, String>).hasRedChild())
    }

    @Test
    fun `should return false if both children are black`() {
        tree.add(1, "A")
        tree.add(2, "B")
        tree.add(3, "C")

        // because Red-Black tree is self-balancing, it balances the nodes automatically, that is wht for the sake of
        // the test we need to set the color of the nodes to black manually
        tree.getNode(1)?.let { (it as Node.RBNode<Int, String>).color = Node.RBNode.Color.BLACK }
        tree.getNode(3)?.let { (it as Node.RBNode<Int, String>).color = Node.RBNode.Color.BLACK }

        assert(!(tree.getNode(2) as Node.RBNode<Int, String>).hasRedChild())
    }

    @Test
    fun `should return true if left child is red`() {
        tree.add(2, "B")
        tree.add(1, "A")
        tree.setColored(true)

        assert((tree.getNode(2) as Node.RBNode<Int, String>).hasRedChild())
    }

    @Test
    fun `should return true if right child is red`() {
        tree.add(1, "A")
        tree.add(2, "B")
        tree.setColored(true)

        assert((tree.getNode(1) as Node.RBNode<Int, String>).hasRedChild())
    }
}