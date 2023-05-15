package com.example.sqlitedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sqlitedemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding?=null
    private var sqLiteHelper:SQLiteHelper?=null
    private var adapter:StudentAdapter?=null
    private var std:StudentModel?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        sqLiteHelper= SQLiteHelper(this)

        val recyle=binding?.recyclerview

        recyle?.layoutManager=LinearLayoutManager(this)
        adapter= StudentAdapter()
        recyle?.adapter=adapter

        // save student
        binding?.button?.setOnClickListener {

            val name=binding?.editTextTextPersonName?.text.toString()
            val email=binding?.editTextTextPersonName2?.text.toString()

            val std=StudentModel1(name=name, email = email)
            val status= sqLiteHelper!!.insertStudent(std)
            if (status>-1){
                Toast.makeText(this,"Student Added.....",Toast.LENGTH_SHORT).show()
                binding?.editTextTextPersonName?.setText("")
                binding?.editTextTextPersonName2?.setText("")
            }
            else{
                Toast.makeText(this,"Record not saved",Toast.LENGTH_SHORT).show()
            }

        }

        //display student
        binding?.button2?.setOnClickListener {
           getStudents()

        }
        // get value in Edit Text
        adapter?.setOnClickItem {
           // Toast.makeText(this,it.name,Toast.LENGTH_SHORT).show()

            // update record
            binding?.editTextTextPersonName?.setText(std!!.name)
            binding?.editTextTextPersonName2?.setText(it.email)

            std=it
        }
        // delete student item
        adapter?.setOnClickDeleteItem {
            deleteStudent(it.id)
        }

        // update student
        binding?.btnupdate?.setOnClickListener {

            updateStudent()
        }

        //delete record



    }
    private fun getStudents(){
        val stdList=sqLiteHelper!!.getAllStudent()
        Log.d("TAG","${stdList.size}")

        // display data in recycler view

        adapter?.addItems(stdList)
    }
    // student update function
    private fun updateStudent(){
        val name=binding?.editTextTextPersonName?.text.toString()
        val email=binding?.editTextTextPersonName2?.text.toString()
        // check record not change
        if (name==std?.name && email==std?.email){
            Toast.makeText(this,"Record not change...",Toast.LENGTH_SHORT).show()
            return
        }
        val std=StudentModel(id = std!!.id, name = name, email = email)
        val status=sqLiteHelper?.updataStudent(std)
        if (status!!>-1){
            binding?.editTextTextPersonName?.setText("")
            binding?.editTextTextPersonName2?.setText("")
            getStudents()
        }
        else{
            Toast.makeText(this,"Update failed...",Toast.LENGTH_SHORT).show()
        }
    }
    // student delete function
    private fun deleteStudent(id:Int){
        if (id==null) return
        val builder= AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete item?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){dialog, _ ->
            sqLiteHelper?.deleteStudentById(id)
            getStudents()
        dialog.dismiss()}
        builder.setNegativeButton("No"){dialog, _ ->
            dialog.dismiss()}

        val alert=builder.create()
        alert.show()

    }
}