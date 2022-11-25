package com.teavaro.ecommDemoApp.core

import com.teavaro.funnelConnect.utils.stringUtils.IStringUtils
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

internal object StringUtils: IStringUtils {

    fun stringToSha256String(string: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(string.encodeToByteArray())
        return bytes.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun decodeURL(string: String): String {
        return URLDecoder.decode(string, StandardCharsets.UTF_8.toString())
    }
}