package com.jpapplications.carinsurance.presentation.screens.recommendation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import com.jpapplications.carinsurance.domain.model.CarRecommendation
import com.jpapplications.carinsurance.domain.model.RecommendationHull
import com.jpapplications.carinsurance.domain.model.RecommendationLiability
import com.jpapplications.carinsurance.domain.model.RecommendationMetaData
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class RecommendationScreenTest {
    @Test
    fun displaysPremiumAndCoverageDetails() = runComposeUiTest {
        setContent {
            MaterialTheme {
                RecommendationScreen(
                    uiState = RecommendationUiState(testRecommendation),
                    onEvent = {},
                )
            }
        }

        onNodeWithText("Deine Empfehlung").assertIsDisplayed()
        onNodeWithText("Gesamtprämie").assertIsDisplayed()
        onNodeWithText("CHF 1293.90").assertIsDisplayed()
        onNodeWithText("Haftpflicht").assertIsDisplayed()
        onNodeWithText("Bonusschutz").assertIsDisplayed()
        onNodeWithText("Vollkasko-Selbstbehalt").assertIsDisplayed()
    }

    private companion object {
        val testRecommendation =
            CarRecommendation(
                gesamtBruttoPraemie = 1293.9,
                metaData =
                    RecommendationMetaData(
                        uwfLiability = "",
                        cffLiability = "",
                        uwfAssistance = "",
                        cffAssistance = "",
                        uwfHull = "",
                        cffHullTk = "",
                        cffHullVk = "",
                        uwfPassenger = "",
                        cffPassenger = "",
                        keepOnChanges = false,
                    ),
                krf = "",
                haftpflicht =
                    RecommendationLiability(
                        bonusschutz = true,
                        gfverzicht = true,
                        bonusMalusStufe = 1,
                        bonusMalusProzent = 40,
                        haftpflichtSelbstbehalt = 0,
                    ),
                kasko =
                    RecommendationHull(
                        kaskoArt = "VOLLKASKO",
                        vollkaskoSelbstbehalt = 1000,
                        teilkaskoSelbstbehalt = 0,
                        entschaedigungsart = "PLUS",
                        parkschaeden = "INSURED_1000",
                        kaskoGlasbruch = "BASIC",
                        bonusschutz = true,
                        persoenlicheEffekten = "INSURED_2000",
                        bonusMalusStufe = 1,
                        bonusMalusProzent = 40,
                    ),
            )
    }
}
