package com.androidshowtime.marsestate.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

enum class MarsFilterClass(val type: String) {
    SHOW_BUY("buy"), SHOW_RENT("rent"),
    SHOW_ALL("all")
}

//base URL
private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"


/*using MoshiBuilder to create MoshiObject i.e. the
JSON-String-to-Kotlin-Object converter*/

/*For Moshi's annotations to work properly with Kotlin
add the KotlinJsonAdapterFactory*/

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
//now that we have Moshi in place, we are no longer using the
//JSON String but Moshi will Parse the String to Kotlin Object


//using Retrofit Builder to create Retrofit Object
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi)) //handles JSON
    .addCallAdapterFactory(CoroutineCallAdapterFactory()) //replace the default all Object with Deferred Object
    .baseUrl(BASE_URL)
    .build()


//interface that defines how Retrofit talks to web server

/*
define a public object called MarsAPI to initialize the Retrofit service
object ensures only one instance of MarsAPI will be created
*/
interface MarsAPIService {
    @GET("realestate")
    fun getPropertiesAsync(@Query("filter") type: String): Deferred<List<MarsProperty>>

    /*getProperties() returns a Deferred Interface which define a
    coroutine Job which has await() method*/
}


/*
define a public object called MarsAPI to initialize the Retrofit service
object ensures only one instance of MarsAPI will be created
*/

object MarsAPI {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit2 = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create()) //handle JSON String Obj to Kotlin's
        .addCallAdapterFactory(CoroutineCallAdapterFactory()) //replace the default all Object with Deferred Object
        .baseUrl(BASE_URL)
        .build()

    val retrofitService = retrofit.create(MarsAPIService::class.java)

}



