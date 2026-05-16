package com.cmc.caudex.data.remote.interceptor

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
) : Interceptor {
    @SuppressLint("HardwareIds")
    override fun intercept(chain: Interceptor.Chain): Response {

        val deviceUid = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID,
        )

        val request = chain.request().newBuilder()
            .addHeader("X-Device-UID", deviceUid)
            .addHeader("X-OS-Type", "android")
            .build()

        return chain.proceed(request)
    }
}
