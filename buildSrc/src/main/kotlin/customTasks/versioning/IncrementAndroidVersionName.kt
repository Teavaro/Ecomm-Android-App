package customTasks.versioning

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import utils.GradleUtils
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files

// Gradle classes should always be open
open class IncrementAndroidVersionName: DefaultTask() {

    @get:Input
    var releaseType: ReleaseType = ReleaseType.Minor

    enum class ReleaseType {
        Major, Minor, Fix
    }

    @TaskAction
    fun action() {
        val updatedVersionName = getUpdatedVersionName(Build.versionName)
        val file = GradleUtils.androidBuildVersionsFile
        val fileLines = Files.readAllLines(file.toPath(), Charset.defaultCharset())
        val updatedLines = GradleUtils.getUpdatedLines(fileLines, "versionName", updatedVersionName)
        try {
            Files.write(file.toPath(), updatedLines, Charset.defaultCharset())
        }
        catch (e: IOException) {
            println(e)
        }
    }


    private fun getUpdatedVersionName(currentVersionName: String): String {
        val versionParts = currentVersionName.split(".")
        var major = versionParts.first().toInt()
        var minor = versionParts[1].toInt()
        var fix = versionParts[2].toInt()
        when(releaseType) {
            ReleaseType.Major -> {
                major += 1
                minor = 0
                fix = 0
            }
            ReleaseType.Minor -> {
                minor += 1
                fix = 0
            }
            ReleaseType.Fix -> fix += 1
        }
        return "$major.$minor.$fix"
    }
}