package com.jpapplications.carinsurance.presentation.screens.recommendation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import carinsurance.shared.generated.resources.recommendation_coverage_accident
import carinsurance.shared.generated.resources.recommendation_coverage_comprehensive
import carinsurance.shared.generated.resources.recommendation_coverage_liability
import carinsurance.shared.generated.resources.recommendation_premium
import carinsurance.shared.generated.resources.recommendation_premium_value
import carinsurance.shared.generated.resources.recommendation_title
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
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

        recommendation.haftpflicht?.let {
            CoverageCard(
                title = stringResource(Res.string.recommendation_coverage_liability),
                details = it,
            )
        }
        recommendation.kasko?.let {
            CoverageCard(
                title = stringResource(Res.string.recommendation_coverage_comprehensive),
                details = it,
            )
        }
        recommendation.insassenUnfallDto?.let {
            CoverageCard(
                title = stringResource(Res.string.recommendation_coverage_accident),
                details = it,
            )
        }
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
private fun CoverageCard(
    title: String,
    details: JsonObject,
) {
    if (details.isEmpty()) return

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
            JsonObjectDetails(details)
        }
    }
}

@Composable
private fun JsonObjectDetails(details: JsonObject) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        details.forEach { (key, value) ->
            JsonValueDetail(
                label = key.toDisplayLabel(),
                value = value,
            )
        }
    }
}

@Composable
private fun JsonValueDetail(
    label: String,
    value: JsonElement,
) {
    if (value == JsonNull) return

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        when (value) {
            is JsonObject -> {
                Text(label, style = MaterialTheme.typography.labelLarge)
                JsonObjectDetails(value)
            }
            is JsonArray -> {
                Text(label, style = MaterialTheme.typography.labelLarge)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    value.forEachIndexed { index, item ->
                        if (item != JsonNull) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(
                                    text = "${index + 1}.",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    when (item) {
                                        is JsonObject -> JsonObjectDetails(item)
                                        is JsonArray -> JsonValueDetail(
                                            label = "",
                                            value = item,
                                        )
                                        is JsonPrimitive -> Text(
                                            text = item.content,
                                            style = MaterialTheme.typography.bodyMedium,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            is JsonPrimitive -> {
                Text(label, style = MaterialTheme.typography.labelMedium)
                Text(value.content, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

private fun String.toDisplayLabel(): String =
    replace(Regex("([a-z0-9])([A-Z])"), "$1 $2")
        .replace('_', ' ')
        .replaceFirstChar { it.uppercase() }

private fun Double.formatSwissCurrency(): String {
    val minorUnits = (this * 100).roundToLong()
    val wholeUnits = minorUnits / 100
    val fractionalUnits = abs(minorUnits % 100).toString().padStart(2, '0')
    return "$wholeUnits.$fractionalUnits"
}
