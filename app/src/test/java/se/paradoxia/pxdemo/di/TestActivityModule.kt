package se.paradoxia.pxdemo.di

/**
 * Created by mikael on 2018-01-30.
 */
import dagger.Module
import dagger.android.ContributesAndroidInjector
import se.paradoxia.pxdemo.MainActivity


//@Module(includes = [TestViewModelModule::class])
@Module
abstract class TestActivityModule {

    @ContributesAndroidInjector(modules = [TestFragmentModule::class])
    internal abstract fun bindMainActivity(): MainActivity

}