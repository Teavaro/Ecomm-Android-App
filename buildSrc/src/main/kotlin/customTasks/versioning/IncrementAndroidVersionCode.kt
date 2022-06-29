package customTasks.versioning

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import utils.GradleUtils
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files

// Gradle classes should always be open
open class IncrementAndroidVersionCode: DefaultTask() {

    @TaskAction
    fun action() {
        val currentVersionCode = Build.versionCode
        val updatedVersionCode = currentVersionCode + 1
        val file = GradleUtils.androidBuildVersionsFile
        val fileLines = Files.readAllLines(file.toPath(), Charset.defaultCharset())
        val updatedLines = GradleUtils.getUpdatedLines(fileLines, "versionCode", updatedVersionCode)
        try {
            Files.write(file.toPath(), updatedLines, Charset.defaultCharset())
        }
        catch (e: IOException) {
            println(e)
        }
    }
}