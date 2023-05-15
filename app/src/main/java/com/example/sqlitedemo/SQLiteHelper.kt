package com.example.sqlitedemo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object{
        private val DATABASE_VERSION=1
        private val DATABASE_NAME="student.db"
        private val TBL_STUDENT="student"

        private val NAME="name"
        private val EMAIL="email"
        private val ID="id"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTbStudent= ("CREATE TABLE  $TBL_STUDENT("
                + "$ID INTEGER PRIMARY KEY AUTOINCREMENT," + "$NAME TEXT,"
                + "$EMAIL TEXT)")
        db?.execSQL(createTbStudent)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TBL_STUDENT ")
        onCreate(db)
    }

    fun insertStudent(std1:StudentModel1):Long{
        val db=this.writableDatabase
        val contentValues=ContentValues()
        //contentValues.put(ID,std.id)
        contentValues.put(NAME,std1.name)
        contentValues.put(EMAIL,std1.email)
        val success=db.insert(TBL_STUDENT,null,contentValues)
        db.close()
        return success
    }


    fun getAllStudent():ArrayList<StudentModel>{
        val stdList:ArrayList<StudentModel> = ArrayList()
        val selectQuery="SELECT * FROM $TBL_STUDENT"
        val db=this.readableDatabase
        val cursor:Cursor?
        try {
            cursor=db.rawQuery(selectQuery,null)
        }
        catch (ex:Exception){
            ex.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id:Int
        var name:String
        var email:String

        if (cursor.moveToNext()){
            do{
                id=cursor.getInt(cursor.getColumnIndex("id"))
                name=cursor.getString(cursor.getColumnIndex("name"))
                email=cursor.getString(cursor.getColumnIndex("email"))

                val std=StudentModel(id = id, name = name, email = email)
                stdList.add(std)

            }while (cursor.moveToNext())
        }
        return stdList
    }
     // update student
    fun updataStudent(std:StudentModel):Int{
        val db =this.writableDatabase
        val contentValues=ContentValues()
        contentValues.put(ID,std.id)
        contentValues.put(NAME,std.name)
        contentValues.put(EMAIL,std.email)

        val success=db.update(TBL_STUDENT,contentValues,"id="+std.id,null)
        db.close()
        return success
    }
    // delete student
    fun deleteStudentById(id:Int):Int{
        val db=this.writableDatabase
        val contentValues=ContentValues()
        contentValues.put(ID,id)

        val success=db.delete(TBL_STUDENT,"id=$id",null)
        db.close()
        return success
    }
}