package se.paradoxia.pxdemo.di

/**
 * Created by mikael on 2018-01-30.
 */
import dagger.Module
import dagger.android.ContributesAndroidInjector
import se.paradoxia.pxdemo.home.HomeFragment

@Module
abstract class TestFragmentModule {

    @ContributesAndroidInjector
    internal abstract fun bindHomeFragment(): HomeFragment


}