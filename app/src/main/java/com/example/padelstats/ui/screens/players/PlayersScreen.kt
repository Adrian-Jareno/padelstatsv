package com.example.padelstats.ui.screens.players
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.padelstats.data.entity.PlayerEntity

@Composable
fun PlayersScreen(
    vm: PlayersViewModel,
    padding: PaddingValues
) {
    val players by vm.players.collectAsState()

    var showCreate by remember { mutableStateOf(false) }
    var editingPlayer by remember { mutableStateOf<PlayerEntity?>(null) }
    var deletingPlayer by remember { mutableStateOf<PlayerEntity?>(null) }

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Jugadores", style = MaterialTheme.typography.headlineSmall)


        Button(
            onClick = { showCreate = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear jugador")
        }

        if (players.isEmpty()) {
            Text(
                "No hay jugadores todavía. Pulsa \"Crear jugador\" para añadir uno.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(players, key = { it.id }) { p ->
                    PlayerRow(
                        player = p,
                        onEdit = { editingPlayer = p },
                        onDelete = { deletingPlayer = p }
                    )
                }
            }
        }
    }

    if (showCreate) {
        PlayerFormDialog(
            title = "Crear jugador",
            initial = null,
            onDismiss = { showCreate = false },
            onConfirm = { name, level, position ->
                vm.createPlayer(name, level, position)
                showCreate = false
            }
        )
    }

    editingPlayer?.let { p ->
        PlayerFormDialog(
            title = "Modificar jugador",
            initial = p,
            onDismiss = { editingPlayer = null },
            onConfirm = { name, level, position ->
                vm.updatePlayer(p.id, name, level, position)
                editingPlayer = null
            }
        )
    }

    deletingPlayer?.let { p ->
        AlertDialog(
            onDismissRequest = { deletingPlayer = null },
            title = { Text("Eliminar jugador") },
            text = { Text("¿Seguro que quieres eliminar a \"${p.name}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.deletePlayer(p.id)
                        deletingPlayer = null
                    }
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { deletingPlayer = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun PlayerRow(
    player: PlayerEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEdit() }
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text(player.name, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text("Nivel: ${player.level}", style = MaterialTheme.typography.bodyMedium)
                Text("Posición: ${player.position}", style = MaterialTheme.typography.bodyMedium)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Modificar")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}
