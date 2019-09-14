package com.nuryazid.dagersapr.base

import androidx.lifecycle.ViewModel
import com.nuryazid.dagersapr.injection.component.DaggerViewModelInjector
import com.nuryazid.dagersapr.injection.component.ViewModelInjector
import com.nuryazid.dagersapr.module.NetworkModule
import com.nuryazid.dagersapr.ui.post.PostListViewModel

abstract class BaseViewModel:ViewModel(){
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is PostListViewModel -> injector.inject(this)
        }
    }
}