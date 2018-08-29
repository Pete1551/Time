package com.aston.phenders.time01.database

import com.aston.phenders.time01.models.User
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import org.jetbrains.anko.warn


class UserTable(val db: DatabaseHelper) : AnkoLogger {

    fun getUser(): User {

        var user = User()

        var userParser = rowParser { userID: Int,
                                     userName: String?,
                                     password: String?,
                                     projectCode: String?,
                                     projectTask: String?,
                                     category: String?,
                                     workingHours: Float,
                                     worksWeekends: String? ->

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


    fun updateUser(user: User) {
        db.use {
            warn("In Update")
            warn(user.projectCode)
            update("user",
                    "projectCode" to user.projectCode,
                    "projectTask" to user.projectTask,
                    "category" to user.category,
                    "workingHours" to user.workingHours,
                    "worksWeekends" to user.worksWeekends).exec()



            //TRY SETTING WHERE ARGS
        }
        warn("out of update")

    }

}