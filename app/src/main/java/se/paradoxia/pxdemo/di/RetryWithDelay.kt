package se.paradoxia.pxdemo.di

import java.util.concurrent.TimeUnit
import io.reactivex.functions.Function
import io.reactivex.*
import timber.log.Timber

class RetryWithDelay
    (private val maxRetries: Int, private val retryDelayMillis: Long) :
    Function<Observable<Throwable>, Observable<Long>> {
    override fun apply(attempts: Observable<Throwable>): Observable<Long> {
        return attempts
            .flatMap(object : Function<Throwable, Observable<Long>> {

                private var retryCount: Int = 0

                override fun apply(throwable: Throwable): Observable<Long> {
                    return if (++retryCount < maxRetries) {
                        Timber.w("Retrying with $retryDelayMillis ms delay, attempt #$retryCount")
                        Observable.timer(
                            retryDelayMillis,
                            TimeUnit.MILLISECONDS
                        )
                    } else {
                        Timber.e("Retrying failed after $maxRetries")
                        Observable.error<Long>(throwable)
                    }
                }
            })
    }
}