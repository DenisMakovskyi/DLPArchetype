package ua.com.wl.archetype.core.android.preferences

import android.content.Context
import android.content.SharedPreferences

/**
 * @author Denis Makovskyi
 */
@Deprecated("Moved to DLPCore.")
abstract class BasePreferences(
        context: Context,
        name: String, mode:
        Int = Context.MODE_PRIVATE
) {

    private val preferences: SharedPreferences = context.getSharedPreferences(name, mode)

    fun clear() = preferences.edit().clear().apply()

    protected fun save(key: String, value: Boolean, commit: Boolean = false): Boolean {
        return preferences.edit()
                .putBoolean(key, value)
                .let { saveChanges(it, commit) }
    }

    protected fun save(key: String, value: Int, commit: Boolean = false): Boolean {
        return preferences.edit()
                .putInt(key, value)
                .let { saveChanges(it, commit) }
    }

    protected fun save(key: String, value: Long, commit: Boolean = false): Boolean {
        return preferences.edit()
                .putLong(key, value)
                .let { saveChanges(it, commit) }
    }

    protected fun save(key: String, value: Float, commit: Boolean = false): Boolean {
        return preferences.edit()
                .putFloat(key, value)
                .let { saveChanges(it, commit) }
    }

    protected fun save(key: String, value: String?, commit: Boolean = false): Boolean {
        return preferences.edit()
                .putString(key, value)
                .let { saveChanges(it, commit) }
    }

    protected fun save(key: String, value: Set<String>?, commit: Boolean = false): Boolean {
        return preferences.edit()
                .putStringSet(key, value)
                .let { saveChanges(it, commit) }
    }

    protected fun getBoolean(key: String, default: Boolean = false): Boolean {
        return preferences.getBoolean(key, default)
    }

    protected fun getInteger(key: String, default: Int = 0): Int {
        return preferences.getInt(key, default)
    }

    protected fun getLong(key: String, default: Long = 0L): Long {
        return preferences.getLong(key, default)
    }

    protected fun getFloat(key: String, default: Float = 0.0F): Float {
        return preferences.getFloat(key, default)
    }

    protected fun getString(key: String, default: String? = null): String? {
        return preferences.getString(key, default)
    }

    protected fun getStringSet(key: String, default: Set<String>?): Set<String>? {
        return preferences.getStringSet(key, default)
    }

    protected fun remove(key: String) = preferences.edit().remove(key).apply()

    private fun saveChanges(editor: SharedPreferences.Editor, commit: Boolean): Boolean {
        return if (commit) {
            editor.commit()
        } else {
            editor.apply()
            true
        }
    }
}