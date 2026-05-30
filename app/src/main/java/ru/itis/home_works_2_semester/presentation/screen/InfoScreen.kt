package ru.itis.home_works_2_semester.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import ru.itis.home_works_2_semester.R
import ru.itis.home_works_2_semester.presentation.viewmodel.DetailViewModel
import ru.itis.home_works_2_semester.ui.theme.NeonGreen
import ru.itis.home_works_2_semester.ui.theme.PortalGreen
import ru.itis.home_works_2_semester.utils.ResManager

@Composable
fun InfoScreen(
    navController: NavController,
    viewModel: DetailViewModel,
    resManager: ResManager
) {
    val character by viewModel.character.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> CircularProgressIndicator(color = NeonGreen)
                error != null -> when {
                    "404" in error!! -> Text(resManager.getString(R.string.error_404))
                    "50" in error!! -> Text(resManager.getString(R.string.error_5xx))
                    else -> Text(
                        text = resManager.getString(R.string.error_prefix, error ?: ""),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                character != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Аватарка
                        Image(
                            painter = rememberAsyncImagePainter(character!!.remoteImage),
                            contentDescription = character!!.remoteName,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                                .border(3.dp, NeonGreen, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = character!!.remoteName,
                            style = MaterialTheme.typography.headlineMedium,
                            color = PortalGreen
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Блок информации
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                InfoRow(label = resManager.getString(R.string.status_label), value = character!!.remoteStatus)
                                InfoRow(label = resManager.getString(R.string.species_label), value = character!!.remoteSpecies)
                                InfoRow(label = resManager.getString(R.string.gender_label), value = character!!.remoteGender)
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Кнопка назад
                        Button(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = PortalGreen
                            ),
                            shape = RoundedCornerShape(8.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                    colors = listOf(NeonGreen, PortalGreen)
                                ),
                                width = 2.dp
                            )
                        ) {
                            Text(resManager.getString(R.string.back_to_list), style = MaterialTheme.typography.labelLarge)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                else -> Text(resManager.getString(R.string.no_data))
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            ),
            color = NeonGreen
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = PortalGreen
        )
    }
}