package ru.itis.home_works_2_semester

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.itis.home_works_2_semester.navigation.InfoScreenObject
import ru.itis.home_works_2_semester.navigation.SearchScreenObject
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
                startDestination = SearchScreenObject.route,
                modifier = Modifier.padding(innerPadding)
            ) {
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