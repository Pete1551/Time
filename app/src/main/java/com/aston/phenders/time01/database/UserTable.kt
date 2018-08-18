package com.aston.phenders.time01.database

import com.aston.phenders.time01.models.UserPrefs
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import java.util.logging.Logger


class UserTable(val db: DatabaseHelper) {

    val Log = Logger.getLogger(UserTable::class.java.name)


    fun getPrefs(): ArrayList<UserPrefs> {

        var prefs = ArrayList<UserPrefs>()


        var prefsParser = rowParser { id: Int, name: String, workingdays: String, workinghours: String ->
            val pref = UserPrefs()
            pref.id = id
            pref.name = name
            pref.workingdays = workingdays
            pref.workinghours = workinghours

            prefs.add(pref)

        }

        db.use {
            select("User").parseList(prefsParser)
        }


        return prefs


    }


    fun updatePrefs() {
        db.use {
            insert("User",
                    "id" to 1,
                    "name" to "Pete",
                    "workingdays" to "mon-Fri",
                    "workinghours" to "7.5"
            )
        }

    }

}