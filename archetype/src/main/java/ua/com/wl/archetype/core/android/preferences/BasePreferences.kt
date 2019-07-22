package ua.com.wl.archetype.core.android.preferences

import android.content.Context
import android.content.SharedPreferences

/**
 * @author Denis Makovskyi
 */

abstract class BasePreferences(context: Context, name: String, mode: Int = Context.MODE_PRIVATE) {

    private val preferences: SharedPreferences = context.getSharedPreferences(name, mode)

    fun clear() = preferences.edit().clear()

    protected fun save(key: String, value: Boolean, commit: Boolean = false): Boolean = preferences.edit()
        .putBoolean(key, value)
        .let { saveChanges(it, commit) }

    protected fun save(key: String, value: Int, commit: Boolean = false): Boolean = preferences.edit()
        .putInt(key, value)
        .let { saveChanges(it, commit) }

    protected fun save(key: String, value: Long, commit: Boolean = false) = preferences.edit()
        .putLong(key, value)
        .let { saveChanges(it, commit) }

    protected fun save(key: String, value: Float, commit: Boolean = false) = preferences.edit()
        .putFloat(key, value)
        .let { saveChanges(it, commit) }

    protected fun save(key: String, value: String?, commit: Boolean = false) = preferences.edit()
        .putString(key, value)
        .let { saveChanges(it, commit) }

    protected fun save(key: String, value: Set<String>?, commit: Boolean = false) = preferences.edit()
        .putStringSet(key, value)
        .let { saveChanges(it, commit) }

    protected fun getBoolean(key: String, default: Boolean = false): Boolean = preferences.getBoolean(key, default)

    protected fun getInteger(key: String, default: Int = 0): Int = preferences.getInt(key, default)

    protected fun getLong(key: String, default: Long = 0L): Long = preferences.getLong(key, default)

    protected fun getFloat(key: String, default: Float = 0.0F): Float = preferences.getFloat(key, default)

    protected fun getString(key: String, default: String? = null): String? = preferences.getString(key, default)

    protected fun getStringSet(key: String, default: Set<String>?): Set<String>? = preferences.getStringSet(key, default)

    protected fun remove(key: String) = preferences.edit().remove(key).apply()

    private fun saveChanges(editor: SharedPreferences.Editor, commit: Boolean): Boolean =
        if (commit) {
            editor.commit()

        } else {
            editor.apply()
            true
        }
}