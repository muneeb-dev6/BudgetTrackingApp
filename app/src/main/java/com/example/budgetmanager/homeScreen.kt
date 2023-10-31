package com.example.budgetmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class homeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        var homeFragment = homeFragment()
        var budgetFragment = budgetFragment()
        var statFragment = stats_fragment()
        var history_fragment = transactions_history()
        makeCurrentFragment(homeFragment)
        findViewById<BottomNavigationView>(R.id.navbar).setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> makeCurrentFragment(homeFragment)
                R.id.budget -> makeCurrentFragment(budgetFragment)
                R.id.Stat -> makeCurrentFragment(statFragment)
                R.id.history -> makeCurrentFragment(history_fragment)
            }
            true
        }
        findViewById<FloatingActionButton>(R.id.add).setOnClickListener{
            startActivity(Intent(this@homeScreen,transaction::class.java))
        }
    }
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.frame,fragment)
            commit()
        }

}