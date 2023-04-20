package command

import Config
import ago
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.enum
import net.pproj.tutter.api.PostApi
import net.pproj.tutter.model.PostWithAuthor

enum class ListSubject {
    Tutts, Tags, Authors;
}

class List : CliktCommand(help = "Listing for ${ListSubject.values().joinToString(", ") { it.name.toLowerCase() }}}") {
    private val subject by argument().enum<ListSubject>()

    override fun run() {
        try {
            when (subject) {
                ListSubject.Tutts -> listTutts()
                ListSubject.Tags -> echo("Listing tags")
                ListSubject.Authors -> echo("Listing authors")
            }
        } catch (e: Exception) {
            echo("Cannot list ${subject}: ${e.message}")
        }
    }

    private fun listTutts() = PostApi(Config.baseUrl).postGet().forEach {
            echo(it.humanFriendly())
            echo("---")
        }
}

private fun PostWithAuthor.humanFriendly(): String {
    val ago = this.createdAt.ago()
    return """
        |$ago by ${this.author?.name}:
        |${this.text}
    """.trimMargin()
}

