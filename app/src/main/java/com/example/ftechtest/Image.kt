package com.example.ftechtest

import java.io.Serializable

data class Image(
    var title: String? = null,
    val size: String? = null,
    var path: String? = null,
    var bucket: String? = null,
) : Serializable {
}