import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import task.GitRepoGetter

class TuttiBuildPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val tasks = project.tasks
        val downloadSpec = tasks.register<GitRepoGetter>("downloadTutterRepo")

        tasks.getByName("compileKotlin").dependsOn(downloadSpec)
    }
}
