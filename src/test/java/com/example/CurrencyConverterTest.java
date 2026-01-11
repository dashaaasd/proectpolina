package com.example;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeMethod;
import static org.testng.Assert.*;

public class CurrencyConverterTest {

    private CurrencyConverter converter;

    @BeforeMethod(groups = {"currency", "fast", "smoke"})
    public void setUp() {
        converter = new CurrencyConverter();
    }

    @Test(groups = {"currency", "fast", "smoke"})
    public void testConvertToUSD() {
        // Курс: 1 USD = 75 RUB
        double usdAmount = converter.convertToUSD(7500, 75.0);
        assertEquals(usdAmount, 100.0, 0.001, "Конвертация в USD неверна");

        // Проверка обратного расчета
        assertEquals(usdAmount * 75.0, 7500.0, 0.001);
    }

    @Test(groups = {"currency", "fast"})
    public void testConvertToEUR() {
        // Курс: 1 EUR = 85 RUB
        double eurAmount = converter.convertToEUR(8500, 85.0);
        assertEquals(eurAmount, 100.0, 0.001, "Конвертация в EUR неверна");

        // Проверка с дробными значениями
        eurAmount = converter.convertToEUR(4250, 85.0);
        assertEquals(eurAmount, 50.0, 0.001);
    }

    @Test(groups = {"currency"})
    public void testCalculateConversionLoss() {
        // RUB -> USD -> EUR -> RUB
        // Начальная сумма: 10000 RUB
        // Курс: USD = 75 RUB, EUR = 85 RUB
        double loss = converter.calculateConversionLoss(10000, 75.0, 85.0);

        // Расчет вручную:
        // 10000 RUB -> 133.3333 USD (10000 / 75)
        // 133.3333 USD -> 117.6471 EUR (133.3333 * 75 / 85)
        // 117.6471 EUR -> 10000 RUB (117.6471 * 85) - нет потерь?
        // На самом деле есть потери из-за округления при конвертациях

        assertTrue(loss >= 0, "Потери не могут быть отрицательными");
        assertTrue(loss < 10000, "Потери не могут превышать начальную сумму");
    }

    @Test(groups = {"currency"})
    public void testCalculateConversionLossWithCommission() {
        double loss = converter.calculateConversionLossWithCommission(10000, 75.0, 85.0, 1.0);

        // Потери должны быть больше, чем без комиссии
        double lossWithoutCommission = converter.calculateConversionLoss(10000, 75.0, 85.0);
        assertTrue(loss > lossWithoutCommission, "Потери с комиссией должны быть больше");

        // Комиссия 1% от 10000 = 100 руб
        assertEquals(loss, lossWithoutCommission + 100, 0.01, "Неверный расчет комиссии");
    }

    @DataProvider(name = "currencyData")
    public Object[][] currencyDataProvider() {
        return new Object[][] {
                // rubles, usdRate, eurRate, expectedUSD, expectedEUR
                {7500.0, 75.0, 85.0, 100.0, 88.2353},
                {10000.0, 80.0, 90.0, 125.0, 111.1111},
                {5000.0, 70.0, 80.0, 71.4286, 62.5},
                {0.0, 75.0, 85.0, 0.0, 0.0},
                {1.0, 1.0, 1.0, 1.0, 1.0}
        };
    }

    @Test(dataProvider = "currencyData", groups = {"currency", "data-provider"})
    public void testCurrencyConversionsWithDataProvider(double rubles, double usdRate,
                                                        double eurRate, double expectedUSD,
                                                        double expectedEUR) {
        double usdAmount = converter.convertToUSD(rubles, usdRate);
        double eurAmount = converter.convertToEUR(rubles, eurRate);

        assertEquals(usdAmount, expectedUSD, 0.0001, "Неверная конвертация в USD");
        assertEquals(eurAmount, expectedEUR, 0.0001, "Неверная конвертация в EUR");

        // Проверка обратных расчетов (с учетом погрешности округления)
        assertEquals(usdAmount * usdRate, rubles, 0.01);
        assertEquals(eurAmount * eurRate, rubles, 0.01);
    }

    @DataProvider(name = "lossData")
    public Object[][] lossDataProvider() {
        return new Object[][] {
                {10000.0, 75.0, 85.0},
                {5000.0, 70.0, 80.0},
                {1000.0, 65.0, 75.0},
                {100.0, 50.0, 60.0}
        };
    }

    @Test(dataProvider = "lossData", groups = {"currency", "data-provider"})
    public void testConversionLossWithDataProvider(double rubles, double usdRate, double eurRate) {
        double loss = converter.calculateConversionLoss(rubles, usdRate, eurRate);

        // Потери должны быть неотрицательными
        assertTrue(loss >= 0, "Потери не могут быть отрицательными");

        // Потери не могут превышать начальную сумму
        assertTrue(loss <= rubles, "Потери не могут превышать начальную сумму");

        // Если курсы равны, потери должны быть 0
        if (usdRate == eurRate) {
            assertEquals(loss, 0.0, 0.001, "При равных курсах потерь быть не должно");
        }
    }

    @Test(groups = {"currency", "exceptions"},
            expectedExceptions = IllegalArgumentException.class)
    public void testNegativeRubles() {
        converter.convertToUSD(-1000, 75.0);
    }

    @Test(groups = {"currency", "exceptions"},
            expectedExceptions = IllegalArgumentException.class)
    public void testZeroExchangeRate() {
        converter.convertToUSD(1000, 0);
    }

    @Test(groups = {"currency", "exceptions"},
            expectedExceptions = IllegalArgumentException.class)
    public void testNegativeExchangeRate() {
        converter.convertToUSD(1000, -75.0);
    }

    @Test(groups = {"currency", "exceptions"},
            expectedExceptions = IllegalArgumentException.class)
    public void testInvalidCommission() {
        converter.calculateConversionLossWithCommission(10000, 75.0, 85.0, -1.0);
    }

    @Test(groups = {"currency", "exceptions"},
            expectedExceptions = IllegalArgumentException.class)
    public void testTooHighCommission() {
        converter.calculateConversionLossWithCommission(10000, 75.0, 85.0, 150.0);
    }

    @Test(groups = {"currency", "boundary"})
    public void testZeroRubles() {
        double usdAmount = converter.convertToUSD(0, 75.0);
        double eurAmount = converter.convertToEUR(0, 85.0);

        assertEquals(usdAmount, 0.0, 0.001);
        assertEquals(eurAmount, 0.0, 0.001);

        double loss = converter.calculateConversionLoss(0, 75.0, 85.0);
        assertEquals(loss, 0.0, 0.001);
    }
}