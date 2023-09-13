package ru.kovsheful.wallcraft.data.entities.results

import com.google.gson.annotations.SerializedName
import ru.kovsheful.wallcraft.data.entities.Media

data class GetCollectionResult(
    val id: String,
    val media: List<Media>,
    @SerializedName("next_page")
    val nextPage: String,
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("total_results")
    val totalResults: Int
)