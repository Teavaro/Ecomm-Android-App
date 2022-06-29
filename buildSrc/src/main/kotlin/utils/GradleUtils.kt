package utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object GradleUtils {

    val androidBuildVersionsFile = File("./buildSrc/src/main/kotlin/Build.kt")

    fun getUpdatedLines(fileLines: MutableList<String>, value: String, newVal: Any): List<CharSequence> {
        val lines: MutableList<CharSequence> = mutableListOf()
        var lineContent: Array<String>
        for (line in fileLines) {
            if (line.contains(value)) {
                lineContent = line.split(" = ".toRegex()).toTypedArray()
                if(newVal is String)
                    lines.add(lineContent.first() + " = " + "\"$newVal\"")
                else
                    lines.add(lineContent.first() + " = " + newVal)
            }
            else {
                lines.add(line)
            }
        }
        return lines
    }

    fun updatePropertiesFileField(filePath: String, property: String, value: String) {
        val inputStream = FileInputStream(filePath)
        val props = Properties()
        props.load(inputStream)
        inputStream.close()
        val fileOutputStream = FileOutputStream(filePath)
        props.setProperty(property, "\"$value\"")
        props.store(fileOutputStream, null)
        fileOutputStream.close()
    }
}