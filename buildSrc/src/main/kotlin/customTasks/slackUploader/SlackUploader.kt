package customTasks.slackUploader

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import utils.GradleSecrets
import java.io.File
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.InlineDataPart
import com.github.kittinunf.result.Result

open class SlackUploader: DefaultTask() {

    @get:Input
    lateinit var slackUploaderInfo: SlackUploaderInfo

    @TaskAction
    fun action() {
        if (!::slackUploaderInfo.isInitialized) return
        val token = GradleSecrets.getSlackToken()
        if (token.isEmpty())
            throw InvalidSlackTokenException()
        val channels = slackUploaderInfo.channels.joinToString(separator = ",")
        if (channels.isEmpty())
            throw InvalidChannelException()
        val comment = slackUploaderInfo.comment
        val variantName = slackUploaderInfo.variantName
        val outputFolderPath = "${slackUploaderInfo.outputApkFolderPath}/$variantName"
        File(outputFolderPath)
            .walk()
            .filter { item -> item.toString().endsWith(".apk") }
            .sortedBy { it.lastModified() }
            .firstOrNull()?.let {
                println("APK Path ${it.path}")
                upload(token, it, comment, channels)
            } ?:
            run {
                throw InvalidFilePathException()
            }
    }

    private fun upload(token: String, apk: File, comment: String, channels: String) {
        Fuel.upload("https://slack.com/api/files.upload")
            .add(InlineDataPart(channels, "channels"))
            .add(InlineDataPart(apk.name, "filename"))
            .add(InlineDataPart(comment, "initial_comment"))
            .add(FileDataPart(apk, name = "file", filename=apk.name))
            .header("Authorization", "Bearer $token")
            .responseString { result ->
                when(result) {
                    is Result.Success -> println(result.value)
                    is Result.Failure -> throw SlackFileUploaderException(result.error.message ?: result.getException().localizedMessage)
                }
            }.join()
    }
}