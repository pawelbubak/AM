package com.example.lab7.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.lab7.model.User

class UserRepository(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "com.example.lab7.db"

        const val TABLE_NAME = "USERS"
        const val COL_ID = "ID"
        const val COL_NAME = "NAME"
        const val COL_USER = "USERNAME"
        const val COL_PASS = "PASSWORD"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE $TABLE_NAME($COL_ID INTEGER PRIMARY KEY, $COL_USER TEXT type UNIQUE, $COL_PASS TEXT, $COL_NAME TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE_NAME IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    val allUsers: List<User>
        get() {
            val users = ArrayList<User>()
            val selectQuery = "SELECT * FROM $TABLE_NAME"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val user = User()
                    user.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                    user.name = cursor.getString(cursor.getColumnIndex(COL_NAME))
                    user.username = cursor.getString(cursor.getColumnIndex(COL_USER))
                    user.password = cursor.getString(cursor.getColumnIndex(COL_PASS))

                    users.add(user)
                } while (cursor.moveToNext())
            }
            db.close()
            return users
        }

    fun addUser(user: User): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_NAME, user.name)
        values.put(COL_USER, user.username)
        values.put(COL_PASS, user.password)

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

            return user
        }
        return null
    }

    fun getUser(id: Long): User? {
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_ID=$id"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            val user = User()
            user.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
            user.name = cursor.getString(cursor.getColumnIndex(COL_NAME))
            user.username = cursor.getString(cursor.getColumnIndex(COL_USER))
            user.password = cursor.getString(cursor.getColumnIndex(COL_PASS))

            return user
        }
        return null
    }
}