package com.example.sqlitedemo

import kotlin.random.Random

data class StudentModel(
      var id:Int,
    var name:String,
    var email:String

){
//    companion object {
//        fun getAutoId(): Int {
//            val random = Random
//            return random.nextInt(100)
//        }
//    }
}
