package se.paradoxia.pxdemo.di

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import se.paradoxia.pxdemo.home.viewmodel.HomeViewModel
import se.paradoxia.pxdemo.personalinfo.viewmodel.PersonalInfoViewModel

@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonalInfoViewModel::class)
    abstract fun bindPersonalInfoViewModel(viewModel: PersonalInfoViewModel): ViewModel

}