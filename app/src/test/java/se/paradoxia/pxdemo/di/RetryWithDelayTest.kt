package se.paradoxia.pxdemo.di

import android.os.SystemClock
import io.reactivex.Observable
import org.junit.Test
import se.paradoxia.pxdemo.Junit4TestBase
import kotlin.test.assertEquals

class RetryWithDelayTest : Junit4TestBase() {

    override fun setUp() {}

    override fun tearDown() {}

    @Test
    fun shouldRetryThreeTimesWithDelayBetweenAndSucceed() {

        var callCounter = 0
        var finalResult: String? = null

        Observable.create<String> { emitter ->
            if (++callCounter < 3) {
                emitter.onError(RuntimeException("Error!!!"))
            } else {
                emitter.onNext("Tada!!")
            }
            emitter.onComplete()
        }
            .retryWhen(RetryWithDelay(maxRetries = 3, retryDelayMillis = 100))
            .blockingForEach { result ->
                finalResult = result
            }

        assertEquals("Tada!!", finalResult)
        assertEquals(3, callCounter)

    }

    @Test
    fun shouldRetryThreeTimesWithDelayBetweenAndFail() {

        var callCounter = 0
        var finalResult: String? = null

        Observable.create<String> { emitter ->
            if (++callCounter < 3) {
                emitter.onError(RuntimeException("Error!!!"))
            } else {
                emitter.onNext("Tada!!")
            }
            emitter.onComplete()
        }
            .retryWhen(RetryWithDelay(maxRetries = 2, retryDelayMillis = 100))
            .blockingSubscribe(
                { result ->
                    finalResult = result
                }, { error ->
                    finalResult = error.message
                })

        assertEquals("Error!!!", finalResult)
        assertEquals(2, callCounter)

    }

    @Test
    fun shouldRetryWithTwoSecondDelay() {

        var callCounter = 0
        val start = System.currentTimeMillis()
        Observable.create<String> { emitter ->
            if (++callCounter < 2) {
                emitter.onError(RuntimeException("Error!!!"))
            } else {
                emitter.onNext("Tada!!")
            }
            emitter.onComplete()
        }
            .retryWhen(RetryWithDelay(maxRetries = 3, retryDelayMillis = 2000))
            .blockingSingle()

        val end = System.currentTimeMillis()
        val finalDelay = end - start

        assertEquals(true, finalDelay >= 2000)



    }


}