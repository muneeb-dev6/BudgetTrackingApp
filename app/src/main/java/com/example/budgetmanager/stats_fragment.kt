package com.example.budgetmanager

import android.graphics.Color
import android.graphics.fonts.FontStyle
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.gms.common.SignInButton.ColorScheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [stats_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class stats_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var medicalBudget:Long? = 0
    private var groceryBudget:Long? = 0
    private var entertainBudget:Long? = 0
    private var stationaryBudget:Long? = 0
    private var billsBudget:Long? = 0
    private var transportBudget:Long? = 0
    private var electronicsBudget:Long? = 0
    private var refreshmentBudget:Long? = 0
    private var totalBudget:Long? = 0
    private lateinit var PieChart:PieChart
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
        return inflater.inflate(R.layout.fragment_stats_fragment, container, false)
    }

    override fun onPause() {
        super.onPause()
        medicalBudget = 0
        groceryBudget = 0
        entertainBudget = 0
        stationaryBudget = 0
        billsBudget = 0
        transportBudget = 0
        electronicsBudget = 0
        refreshmentBudget = 0
        totalBudget = 0
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PieChart = requireView().findViewById(R.id.pieChart)
        var currentUser = FirebaseAuth.getInstance().currentUser
        var db = FirebaseFirestore.getInstance()
        var transactionsRef = db.collection("Transactions").document(currentUser!!.uid)
        transactionsRef.get().addOnSuccessListener {documentSnapshot ->
            if(documentSnapshot.exists()){
                val transactionList = documentSnapshot.get("transactions") as? ArrayList<*>
                if (transactionList != null) {
                    for(transactions in transactionList){
                        if(transactions is Map<*, *>){
                            var transactionTitle = transactions["title"] as? String
                            var amount = transactions["amount"] as? Number
                            if(amount!=null){
                                totalBudget = totalBudget?.plus(amount.toLong())
                            }
                            if(transactionTitle.equals("Medical Check-up")){
                                var amount = transactions["amount"] as? Number
                                if(amount!=null){
                                    medicalBudget = medicalBudget?.plus(amount.toLong())
                                }
                            }
                            else if(transactionTitle.equals("Groceries")){
                                var amount = transactions["amount"] as? Number
                                if(amount!=null){
                                    groceryBudget = groceryBudget?.plus(amount.toLong())
                                }
                            }
                            else if(transactionTitle.equals("Entertainment")){
                                var amount = transactions["amount"] as? Number
                                if(amount!=null){
                                    entertainBudget = entertainBudget?.plus(amount.toLong())
                                }
                            }
                            else if(transactionTitle.equals("Stationery items")){
                                var amount = transactions["amount"] as? Number
                                if(amount!=null){
                                    stationaryBudget = stationaryBudget?.plus(amount.toLong())
                                }
                            }
                            else if(transactionTitle.equals("Utility Bills")){
                                var amount = transactions["amount"] as? Number
                                if(amount!=null){
                                    billsBudget = billsBudget?.plus(amount.toLong())
                                }
                            }
                            else if(transactionTitle.equals("Transportation")){
                                var amount = transactions["amount"] as? Number
                                if(amount!=null){
                                    transportBudget = transportBudget?.plus(amount.toLong())
                                }
                            }
                            else if(transactionTitle.equals("Electronics")){
                                var amount = transactions["amount"] as? Number
                                if(amount!=null){
                                    electronicsBudget = electronicsBudget?.plus(amount.toLong())
                                }
                            }
                            else{
                                var amount = transactions["amount"] as? Number
                                if(amount!=null){
                                    refreshmentBudget = refreshmentBudget?.plus(amount.toLong())
                                }
                            }
                        }
                    }
                    sendEntriesInChart()
                }
            }
        }
            .addOnFailureListener{ exception ->
                Toast.makeText(requireActivity(),"Error has occurred. $exception",Toast.LENGTH_SHORT).show()
            }
    }

    private fun sendEntriesInChart() {
        var entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry((100* medicalBudget!! / totalBudget!!).toFloat(),"Medical"))
        entries.add(PieEntry((100* groceryBudget!! / totalBudget!!).toFloat(),"Grocery"))
        entries.add(PieEntry((100* entertainBudget!! / totalBudget!!).toFloat(),"Entertainment"))
        entries.add(PieEntry((100* stationaryBudget!! / totalBudget!!).toFloat(),"Stationary"))
        entries.add(PieEntry((100* billsBudget!! / totalBudget!!).toFloat(),"Bills"))
        entries.add(PieEntry((100* transportBudget!! / totalBudget!!).toFloat(),"Transport"))
        entries.add(PieEntry((100* electronicsBudget!! / totalBudget!!).toFloat(),"Electronics"))
        entries.add(PieEntry((100* refreshmentBudget!! / totalBudget!!).toFloat(),"Refreshment"))
        var dataSet = PieDataSet(entries,"")
        var colors = mutableListOf<Int>(Color.parseColor("#D36582"), Color.parseColor("#2B59C3"), Color.parseColor("#BBBBBF"), Color.parseColor("#FFBB86FC"), Color.parseColor("#348AA7"), Color.parseColor("#415a77"), Color.parseColor("#FF03DAC5"), Color.parseColor("#FFD447"))
        dataSet.setColors(colors)
        var pieData = PieData(dataSet)
        PieChart.data = pieData
        PieChart.description.isEnabled = false
        PieChart.animateY(1000)
        PieChart.invalidate()

        PieChart.legend.setDrawInside(false)
        PieChart.legend.isEnabled = true
        PieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        PieChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        PieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        PieChart.legend.textColor = Color.parseColor("#778da9")



    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment stats_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            stats_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

