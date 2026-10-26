package com.sentinela.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sentinela.app.model.Alerta
import com.sentinela.app.model.Severidade

/** Linha da tabela `alertas` no banco local. A severidade é gravada pelo nome do enum. */
@Entity(tableName = "alertas")
data class AlertaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tipo: String,
    val local: String,
    val descricao: String,
    val severidade: Severidade,
    val criadoEm: Long,
    val oficial: Boolean
)

fun AlertaEntity.paraModelo(): Alerta = Alerta(
    id = id,
    tipo = tipo,
    local = local,
    descricao = descricao,
    severidade = severidade,
    criadoEm = criadoEm,
    oficial = oficial
)

fun Alerta.paraEntidade(): AlertaEntity = AlertaEntity(
    id = id,
    tipo = tipo,
    local = local,
    descricao = descricao,
    severidade = severidade,
    criadoEm = criadoEm,
    oficial = oficial
)
