package com.androidshowtime.marsestate


import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.androidshowtime.marsestate.network.MarsProperty
import com.androidshowtime.marsestate.overview.MarsAPIStatus
import com.androidshowtime.marsestate.overview.PhotoGridAdapter
import com.bumptech.glide.Glide


/*This file will hold the binding adapters that you use throughout the app.*/


/*using a binding adapter to take the URL from an XML attribute associated
with an ImageView and  use Glide to load the image. */

/*Binding adapters are extension methods that sit between a view and bound data
to provide custom behavior when the data changes. In this case, the custom
behavior is to call Glide to load an image from a URL into an ImageView.*/

@BindingAdapter("imageUrlX")
fun bindImage(imgView: ImageView, imgUrl: String?) {


    //Let returns a copy of the changed object in this case we are changing imgUrl

    /*we use let to convert URL String to a Uri object use HTTPs scheme, because
    the server you pull the images from requires that scheme. To use HTTPS scheme
    append buildUpon.scheme("https") to toUri() builder method
     */

    /*toUri() method is a Kotlin extension fxn from Android KTX core library,so
    * it just looks like it is part of the String Class*/
    imgUrl?.let {
        val imgUri = imgUrl.toUri()
            .buildUpon()
            .scheme("https")
            .build()



        Glide.with(imgView.context)
            .load(imgUri)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
            .into(imgView)


    }


    /*@BindingAdapter("listDataX")
    fun bindRecyclerView(recyclerView: RecyclerView, data: List<MarsProperty>?) {

        val adapter = recyclerView.adapter as PhotoGridAdapter

        adapter.submitList(data)
    }*/


}

@BindingAdapter("listDataX")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<MarsProperty>?) {

    val adapter = recyclerView.adapter as PhotoGridAdapter

    //tells recyclerView when the new list is available
    adapter.submitList(data)


}


@BindingAdapter("marsAPIStatusX")
fun bindStatus(statusImageView: ImageView, status: MarsAPIStatus?) {

    when (status) {
        MarsAPIStatus.LOADING -> {

            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }

        MarsAPIStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }

        MarsAPIStatus.DONE -> {
            statusImageView.visibility = View.GONE

        }

    }


}
//
//@BindingAdapter("textTypeX")
//fun displayTextType(textView:TextView,marsProperty: MarsProperty){
//val app = requireNotNull(activity).application
//
//    val detailFrag = DetailViewModel(marsProperty, app)
//
//        app.applicationContext.getString(
//            R.string.display_type,
//            textView.context.getString(
//                when (marsProperty.isRentable) {
//                    true -> R.string.type_rent
//                    false -> R.string.type_sale
//                }
//            )
//        )
//
//
//}