package com.cmc.caudex.data.remote.interceptor

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

private const val HEADER_DEVICE_UID = "X-Device-UID"
private const val HEADER_OS_TYPE = "X-OS-Type"
private const val OS_TYPE_ANDROID = "android"

class HeaderInterceptor @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : Interceptor {
    @SuppressLint("HardwareIds")
    override fun intercept(chain: Interceptor.Chain): Response {

        val deviceUid = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID,
        )

        val request = chain.request().newBuilder()
            .addHeader(HEADER_DEVICE_UID, deviceUid)
            .addHeader(HEADER_OS_TYPE, OS_TYPE_ANDROID)
            .build()

        return chain.proceed(request)
    }
}
