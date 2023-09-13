package ru.kovsheful.wallcraft.data.entities.results

import com.google.gson.annotations.SerializedName
import ru.kovsheful.wallcraft.data.entities.CollectionEntity

data class GetListOfCollectionsResult(
    val collections: List<CollectionEntity>,
    @SerializedName("next_page")
    val nextPage: String,
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("total_results")
    val totalResults: Int
)