package com.example.budgetmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.viewbinding.ViewBindings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [budgetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class budgetFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var budget: Int = 0
    var uid: String = ""
    private lateinit var budgetBar: TextView
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
        return inflater.inflate(R.layout.fragment_budget, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        requireView().findViewById<Button>(R.id.addbudget).setOnClickListener {
            sendBudget()
        }
    }
    private fun sendBudget() {
        budgetBar = requireView().findViewById(R.id.enterbudget)
        try {
            budget = budgetBar.text.toString().toInt()
            val budgetModel = budgetModel(uid,budget)
            if(budget>0){
                val user = FirebaseAuth.getInstance().currentUser
                val budgetDocRef = FirebaseFirestore.getInstance().collection("BUDGETS").document(user!!.uid)
                budgetDocRef.set(budgetModel)
                Toast.makeText(requireActivity(),"Budget amount successfully updated.",Toast.LENGTH_SHORT).show()
            }
           else{
               Toast.makeText(requireActivity(),"Budget value is not valid!",Toast.LENGTH_SHORT).show()
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(requireActivity(), "Invalid budget value", Toast.LENGTH_LONG).show()
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment budgetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            budgetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}