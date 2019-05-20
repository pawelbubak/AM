package com.example.lab7.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.lab7.model.User

class UserRepository(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 7
        private const val DATABASE_NAME = "com.example.lab7.db"

        const val TABLE_NAME = "USERS"
        const val COL_ID = "ID"
        const val COL_NAME = "NAME"
        const val COL_USER = "USERNAME"
        const val COL_PASS = "PASSWORD"
        const val COL_POINTS = "POINTS"

        private const val SQL_CREATE_ENTRIES = "CREATE TABLE $TABLE_NAME($COL_ID INTEGER PRIMARY KEY, $COL_USER TEXT NOT NULL UNIQUE, $COL_PASS TEXT, $COL_NAME TEXT, $COL_POINTS INTEGER NOT NULL)"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun addUser(user: User): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_NAME, user.name)
        values.put(COL_USER, user.username)
        values.put(COL_PASS, user.password)
        values.put(COL_POINTS, user.points)

        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun getUser(username: String): User? {
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE UPPER($COL_USER)=UPPER(\'$username\')"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            val user = User()
            user.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
            user.name = cursor.getString(cursor.getColumnIndex(COL_NAME))
            user.username = cursor.getString(cursor.getColumnIndex(COL_USER))
            user.password = cursor.getString(cursor.getColumnIndex(COL_PASS))
            user.points = cursor.getInt(cursor.getColumnIndex(COL_POINTS))

            cursor.close()
            db.close()
            return user
        }
        cursor.close()
        db.close()
        return null
    }

    fun getUser(id: Int): User? {
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_ID=$id"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            val user = User()
            user.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
            user.name = cursor.getString(cursor.getColumnIndex(COL_NAME))
            user.username = cursor.getString(cursor.getColumnIndex(COL_USER))
            user.password = cursor.getString(cursor.getColumnIndex(COL_PASS))
            user.points = cursor.getInt(cursor.getColumnIndex(COL_POINTS))

            cursor.close()
            db.close()
            return user
        }
        cursor.close()
        db.close()
        return null
    }

    fun updateUser(user: User) {
        val db = this.readableDatabase
        val values = ContentValues().apply {
            put(COL_POINTS, user.points)
        }
        db.update(TABLE_NAME, values, "$COL_ID = ?", arrayOf("${user.id}"))
        db.close()
    }
}