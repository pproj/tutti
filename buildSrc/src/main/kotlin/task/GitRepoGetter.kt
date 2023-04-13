package task

import org.ajoberstar.grgit.Grgit
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class GitRepoGetter : DefaultTask() {
    @get:Input
    abstract val repoUrl: Property<String>

    @Internal
    val repoPath: Provider<File> = repoUrl.map {
        project.buildDir.resolve(repoNameFromUrl(it))
    }

    @TaskAction
    fun haveRepo() {
        ensureUpToDateRepo(repoPath.get())
    }

    private fun repoNameFromUrl(url: String): String {
        val lastSlash = url.lastIndexOf('/')
        val lastDot = url.lastIndexOf('.')
        return url.substring(lastSlash + 1, lastDot)
    }

    private fun ensureUpToDateRepo(repoPath: File) {
        if (repoPath.exists()) {
            logger.warn("Repo is present. Pulling.")
            Grgit.open { currentDir = repoPath }.pull()
            return
        }
        logger.info("Cloning repo into $repoPath")
        Grgit.clone {
            dir = repoPath
            uri = repoUrl.get()
        }
    }
}