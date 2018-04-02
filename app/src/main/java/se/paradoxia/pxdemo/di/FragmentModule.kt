package se.paradoxia.pxdemo.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import se.paradoxia.pxdemo.home.view.HomeViewFragment
import se.paradoxia.pxdemo.personalinfo.view.PersonalInfoFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun bindHomeView(): HomeViewFragment

    @ContributesAndroidInjector
    abstract fun bindPersonalInfoView(): PersonalInfoFragment


}