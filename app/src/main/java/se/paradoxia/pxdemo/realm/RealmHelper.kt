package se.paradoxia.pxdemo.realm

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import io.realm.RealmResults

/**
 * Created by mikael on 2018-01-26.
 */
abstract class RealmHelper<in T> {


    fun <E : RealmObject> findById(fieldName: String, idKey: String?, clazz: Class<E>): E? {
        val realm = allowDeleteIfMigrationNeeded()
        return realm.where(clazz).equalTo(fieldName, idKey).findFirst()
    }

    fun <T : RealmObject> findAll(clazz: Class<T>): RealmResults<T> {
        val realm = allowDeleteIfMigrationNeeded()
        return realm.where(clazz).findAll()
    }

    fun save(obj: T): RealmObject {
        val realm = allowDeleteIfMigrationNeeded()
        realm.beginTransaction()
        val result = realm.copyToRealm(obj as RealmObject)
        realm.commitTransaction()
        return result
    }

    fun saveOrUpdate(obj: T): RealmObject {
        val realm = allowDeleteIfMigrationNeeded()
        realm.beginTransaction()
        val result = realm.copyToRealmOrUpdate(obj as RealmObject)
        realm.commitTransaction()
        return result
    }

    fun delete(obj: RealmObject) {
        val realm = allowDeleteIfMigrationNeeded()
        realm.beginTransaction()
        obj.deleteFromRealm()
        realm.commitTransaction()
    }

    /**
     * If "Migration Needed" is triggered by Realm due to RealmObject
     * properties/annotations etc. has been modified, then allow Realm
     * to just delete previously stored objects and indices.
     */
    private fun allowDeleteIfMigrationNeeded(): Realm {
        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        return Realm.getInstance(config)

    }

}