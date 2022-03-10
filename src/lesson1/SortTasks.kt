@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import java.lang.IllegalArgumentException

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortTimes(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 *
 * Ресурсоемкость : O(n*log(n)) = O(n*log(n)) + O(n*log(n))
 * Трудоемкость: O(n)
 */
fun sortAddresses(inputName: String, outputName: String) {
    val map = mutableMapOf<String, MutableList<String>>()
    for (line in File(inputName).readLines()) {
        if (!Regex("[А-яёЁ]+ [А-яёЁ]+ - [А-яёЁ-]+ \\d+").matches(line)) throw IllegalArgumentException("$line")
        val (name, address) = line.split(" - ")
        val newNames = map.getOrDefault(address, mutableListOf())
        newNames.add(name)
        map[address] = newNames
    }
    val sortedMap = map.toSortedMap(
        compareBy<String> { it.split(" ")[0] }.thenBy { it.split(" ")[1].toInt() }) //O(n*log(n))
    sortedMap.map { it.value.sort() } //O(n*log(n))

    File(outputName).bufferedWriter().use { writer ->
        for ((address, names) in sortedMap) {
            val namesToString = names.joinToString(", ")
            writer.write("$address - $namesToString")
            writer.newLine()
        }
    }
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */
fun sortTemperatures(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 *
 * Ресурсоемкость : O(n) = O(n) + O(n))
 * Трудоемкость: O(n)
 */
fun sortSequence(inputName: String, outputName: String) {
    val digitsFrequency = mutableMapOf<Int, Int>()
    val digits = File(inputName).readLines().map { it.toInt() }
    var max = Int.MAX_VALUE
    digitsFrequency[max] = 0
    for (digit in digits) { // O(n)
        digitsFrequency[digit] = digitsFrequency.getOrDefault(digit, 0) + 1
        if (digitsFrequency[digit]!! == digitsFrequency[max]!! && digit < max ||
            digitsFrequency[digit]!! > digitsFrequency[max]!!
        )
            max = digit
    }

    File(outputName).bufferedWriter().use { writer -> //O(n)
        for (digit in digits) {
            if (digit != max) {
                writer.write("$digit")
                writer.newLine()
            }
        }
        for (i in 1..digitsFrequency[max]!!) {
            writer.write("$max")
            writer.newLine()
        }
    }
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    TODO()
}

