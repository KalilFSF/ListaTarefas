package com.kalilf.listatarefas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kalilf.listatarefas.viewmodel.TarefasUiState
import com.kalilf.listatarefas.viewmodel.TarefasViewModel

@Composable
fun TarefasScreen(viewModel: TarefasViewModel = viewModel()) {
    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var tempo by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descricao") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = tempo, onValueChange = { tempo = it }, label = { Text("Tempo") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                val tempoInt = tempo.toIntOrNull() ?: return@Button
                viewModel.salvarTarefa(nome, descricao, tempoInt)
                nome = ""; descricao = ""; tempo = ""
            },
            modifier = Modifier.padding(top = 12.dp)
        ) { Text("Salvar Tarefas") }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = viewModel.uiState) {
            is TarefasUiState.Loading -> CircularProgressIndicator()
            is TarefasUiState.Error -> Text("Erro: ${state.message}")
            is TarefasUiState.Success -> LazyColumn {
                items(state.tarefas, key = { it.id }) { tarefa ->
                    ListItem(
                        headlineContent = { Text(tarefa.nome) },
                        supportingContent = { Text("${tarefa.descricao} · ${tarefa.tempo}") }
                    )
                }
            }
        }
    }
}