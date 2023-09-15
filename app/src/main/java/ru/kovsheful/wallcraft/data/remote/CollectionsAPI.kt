package ru.kovsheful.wallcraft.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import ru.kovsheful.wallcraft.data.entities.results.GetCollectionResult
import ru.kovsheful.wallcraft.data.entities.results.GetListOfCollectionsResult

interface CollectionsAPI {

    @GET("collections/featured?per_page=5")
    suspend fun getListOfCollections(): GetListOfCollectionsResult

    //Get only first image to use it as title. Separate function allows now to waste another pictures
    @GET("collections/{id}?per_page=1")
    suspend fun getCollectionTitleImageById(@Path("id")id: String): GetCollectionResult

    @GET("collections/{id}?per_page=5")
    suspend fun getCollectionImages(@Path("id")id: String): GetCollectionResult
}