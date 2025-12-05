package com.payable.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.onboardingDataStore: DataStore<Preferences> by preferencesDataStore("onboarding")

class OnboardingDataStore(
    private val context: Context,
) {

    private val keyShown = booleanPreferencesKey("shown")

    val onboardingShown = context.onboardingDataStore.data.map { prefs ->
        prefs[keyShown] ?: false
    }

    suspend fun markAsShown() {
        context.onboardingDataStore.edit { prefs ->
            prefs[keyShown] = true
        }
    }
}



