package ru.kovsheful.wallcraft.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.kovsheful.wallcraft.data.entities.results.GetCollectionResult
import ru.kovsheful.wallcraft.data.entities.results.GetListOfCollectionsResult

interface CollectionsAPI {

    @GET("collections/featured")
    suspend fun getListOfCollections(@Query("per_page") quantity: Int): GetListOfCollectionsResult

    //Get only first image to use it as title. Separate function allows now to waste another pictures
    @GET("collections/{id}?per_page=1")
    suspend fun getCollectionTitleImageById(@Path("id")id: String): GetCollectionResult

    @GET("collections/{id}")
    suspend fun getCollectionImages(@Path("id") id: String, @Query("per_page") quantity: Int): GetCollectionResult
}