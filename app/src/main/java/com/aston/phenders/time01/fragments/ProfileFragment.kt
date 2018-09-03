package com.aston.phenders.time01.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.aston.phenders.time01.R
import com.aston.phenders.time01.activities.MainActivity
import com.aston.phenders.time01.database.DatabaseHelper
import com.aston.phenders.time01.database.UserTable
import com.aston.phenders.time01.models.User
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.warn

class ProfileFragment : Fragment(), AnkoLogger {
    //var text: TextView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        var codeView = view.findViewById<EditText>(R.id.text_input_profile_project_code)
        var taskView = view.findViewById<EditText>(R.id.text_input_profile_project_task)
        var categoryView = view.findViewById<Spinner>(R.id.profile_category_spinner)
        val adapter = ArrayAdapter.createFromResource(context,
                R.array.categories_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoryView!!.adapter = adapter
        var hoursView = view.findViewById<EditText>(R.id.num_profile_input_hours)
        var weekendView = view.findViewById<CheckBox>(R.id.checkbox_profile_include_weekends)
        val savePrefsButton = view.findViewById<Button>(R.id.button_save_prefs)

        adapter.getPosition("12324")
        val db = DatabaseHelper(activity!!.applicationContext)
        val userTable = UserTable(db)

        var user = userTable.getUser()

        warn("user prefs retrieved")
        codeView.setText(user.projectCode)
        taskView.setText(user.projectTask)
        categoryView.setSelection(adapter.getPosition(user.category))
        hoursView.setText(user.workingHours.toString())
        weekendView.isChecked = user.worksWeekends!!.toBoolean()


        savePrefsButton.setOnClickListener {
            var newPrefs = User()
            newPrefs.projectCode = codeView.text.toString()
            newPrefs.projectTask = taskView.text.toString()
            newPrefs.category = categoryView.selectedItem.toString()
            newPrefs.workingHours = hoursView.text.toString().toFloat()
            newPrefs.worksWeekends = weekendView.isChecked.toString()

            userTable.updateUser(newPrefs)
            (activity as MainActivity).loadPrefs = true //reflect changes in book time page
            toast("Preferences Updated.")

        }
        return view
    }


}