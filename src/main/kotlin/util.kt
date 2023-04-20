import java.time.OffsetDateTime

fun OffsetDateTime?.ago(): String {
    if (this == null) return "never"
    val now = OffsetDateTime.now()
    val diff = now.toEpochSecond() - this.toEpochSecond()
    return when {
        diff < 60 -> "$diff seconds ago"
        diff < 60 * 60 -> "${diff / 60} minutes ago"
        diff < 60 * 60 * 24 -> "${diff / 60 * 60} hours ago"
        else -> "${diff / 60 * 60 * 24} days ago"
    }
}