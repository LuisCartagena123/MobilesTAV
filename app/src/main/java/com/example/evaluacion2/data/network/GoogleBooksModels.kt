package com.example.evaluacion2.data.network

data class GoogleBooksResponse(
    val items: List<VolumeItem>? = null
)

data class VolumeItem(
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val description: String? = null,
    val pageCount: Int? = null,
    val imageLinks: ImageLinks? = null
)

data class ImageLinks(
    val thumbnail: String? = null
)
