package com.androidshowtime.marsestate.overview


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.androidshowtime.marsestate.R
import com.androidshowtime.marsestate.databinding.FragmentOverviewBinding
import com.androidshowtime.marsestate.network.MarsFilterClass

/**
 * This fragment shows the the status of the Mars real-estate web services transaction.
 */
class OverviewFragment : Fragment() {

    /**
     * Lazily initialize our [OverviewViewModel].
     * This means the OverViewViewModel is created the first time it us used
     */
    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this).get(OverviewViewModel::class.java)
    }

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentOverviewBinding.inflate(inflater)

        // Allows DataBinding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        /*giving the binding access to the OverviewViewModel - because we have set the
         lifecycle Owner any LiveData used in data binding will automatically be
         observed for any changes and the UI will be updated accordingly*/
        binding.viewModel = viewModel


        binding.photosGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {

            viewModel.displayPropertyDetails(it)
        })


        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {

            if (it != null) {
                this.findNavController()
                    .navigate(OverviewFragmentDirections.actionShowDetail(it))
                viewModel.displayPropertyDetailsComplete()
            }
        })

        return binding.root
    }

    /**
     * Inflates the overflow menu that contains filtering options.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.show_rent_menu -> viewModel.updateFilter(MarsFilterClass.SHOW_RENT)
            R.id.show_buy_menu -> viewModel.updateFilter(MarsFilterClass.SHOW_BUY)
            else -> viewModel.updateFilter(MarsFilterClass.SHOW_ALL)
        }

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
}
