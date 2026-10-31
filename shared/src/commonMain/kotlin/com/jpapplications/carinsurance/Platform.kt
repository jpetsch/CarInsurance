package com.jpapplications.carinsurance

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform