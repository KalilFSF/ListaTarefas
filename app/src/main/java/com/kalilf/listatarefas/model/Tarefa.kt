package com.kalilf.listatarefas.model

data class Tarefa(
    val id: Long = 0,
    val nome: String,
    val descricao: String,
    val tempo: Int
)