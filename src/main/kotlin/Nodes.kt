open class Node<K : Comparable<K>, V, T: Node<K, V, T>> internal constructor(
    var key: K,
    var value: V,
    internal var left: Node<K, V, T>? = null,
    internal var right: Node<K, V, T>? = null,
    internal var parent: Node<K, V, T>? = null)
{

    class BinaryNode<K : Comparable<K>, V>(
        key: K,
        value: V,
        left: BinaryNode<K, V>? = null,
        right: BinaryNode<K, V>? = null,
        parent: BinaryNode<K, V>? = null
    ) : Node<K, V, BinaryNode<K, V>>(key, value, left, right, parent)

    class RBNode<K : Comparable<K>, V>(
        key: K,
        value: V,
        left: RBNode<K, V>? = null,
        right: RBNode<K, V>? = null,
        parent: RBNode<K, V>? = null,
        var color: Color = Color.RED
    ) : Node<K, V, RBNode<K, V>>(key, value, left, right, parent) {
        enum class Color { RED, BLACK }

        fun max(): RBNode<K, V> {
            var current = this
            while (current.right != null) {
                current = current.right as RBNode<K, V>
            }
            return current
        }

        fun min(): RBNode<K, V> {
            var current = this
            while (current.left != null) {
                current = current.left as RBNode<K, V>
            }
            return current
        }
    }
    class AVLNode<K : Comparable<K>, V>(
        key: K,
        value: V,
        left: AVLNode<K, V>? = null,
        right: AVLNode<K, V>? = null,
        parent: AVLNode<K, V>? = null,
        var height: Int = 0
    ) : Node<K, V, AVLNode<K, V>>(key, value, left, right, parent)

    override fun toString(): String {
        return "($key: $value)"
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is Node<*, *, *>) false else (this.key == other.key && this.value == other.value)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

}