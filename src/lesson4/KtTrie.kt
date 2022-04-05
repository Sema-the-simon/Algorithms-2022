package lesson4

import java.util.*
import kotlin.collections.ArrayDeque

/**
 * Префиксное дерево для строк
 */
class KtTrie : AbstractMutableSet<String>(), MutableSet<String> {

    private class Node {
        val children: SortedMap<Char, Node> = sortedMapOf()
    }

    private val root = Node()

    override var size: Int = 0
        private set

    override fun clear() {
        root.children.clear()
        size = 0
    }

    private fun String.withZero() = this + 0.toChar()

    private fun findNode(element: String): Node? {
        var current = root
        for (char in element) {
            current = current.children[char] ?: return null
        }
        return current
    }

    override fun contains(element: String): Boolean =
        findNode(element.withZero()) != null

    override fun add(element: String): Boolean {
        var current = root
        var modified = false
        for (char in element.withZero()) {
            val child = current.children[char]
            if (child != null) {
                current = child
            } else {
                modified = true
                val newChild = Node()
                current.children[char] = newChild
                current = newChild
            }
        }
        if (modified) {
            size++
        }
        return modified
    }

    override fun remove(element: String): Boolean {
        val current = findNode(element) ?: return false
        if (current.children.remove(0.toChar()) != null) {
            size--
            return true
        }
        return false
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: [java.util.Iterator] (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    override fun iterator(): MutableIterator<String> = TrieIterator()

    inner class TrieIterator internal constructor() : MutableIterator<String> {

        private val queue = ArrayDeque<String>()
        private var current: String? = null

        private fun initStack(node: Node, resultStr: String) { //O(n) n = size - кол-во.
            for ((char, child) in node.children) {
                if (char == 0.toChar()) queue.addLast(resultStr)
                else initStack(child, resultStr + char)
            }
        }

        init {
            initStack(root, "")
        }

        //трудоемкость: O(1)
        //ресурсоемкость O(1)
        override fun hasNext(): Boolean = queue.isNotEmpty()

        //трудоемкость: O(1)
        //ресурсоемкость O(1)
        override fun next(): String {
            if (!hasNext()) throw NoSuchElementException()
            current = queue.removeFirst()
            return current!!
        }

        // трудоемкость: O(h * log(n)) h - высота дерева, n - размер алфавита
        // ресурсоемкость: O(1)
        override fun remove() {
            if (current == null) throw IllegalStateException()
            remove(current)
            current = null
        }
    }
}
