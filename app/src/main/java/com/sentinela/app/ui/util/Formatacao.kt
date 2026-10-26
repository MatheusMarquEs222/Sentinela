package com.sentinela.app.ui.util

import com.sentinela.app.model.Alerta
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val formatoHora = DateTimeFormatter.ofPattern("HH:mm")
private val formatoDataHora = DateTimeFormatter.ofPattern("dd/MM HH:mm")

/** Horário do alerta para exibição: só a hora se for de hoje, senão dia e hora. */
fun Alerta.horarioFormatado(zona: ZoneId = ZoneId.systemDefault()): String {
    val momento = Instant.ofEpochMilli(criadoEm).atZone(zona)
    val formato = if (momento.toLocalDate() == LocalDate.now(zona)) formatoHora else formatoDataHora
    return momento.format(formato)
}
