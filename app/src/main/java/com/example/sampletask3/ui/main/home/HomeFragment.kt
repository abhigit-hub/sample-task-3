package com.example.sampletask3.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampletask3.MyApp
import com.example.sampletask3.R
import com.example.sampletask3.databinding.FragmentHomeBinding
import com.example.sampletask3.di.component.DaggerFragmentComponent
import com.example.sampletask3.di.module.FragmentModule
import com.example.sampletask3.ui.main.SharedViewModel
import com.example.sampletask3.ui.main.home.details.GroupAdapter
import com.example.sampletask3.utils.display.Toaster
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class HomeFragment : Fragment() {

    companion object {
        const val TAG = "HomeFragment"

        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: SharedViewModel

    @Inject
    lateinit var layoutManager: LinearLayoutManager

    @Inject
    lateinit var adapter: GroupAdapter

    lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        setUpObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView(view)
    }

    private fun injectDependencies() {
        DaggerFragmentComponent.builder()
            .applicationComponent((context?.applicationContext as MyApp).applicationComponent)
            .fragmentModule(FragmentModule(this))
            .build()
            .inject(this)
    }

    private fun setUpObservers() {
        viewModel.getMessageString().observe(this, Observer {
            it.data?.run { showMessage(this) }
        })

        viewModel.getMessageStringId().observe(this, Observer {
            it.data?.run { showMessage(this) }
        })

        viewModel.getSnackbarMessage().observe(this, Observer {
            it.data?.run { showSnackbarMessage(this) }
        })

        viewModel.getGroupList().observe(this, Observer {
            it?.run {
                if (isNotEmpty()) {
                    adapter.appendData(this)
                }
            }
        })

        viewModel.getExclusionMap().observe(this, Observer {
            it?.run {
                if (isNotEmpty())
                    adapter.updateExclusionMap(this)
            }
        })
    }

    private fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

    private fun showMessage(message: String) = context?.let { Toaster.show(it, message) }

    private fun showSnackbarMessage(resId: Int) {
        val snackbar = activity?.getString(resId)?.let {
            Snackbar.make(activity!!.findViewById(android.R.id.content), it, Snackbar.LENGTH_SHORT)
        }

        val view = snackbar?.view

        val snackTV = view?.findViewById(R.id.snackbar_text) as TextView
        this.activity?.let { ContextCompat.getColor(it, R.color.white) }?.let { snackTV.setTextColor(it) }

        snackbar.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
        snackbar.show()
    }

    private fun setUpView(view: View) {
        binding.viewModel = viewModel
        binding.recyclerviewGroup.layoutManager = layoutManager
        binding.recyclerviewGroup.adapter = adapter
    }
}