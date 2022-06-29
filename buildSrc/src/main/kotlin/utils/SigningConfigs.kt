package utils

import java.io.File

data class SigningConfigs(val storeFile: File, val keyAlias: String, val keyPassword: String, val storePassword: String)