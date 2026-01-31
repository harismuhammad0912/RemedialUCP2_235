package com.example.remedialucp2_235.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "books",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE // Aturan default, nanti kita override di Logic
    )]
)
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val categoryId: Int,
    val status: String = "Tersedia", // "Tersedia" atau "Dipinjam"
    val isDeleted: Boolean = false
)