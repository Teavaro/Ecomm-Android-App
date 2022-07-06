package customTasks.slackUploader

import java.io.File

data class SlackUploaderInfo(var channels: List<String>,
                             // Debug, Release,...
                             var variantName: String,
                             var comment: String = getChangeLog(),
                             var outputApkFolderPath: String = "./app/build/outputs/apk/") {

    companion object {
        /*
         There is a .txt file named ChangeLog where we can write a brief about what is new/changed
         in the .APK that we are about to post to Slack channel(s).
         We can leave it empty if not want to provide info about this APK
         but please make sure to update it or erase its previous content upon
         each release to avoid any confusion.
         */
        fun getChangeLog(): String {
            val changeLogFilePath = "./ChangeLog.txt"
            val file = File(changeLogFilePath)
            return file.readText()
        }
    }
}