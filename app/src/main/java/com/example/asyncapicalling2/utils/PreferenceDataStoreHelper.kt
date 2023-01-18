package com.example.asyncapicalling2.utils


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first


/**
 * Description : This helper class provide the local storage in application using the preferences datastore.
 *
 * @author Abhishek Oza
 * @since 11th jan 23
 */
class PreferenceDataStoreHelper(val dataStore: DataStore<Preferences>) {

    /**
     * Description : This is the generic function which can handle to store the datastore for any type.
     *
     * @author Abhishek Oza
     * @since 11th jan 23
     */
    suspend fun <T> genericWrite(key: String, value: T) {
        /**
         * This instruction will check that if value type is below given type
         * then perform according that type.
         */
        when (value) {
            is String -> {
                val dataStoreKey = stringPreferencesKey(key)
                dataStore.edit {
                    it[dataStoreKey] = value
                }
            }
            is Int -> {
                val dataStoreKey = intPreferencesKey(key)
                dataStore.edit {
                    it[dataStoreKey] = value
                }
            }
            is Boolean -> {
                val dataStoreKey = booleanPreferencesKey(key)
                dataStore.edit {
                    it[dataStoreKey] = value
                }
            }
            is Float -> {
                val dataStoreKey = floatPreferencesKey(key)
                dataStore.edit {
                    it[dataStoreKey] = value
                }
            }
            is Long -> {
                val dataStoreKey = longPreferencesKey(key)
                dataStore.edit {
                    it[dataStoreKey] = value
                }
            }
            is Double -> {
                val dataStoreKey = doublePreferencesKey(key)
                dataStore.edit {
                    it[dataStoreKey] = value
                }
            }
            else -> {
                val dataStoreKey = stringPreferencesKey(key)
                val arrayValue = value as T
                val gson = Gson()
                val json: String = gson.toJson(arrayValue)
                dataStore.edit {
                    it[dataStoreKey] = json
//                    Log.d("the it here", "${it[dataStoreKey]}")
                }
//                for (i in arrayValue.indices) {
//                    dataStore.edit {
//                        it[dataStoreKey] = arrayValue[i].toString()
//                    }
//                }
            }
        }
    }

    /**
     * Description : This is the inline generic function which can handle to read the data for
     *               any type which was stored in preferences datastore.
     * @author Abhishek Oza
     * @since 11th jan 23
     */
    suspend inline fun <reified T> genericRead(key: String): T {
        return when (T::class.java) {
            java.lang.Integer::class.java -> {
                val dataStoreKey = intPreferencesKey(key)
                val returnData = dataStore.data.first()

                returnData[dataStoreKey] as T
            }
            String::class.java -> {
                val dataStoreKey = stringPreferencesKey(key)
                val returnData = dataStore.data.first()

                returnData[dataStoreKey] as T
            }
            java.lang.Boolean::class.java -> {
                val dataStoreKey = booleanPreferencesKey(key)
                val returnData = dataStore.data.first()

                returnData[dataStoreKey] as T
            }
            java.lang.Float::class.java -> {
                val dataStoreKey = floatPreferencesKey(key)
                val returnData = dataStore.data.first()

                returnData[dataStoreKey] as T
            }
            java.lang.Double::class.java -> {
                val dataStoreKey = doublePreferencesKey(key)
                val returnData = dataStore.data.first()

                returnData[dataStoreKey] as T
            }
            java.lang.Long::class.java -> {
                val dataStoreKey = longPreferencesKey(key)
                val returnData = dataStore.data.first()

                returnData[dataStoreKey] as T
            }
            else -> {
                val dataStoreKey = stringPreferencesKey(key)
                val returnData = dataStore.data.first()

                val gson = GsonBuilder().create()
                val theList = gson.fromJson<T>(returnData[dataStoreKey], object :TypeToken<T>(){}.type)

                theList as T

            }
        }
    }

    suspend fun <T>genericReadJson(key:String):T{
        val dataStoreKey = stringPreferencesKey(key)
        val returnData = dataStore.data.first()

        val gson = GsonBuilder().create()
        val theList = gson.fromJson<T>(returnData[dataStoreKey], object :TypeToken<T>(){}.type)

        return theList as T
    }

    suspend fun <T>genericWriteJson(key:String,value:T){
        val dataStoreKey = stringPreferencesKey(key)
        val arrayValue = value as T
        val gson = Gson()
        val json: String = gson.toJson(arrayValue)
        dataStore.edit {
            it[dataStoreKey] = json
//                    Log.d("the it here", "${it[dataStoreKey]}")
        }
    }

}