package com.example.remedialucp2_235

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.remedialucp2_235.ui.navigation.PengelolaHalaman
import com.example.remedialucp2_235.ui.theme.RemedialUCP2_235Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RemedialUCP2_235Theme {
                // Surface sebagai wadah utama aplikasi
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Memanggil PengelolaHalaman untuk menangani navigasi
                    PengelolaHalaman()
                }
            }
        }
    }
}