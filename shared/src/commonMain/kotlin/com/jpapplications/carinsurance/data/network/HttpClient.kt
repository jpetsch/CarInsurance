package com.jpapplications.carinsurance.data.network

import io.ktor.client.HttpClient as KtorHttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal fun provideHttpClient() = KtorHttpClient(createPlatformEngine()) {
    expectSuccess = true

    defaultRequest {
        header(
            HttpHeaders.UserAgent,
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36",
        )
        accept(ContentType.Application.Json)
    }

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}

internal expect fun createPlatformEngine(): HttpClientEngine
