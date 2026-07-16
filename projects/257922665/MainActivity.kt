package com.example.project1

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mortgage = Mortgage(30, 0.065, 300000.0)
        logMortgage("", mortgage)

        mortgage.setYears(15)
        mortgage.setInterestRate(0.07)
        mortgage.setAmount(100000.0)
        logMortgage("New ", mortgage)
    }

    private fun logMortgage(prefix: String, mortgage: Mortgage) {
        Log.d(TAG, "${prefix}Monthly Payment: ${mortgage.formattedMonthlyPayment()}")
        Log.d(TAG, "${prefix}Total Payment: ${mortgage.formattedTotalPayments()}")
    }

    private companion object {
        const val TAG = "MainActivity"
    }
}
