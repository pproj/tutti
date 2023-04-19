import net.pproj.tutter.api.AuthorApi

fun main() {
    println("Hello, world!")
    val baseUrl = "http://localhost:8080/api"
    val authors = AuthorApi(baseUrl).authorGet()
    println(authors)
}