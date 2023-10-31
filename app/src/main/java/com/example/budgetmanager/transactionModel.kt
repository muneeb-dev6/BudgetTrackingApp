package com.example.budgetmanager

import java.util.Date

open class transactionModel {
     private var title:String = ""
     private var amount:Int? = 0
     private var desc:String = ""
     private lateinit var date: Date
    constructor(title:String,amount:Int?,desc:String,date: Date){
        this.title = title
        this.amount = amount
        this.desc = desc
        this.date = date
    }
    constructor(transactionObj: transactionModel?){
        if (transactionObj != null) {
            setTitle(transactionObj.title)
        }
        if (transactionObj != null) {
            setAmount(transactionObj.amount)
        }
        if (transactionObj != null) {
            setDesc(transactionObj.desc)
        }
        if (transactionObj != null) {
            setDate(transactionObj.date)
        }
    }
    constructor(){
        title = ""
        amount = 0
        desc = ""
        date = Date()
    }
    fun setTitle(title:String){
        this.title = title
    }
    fun setAmount(amount:Int?){
        this.amount = amount
    }
    fun setDesc(desc:String){
        this.desc = desc
    }
    fun setDate(date: Date){
        this.date = date
    }
    fun getTitle():String{
        return title
    }
    fun getAmount():Int?{
        return amount

    }
    fun getDesc():String{
        return desc
    }
    fun getDate():Date{
        return date
    }
}