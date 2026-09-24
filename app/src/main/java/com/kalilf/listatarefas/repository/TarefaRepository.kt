package com.kalilf.listatarefas.repository

import android.content.ContentValues
import com.kalilf.listatarefas.database.DatabaseHelper
import com.kalilf.listatarefas.model.Tarefa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TarefaRepository(private val dbHelper: DatabaseHelper) {

    suspend fun inserir(tarefa: Tarefa) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put(DatabaseHelper.COL_NOME, tarefa.nome)
            put(DatabaseHelper.COL_DESCRICAO, tarefa.descricao)
            put(DatabaseHelper.COL_TEMPO, tarefa.tempo)
        }
        db.insert(DatabaseHelper.TABLE_TAREFAS, null, valores)
    }
    suspend fun listarTodos(): List<Tarefa> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_TAREFAS,
            null, null, null, null, null,
            "${DatabaseHelper.COL_TEMPO} DESC"
        )
        val tarefas = mutableListOf<Tarefa>()
        cursor.use {
            while (it.moveToNext()) {
                tarefas.add(
                    Tarefa(
                        id = it.getLong(it.getColumnIndexOrThrow(DatabaseHelper.COL_ID)),
                        nome = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_NOME)),
                        descricao = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRICAO)),
                        tempo = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COL_TEMPO))
                    )
                )
            }
        }
        tarefas
    }
}