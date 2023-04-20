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

class List : CliktCommand(help = "List tutts, tags, or authors") {
    val subject by argument().enum<ListSubject>()

    override fun run() {
        try {
            when (subject) {
                ListSubject.Tutts -> listTutts()
                ListSubject.Tags -> echo("Listing tags")
                ListSubject.Authors -> echo("Listing authors")
            }
        } catch (e: Exception) {
            echo("Cannot list ${subject.toString()}: ${e.message}")
        }
    }

    private fun listTutts() {
        val tutts = PostApi(Config.baseUrl).postGet()
        if (tutts.isEmpty()) {
            echo("No tutts yet.")
            return
        }

        tutts.forEach {
            echo(it.formated())
            echo("---")
        }
    }
}

private fun PostWithAuthor.formated(): String {
    val ago = this.createdAt.ago()
    return """
        |$ago by ${this.author?.name}:
        |${this.text}
    """.trimMargin()
}

