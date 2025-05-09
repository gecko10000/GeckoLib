package gecko10000.geckolib.extensions

fun <T> List<T>.updated(index: Int, block: (T) -> T) = this.updated(index, block(this[index]))

fun <T> List<T>.updated(index: Int, value: T): List<T> {
    return buildList {
        this.addAll(this@updated.subList(0, index))
        this.add(value)
        this.addAll(this@updated.subList(index + 1, this@updated.size))
    }
}

fun <T> List<T>.extend(element: T, amount: Int? = null, targetAmount: Int? = null): List<T> {
    val toAdd = amount ?: (targetAmount!! - this.size)
    if (toAdd <= 0) return this
    return this.plus(List(toAdd) { element })
}
