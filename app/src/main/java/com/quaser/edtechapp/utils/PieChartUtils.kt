package com.quaser.edtechapp.utils

import ir.mahozad.android.PieChart

class PieChartUtils {
    fun initialisePie(pieChart: PieChart, correct: Int, wrong: Int, blue: Int, red: Int){
        pieChart.slices = listOf(
            PieChart.Slice(correct/100f, blue),
            PieChart.Slice(wrong/100f, red)
        )
    }
}