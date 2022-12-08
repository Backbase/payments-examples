package com.mybank.payments.extension.util;

import java.math.BigDecimal;

public interface MathUtil {

    static BigDecimal calculatePercentage(BigDecimal base, int pct) {
        return base.multiply(BigDecimal.valueOf((double) pct / 100));
    }
}
