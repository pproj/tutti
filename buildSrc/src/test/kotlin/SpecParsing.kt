import generator.ModelGenerator
import io.swagger.v3.parser.OpenAPIV3Parser
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class SpecParsing {
    val specFile = File("src/test/resources/spec.yaml")

    @Test
    fun title() {
        val spec = OpenAPIV3Parser().read(specFile.absolutePath)
        assertEquals("Tutter - OpenAPI 3.0", spec.info.title)
        ModelGenerator(spec).generate()
    }
}