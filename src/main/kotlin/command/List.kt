package command

import Config
import ago
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.enum
import net.pproj.tutter.api.AuthorApi
import net.pproj.tutter.api.PostApi
import net.pproj.tutter.api.TagApi
import net.pproj.tutter.model.PostWithAuthor

enum class ListSubject {
    Tutts, Tags, Authors;
}

val subjects = ListSubject.values().joinToString(", ") { it.name.toLowerCase() }

class List : CliktCommand(help = "Listing for $subjects") {
    private val subject by argument().enum<ListSubject>()

    override fun run() {
        try {
            when (subject) {
                ListSubject.Tutts -> listTutts()
                ListSubject.Tags -> listTags()
                ListSubject.Authors -> listAuthors()
            }
        } catch (e: Exception) {
            echo("Cannot list ${subject}: ${e.message}")
        }
    }

    private fun listAuthors() = AuthorApi(Config.baseUrl).authorGet().forEach {
        echo("${it.name} (first seen ${it.firstSeen})")
    }

    private fun listTags() = TagApi(Config.baseUrl).tagGet().forEach {
        echo("#{it.tag} (first seen ${it.firstSeen})")
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

