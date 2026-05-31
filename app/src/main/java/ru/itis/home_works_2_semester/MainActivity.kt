package ru.itis.home_works_2_semester

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.itis.home_works_2_semester.navigation.ChartScreenObject
import ru.itis.home_works_2_semester.navigation.InfoScreenObject
import ru.itis.home_works_2_semester.navigation.SearchScreenObject
import ru.itis.home_works_2_semester.presentation.components.CircularPieChart
import ru.itis.home_works_2_semester.presentation.screen.InfoScreen
import ru.itis.home_works_2_semester.presentation.screen.SearchScreen
import ru.itis.home_works_2_semester.presentation.viewmodel.DetailViewModel
import ru.itis.home_works_2_semester.presentation.viewmodel.SearchViewModel
import ru.itis.home_works_2_semester.ui.theme.Home_Works_2_SemesterTheme
import ru.itis.home_works_2_semester.utils.ResManager

class MainActivity : ComponentActivity() {

    private lateinit var resManager: ResManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        resManager = ResManager(ctx = applicationContext)
        setContent {
            Home_Works_2_SemesterTheme(
                darkTheme = true,
                dynamicColor = false
            ){
                AppNavigation()
            }
        }

    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()

        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = ChartScreenObject.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(ChartScreenObject.route) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        CircularPieChart(
                            data = listOf(
                                1 to 25,
                                2 to 15,
                                3 to 10,
                                4 to 20,
                                5 to 12,
                                6 to 8,
                                7 to 10
                            ),
                            labels = listOf(
                                "Excel files",
                                "Dynamic libraries",
                                "XML configs",
                                "Archives",
                                "System files",
                                "RE",
                                "Others"
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                composable(SearchScreenObject.route) {
                    val viewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)
                    SearchScreen(
                        navController = navController,
                        viewModel = viewModel,
                        resManager = resManager
                    )
                }

                composable(InfoScreenObject.route) { backStackEntry ->
                    val viewModel: DetailViewModel = viewModel(factory = DetailViewModel.Factory)
                    InfoScreen(
                        navController = navController,
                        viewModel = viewModel,
                        resManager = resManager
                    )
                }
            }
        }
    }
}