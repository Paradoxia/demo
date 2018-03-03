package se.paradoxia.pxdemo.util

/**
 * Created by mikael on 2018-03-03.
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ModelField(val order : Int, val isField : Boolean)