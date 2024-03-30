import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NodeTest{
    @Test
    fun `Node should be equal if values and keys are equal`() {
        val key = 15
        val value = "Mitochondria"

        val nodeOne = Node<Int, String, Node.BinaryNode<Int, String>>(key, value)
        val nodeSecond = Node<Int, String, Node.BinaryNode<Int, String>>(key, value)

        assert(nodeOne == nodeSecond)
    }

    @Test
    fun `Node should be equal if values aren't equal`() {
        val key = 15
        val valueOne = "Mitochondria"
        val valueTwo = "Mit0ch0ndr1a"

        val nodeOne = Node<Int, String, Node.BinaryNode<Int, String>>(key, valueOne)
        val nodeSecond = Node<Int, String, Node.BinaryNode<Int, String>>(key, valueTwo)

        assert(nodeOne != nodeSecond)
    }
    @Test
    fun `Node should be equal if keys aren't equal`() {
        val keyOne = 15
        val keyTwo = 150
        val value = "Mitochondria"

        val nodeOne = Node<Int, String, Node.BinaryNode<Int, String>>(keyOne, value)
        val nodeSecond = Node<Int, String, Node.BinaryNode<Int, String>>(keyTwo, value)

        assert(nodeOne != nodeSecond)
    }

    @Test
    fun `Node should be equal if keys aren't equal and values aren't equal`() {
        val keyOne = 15
        val keyTwo = 150
        val valueOne = "Mitochondria"
        val valueTwo = "Mit0ch0ndr1a"

        val nodeOne = Node<Int, String, Node.BinaryNode<Int, String>>(keyOne, valueOne)
        val nodeSecond = Node<Int, String, Node.BinaryNode<Int, String>>(keyTwo, valueTwo)

        assert(nodeOne != nodeSecond)
    }

    @Test
    fun `Node shouldn't be equal to notNode`() {
        val keyOne = 15
        val valueOne = "Mitochondria"

        val nodeOne = Node<Int, String, Node.BinaryNode<Int, String>>(keyOne, valueOne)

        val notNode = arrayOf(keyOne, valueOne)

        assert(nodeOne != notNode)
    }
}