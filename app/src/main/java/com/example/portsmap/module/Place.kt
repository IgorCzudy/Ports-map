package com.example.portsmap.module

import java.io.Serializable

class Place(val title: String, val description: String,
            val latitude: Double, val longitude: Double): Serializable {
}