package com.danyeon.newsreader.core.util

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

object WebNavigationHelper {
    fun openCustomTab(context: Context, url: String) {
        val parsedUri = runCatching { url.toUri() }.getOrNull() ?: return

        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setShareState(CustomTabsIntent.SHARE_STATE_ON)
            .build()

        customTabsIntent.launchUrl(context, parsedUri)
    }
}