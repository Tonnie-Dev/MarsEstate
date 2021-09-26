package com.androidshowtime.marsestate.detail

import android.app.Application
import androidx.lifecycle.*
import com.androidshowtime.marsestate.R
import com.androidshowtime.marsestate.network.MarsProperty


/**
 * The [ViewModel] that is associated with the [DetailFragment].
 */
class DetailViewModel(marsProperty: MarsProperty, app: Application) :
    AndroidViewModel(app) {


    private val _selectedProperty = MutableLiveData<MarsProperty>()
    val selectedProperty: LiveData<MarsProperty>
        get() = _selectedProperty

    init {
        _selectedProperty.value = marsProperty
    }

    //displays - KES 450,000 - <string name="display_price_monthly_rental">$%,.0f/month</string>
    val displayPropertyPrice = Transformations.map(selectedProperty) {


        //getString() accepts 2 arguments - String and String format
        //you need the applicationContext and to import the R class
        //import com.androidshowtime.packagename.R
        app.applicationContext.getString(
            when (it.isRentable) {

                true -> R.string.display_price_monthly_rental
                false -> R.string.display_price
            }, it.price
        )

    }

    //displays - For Sale - <string name="display_type">For %s</string>
    val displayPropertyType = Transformations.map(selectedProperty) {

        app.applicationContext.getString(
            R.string.display_type,
            app.applicationContext.getString(
                when (it.isRentable) {
                    true -> R.string.type_rent
                    false -> R.string.type_sale
                }
            )
        )
    }

    class DetailViewModelFactory(val marsProperty: MarsProperty, val app: Application) :
        ViewModelProvider
        .Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(marsProperty, app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}