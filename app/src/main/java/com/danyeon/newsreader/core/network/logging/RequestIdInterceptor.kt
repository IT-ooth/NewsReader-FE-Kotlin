package com.danyeon.newsreader.core.network.logging

import okhttp3.Interceptor
import okhttp3.Response
import java.util.UUID
import android.util.Log

class RequestIdInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // 요청마다 UUID 생성
        val requestId = UUID.randomUUID().toString().take(8)

        val request = chain.request()
            .newBuilder()
            .addHeader("X-Request-ID", requestId)
            .build()

        Log.d("API", "➡️ [REQ $requestId] ${request.method} ${request.url}")

        val start = System.currentTimeMillis()
        val response = chain.proceed(request)
        val tookMs = System.currentTimeMillis() - start

        Log.d(
            "API",
            "⬅️ [RES $requestId] ${response.code} (${tookMs}ms) ${response.request.url}"
        )

        return response
    }
}