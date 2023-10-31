package com.example.budgetmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class transactions_history : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var title: String? = ""
    private var amount: Number? = 0
    private lateinit var time: Date
    private var history: ArrayList<Map<*, *>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val transactionDocRef = db.collection("Transactions").document(currentUser!!.uid)
        transactionDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val transactionList = documentSnapshot.get("transactions") as? ArrayList<*>
                if (transactionList != null) {
                    history = ArrayList(transactionList.filterIsInstance<Map<*, *>>())
                    val historyAdapter = history?.let { recyclerViewAdapter(it) }
                    val rvHistory = requireView().findViewById<RecyclerView>(R.id.histories)
                    rvHistory.adapter = historyAdapter
                    rvHistory.layoutManager = LinearLayoutManager(requireActivity())
                    historyAdapter?.updateHistory(history) // Call setHistory() after creating the adapter instance
                }
                else{
                    Toast.makeText(requireActivity(),"No history found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transactions_history, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            transactions_history().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
