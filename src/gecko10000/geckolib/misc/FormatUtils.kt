package gecko10000.geckolib.misc

import java.util.*


object FormatUtils {

    fun String.titlecase(): String {
        val split = this.split(' ')
        val joiner = StringJoiner(" ")
        for (str in split) {
            if (str.isEmpty()) {
                joiner.add("")
                continue
            }
            if (str.length == 1) {
                joiner.add(str.uppercase())
                continue
            }
            joiner.add(str[0].uppercase() + str.substring(1).lowercase())
        }
        return joiner.toString()
    }

    /**
     * Splits a line of text across multiple lines, attempting to limit the length of each line
     * @param line The line to wrap
     * @param maxLength The max length to cap each line at
     * @return The list of separate lines
     */
    fun String.lineWrap(maxLength: Int): List<String> {
        val split = this.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val lines = mutableListOf<String>()
        var current = StringBuilder()
        for (word in split) {
            if (current.length + word.length + 1 <= maxLength) {
                current.append(word).append(' ')
            } else {
                if (current.isNotEmpty()) {
                    lines.add(current.substring(0, current.length - 1))
                }
                current = StringBuilder(word).append(' ')
            }
        }
        if (current.isNotEmpty()) {
            lines.add(current.substring(0, current.length - 1))
        }
        return lines
    }

}
