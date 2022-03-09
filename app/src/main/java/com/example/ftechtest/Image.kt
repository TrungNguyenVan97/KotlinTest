package com.example.ftechtest

import java.io.Serializable

data class Image(
    var title: String,
    val size: String,
    var path: String,
) : Serializable {
}