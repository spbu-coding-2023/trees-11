import java.util.*
import kotlin.math.ceil
import kotlin.math.floor


open class Tree <K: Comparable<K>, V: Any, T> (
    var root: Node<K, V, T>? = null
): Iterable<Node<K, V, T>> {
    open fun add(key: K, value: V) {
        var node: Node<K, V, T> = Node<K, V, T>(key, value)
        this.addNode(node)
    }
    open fun addNode(node: Node<K, V, T>) {
        if (this.root == null) {
            this.root = node
        } else {
            var root: Node<K, V, T> = this.root!!
            // not-null assertion operator because null check
            var isLeft: Boolean = root.key > node.key
            addNode(node,
                   (if (isLeft) root.left else root.right) as Node<K, V, T>?,
                   root,
                   isLeft)
        }
    }

    private tailrec fun addNode(node: Node<K, V, T>, current: Node<K, V, T>?, parent: Node<K, V, T>, isLeft: Boolean) {
        if (current == null) {
            if (isLeft) parent.left = node as T
            else parent.right = node as T

            node.parent = parent as T
        } else {
            require(current.key != node.key) {
                throw IllegalArgumentException("Multiple uses of key: ${node.key}. To modify value use set function")
            }
            val isAddedLeft: Boolean = current.key > node.key
            addNode(node,
                (if (isAddedLeft) current.left else current.right) as Node<K, V, T>?,
                current,
                isAddedLeft)
        }
    }

    operator fun set(key: K, value: V) {
        if (!this.isKey(key)) {
            this.add(key, value)
        }
        else {
            this.getNode(key)!!.value = value
            // not-null assertion operator because key is exist in tree
        }
    }

    operator fun get(key: K): V? {
        var node: Node<K, V, T>? = getNode(key)
        return if (node == null) null else node.value
    }
    fun getNode(key: K): Node<K, V, T>? {
        return getNode(key, this.root)
    }

    private tailrec fun getNode(key: K, current: Node<K, V, T>?): Node<K, V, T>? {
        return if (current == null) null
        else if (current.key == key) current
        else if (current.key > key) getNode(key, current.left as Node<K, V, T>?)
        else getNode(key, current.right as Node<K, V, T>?)
    }

    fun getOrDefault(key: K, default: V): V {
        var node: Node<K, V, T>? = getNode(key)
        return node?.value ?: default
    }
    fun getOrDefaultNode(key: K, default: Node<K, V, T>): Node<K, V, T> {
        return getNode(key, root) ?: default
    }

    open fun delete(key: K) {
        if (this.getNode(key) != null) this.delete(this.getNode(key)!!)
    }

    private fun delete(current: Node<K, V, T>) {
        var parent = current.parent
        if (current.left == null && current.right == null) {
            if (parent == null) {
                this.root = null
            } else if ((parent as Node<K, V, T>).left === current) {
                (parent as Node<K, V, T>).left = null
            } else {
                (parent as Node<K, V, T>).right = null
            }
        } else if (current.left == null || current.right == null) {
            if (current.left == null) {
                if (parent == null) {
                    this.root = current.right as Node<K, V, T>
                } else {
                    if ((parent as Node<K, V, T>).left === current) {
                        (parent as Node<K, V, T>).left = current.right
                    } else {
                        (parent as Node<K, V, T>).right = current.right
                    }
                }
                (current.right as Node<K, V, T>).parent = parent
            } else {
                if (parent == null) {
                    this.root = current.left as Node<K, V, T>
                } else {
                    if ((parent as Node<K, V, T>).left === current) {
                        (parent as Node<K, V, T>).left = current.left
                    } else {
                        (parent as Node<K, V, T>).right = current.left
                    }
                    (current.left as Node<K, V, T>).parent = parent
                }
            }
        } else {
            var next = getNext(current)!!
            current.key = next.key
            if ((next.parent as Node<K, V, T>).left === next) {
                (next.parent as Node<K, V, T>).left = next.right
                if (next.right != null) {
                    (next.right as Node<K, V, T>).parent = next.parent
                }
            } else {
                (next.parent as Node<K, V, T>).right = next.right
                if (next.right != null) {
                    (next.right as Node<K, V, T>).parent = next.parent
                }
            }

            next.left = current.left
            if (next.left != null) (next.left as Node<K, V, T>).parent = next as T
            next.right = current.right
            if (next.left != null) (next.right as Node<K, V, T>).parent = next as T
            next.parent = current.parent
            if (next.parent != null) {
                if ((next.parent as Node<K, V, T>).left === current) {
                    (next.parent as Node<K, V, T>).left = next as T
                } else {
                    (next.parent as Node<K, V, T>).right = next as T
                }
            } else {
                this.root = next
                next.parent = null
            }
        }
    }

    fun isKey(key: K): Boolean {
        return isKey(key, this.root)
    }

    private tailrec fun isKey(key: K, current: Node<K, V, T>?): Boolean {
        if (current == null) return false
        else if (current.key == key) return true
        else if (current.key > key) return isKey(key, current.left as Node<K, V, T>?)
        else return isKey(key, current.right as Node<K, V, T>?)
    }

    open fun min(): Node<K, V, T>? {
        return if (root == null) null else min(root)
    }

    private fun min(node: Node<K, V, T>?): Node<K, V, T>? {
        if (node != null) {
            return if (node.left == null) node else min(node.left as Node<K, V, T>?)
        }
        else return null
    }

    open fun max(): Node<K, V, T>? {
        return if (root == null) null else max(root)
    }

    private fun max(node: Node<K, V, T>?): Node<K, V, T>? {
        if (node != null) {
            return if (node.right == null) node else min(node.right as Node<K, V, T>?)
        }
        else return null
    }

    fun getNext(node: Node<K, V, T>): Node<K, V, T>? {
        if (node.right != null) {
            // If the right subtree is not null, the successor is the leftmost node in the right subtree
            return min(node.right as Node<K, V, T>)
        }

        // If the right subtree is null, the successor is the first ancestor whose left child is also an ancestor
        var current = node
        var parent = node.parent as? Node<K, V, T>
        while (parent != null && current == parent.right) {

            current = parent
            parent = parent.parent as? Node<K, V, T>
        }
        return parent
    }

    fun getPrev(node: Node<K, V, T>): Node<K, V, T>? {
        if (node.left != null) {
            // If the left subtree is not null, the predecessor is the rightmost node in the left subtree
            return max(node.left as? Node<K, V, T>)
        }

        // If the left subtree is null, the predecessor is the first ancestor whose right child is also an ancestor
        var current = node
        var parent = node.parent as? Node<K, V, T>
        while (parent != null && current == parent.left) {
            current = parent
            parent = parent.parent as? Node<K, V, T>
        }
        return parent
    }

    inner class ByKeyIterator<K : Comparable<K>, V: Any>(private val tree: Tree<K, V, T>) :
        Iterator<Node<K, V, T>> {
        private var current: Node<K, V, T>? = tree.min()

        override fun hasNext(): Boolean {
            return current != null
        }

        override fun next(): Node<K, V, T> {
            val result = current ?: throw NoSuchElementException("No more elements")
            current = tree.getNext(result)
            return result
        }
    }

    inner class BFSIterator(tree: Tree<K, V, T>): Iterator<Node<K, V, T>> {

        var queue: Queue<Node<K, V, T>> = LinkedList<Node<K, V, T>>()

        init {
            var BSIqueue: Queue<Node<K, V, T>> = LinkedList<Node<K, V, T>>()
            if (tree.root != null) {
                BSIqueue.add(tree.root)
                queue.add(tree.root)
                while (!BSIqueue.isEmpty()) {
                    var current: Node<K, V, T> = BSIqueue.poll()
                    if (current.left != null) {
                        BSIqueue.add(current.left as Node<K, V, T>)
                        queue.add(current.left as Node<K, V, T>)
                    }
                    if (current.right != null) {
                        BSIqueue.add(current.right as Node<K, V, T>)
                        queue.add(current.right as Node<K, V, T>)
                    }
                }
            }
        }

        override fun hasNext(): Boolean {
            return !queue.isEmpty()
        }

        override fun next(): Node<K, V, T> {
            return queue.poll()
        }

    }

    inner class DFSIterator(tree: Tree<K, V, T>): Iterator<Node<K, V, T>> {

        var stack: Stack<Node<K, V, T>> = Stack<Node<K, V, T>>()

        init {
            var DFSstack: Stack<Node<K, V, T>> = Stack<Node<K, V, T>>()
            if (tree.root != null) {
                DFSstack.add(tree.root)
                stack.add(tree.root)
                while (!DFSstack.isEmpty()) {
                    var current: Node<K, V, T> = DFSstack.pop()
                    if (current.left != null) {
                        DFSstack.add(current.left as Node<K, V, T>)
                        stack.add(current.left as Node<K, V, T>)
                    }
                    if (current.right != null) {
                        DFSstack.add(current.right as Node<K, V, T>)
                        stack.add(current.right as Node<K, V, T>)
                    }
                }
            }
        }

        override fun hasNext(): Boolean {
            return !stack.isEmpty()
        }

        override fun next(): Node<K, V, T> {
            return stack.pop()
        }

    }

    override fun iterator(): Iterator<Node<K, V, T>> {
        return ByKeyIterator(this)
    }

    fun iterateBFS(): BFSIterator {
        return BFSIterator(this)
    }

    fun iterateDFS(): DFSIterator {
        return DFSIterator(this)
    }

    open fun merge(tree: Tree<K, V, T>) {
        if (this.root != null && tree.root != null) {
            require(this.max()!!.key < tree.min()!!.key) {
                "Merge operation is defined only when attachable tree's keys is always bigger than base tree's keys"
            }
        }
        if (this.root == null) this.root = tree.root
        else this.max()!!.right = tree.root as T?
    }

    fun split(x: K): Pair<Tree<K, V, T>, Tree<K, V, T>> {
        var lowerTree: Tree<K, V, T> = Tree<K, V, T>()
        var biggerTree: Tree<K, V, T> = Tree<K, V, T>()

        for (i in this.iterateBFS()) {
            if (i.key < x) lowerTree.addNode(i)
            else biggerTree.addNode(i)
        }
        return Pair(lowerTree, biggerTree)
    }

    fun clone(): Tree<K, V, T> {
        var clonedTree: Tree<K, V, T> = Tree<K, V, T>()

        for (i in this.iterateBFS()) clonedTree.add(i.key, i.value)
        return clonedTree
    }

    open fun toStringBeautifulWidth(): String {
        return if (this.root == null) ""
        else this.toStringBeautifulWidth(StringBuilder(), true, StringBuilder(), this.root!!).toString()
        // not-null assertion operator because null check
    }

    private fun toStringBeautifulWidth(
        prefix: StringBuilder,
        isTail: Boolean,
        buffer: StringBuilder,
        current: Node<K, V, T>): StringBuilder
    {
        if (current.right != null) {
            var newPrefix = StringBuilder()
            newPrefix.append(prefix)
            newPrefix.append(if (isTail) "|   " else "    ")
            this.toStringBeautifulWidth(newPrefix, false, buffer, current.right as Node<K, V, T>)
        }
        buffer.append(prefix)
        buffer.append(if (isTail) "└── " else "┌── ")
        buffer.append(current.toString())
        buffer.append("\n")
        if (current.left != null) {
            var newPrefix = StringBuilder()
            newPrefix.append(prefix)
            newPrefix.append(if (!isTail) "|   " else "    ")
            this.toStringBeautifulWidth(newPrefix, true, buffer, current.left as Node<K, V, T>)
        }

        return buffer
    }

    fun toStringBeautifulHeight(): String {
        if (this.root == null) return ""
        else {
            var buffer: StringBuilder = StringBuilder()

            var lines: MutableList<MutableList<String?>> = mutableListOf<MutableList<String?>>()

            var level: MutableList<Node<K, V, T>?> = mutableListOf<Node<K, V, T>?>()
            var next: MutableList<Node<K, V, T>?> = mutableListOf<Node<K, V, T>?>()

            level.add(this.root)

            var nodeNumber: Int = 1
            var widtest: Int = 0

            while (nodeNumber != 0) {
                var line: MutableList<String?> = mutableListOf<String?>()

                nodeNumber = 0

                for (node in level) {
                    if (node == null) {
                        line.add(null)

                        next.add(null)
                        next.add(null)
                    } else {
                        var strNode: String = node.toString()
                        line.addLast(strNode)

                        if (strNode.length > widtest) widtest = strNode.length

                        next.add(node.left as Node<K, V, T>?)
                        next.add(node.right as Node<K, V, T>?)

                        if (node.left != null) nodeNumber++
                        if (node.right != null) nodeNumber++
                    }
                }

                widtest += widtest % 2

                lines.add(line)
                var swap = level
                level = next
                next = swap
                next.clear()
            }

            var perpiece: Int = lines[lines.size - 1].size * (widtest + 4)

            for (i in 1..perpiece / 2) buffer.append("─")
            buffer.append("┐\n")

            for (i in 0..(lines.size - 1)) {
                var line: MutableList<String?> = lines[i]

                var hpw: Int = floor(perpiece / 2f - 1).toInt()

                if (i > 0) {
                    for (j in 0..(line.size - 1)) {
                        var c: Char = ' '

                        if (j % 2 == 1) {
                            if (line[j - 1] != null) {
                                c = if (line[j] != null) '┴' else '┘'
                            } else {
                                if (j < line.size && line[j] != null) c = '└'
                            }
                        }
                        buffer.append(c)

                        if (line[j] == null) {
                            for (k in 0..(perpiece - 2)) {
                                buffer.append(" ")
                            }
                        } else {
                            for (k in 0..(hpw - 1)) {
                                buffer.append(if (j % 2 == 0) " " else "─")
                            }
                            buffer.append(if (j % 2 == 0) "┌" else "┐")
                            for (k in 0..(hpw - 1)) {
                                buffer.append(if (j % 2 != 0) " " else "─")
                            }
                        }
                    }
                    buffer.append("\n")
                }
                for (j in 0..(line.size - 1)) {
                    var f: String? = line[j]
                    if (f == null) f = ""
                    var gap1: Int = ceil(perpiece / 2f - f.length / 2f).toInt()
                    var gap2: Int = floor(perpiece / 2f - f.length / 2f).toInt()

                    for (k in 1..gap1) {
                        buffer.append(" ")
                    }
                    buffer.append(f)
                    for (k in 1..gap2) {
                        buffer.append(" ")
                    }
                }
                buffer.append("\n")
                perpiece /= 2
            }
            return buffer.toString()
        }
    }
}