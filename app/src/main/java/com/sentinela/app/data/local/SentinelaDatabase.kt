package com.sentinela.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AlertaEntity::class], version = 1, exportSchema = true)
abstract class SentinelaDatabase : RoomDatabase() {

    abstract fun alertaDao(): AlertaDao

    companion object {
        private const val NOME_ARQUIVO = "sentinela.db"

        fun criar(context: Context): SentinelaDatabase =
            Room.databaseBuilder(context, SentinelaDatabase::class.java, NOME_ARQUIVO).build()
    }
}
