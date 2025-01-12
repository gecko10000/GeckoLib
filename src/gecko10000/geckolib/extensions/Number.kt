package gecko10000.geckolib.extensions

fun Number.smartS(): String {
    return if (this == 1) "" else "s"
}
