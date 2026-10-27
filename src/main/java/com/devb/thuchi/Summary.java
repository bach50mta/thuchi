package com.devb.thuchi;

import java.math.BigDecimal;

public record Summary(BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal balance) {
}