package com.example.padelstats.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.SportsTennis
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.padelstats.data.repo.PadelRepository
import com.example.padelstats.ui.navigation.Routes
import com.example.padelstats.ui.screens.matches.MatchesScreen
import com.example.padelstats.ui.screens.matches.MatchesViewModel
import com.example.padelstats.ui.screens.players.PlayersScreen
import com.example.padelstats.ui.screens.players.PlayersViewModel
import com.example.padelstats.ui.screens.stats.StatsScreen

@Composable
fun AppRoot(repo: PadelRepository) {
    val navController = rememberNavController()

    val items = listOf(
        BottomItem(
            route = Routes.PLAYERS,
            label = "Jugadores",
            icon = Icons.Outlined.Groups
        ),
        BottomItem(
            route = Routes.MATCHES,
            label = "Partidos",
            icon = Icons.Outlined.SportsTennis
        ),
        BottomItem(
            route = Routes.STATS,
            label = "Stats",
            icon = Icons.Outlined.BarChart
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val current = currentRoute(navController)
                items.forEach { item ->
                    NavigationBarItem(
                        selected = current == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Routes.PLAYERS
        ) {
            composable(Routes.PLAYERS) {
                val vm: PlayersViewModel = viewModel(
                    factory = PlayersViewModel.factory(repo)
                )
                PlayersScreen(
                    vm = vm,
                    padding = padding
                )
            }

            composable(Routes.MATCHES) {
                val vm: MatchesViewModel = viewModel(
                    factory = MatchesViewModel.factory(repo)
                )
                MatchesScreen(
                    vm = vm,
                    padding = padding
                )
            }

            composable(Routes.STATS) {
                // IMPORTANTE: aquí es donde normalmente faltaba el repo.
                StatsScreen(
                    repo = repo,
                    padding = padding
                )
            }
        }
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val backStackEntry by navController.currentBackStackEntryAsState()
    return backStackEntry?.destination?.route
}

private data class BottomItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)
