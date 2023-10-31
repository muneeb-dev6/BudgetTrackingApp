package com.example.budgetmanager

class budgetModel {
    private var budget: Int = 0
    private var uid: String = ""
    constructor(uid:String, budget: Int) {
        this.uid = uid
        this.budget = budget
    }
    constructor() {
        this.uid = ""
        this.budget = 0
    }
    fun setUid(uid:String){
        this.uid = uid
    }
    fun getUid():String{
        return uid
    }
    fun setBudget(budget:Int){
        this.budget = budget
    }
    fun getBudget():Int{
        return budget
    }
}