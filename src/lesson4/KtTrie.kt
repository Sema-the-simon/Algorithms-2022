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

        /**
         * храним текущую ветку в виде String
         * подъем по ветке осуществляется с помощбю стэка
         * запоминаем всех "родственников" текущего узла в стэк
         * затем при необходимости начинаем спуск с нужного узла - родственника
         * для пропуска нескольких узлов без детей используем пустые узлы с именем 1.toChar
         **/

        private var curWord = ""
        private val nodesStack = ArrayDeque<Pair<Char, Node>>()
        private var curNode: Node? = null
        private var curNodeName: Char? = null
        private var removableString = ""

        init {
            findLift(root, null)
            if (curNodeName != 0.toChar()) initWord()
        }


        /**
         * функция для поиска самого "нижнего" и левого листа среди детей текущего
         * запоминаем путь по которому двигаемся в curWord
         *
         * трудоемкость: O(h), где h - высота дерева
         * ресурсоемкость: O(n), где n - кол-во узлов
         **/
        private fun findLift(node: Node, nodeName: Char?) {
            curNode = node
            curNodeName = nodeName
            while (curNode!!.children.isNotEmpty()) {
                if (curNodeName != 0.toChar() && curNodeName != null)
                    curWord += curNodeName

                nodesStack.addFirst(1.toChar() to Node())
                val children = sortedMapOf<Char, Node>()
                children.putAll(curNode!!.children)
                curNodeName = children.firstKey()
                curNode = children[curNodeName]!!

                children.remove(children.firstKey())
                addToNodesStack(children)

            }
        }

        /**
         * функция для добавления в стэк всех "родственников"
         * все "родственники" - siblings добавляются в конец стэка в порядке возрастания
         **/
        private fun addToNodesStack(sortedMap: SortedMap<Char, Node>) {
            val listOfPairs = sortedMap.toList()
            for (i in listOfPairs.size - 1 downTo 0) {
                nodesStack.addFirst(listOfPairs[i])
            }
        }

        /**
         * функция для поиска слудующего подходящего узла для next()
         * подъем до нужного родителя с другими детьми затем
         * вызов findLift() - спуск к листьяи дерева
         *
         * трудоемкость: O(h), где h - высота дерева
         * ресурсоемкость: O(n), где n - кол-во узлов
         **/
        private fun initWord() {
            while (nodesStack.isNotEmpty()) {
                val pair = nodesStack.removeFirst()
                curNodeName = pair.first
                curNode = pair.second
                if (curNodeName == 1.toChar()) {
                    if (curWord.isNotEmpty())
                        curWord = curWord.dropLast(1)
                } else {
                    findLift(curNode!!, curNodeName)
                    if (curNodeName == 0.toChar()) break
                }
            }
        }

        //трудоемкость: O(1)
        //ресурсоемкость O(1)
        override fun hasNext(): Boolean = curNodeName != null && curNodeName == 0.toChar()

        //трудоемкость: O(h), где h - высота дерева
        //ресурсоемкость: O(n), где n - кол-во узлов
        override fun next(): String {
            if (!hasNext()) throw NoSuchElementException()
            val next = curWord
            initWord()
            removableString = next
            return next
        }

        // трудоемкость: O(h * log(n)) h - высота дерева, n - размер алфавита
        // ресурсоемкость: O(1)
        override fun remove() {
            if (removableString.isEmpty()) throw IllegalStateException()
            remove(removableString)
            removableString = ""
        }
    }
}
