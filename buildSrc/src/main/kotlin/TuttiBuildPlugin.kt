import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import task.GitRepoGetter

class TuttiBuildPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val tasks = project.tasks
        val downloadSpec = tasks.register<GitRepoGetter>("downloadTutterRepo")
//        val modelGen = tasks.register<ApiModelGenerator>("generateApiModels")
//        modelGen.configure {
//            val apiSpecPath = "apidoc/spec.yaml"
//            specFile.set(downloadSpec.flatMap { it.repoPath }.map { it.resolve(apiSpecPath) })
//            dependsOn(downloadSpec)
//        }
        tasks.getByName("compileKotlin").dependsOn(downloadSpec)
    }
}
