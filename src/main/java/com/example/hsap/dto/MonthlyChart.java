package com.example.hsap.dto;

import lombok.Data;

@Data
public class MonthlyChart {
    private CategoryDTO categoryDTO;
    // 월별 수입 (0-11)
    private int[] monthlyIncome = new int[12];
    // 월별 지출 (0-11)
    private int[] monthlyExpenditure = new int[12];

    public void addIncome(int month, int income) {
        monthlyIncome[month] += income;
    }

    public void addExpenditure(int month, int expenditure) {
        monthlyExpenditure[month] += expenditure;
    }
}
