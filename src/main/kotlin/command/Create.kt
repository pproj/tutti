package command

import Config
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.validate
import net.pproj.tutter.api.PostApi
import net.pproj.tutter.client.infrastructure.ClientError
import net.pproj.tutter.client.infrastructure.ClientException
import net.pproj.tutter.model.NewPostBody

class Create : CliktCommand(help = "Create a new tutt") {
    val author by option(
        "-a", "--author",
        help = "author of the tutt",
        envvar = "TUTTI_AUTHOR"
    ).default("not-set")
        .validate {
            require(it != "not-set") { "author is required (as envvar or flag)" }
            require(it.length > 2) { "author must be at least 3 characters long" }
        }

    override fun run() {
        echo("New tutt by $author read from stdin:")
        val text = generateSequence(::readLine)
            .map(String::trim)
            .toList()
            .dropLastWhile { it == "" } // drop empty lines at the end
            .joinToString("\n")

        if (text.isEmpty()) {
            echo("Empty tutt, aborting.")
            return
        }

        try {
            val post = PostApi(Config.baseUrl).postPost(
                NewPostBody(
                    author = author,
                    text = text,
                )
            )
            echo("Created tutt #${post.id}")
        } catch (e: ClientException) {
            echo(e.message)
            val resp = e.response
            if (resp is ClientError<*>) {
                echo(resp.body)
            }
        }
    }
}
