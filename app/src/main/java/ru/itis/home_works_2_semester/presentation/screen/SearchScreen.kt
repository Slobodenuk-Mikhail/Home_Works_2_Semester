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
import ru.itis.home_works_2_semester.data.model.CharacterModel


@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel,
    resManager: ResManager
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                value = uiState.query,
                onValueChange = { viewModel.onQueryChanged(it) },
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
                    viewModel.searchCharacters()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.query.isNotBlank(),
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
                uiState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = NeonGreen
                )

                uiState.error != null -> when {
                    "404" in uiState.error!! -> Text(resManager.getString(R.string.error_404))
                    "50" in uiState.error!! -> Text(resManager.getString(R.string.error_5xx))
                    else -> Text(
                        text = resManager.getString(R.string.error_prefix, uiState.error ?: ""),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = uiState.characterList,
                        key = { it.id }
                    ) { character ->
                        CharacterItem(
                            character = character,
                            onClick = {
                                navController.navigate(InfoScreenObject.createRoute(character.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(
    character: CharacterModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
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
