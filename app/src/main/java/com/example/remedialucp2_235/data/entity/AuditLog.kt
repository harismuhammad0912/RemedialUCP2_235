package com.example.remedialucp2_235.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audit_logs")
data class AuditLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val action: String, // "DELETE", "UPDATE"
    val timestamp: Long = System.currentTimeMillis(),
    val details: String
)