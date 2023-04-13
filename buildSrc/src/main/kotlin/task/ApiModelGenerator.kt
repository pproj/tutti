package task

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class ApiModelGenerator : DefaultTask() {
    @get:Input
    abstract val specFile: Property<File>

    @TaskAction
    fun doSomething() {
        logger.debug("Spec file path: ${specFile.get()}")

    }
}