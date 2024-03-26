import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

open class Tree <K: Comparable<K>, V: Any, T: Node<K, V, T>> (
    var root: Node<K, V, T>? = null
): Iterable<Node<K, V, T>> {
    open fun add(key: K, value: V) {
        val node: Node<K, V, T> = Node<K, V, T>(key, value)
        this.addNode(node)
    }
    open fun addNode(node: Node<K, V, T>) {
        if (this.root == null) {
            this.root = node
        } else {
            val root: Node<K, V, T> = this.root!!
            // not-null assertion operator because null check
            val isLeft: Boolean = root.key > node.key
            addNode(node,
                (if (isLeft) root.left else root.right),
                root,
                isLeft)
        }
    }

    private tailrec fun addNode(node: Node<K, V, T>, current: Node<K, V, T>?, parent: Node<K, V, T>, isLeft: Boolean) {
        if (current == null) {
            if (isLeft) parent.left = node
            else parent.right = node

            node.parent = parent
        } else {
            require(current.key != node.key) {
                throw IllegalArgumentException("Multiple uses of key: ${node.key}. To modify value use set function")
            }
            val isAddedLeft: Boolean = current.key > node.key
            addNode(node,
                (if (isAddedLeft) current.left else current.right),
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

    operator fun get(key: K): V? = getNode(key)?.value

    fun getNode(key: K): Node<K, V, T>? {
        return getNode(key, this.root)
    }

    private tailrec fun getNode(key: K, current: Node<K, V, T>?): Node<K, V, T>? {
        return if (current == null) null
        else if (current.key == key) current
        else if (current.key > key) getNode(key, current.left)
        else getNode(key, current.right)
    }

    fun getOrDefault(key: K, default: V): V {
        val node: Node<K, V, T>? = getNode(key)
        return node?.value ?: default
    }
    fun getOrDefaultNode(key: K, default: Node<K, V, T>): Node<K, V, T> {
        return getNode(key, root) ?: default
    }

    open fun delete(key: K) {
        if (this.getNode(key) != null) this.delete(this.getNode(key)!!)
    }

    private fun delete(current: Node<K, V, T>) {
        val parent = current.parent
        if (current.left == null && current.right == null) {
            if (parent == null) {
                this.root = null
            } else if (parent.left === current) {
                parent.left = null
            } else {
                parent.right = null
            }
        } else if (current.left == null || current.right == null) {
            if (current.left == null) {
                if (parent == null) {
                    this.root = current.right as Node<K, V, T>
                } else {
                    if (parent.left === current) {
                        parent.left = current.right
                    } else {
                        parent.right = current.right
                    }
                }
                (current.right as Node<K, V, T>).parent = parent
            } else {
                if (parent == null) {
                    this.root = current.left as Node<K, V, T>
                } else {
                    if (parent.left === current) {
                        parent.left = current.left
                    } else {
                        parent.right = current.left
                    }
                    (current.left as Node<K, V, T>).parent = parent
                }
            }
        } else {
            val next = getNext(current)!!
            current.key = next.key
            current.value = next.value
            if (next.parent!!.left === next) {
                next.parent!!.left = next.right
                if (next.right != null) {
                    next.right!!.parent = next.parent
                }
            } else {
                next.parent!!.right = next.right
                if (next.right != null) {
                    next.right!!.parent = next.parent
                }
            }

            next.left = current.left
            if (next.left != null) next.left!!.parent = next
            next.right = current.right
            if (next.right != null) next.right!!.parent = next
            next.parent = current.parent
            if (next.parent != null) {
                if (next.parent!!.left === current) {
                    next.parent!!.left = next
                } else {
                    next.parent!!.right = next
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
        return if (current == null) false
        else if (current.key == key) true
        else if (current.key > key) isKey(key, current.left)
        else isKey(key, current.right)
    }

    open fun min(): Node<K, V, T>? {
        val rootNow = this.root
        return if (rootNow == null) null else min(rootNow)
    }

    internal fun min(node: Node<K, V, T>): Node<K, V, T>? {
        val left = node.left
        return if (left == null) node else min(left)
    }

    open fun max(): Node<K, V, T>? {
        val rootNow = this.root
        return if (rootNow == null) null else max(rootNow)
    }

    internal fun max(node: Node<K, V, T>): Node<K, V, T>? {
        val right = node.right
        return if (right == null) node else max(right)
    }

    fun getNext(node: Node<K, V, T>): Node<K, V, T>? {
        val right = node.right
        if (right != null) {
            // If the right subtree is not null, the successor is the leftmost node in the right subtree
            return min(right)
        }

        // If the right subtree is null, the successor is the first ancestor whose left child is also an ancestor
        var current = node
        var parent = node.parent
        while (parent != null && current == parent.right) {
            current = parent
            parent = parent.parent
        }
        return parent
    }

    fun getPrev(node: Node<K, V, T>): Node<K, V, T>? {
        val left = node.left
        if (left != null) {
            // If the left subtree is not null, the predecessor is the rightmost node in the left subtree
            return max(left)
        }

        // If the left subtree is null, the predecessor is the first ancestor whose right child is also an ancestor
        var current = node
        var parent = node.parent
        while (parent != null && current == parent.left) {
            current = parent
            parent = parent.parent
        }
        return parent
    }

    inner class ByKeyIterator(
        private val tree: Tree<K, V, T>
    ) : Iterator<Node<K, V, T>> {
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

        private var queue: Queue<Node<K, V, T>> = LinkedList()

        init {
            val BSIqueue: Queue<Node<K, V, T>> = LinkedList()
            if (tree.root != null) {
                BSIqueue.add(tree.root)
                queue.add(tree.root)
                while (!BSIqueue.isEmpty()) {
                    val current: Node<K, V, T> = BSIqueue.poll()
                    if (current.left != null) {
                        BSIqueue.add(current.left)
                        queue.add(current.left)
                    }
                    if (current.right != null) {
                        BSIqueue.add(current.right)
                        queue.add(current.right)
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

        private var stack: Queue<Node<K, V, T>> = LinkedList()

        init {
            addToStack(tree.root)
        }

        private fun addToStack(current: Node<K, V, T>?) {
            if (current != null) {
                stack.add(current)
                this.addToStack(current.left)
                this.addToStack(current.right)
            }
        }

        override fun hasNext(): Boolean {
            return !stack.isEmpty()
        }

        override fun next(): Node<K, V, T> {
            return stack.poll()
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
        else this.max()!!.right = tree.root
    }

    fun split(x: K): Pair<Tree<K, V, T>, Tree<K, V, T>> {
        val lowerTree: Tree<K, V, T> = Tree()
        val biggerTree: Tree<K, V, T> = Tree()

        for (i in this.iterateBFS()) {
            if (i.key < x) lowerTree.addNode(i)
            else biggerTree.addNode(i)
        }
        return Pair(lowerTree, biggerTree)
    }

    fun clone(): Tree<K, V, T> {
        val clonedTree: Tree<K, V, T> = Tree()

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
            val newPrefix = StringBuilder()
            newPrefix.append(prefix)
            newPrefix.append(if (isTail) "|   " else "    ")
            this.toStringBeautifulWidth(newPrefix, false, buffer, current.right as Node<K, V, T>)
        }
        buffer.append(prefix)
        buffer.append(if (isTail) "└── " else "┌── ")
        buffer.append(current.toString())
        buffer.append("\n")
        if (current.left != null) {
            val newPrefix = StringBuilder()
            newPrefix.append(prefix)
            newPrefix.append(if (!isTail) "|   " else "    ")
            this.toStringBeautifulWidth(newPrefix, true, buffer, current.left as Node<K, V, T>)
        }

        return buffer
    }

    fun toStringBeautifulHeight(): String {
        if (this.root == null) return ""
        else {
            val buffer: StringBuilder = StringBuilder()

            val lines: MutableList<MutableList<String?>> = mutableListOf()

            var level: MutableList<Node<K, V, T>?> = mutableListOf()
            var next: MutableList<Node<K, V, T>?> = mutableListOf()

            level.add(this.root)

            var nodeNumber = 1
            var widtest = 0

            while (nodeNumber != 0) {
                val line: MutableList<String?> = mutableListOf()

                nodeNumber = 0

                for (node in level) {
                    if (node == null) {
                        line.add(null)

                        next.add(null)
                        next.add(null)
                    } else {
                        val strNode: String = node.toString()
                        line.addLast(strNode)

                        if (strNode.length > widtest) widtest = strNode.length

                        next.add(node.left)
                        next.add(node.right)

                        if (node.left != null) nodeNumber++
                        if (node.right != null) nodeNumber++
                    }
                }

                widtest += widtest % 2

                lines.add(line)
                val swap = level
                level = next
                next = swap
                next.clear()
            }

            var perpiece: Int = lines[lines.size - 1].size * (widtest + 4)

            for (i in 1..perpiece / 2) buffer.append("─")
            buffer.append("┐\n")

            for (i in 0..<lines.size) {
                val line: MutableList<String?> = lines[i]

                val hpw: Int = floor(perpiece / 2f - 1).toInt()

                if (i > 0) {
                    for (j in 0..<line.size) {
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
                            for (k in 0..<hpw) {
                                buffer.append(if (j % 2 == 0) " " else "─")
                            }
                            buffer.append(if (j % 2 == 0) "┌" else "┐")
                            for (k in 0..<hpw) {
                                buffer.append(if (j % 2 != 0) " " else "─")
                            }
                        }
                    }
                    buffer.append("\n")
                }
                for (j in 0..<line.size) {
                    var f: String? = line[j]
                    if (f == null) f = ""
                    val gap1: Int = ceil(perpiece / 2f - f.length / 2f).toInt()
                    val gap2: Int = floor(perpiece / 2f - f.length / 2f).toInt()

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