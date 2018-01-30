package se.paradoxia.pxdemo.home

import android.support.v7.widget.RecyclerView
import android.widget.FrameLayout
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import se.paradoxia.pxdemo.BuildConfig
import se.paradoxia.pxdemo.MainActivity
import se.paradoxia.pxdemo.R
import se.paradoxia.pxdemo.di.TestApp

/**
 * Created by mikael on 2018-01-30.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, application = TestApp::class, qualifiers = "w360dp-h640dp-xhdpi")
class HomeFragmentTest {

    private lateinit var mainActivity: MainActivity

    @Before
    fun setup() {
        mainActivity = Robolectric.setupActivity(MainActivity::class.java)
    }

    @Test
    fun shouldX() {

        val z : FrameLayout = mainActivity.findViewById(R.id.flPage)

        val recyclerView : RecyclerView = z.findViewById(R.id.recViewHome)


        val firstCount = recyclerView.childCount
        recyclerView.scrollToPosition(1)
        val secondCount = recyclerView.childCount
        val itemCount = recyclerView.adapter.itemCount



        // recyclerView.childCount

/*        val fragmentController = Robolectric.buildFragment(HomeFragment::class.java, AppCompatActivity::class.java)
        fragmentController.create().start().resume().visible() */

        println("z")



    }




}
