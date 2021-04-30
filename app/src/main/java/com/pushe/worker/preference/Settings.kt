package com.pushe.worker.preference

//import android.annotation.TargetApi
//import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.pushe.worker.R
import java.util.*

private const val TITLE_TAG = "settingsActivityTitle"

class Settings : AppCompatActivity(),
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, HeadersFragment())
                    .commit()
        } else {
            title = savedInstanceState.getCharSequence(TITLE_TAG)
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                setTitle(R.string.app_name)
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TITLE_TAG, title)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (supportFragmentManager.popBackStackImmediate()) {
            return true
        }
        return super.onSupportNavigateUp()
    }

    override fun onPreferenceStartFragment(
            caller: PreferenceFragmentCompat,
            pref: Preference
    ): Boolean {
        // Instantiate the new Fragment
        val args = pref.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
                classLoader,
                pref.fragment
        ).apply {
            arguments = args
            setTargetFragment(caller, 0)
        }
        // Replace the existing Fragment with the new Fragment
        supportFragmentManager.beginTransaction()
                .replace(R.id.settings, fragment)
                .addToBackStack(null)
                .commit()
        title = pref.title
        return true
    }

    /**
     * {@inheritDoc}
     */
//    fun onIsMultiPane(): Boolean {
//        return isXLargeTablet(this)
//    }

     class HeadersFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.pref_headers, rootKey)
        }
    }

    class GeneralPreferenceFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.pref_general, rootKey)
        }
    }

    class ErpPreferenceFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.pref_erp, rootKey)
            bindPreferenceSummaryToValue(Objects.requireNonNull(
                    findPreference("erp_path"))!!)
            bindPreferenceSummaryToValue(Objects.requireNonNull(
                    findPreference("erp_user"))!!)
            bindPreferenceSummaryToValue(Objects.requireNonNull(
                    findPreference("erp_password"))!!)
        }
    }

    companion object {
        private fun bindPreferenceSummaryToValue(preference: Preference) {
            // Set the listener to watch for value changes.
            preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            // Trigger the listener immediately with the preference's
            // current value.
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.context)
                            .getString(preference.key, ""))
        }

        private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            preference.summary = newValue.toString()
            true
        }
    }
//    companion object {
//        /**
//         * A preference value change listener that updates the preference's summary
//         * to reflect its new value.
//         */
//        private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference: Preference, value: Any ->
//            val stringValue = value.toString()
//            if (preference is ListPreference) {
//                // For list preferences, look up the correct display value in
//                // the preference's 'entries' list.
//                val listPreference = preference
//                val index = listPreference.findIndexOfValue(stringValue)
//
//                // Set the summary to reflect the new value.
//                preference.setSummary(
//                        if (index >= 0) listPreference.entries[index] else null)
//            } else {
//                // For all other preferences, set the summary to the value's
//                // simple string representation.
//                preference.summary = stringValue
//            }
//            true
//        }
//
//        /**
//         * Helper method to determine if the device has an extra-large screen. For
//         * example, 10" tablets are extre-large.
//         */
//        private fun isXLargeTablet(context: Context): Boolean {
//            return (context.resources.configuration.screenLayout
//                    and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE
//        }
//
//        /**
//         * Binds a preference's summary to its value. More specifically, when the
//         * preference's value is changed, its summary (line of text below the
//         * preference title) is updated to reflect the value. The summary is also
//         * immediately updated upon calling this method. The exact display format is
//         * dependent on the type of preference.
//         *
//         * @see .sBindPreferenceSummaryToValueListener
//         */
//    }
}