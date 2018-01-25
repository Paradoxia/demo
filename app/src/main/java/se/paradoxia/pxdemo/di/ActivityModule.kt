package se.paradoxia.pxdemo.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import se.paradoxia.pxdemo.MainActivity

@Module(includes = [ViewModelModule::class])
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun bindMainActivity(): MainActivity

}

