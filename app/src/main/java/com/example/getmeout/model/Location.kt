package com.example.getmeout.model

import androidx.room.PrimaryKey

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "location_table")

data class Location(
    @PrimaryKey() val uid: Int,
    @ColumnInfo(name = "status") var status: Boolean
)