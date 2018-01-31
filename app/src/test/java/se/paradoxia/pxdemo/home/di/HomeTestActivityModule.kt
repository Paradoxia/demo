package se.paradoxia.pxdemo.home.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import se.paradoxia.pxdemo.di.FragmentModule
import se.paradoxia.pxdemo.home.StubMainActivity

/**
 * Created by mikael on 2018-01-31.
 */
@Module
abstract class HomeTestActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun bindStubMainActivity(): StubMainActivity

}