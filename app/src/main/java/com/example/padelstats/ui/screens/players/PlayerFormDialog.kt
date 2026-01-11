package com.example.padelstats.ui.screens.players

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.padelstats.data.entity.PlayerEntity

private data class DropdownOption(val label: String, val value: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerFormDialog(
    title: String,
    initial: PlayerEntity? = null,
    onDismiss: () -> Unit,
    onConfirm: (name: String, level: String, position: String) -> Unit
) {
    var name by remember { mutableStateOf(initial?.name.orEmpty()) }

    val levelOptions = listOf(
        DropdownOption("Bajo (Playtomic 1-3)", "Bajo (1-3)"),
        DropdownOption("Intermedio (Playtomic 3-5)", "Intermedio (3-5)"),
        DropdownOption("Alto (Playtomic 5-7)", "Alto (5-7)")
    )
    val positionOptions = listOf(
        DropdownOption("Drive", "Drive"),
        DropdownOption("Revés", "Reves")
    )

    var selectedLevel by remember {
        mutableStateOf(initial?.level?.takeIf { it.isNotBlank() } ?: levelOptions[1].value)
    }
    var selectedPosition by remember {
        mutableStateOf(initial?.position?.takeIf { it.isNotBlank() } ?: positionOptions[0].value)
    }

    var levelExpanded by remember { mutableStateOf(false) }
    var positionExpanded by remember { mutableStateOf(false) }

    val canSave = name.trim().isNotEmpty()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Nivel dropdown
                ExposedDropdownMenuBox(
                    expanded = levelExpanded,
                    onExpandedChange = { levelExpanded = !levelExpanded }
                ) {
                    OutlinedTextField(
                        value = levelOptions.firstOrNull { it.value == selectedLevel }?.label ?: selectedLevel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Nivel") },
                        trailingIcon = {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = levelExpanded,
                        onDismissRequest = { levelExpanded = false }
                    ) {
                        levelOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt.label) },
                                onClick = {
                                    selectedLevel = opt.value
                                    levelExpanded = false
                                }
                            )
                        }
                    }
                }

                // Posición dropdown
                ExposedDropdownMenuBox(
                    expanded = positionExpanded,
                    onExpandedChange = { positionExpanded = !positionExpanded }
                ) {
                    OutlinedTextField(
                        value = positionOptions.firstOrNull { it.value == selectedPosition }?.label ?: selectedPosition,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Posición") },
                        trailingIcon = {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = positionExpanded,
                        onDismissRequest = { positionExpanded = false }
                    ) {
                        positionOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt.label) },
                                onClick = {
                                    selectedPosition = opt.value
                                    positionExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = canSave,
                onClick = { onConfirm(name.trim(), selectedLevel, selectedPosition) }
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
