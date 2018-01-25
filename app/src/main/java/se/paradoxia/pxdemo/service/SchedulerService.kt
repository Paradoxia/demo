package se.paradoxia.pxdemo.service

import io.reactivex.Scheduler

/**
 * Created by mikael on 2018-01-23.
 */
interface SchedulerService {

    fun mainThread(): Scheduler
    fun computation(): Scheduler
    fun trampoline(): Scheduler
    fun newThread(): Scheduler
    fun io(): Scheduler

}