package com.example.budgetmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp

class recyclerViewAdapter(var history: ArrayList<Map<*,*>>?):RecyclerView.Adapter<recyclerViewAdapter.historyViewHolder>() {
    inner class historyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): historyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_transactions,parent, false)
        return historyViewHolder(view)
    }
    fun updateHistory(history: ArrayList<Map<*, *>>?) {
        this.history = history
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: historyViewHolder, position: Int) {
        val transactionItem = history?.get(position)
        holder.itemView.findViewById<TextView>(R.id.label).text = transactionItem?.get("title") as? String
        val amount = transactionItem?.get("amount") as? Number
        holder.itemView.findViewById<TextView>(R.id.amo).text = "$${amount.toString()}"
//        val date = transactionItem?.get("date") as? Timestamp
    }
    override fun getItemCount(): Int {
        return history!!.size
    }
}