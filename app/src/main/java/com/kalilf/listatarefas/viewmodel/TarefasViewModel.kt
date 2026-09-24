package com.kalilf.listatarefas.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kalilf.listatarefas.database.DatabaseHelper
import com.kalilf.listatarefas.model.Tarefa
import com.kalilf.listatarefas.repository.TarefaRepository
import kotlinx.coroutines.launch

sealed interface TarefasUiState {
    object Loading : TarefasUiState
    data class Success(val tarefas: List<Tarefa>) : TarefasUiState
    data class Error(val message: String) : TarefasUiState
}

class TarefasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TarefaRepository(DatabaseHelper(application))

    var uiState: TarefasUiState by mutableStateOf(TarefasUiState.Loading)
        private set

    init { carregarTarefas() }

    fun carregarTarefas() {
        viewModelScope.launch {
            uiState = TarefasUiState.Loading
            uiState = try {
                TarefasUiState.Success(repository.listarTodos())
            } catch (e: Exception) {
                TarefasUiState.Error(e.message ?: "Erro ao acessar o banco de dados.")
            }
        }
    }

    fun salvarTarefa(nome: String, descricao: String, tempo: Int) {
        viewModelScope.launch {
            repository.inserir(Tarefa(nome= nome, descricao = descricao, tempo = tempo))
            carregarTarefas()
        }
    }
}