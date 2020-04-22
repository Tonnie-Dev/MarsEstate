package com.androidshowtime.marsestate.overview


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidshowtime.marsestate.network.MarsAPI
import com.androidshowtime.marsestate.network.MarsFilterClass
import com.androidshowtime.marsestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


enum class MarsAPIStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {
    // The internal MutableLiveData String that stores the most
    // recent response
    private val _status = MutableLiveData<MarsAPIStatus>()


    // The external immutable LiveData for the response String
    val status: LiveData<MarsAPIStatus>
        get() = _status

    // Call getMarsRealEstateProperties() on init so we can display
    // status immediately.


    private val _properties = MutableLiveData<List<MarsProperty>>()
    val properties: LiveData<List<MarsProperty>>
        get() = _properties


    //when this LiveData changes to non-null navigation is triggered
    private val _navigateToSelectedProperty = MutableLiveData<MarsProperty>()
    val navigateToSelectedProperty: LiveData<MarsProperty>
        get() = _navigateToSelectedProperty

    /*to use Deferred Object returned from retrofit you need to be inside
    a coroutine so one needs to be created*/
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        getMarsRealEstateProperties(MarsFilterClass.SHOW_ALL)
    }

    /* Sets the value of the status LiveData to the Mars API status.
     This method will call the Retrofit service and handle the
    returned JSON string*/


    private fun getMarsRealEstateProperties(filter: MarsFilterClass) {
        //launching the coroutine using launch coroutine builder
        coroutineScope.launch {

            //obtaining the deferred object from retrofit
            var getPropertiesDeferred =
                    MarsAPI.retrofitService.getPropertiesAsync(filter.type)

            //try-catch block to handle exceptions
            try {

                _status.value = MarsAPIStatus.LOADING
                //calling await() on deferred object returns a result when ready
                var listResult = getPropertiesDeferred.await()

                //updating the response message when successful
                _status.value = MarsAPIStatus.DONE
                _properties.value = listResult


                //handle the failure response inside the catch block
            }
            catch (e: Exception) {
                _status.value = MarsAPIStatus.ERROR

                _properties.value = ArrayList()
            }
        }
    }


    fun updateFilter(filter: MarsFilterClass) {

        getMarsRealEstateProperties(filter)

    }


    fun displayPropertyDetails(marsProperty: MarsProperty) {
        _navigateToSelectedProperty.value = marsProperty

    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
