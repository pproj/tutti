import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import command.Create
import command.List

class Config {
    companion object {
        val baseUrl by lazy {
            System.getenv("TUTTI_BASE_URL") ?: "http://localhost:8080/api"
        }
    }
}

class Tutti : CliktCommand() {
    override fun run() = Unit
}

fun main(args: Array<String>) = Tutti()
    .subcommands(List(), Create())
    .main(args)
