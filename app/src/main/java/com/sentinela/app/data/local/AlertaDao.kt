package com.sentinela.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/** Operações de CRUD sobre a tabela `alertas`. As leituras são reativas via [Flow]. */
@Dao
interface AlertaDao {

    /** Alertas mais recentes primeiro; emite de novo a cada alteração na tabela. */
    @Query("SELECT * FROM alertas ORDER BY criadoEm DESC, id DESC")
    fun observarTodos(): Flow<List<AlertaEntity>>

    @Query("SELECT * FROM alertas WHERE id = :id")
    fun observarPorId(id: Long): Flow<AlertaEntity?>

    @Query("SELECT COUNT(*) FROM alertas")
    suspend fun contar(): Int

    @Insert
    suspend fun inserir(alerta: AlertaEntity): Long

    @Insert
    suspend fun inserirTodos(alertas: List<AlertaEntity>)

    @Update
    suspend fun atualizar(alerta: AlertaEntity)

    @Delete
    suspend fun excluir(alerta: AlertaEntity)

    @Query("DELETE FROM alertas")
    suspend fun excluirTodos()
}
