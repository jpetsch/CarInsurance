package com.jpapplications.carinsurance.domain.model

sealed interface Gender {
    val apiCode: String

    enum class Known(override val apiCode: String) : Gender {
        MALE("MALE"),
        FEMALE("FEMALE"),
    }

    data class Unknown(override val apiCode: String) : Gender
}

sealed interface VehicleUsage {
    val apiCode: String

    enum class Known(override val apiCode: String) : VehicleUsage {
        PRIVATE("PRIVAT"),
        PRIVATE_AND_TRAVEL("PRIVAT_AND_TRAVEL"),
        OCCASIONAL_BUSINESS("OCCASIONAL_BUSINESS"),
        BUSINESS("BUSINESS"),
        COMMERCIAL_PERSON("COMMERCIAL_PERSON"),
        COMMERCIAL_GOODS("COMMERCIAL_GOODS"),
        RENTAL_PLATFORMS("RENTAL_PLATFORMS"),
        RENTAL("RENTAL"),
    }

    data class Unknown(override val apiCode: String) : VehicleUsage
}

enum class DamageType(val apiCode: String) {
    LIABILITY_PERSONAL("LIABILITY_PERSONAL"),
    LIABILITY_MATERIAL("LIABILITY_MATERIAL"),
    LIABILITY_MATERIAL_AND_PERSONAL("LIABILITY_MATERIAL_AND_PERSONAL"),
    COLLISION_MATERIAL_OWN_VEHICLE("COLLISION_MATERIAL_OWN_VEHICLE"),
    THEFT_VEHICLE("THEFT_VEHICLE"),
    THEFT_PERSONALEFFECTS("THEFT_PERSONALEFFECTS"),
    MARTEN("MARTEN"),
    VANDALISM("VANDALISM"),
    ELEMENTAL("ELEMENTAL"),
    ALPINE_HAZARDS("ALPINE_HAZARDS"),
    HAIL("HAIL"),
    FIRE("FIRE"),
    GLAS("GLAS"),
    ANIMALCOLLISION("ANIMALCOLLISION"),
    PARKINGDAMAGE("PARKINGDAMAGE"),
    ASSISTANCE("ASSISTANCE"),
    PASSENGER("PASSENGER"),
}

enum class DamageFault(val apiCode: String) {
    WITH_BONUSSCHUTZ("WITH_BONUSSCHUTZ"),
    YES_SELF_PAYMENT("YES_SELF_PAYMENT"),
    YES_INSURANCE_PAYMENT("YES_INSURANCE_PAYMENT"),
    NO_SELF_PAYMENT("NO_SELF_PAYMENT"),
    NO_INSURANCE_PAYMENT("NO_INSURANCE_PAYMENT"),
}
