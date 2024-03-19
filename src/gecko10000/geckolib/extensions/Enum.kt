package gecko10000.geckolib.extensions
inline fun <reified T : Enum<T>> T.next(): T {
    val values = enumValues<T>()
    val next = (this.ordinal + 1) % values.size
    return values[next]
}

inline fun <reified T : Enum<T>> T.isMax(): Boolean {
    val count = enumValues<T>().size
    return this.ordinal == count - 1
}
