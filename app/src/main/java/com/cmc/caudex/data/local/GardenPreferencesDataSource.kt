package com.cmc.caudex.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class GardenPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    val gardenId: Flow<String> = dataStore.data.map { it[KEY_GARDEN_ID].orEmpty() }
    val gardenUrl: Flow<String> = dataStore.data.map { it[KEY_GARDEN_URL].orEmpty() }
    val templateUrl: Flow<String> = dataStore.data.map { it[KEY_TEMPLATE_URL].orEmpty() }
    val uploadedPlantId: Flow<Int> = dataStore.data.map { it[KEY_UPLOADED_PLANT_ID] ?: 0 }

    suspend fun saveGarden(gardenId: String, gardenUrl: String, templateUrl: String) {
        dataStore.edit { prefs ->
            prefs[KEY_GARDEN_ID] = gardenId
            prefs[KEY_GARDEN_URL] = gardenUrl
            prefs[KEY_TEMPLATE_URL] = templateUrl
        }
    }

    suspend fun saveUploadedPlantId(plantId: Int) {
        dataStore.edit { it[KEY_UPLOADED_PLANT_ID] = plantId }
    }

    suspend fun getGardenIdOnce(): String = gardenId.firstOrNull().orEmpty()
    suspend fun getGardenUrlOnce(): String = gardenUrl.firstOrNull().orEmpty()
    suspend fun getTemplateUrlOnce(): String = templateUrl.firstOrNull().orEmpty()
    suspend fun getUploadedPlantIdOnce(): Int = uploadedPlantId.firstOrNull() ?: 0

    companion object {
        private val KEY_GARDEN_ID = stringPreferencesKey("garden_id")
        private val KEY_GARDEN_URL = stringPreferencesKey("garden_url")
        private val KEY_TEMPLATE_URL = stringPreferencesKey("template_url")
        private val KEY_UPLOADED_PLANT_ID = intPreferencesKey("uploaded_plant_id")
    }
}
