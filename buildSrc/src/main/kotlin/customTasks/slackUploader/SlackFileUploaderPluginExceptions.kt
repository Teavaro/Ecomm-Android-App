package customTasks.slackUploader

class SlackFileUploaderException(reason: String): Throwable(reason)

class InvalidFilePathException: Throwable("APK Path is invalid, please specify a correct APK path to upload")

class InvalidChannelException: Throwable("Please specify at least one Slack channel to upload the APK")

class InvalidSlackTokenException: Throwable("Missing Slack Bot token")