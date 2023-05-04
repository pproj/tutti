package command

import Config
import ago
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.check
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.clikt.parameters.types.int
import net.pproj.tutter.api.AuthorApi
import net.pproj.tutter.api.PostApi
import net.pproj.tutter.api.TagApi
import net.pproj.tutter.client.infrastructure.ClientError
import net.pproj.tutter.client.infrastructure.ClientException
import net.pproj.tutter.model.PostWithAuthor

enum class ListSubject {
    Tutts, Tags, Authors;
}

val subjects = ListSubject.values().joinToString(", ") { it.name.lowercase() }

class List : CliktCommand(help = "Listing for $subjects") {
    // What to list
    private val subject by argument().enum<ListSubject>()

    /*
     * Filtering options
     */
    private val limit by option(
        "-n", "--limit",
        help = "Limit on the number of displayed items."
    ).int().default(10).check { it > 1 }
    private val tag by option("-t", "--tag", help = "Tag to filter by. (without '#') (e.g. --tag kotlin)")
    private val author by option("-a", "--author", help = "Author's name to filter by.")

    override fun run() {
        try {
            when (subject) {
                ListSubject.Tutts -> listTutts()
                ListSubject.Tags -> listTags()
                ListSubject.Authors -> listAuthors()
            }
        } catch (e: Exception) {
            echo("Cannot list ${subject}: ${e.message}")
            if (e is ClientException) {
                val resp = e.response
                if (resp is ClientError<*>) {
                    echo(resp.body)
                }
            }
        }
    }

    private fun listAuthors() = AuthorApi(Config.baseUrl).authorGet().forEach {
        echo("${it.name} (first seen ${it.firstSeen})")
    }

    private fun listTags() = TagApi(Config.baseUrl).tagGet().forEach {
        echo("#{it.tag} (first seen ${it.firstSeen})")
    }

    private fun listTutts() = PostApi(Config.baseUrl)
        .postGet(
            limit = limit,
            tag = tag,
            authorId = authorId(author)
        )
        .forEach {
            echo(it.humanFriendly())
            echo("---")
        }

    private fun authorId(author: String?): Int? {
        if (author == null)
            return null

        val record = AuthorApi(Config.baseUrl)
            .authorGet()
            .find { it.name == author } ?: throw IllegalArgumentException("Author '$author' does not exist.")

        return record.id
    }
}

private fun PostWithAuthor.humanFriendly(): String {
    val ago = this.createdAt.ago()
    return """
        |$ago by ${this.author?.name}:
        |${this.text}
    """.trimMargin()
}

