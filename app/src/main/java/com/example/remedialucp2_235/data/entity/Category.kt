package com.example.remedialucp2_235.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val parentId: Int? = null, // Untuk Sub-kategori tak terbatas
    val isDeleted: Boolean = false
)