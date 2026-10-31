package com.jpapplications.carinsurance.data.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

internal actual fun createPlatformEngine(): HttpClientEngine = OkHttp.create()
