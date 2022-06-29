package utils

import java.io.FileInputStream
import java.util.*

object GradleSecrets {

    private val secretsFilePath by lazy { "./secrets/tokens.properties" }

    private fun writeValueWithKey(key: String, value: String) {
        GradleUtils.updatePropertiesFileField(secretsFilePath, key, value)
    }

    private fun deleteValueWithKey(key: String) {
        GradleUtils.updatePropertiesFileField(secretsFilePath, key, "")
    }
    
    private fun getValueWithKey(key: String): String {
        val inputStream = FileInputStream(secretsFilePath)
        val props = Properties()
        props.load(inputStream)
        inputStream.close()
        return props[key] as String
    }

    fun getSlackToken() = this.getValueWithKey("slackToken")
}