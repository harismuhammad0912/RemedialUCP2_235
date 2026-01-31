package com.example.remedialucp2_235.data.entity

import androidx.room.Entity

@Entity(primaryKeys = ["bookId", "authorId"])
data class BookAuthorCrossRef(
    val bookId: Int,
    val authorId: Int
)