package com.example.getmeout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages_table")

data class Message(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "message") var message: String,
    @ColumnInfo(name = "selected") var selected: Boolean
)