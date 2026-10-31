package com.jpapplications.carinsurance

import com.jpapplications.carinsurance.domain.model.CarSearchCriteria
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SharedCommonTest {

    @Test
    fun example() {
        assertEquals(3, 1 + 2)
    }

    @Test
    fun carSearchCriteriaAcceptsYearsInSupportedRange() {
        CarSearchCriteria(searchTerm = "", year = CarSearchCriteria.MIN_YEAR)
        CarSearchCriteria(searchTerm = "", year = CarSearchCriteria.MAX_YEAR)
    }

    @Test
    fun carSearchCriteriaAcceptsMissingYear() {
        CarSearchCriteria(searchTerm = "", year = null)
    }

    @Test
    fun carSearchCriteriaRejectsYearsOutsideSupportedRange() {
        assertFailsWith<IllegalArgumentException> {
            CarSearchCriteria(searchTerm = "", year = CarSearchCriteria.MIN_YEAR - 1)
        }
        assertFailsWith<IllegalArgumentException> {
            CarSearchCriteria(searchTerm = "", year = CarSearchCriteria.MAX_YEAR + 1)
        }
    }
}