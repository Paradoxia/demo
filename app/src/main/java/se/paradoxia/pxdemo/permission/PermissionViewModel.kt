package se.paradoxia.pxdemo.permission

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField

/**
 * Created by mikael on 2018-01-29.
 */
class PermissionViewModel : ViewModel() {

    val explanation = ObservableField<String>()

    init {
        explanation.set("klksdflksdlfk")
    }

}