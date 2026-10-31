package com.jpapplications.carinsurance.domain.model

data class Car(
    val matriculationDate: Int,
    val constructionStartYear: Int,
    val catalogPrice: Int,
    val etxCode: Int,
    val description: String,
    val capacity: Int,
    val model: String,
    val doors: Int,
    val seats: Int,
    val emptyLoad: Int,
    val horsePower: Int,
    val enginePowerInKw: Int,
    val vehicleType: String,
    val vehicleClass: Int,
    val constructionType: String,
    val fuel: String,
    val brand: String,
    val eMobility: Boolean,
)
