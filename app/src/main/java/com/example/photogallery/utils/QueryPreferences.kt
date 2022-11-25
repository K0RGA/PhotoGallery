package com.example.photogallery.utils

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit

private const val PREF_SEARCH_QUERY = "searchQuery"

object QueryPreferences {
    fun getStoryQuery(context: Context): String{
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_SEARCH_QUERY, "")!!
    }

    fun getLastResultId(context: Context): String{
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(PREF_SEARCH_QUERY,"")!!
    }

    fun setLastResultId(context: Context, lastResultId: String){
        PreferenceManager.getDefaultSharedPreferences(context).edit{
            putString(PREF_SEARCH_QUERY, lastResultId)
        }
    }
    
    fun setStoryQuery(context: Context, query: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit{
                putString(PREF_SEARCH_QUERY, query)
            }
    }
}