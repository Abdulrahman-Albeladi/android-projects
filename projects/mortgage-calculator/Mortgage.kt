package com.example.project1

import java.text.NumberFormat
import kotlin.math.pow

/**
 * Represents a fixed-rate mortgage and provides payment calculations.
 *
 * Interest rates are expressed as decimal annual rates (for example, 0.05 for 5%).
 */
class Mortgage(
    newYears: Int,
    newInterestRate: Double,
    newAmount: Double
) {
    private var years: Int = 0
    private var interestRate: Double = 0.0
    private var amount: Double = 0.0

    init {
        setYears(newYears)
        setInterestRate(newInterestRate)
        setAmount(newAmount)
    }

    fun getYears(): Int = years

    fun getInterestRate(): Double = interestRate

    fun getAmount(): Double = amount

    fun setYears(newYears: Int) {
        if (newYears >= 0) {
            years = newYears
        }
    }

    fun setInterestRate(newInterestRate: Double) {
        if (newInterestRate >= 0) {
            interestRate = newInterestRate
        }
    }

    fun setAmount(newAmount: Double) {
        if (newAmount >= 0) {
            amount = newAmount
        }
    }

    /** Returns the fixed monthly mortgage payment. */
    fun monthlyPayment(): Double {
        val paymentCount = years * 12
        val monthlyInterestRate = interestRate / 12

        if (monthlyInterestRate == 0.0) {
            return amount / paymentCount
        }

        val discountFactor = (1 + monthlyInterestRate).pow(-paymentCount.toDouble())
        return amount * monthlyInterestRate / (1 - discountFactor)
    }

    /** Returns the total paid over the mortgage term. */
    fun totalPayments(): Double = years * 12 * monthlyPayment()

    fun formattedMonthlyPayment(): String =
        NumberFormat.getCurrencyInstance().format(monthlyPayment())

    fun formattedTotalPayments(): String =
        NumberFormat.getCurrencyInstance().format(totalPayments())
}
