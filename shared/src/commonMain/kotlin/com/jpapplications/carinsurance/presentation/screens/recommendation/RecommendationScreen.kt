package com.jpapplications.carinsurance.presentation.screens.recommendation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import carinsurance.shared.generated.resources.Res
import carinsurance.shared.generated.resources.recommendation_accident_daily_allowance
import carinsurance.shared.generated.resources.recommendation_accident_death_capital
import carinsurance.shared.generated.resources.recommendation_accident_disability_capital
import carinsurance.shared.generated.resources.recommendation_accident_healing_costs
import carinsurance.shared.generated.resources.recommendation_accident_hospital_allowance
import carinsurance.shared.generated.resources.recommendation_accident_pets
import carinsurance.shared.generated.resources.recommendation_accident_person_group
import carinsurance.shared.generated.resources.recommendation_boolean_no
import carinsurance.shared.generated.resources.recommendation_boolean_yes
import carinsurance.shared.generated.resources.recommendation_coverage_accident
import carinsurance.shared.generated.resources.recommendation_coverage_comprehensive
import carinsurance.shared.generated.resources.recommendation_coverage_liability
import carinsurance.shared.generated.resources.recommendation_hull_compensation
import carinsurance.shared.generated.resources.recommendation_hull_full_deductible
import carinsurance.shared.generated.resources.recommendation_hull_glass
import carinsurance.shared.generated.resources.recommendation_hull_bonus_protection
import carinsurance.shared.generated.resources.recommendation_hull_bonus_malus_level
import carinsurance.shared.generated.resources.recommendation_hull_bonus_malus_percent
import carinsurance.shared.generated.resources.recommendation_hull_personal_effects
import carinsurance.shared.generated.resources.recommendation_hull_product
import carinsurance.shared.generated.resources.recommendation_hull_park_damage
import carinsurance.shared.generated.resources.recommendation_hull_partial_deductible
import carinsurance.shared.generated.resources.recommendation_liability_bonus_protection
import carinsurance.shared.generated.resources.recommendation_liability_bonus_malus_level
import carinsurance.shared.generated.resources.recommendation_liability_bonus_malus_percent
import carinsurance.shared.generated.resources.recommendation_liability_deductible
import carinsurance.shared.generated.resources.recommendation_liability_gross_negligence
import carinsurance.shared.generated.resources.recommendation_premium
import carinsurance.shared.generated.resources.recommendation_premium_value
import carinsurance.shared.generated.resources.recommendation_title
import com.jpapplications.carinsurance.domain.model.RecommendationHull
import com.jpapplications.carinsurance.domain.model.RecommendationLiability
import com.jpapplications.carinsurance.domain.model.RecommendationPassengerAccident
import org.jetbrains.compose.resources.stringResource
import kotlin.math.abs
import kotlin.math.roundToLong

@Composable
fun RecommendationScreen(
    uiState: RecommendationUiState,
    onEvent: (event: RecommendationViewModel.RecommendationEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val recommendation = uiState.recommendation

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(Res.string.recommendation_title),
            style = MaterialTheme.typography.headlineSmall,
        )

        PremiumCard(recommendation.gesamtBruttoPraemie)

        recommendation.haftpflicht?.let { LiabilityCard(it) }
        recommendation.kasko?.let { HullCard(it) }
        recommendation.insassenUnfallDto?.let { PassengerAccidentCard(it) }
    }
}

@Composable
private fun PremiumCard(premium: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(Res.string.recommendation_premium),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(
                    Res.string.recommendation_premium_value,
                    premium.formatSwissCurrency(),
                ),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
private fun LiabilityCard(liability: RecommendationLiability) {
    CoverageCard(stringResource(Res.string.recommendation_coverage_liability)) {
        Detail(
            stringResource(Res.string.recommendation_liability_bonus_protection),
            liability.bonusschutz.toYesNo(),
        )
        Detail(
            stringResource(Res.string.recommendation_liability_gross_negligence),
            liability.gfverzicht.toYesNo(),
        )
        Detail(
            stringResource(Res.string.recommendation_liability_bonus_malus_level),
            liability.bonusMalusStufe.toString(),
        )
        Detail(
            stringResource(Res.string.recommendation_liability_bonus_malus_percent),
            "${liability.bonusMalusProzent} %",
        )
        Detail(
            stringResource(Res.string.recommendation_liability_deductible),
            "${liability.haftpflichtSelbstbehalt} CHF",
        )
    }
}

@Composable
private fun HullCard(hull: RecommendationHull) {
    CoverageCard(stringResource(Res.string.recommendation_coverage_comprehensive)) {
        Detail(stringResource(Res.string.recommendation_hull_product), hull.kaskoArt)
        Detail(
            stringResource(Res.string.recommendation_hull_full_deductible),
            "${hull.vollkaskoSelbstbehalt} CHF",
        )
        Detail(
            stringResource(Res.string.recommendation_hull_partial_deductible),
            "${hull.teilkaskoSelbstbehalt} CHF",
        )
        Detail(stringResource(Res.string.recommendation_hull_compensation), hull.entschaedigungsart)
        Detail(stringResource(Res.string.recommendation_hull_park_damage), hull.parkschaeden)
        Detail(stringResource(Res.string.recommendation_hull_glass), hull.kaskoGlasbruch)
        Detail(
            stringResource(Res.string.recommendation_hull_bonus_protection),
            hull.bonusschutz.toYesNo(),
        )
        Detail(
            stringResource(Res.string.recommendation_hull_personal_effects),
            hull.persoenlicheEffekten,
        )
        Detail(
            stringResource(Res.string.recommendation_hull_bonus_malus_level),
            hull.bonusMalusStufe.toString(),
        )
        Detail(
            stringResource(Res.string.recommendation_hull_bonus_malus_percent),
            "${hull.bonusMalusProzent} %",
        )
    }
}

@Composable
private fun PassengerAccidentCard(accident: RecommendationPassengerAccident) {
    CoverageCard(stringResource(Res.string.recommendation_coverage_accident)) {
        Detail(
            stringResource(Res.string.recommendation_accident_person_group),
            accident.insassenUnfallPersonenkreis,
        )
        Detail(
            stringResource(Res.string.recommendation_accident_hospital_allowance),
            "${accident.spitaltaggeld} CHF",
        )
        Detail(
            stringResource(Res.string.recommendation_accident_daily_allowance),
            "${accident.taggeld} CHF",
        )
        Detail(
            stringResource(Res.string.recommendation_accident_healing_costs),
            accident.heilungskosten.toYesNo(),
        )
        Detail(
            stringResource(Res.string.recommendation_accident_disability_capital),
            "${accident.invaliditaetskapital} CHF",
        )
        Detail(
            stringResource(Res.string.recommendation_accident_death_capital),
            "${accident.todesfallkapital} CHF",
        )
        Detail(
            stringResource(Res.string.recommendation_accident_pets),
            accident.haustiere,
        )
    }
}

@Composable
private fun CoverageCard(
    title: String,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            HorizontalDivider()
            content()
        }
    }
}

@Composable
private fun Detail(
    label: String,
    value: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun Boolean.toYesNo(): String =
    stringResource(
        if (this) Res.string.recommendation_boolean_yes else Res.string.recommendation_boolean_no,
    )

private fun Double.formatSwissCurrency(): String {
    val minorUnits = (this * 100).roundToLong()
    val wholeUnits = minorUnits / 100
    val fractionalUnits = abs(minorUnits % 100).toString().padStart(2, '0')
    return "$wholeUnits.$fractionalUnits"
}
