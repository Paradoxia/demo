package se.paradoxia.pxdemo.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import se.paradoxia.pxdemo.home.HomeFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun bindHomeFragment(): HomeFragment

}