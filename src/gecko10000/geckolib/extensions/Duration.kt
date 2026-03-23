package gecko10000.geckolib.extensions

import kotlin.time.Duration

fun Duration.timer(): String {
    val hours = this.inWholeHours
    val minutes = this.inWholeMinutes % 60
    val seconds = this.inWholeSeconds % 60
    if (hours > 0) {
        return String.format("%d:%02d:%02d", hours, minutes, seconds)
    }
    return String.format("%d:%02d", minutes, seconds)
}
