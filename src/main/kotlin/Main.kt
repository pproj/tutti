import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import command.Create
import command.List

class Config {
    companion object {
        val baseUrl by lazy {
            System.getenv("TUTTER_API_URL") ?: "https://tutter.k8s.marcsello.com/api"
        }
    }
}

class Tutti : CliktCommand() {
    override fun run() = Unit
}

fun main(args: Array<String>) = Tutti()
    .subcommands(List(), Create())
    .main(args)
