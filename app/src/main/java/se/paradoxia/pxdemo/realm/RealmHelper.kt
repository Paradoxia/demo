package se.paradoxia.pxdemo.realm

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject

/**
 * Created by mikael on 2018-01-26.
 */
abstract class RealmHelper<in T>(private val realmInstanceMaker: RealmInstanceMaker) {

    fun <E : RealmObject> findById(fieldName: String, idKey: String?, clazz: Class<E>): E? {
        val realm = realmInstanceMaker.getRealmInstance()
        val proxyResult = realm.where(clazz).equalTo(fieldName, idKey).findFirst()
        return if (proxyResult != null) {
            val result = realm.copyFromRealm(proxyResult)
            realm.close()
            result
        } else {
            realm.close()
            null
        }
    }

    fun <T : RealmObject> findAll(clazz: Class<T>): List<T> {
        val realm = realmInstanceMaker.getRealmInstance()
        val proxyResult = realm.where(clazz).findAll()
        val result = realm.copyFromRealm(proxyResult)
        realm.close()
        return result
    }

    fun save(obj: T): RealmObject {
        val realm = realmInstanceMaker.getRealmInstance()
        realm.beginTransaction()
        val proxyResult = realm.copyToRealm(obj as RealmObject)
        realm.commitTransaction()
        val result = realm.copyFromRealm(proxyResult)
        realm.close()
        return result
    }

    fun saveOrUpdate(obj: T): RealmObject {
        val realm = realmInstanceMaker.getRealmInstance()
        realm.beginTransaction()
        val proxyResult = realm.copyToRealmOrUpdate(obj as RealmObject)
        realm.commitTransaction()
        val result = realm.copyFromRealm(proxyResult)
        realm.close()
        return result
    }

    fun delete(obj: RealmObject) {
        val realm = realmInstanceMaker.getRealmInstance()
        realm.beginTransaction()
        obj.deleteFromRealm()
        realm.commitTransaction()
        realm.close()
    }

}

abstract class RealmInstanceMaker {

    /**
     * If "Migration Needed" is triggered by Realm due to RealmObject
     * properties/annotations etc. has been modified, then allow Realm
     * to just delete previously stored objects and indices.
     */
    open fun getRealmInstance(): Realm {
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        return Realm.getInstance(config)
    }

}