package com.mustafacol.jettip.util


fun calculateTipAmount(totalBill: Double, tipPercentage: Int): Double {
    return if (totalBill > 1 && totalBill.toString().isNotEmpty())
        (totalBill * tipPercentage) / 100 else 0.0

}

fun calculatePerPerson(totalBill: Double, tipPercentage: Int, splitBy: Int): Double {
    return if (totalBill > 1 && totalBill.toString().isNotEmpty()) {
        val tipAmount = calculateTipAmount(totalBill, tipPercentage)
        (totalBill + tipAmount) / splitBy
    } else 0.0
}
