package ru.itis.home_works_2_semester.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import ru.itis.home_works_2_semester.navigation.InfoScreenObject
import ru.itis.home_works_2_semester.presentation.viewmodel.SearchViewModel
import ru.itis.home_works_2_semester.ui.theme.NeonGreen
import ru.itis.home_works_2_semester.ui.theme.PortalGreen
import ru.itis.home_works_2_semester.utils.ResManager
import ru.itis.home_works_2_semester.R


@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel,
    resManager: ResManager
) {
    val characterList by viewModel.characterList.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    var query by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Поле ввода
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text(resManager.getString(R.string.search_text_field_label), style = MaterialTheme.typography.labelLarge) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = PortalGreen.copy(alpha = 0.5f),
                    focusedLabelColor = NeonGreen,
                    unfocusedLabelColor = PortalGreen
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = PortalGreen)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Кнопка поиска
            Button(
                onClick = {
                    if (query.isNotBlank()) {
                        viewModel.searchCharacters(query)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = query.isNotBlank(),
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
                Text(resManager.getString(R.string.search_button), style = MaterialTheme.typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = NeonGreen
                )

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

                else -> LazyColumn {
                    items(characterList) { character ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    navController.navigate(InfoScreenObject.createRoute(character.id))
                                }
                                .shadow(8.dp, RoundedCornerShape(12.dp))
                                .border(
                                    width = 1.dp,
                                    color = NeonGreen,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(character.image),
                                    contentDescription = character.name,
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, NeonGreen, CircleShape),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Text(
                                    text = character.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = ">>",
                                    color = NeonGreen,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}