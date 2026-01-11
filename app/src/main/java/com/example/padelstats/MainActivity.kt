package com.example.padelstats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.padelstats.data.repo.AppGraph
import com.example.padelstats.ui.theme.PadelStatsTheme
import com.example.padelstats.ui.AppRoot



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Inicializa el grafo de dependencias (Room + Repo)
        AppGraph.init(applicationContext)

        setContent {
            PadelStatsTheme {
                AppRoot(repo = AppGraph.repo)
            }
        }
    }
}
