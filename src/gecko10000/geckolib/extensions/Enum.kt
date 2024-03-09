package gecko10000.geckolib.extensions
inline fun <reified T : Enum<T>> T.next(): T {
    val values = enumValues<T>()
    val next = (ordinal + 1) % values.size
    return values[next]
}
