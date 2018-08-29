package com.aston.phenders.time01.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.db.*
import org.jetbrains.anko.warn

class DatabaseHelper(context: Context) : ManagedSQLiteOpenHelper(context, "TimeDB.db", null, 1), AnkoLogger {

    override fun onCreate(db: SQLiteDatabase?) {
        //define tables


        db?.createTable("time", false,
                "timeId" to INTEGER + PRIMARY_KEY,
                "month" to TEXT,
                "startDate" to INTEGER,
                "endDate" to INTEGER,
                "year" to TEXT,
                "dates" to TEXT,
                "category" to TEXT,
                "businessReason" to TEXT,
                "projectCode" to TEXT,
                "projectTask" to TEXT,
                "quantity" to REAL)

        db?.createTable("user", true,
                "userID" to INTEGER,
                "userName" to TEXT,
                "password" to TEXT,
                "projectCode" to TEXT,
                "projectTask" to TEXT,
                "category" to TEXT,
                "workingHours" to REAL,
                "worksWeekends" to TEXT)

        //insert default values for user
        db?.insert("user",
                "userID" to 1,
                "UserName" to "hendersp",
                "password" to "test",
                "projectCode" to "1234",
                "projectTask" to "BT",
                "category" to "Standard Time in UK",
                "workingHours" to 7.5F,
                "worksWeekends" to false)
        warn("Inserted")

    }


    /*
    db?.createTable("user", true,
            "id" to INTEGER + PRIMARY_KEY,
            "name" to TEXT,
            "workingDays" to TEXT,
            "workingHours" to TEXT)

    db?.createTable("time", true,
            "timeId" to INTEGER,
            "month" to TEXT,
            "DateTime" to TEXT,
            "category" to TEXT,
            "businessReason" to TEXT,
            "projectCode" to TEXT,
            "projectTask" to TEXT)
    */


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}