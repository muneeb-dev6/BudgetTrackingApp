package com.example.budgetmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.temporal.TemporalAmount
import java.util.*

class transaction : AppCompatActivity() {
    private lateinit var amount: EditText
    private lateinit var description: EditText
    private lateinit var addBtn: Button
    private var currentItem:String = ""
    private lateinit var listOfTransactions: MutableList<transactionModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        setSpinner()
        initializeViews()
        addBtn.setOnClickListener {
            listOfTransactions  = mutableListOf()
            var textamount:String = amount.text.toString()
            var tamount: Int? = null
            if (textamount.isNotEmpty()) {
                try {
                    tamount = textamount.toInt()
                } catch (e: NumberFormatException) {
                    Toast.makeText(this,"Amount is invalid.",Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this,"Amount cannot be empty.",Toast.LENGTH_SHORT).show()
            }
            var tdescription:String = description.text.toString()
            var transactionDate: Date = Date()
            var transactionModel = transactionModel(currentItem,tamount,tdescription,transactionDate)
            //Retrieving firebase values.
            var getUser = FirebaseAuth.getInstance().currentUser?.uid
            var dbRef = FirebaseFirestore.getInstance()
            var budgetDocRef = dbRef.collection("BUDGETS").document(getUser.toString())
            budgetDocRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        var budget:Int = documentSnapshot.getLong("budget")!!.toInt()
                        if((budget > 0) && (budget >= tamount!!)){
                            listOfTransactions.add(transactionModel)
                            sendTransactionDetails(listOfTransactions)
                            budget -= transactionModel.getAmount()!!.toInt()
                            Toast.makeText(this,"Transaction has been succeeded. ",Toast.LENGTH_SHORT).show()
                            budgetDocRef.set(budgetModel(getUser.toString(),budget)) //update the value
                        }
                        else{
                            Toast.makeText(this,"Insufficient budget for the transaction",Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this,"Transaction failed. ",Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this,"Error has occurred",Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun sendTransactionDetails(transactionList:MutableList<transactionModel>) {
        var user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val transactionRef = db.collection("Transactions").document(user!!.uid)
        val transactionsData = transactionList.map { transaction ->
            mapOf(
                "title" to transaction.getTitle(),
                "amount" to transaction.getAmount(),
                "description" to transaction.getDesc(),
                "date" to Date()
            )
        }
        transactionRef.update("transactions", FieldValue.arrayUnion(*transactionsData.toTypedArray()))
    }
    private fun initializeViews() {
        amount = findViewById(R.id.amount)
        description = findViewById(R.id.desc)
        addBtn = findViewById(R.id.addtransaction)
    }
    private fun setSpinner(){
        val expenseTypes = listOf("Medical Check-up", "Groceries", "Entertainment", "Stationery items", "Utility Bills", "Transportation", "Electronics", "Refreshments")
        val adapter = ArrayAdapter<String>(this,R.layout.spinner_custom,expenseTypes)
        findViewById<Spinner>(R.id.spinner).adapter = adapter
        findViewById<Spinner>(R.id.spinner).onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentItem = adapterView?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

}