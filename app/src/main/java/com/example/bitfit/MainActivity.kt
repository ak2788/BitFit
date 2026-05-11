package com.example.bitfit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: FoodEntryAdapter
    private val entries = mutableListOf<FoodEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.rvFoodEntries)
        adapter = FoodEntryAdapter(entries)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val btnAddNewFood = findViewById<Button>(R.id.btnAddNewFood)
        btnAddNewFood.setOnClickListener {
            startActivity(Intent(this, AddEntryActivity::class.java))
        }

        // Collect the Flow here in onCreate — it stays active for the lifetime of the activity
        lifecycleScope.launch {
            FoodDatabase.getInstance(applicationContext)
                .foodEntryDao()
                .getAllEntries()
                .collect { dbEntries ->
                    adapter.updateData(dbEntries)
                    updateDashboard(dbEntries)
                }
        }
    }

    // onResume + loadEntriesFromDatabase() are no longer needed — remove them

    private fun updateDashboard(entries: List<FoodEntry>) {
        val total = entries.sumOf { it.calories }
        val avg = if (entries.isEmpty()) 0 else total / entries.size
        val count = entries.size

        findViewById<TextView>(R.id.tvTotalCalories).text = total.toString()
        findViewById<TextView>(R.id.tvAvgCalories).text = avg.toString()
        findViewById<TextView>(R.id.tvMealCount).text = count.toString()
    }
}