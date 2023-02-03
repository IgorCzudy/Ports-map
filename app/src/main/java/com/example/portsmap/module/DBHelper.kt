package com.example.portsmap.module

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                NAME_COl + " TEXT," +
                PASSWORD_COL + " TEXT" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addName(name : String, password : String ){
        val values = ContentValues()
        values.put(NAME_COl, name)
        values.put(PASSWORD_COL, password)

        val db = this.writableDatabase

        db.insert(TABLE_NAME, null, values)

        db.close()
    }


    fun hasUserNameCorrectPassword(userName: String, password: String): Boolean{
        val db = this.readableDatabase

        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $NAME_COl = ? AND $PASSWORD_COL = ?"
        val c: Cursor = db.rawQuery(selectQuery, arrayOf(userName,password))
        if(c.getCount()>0)
        {
            return true
        }
        return false

    }


    companion object{
        private val DATABASE_NAME = "my_data_base"

        private val DATABASE_VERSION = 1

        val TABLE_NAME = "gfg_table"

        val ID_COL = "id"

        val NAME_COl = "name"

        val PASSWORD_COL = "password"
    }
}
