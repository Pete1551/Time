package com.aston.phenders.time01.database

import com.aston.phenders.time01.models.User
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.warn


class UserTable(val db: DatabaseHelper) : AnkoLogger {

    fun getUser(): User {

        var user = User()

        var userParser = rowParser{ userID: Int,
                           userName: String?,

                           password: String?,
                           projectCode: String?,
                           projectTask: String?,
                           category: String?,
                           workingHours: Float,
                           worksWeekends: Int ->

            warn("value" + userID)
            user.userID = userID
            user.userName = userName
            user.password = password
            user.projectCode = projectCode
            user.projectTask = projectTask
            user.category = category
            user.workingHours = workingHours
            user.worksWeekends = worksWeekends
                       
        }


        db.use {
            select("user")
                    .parseOpt(userParser)

        }
        return user
    }


    fun updateUser() {
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