package se.paradoxia.pxdemo.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import se.paradoxia.pxdemo.home.view.HomeView
import se.paradoxia.pxdemo.personalinfo.view.PersonalInfoView

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun bindHomeView(): HomeView

    @ContributesAndroidInjector
    abstract fun bindPersonalInfoView(): PersonalInfoView


}