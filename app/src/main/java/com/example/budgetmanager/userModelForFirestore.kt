package com.example.budgetmanager
class userModelForFirestore {
    private lateinit var name:String
    private lateinit var email:String
    private lateinit var password:String
    constructor(email: String, password: String,name: String) {
        this.email = email
        this.password = password
        this.name = name
    }
    constructor() {
        this.name = ""
        this.email = ""
        this.password = ""
    }
    fun setPassword(password:String){
        this.password = password
    }
    fun getPassword():String{
        return password
    }
    fun setEmail(email:String){
        this.email = email
    }
    fun getEmail():String{
        return email
    }
    fun setName(name:String){
        this.name = name
    }
    fun getName():String{
        return name
    }
}