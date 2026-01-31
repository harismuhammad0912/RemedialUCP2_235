package com.example.remedialucp2_235.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authors")
data class Author(
    @PrimaryKey(autoGenerate = true)
    val authorId: Int = 0,
    val name: String,
    val isDeleted: Boolean = false
)