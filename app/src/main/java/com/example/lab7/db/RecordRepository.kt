package com.example.lab7.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.lab7.model.Record

class RecordRepository(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "com.example.lab7.db"

        const val TABLE_NAME = "RECORDS"
        const val COL_ID = "ID"
        const val COL_USER = "USERNAME"
        const val COL_VAL = "VALUE"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE $TABLE_NAME($COL_ID INTEGER PRIMARY KEY, $COL_USER TEXT type UNIQUE, $COL_VAL TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
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
        val query = "DELETE FROM $TABLE_NAME"
        db.rawQuery(query, null)
        db.close()
    }

    fun getRecord(username: String): Record? {
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_USER=\'$username\'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            val record = Record()
            record.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
            record.username = cursor.getString(cursor.getColumnIndex(COL_USER))
            record.value = cursor.getInt(cursor.getColumnIndex(COL_VAL))

            return record
        }
        return null
    }
}