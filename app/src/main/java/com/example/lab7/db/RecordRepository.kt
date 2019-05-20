package com.example.lab7.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.lab7.model.Record

class RecordRepository(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 7
        private const val DATABASE_NAME = "com.example.lab7.db"

        const val TABLE_NAME = "RECORDS"
        const val COL_ID = "ID"
        const val COL_USER = "USERNAME"
        const val COL_VAL = "VALUE"

        private const val SQL_CREATE_ENTRIES = "CREATE TABLE $TABLE_NAME($COL_ID INTEGER PRIMARY KEY, $COL_USER TEXT NOT NULL UNIQUE, $COL_VAL TEXT)"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    val allRecord: List<Record>
        get() {
            val records = ArrayList<Record>()
            val selectQuery = "SELECT * FROM $TABLE_NAME"
            val db = this.readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val record = Record()
                    record.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                    record.username = cursor.getString(cursor.getColumnIndex(COL_USER))
                    record.value = cursor.getInt(cursor.getColumnIndex(COL_VAL))

                    records.add(record)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return records
        }

    fun addRecordList(recordList: List<Record>) {
        clearTable()
        recordList.forEach { addRecord(it) }
    }

    private fun addRecord(record: Record) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_USER, record.username)
        values.put(COL_VAL, record.value)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    private fun clearTable() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }

    fun getRecord(username: String): Record? {
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_USER=\'$username\'"
        val db = this.readableDatabase

        var record: Record? = null
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            record = Record()
            record.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
            record.username = cursor.getString(cursor.getColumnIndex(COL_USER))
            record.value = cursor.getInt(cursor.getColumnIndex(COL_VAL))
        }
        cursor.close()
        db.close()

        return record
    }
}