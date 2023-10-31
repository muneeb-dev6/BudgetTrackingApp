package com.example.budgetmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.budgetmanager.transactionModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import java.util.*
import javax.annotation.Nullable
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [homeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class homeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var currentBudget:Int = 0
    private var title:String? = ""
    private var amount:Number? = 0
    private lateinit var time: Date
    private lateinit var cardAmountView:TextView
    private lateinit var lastTransaction:TextView
    private lateinit var displayDate:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardAmountView = requireView().findViewById(R.id.amount)
        lastTransaction = requireView().findViewById(R.id.latest)
        displayDate = requireView().findViewById(R.id.date)
        updateUI()
    }
    private fun updateUI() {
        var currentUser = FirebaseAuth.getInstance().currentUser
        val budgetDocRef = FirebaseFirestore.getInstance().collection("BUDGETS").document(currentUser!!.uid)
        budgetDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                currentBudget = documentSnapshot.getLong("budget")!!.toInt()
                if(currentBudget <= 10){
                    cardAmountView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                }
                cardAmountView.text = "$${currentBudget.toString()}"
            }
        }
            .addOnFailureListener { e ->
                Toast.makeText(requireActivity(),"Error has occurred: $e", Toast.LENGTH_SHORT).show()
            }
        val transactionDocRef = FirebaseFirestore.getInstance().collection("Transactions").document(currentUser!!.uid)
        transactionDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val transactionList = documentSnapshot.get("transactions") as? ArrayList<*>
                    val latestItem = transactionList?.lastOrNull()
                    if(latestItem is Map<*, *>){
                        title = latestItem["title"] as? String
                        amount = latestItem["amount"] as? Number
                        var date = latestItem["date"] as? Timestamp
                        if (date != null) {
                            time = date.toDate()
                        }
                        lastTransaction.text = "$title ($${amount.toString()})"
                        displayDate.text = "Last spent: \n${time.toString()}"
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireActivity(),"Error fetching data: ${exception.message}",Toast.LENGTH_SHORT).show()
            }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment homeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            homeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}