package com.example;

/**
 *
 * У пользователя есть R руб. Выполнить перевод денег сначала в доллары, а потом в евро.
 * Текущие курсы обмена валют пользователь вводит через консоль.
 * Определить, сколько рублей пользователь потерял при конвертации валют.
 */
public class CurrencyConverter {

    /**
     * Конвертация рублей в доллары
     * @param rubles сумма в рублях
     * @param usdRate курс доллара (рублей за 1 доллар)
     * @return сумма в долларах
     */
    public double convertToUSD(double rubles, double usdRate) {
        if (rubles < 0) {
            throw new IllegalArgumentException("Сумма в рублях не может быть отрицательной");
        }
        if (usdRate <= 0) {
            throw new IllegalArgumentException("Курс доллара должен быть положительным");
        }
        return rubles / usdRate;
    }

    /**
     * Конвертация рублей в евро
     * @param rubles сумма в рублях
     * @param eurRate курс евро (рублей за 1 евро)
     * @return сумма в евро
     */
    public double convertToEUR(double rubles, double eurRate) {
        if (rubles < 0) {
            throw new IllegalArgumentException("Сумма в рублях не может быть отрицательной");
        }
        if (eurRate <= 0) {
            throw new IllegalArgumentException("Курс евро должен быть положительным");
        }
        return rubles / eurRate;
    }

    /**
     * Расчет потерь при двойной конвертации (RUB -> USD -> EUR -> RUB)
     * @param initialRubles исходная сумма в рублях
     * @param usdRate курс доллара
     * @param eurRate курс евро
     * @return потери в рублях
     */
    public double calculateConversionLoss(double initialRubles, double usdRate, double eurRate) {
        if (initialRubles < 0 || usdRate <= 0 || eurRate <= 0) {
            throw new IllegalArgumentException("Неверные параметры конвертации");
        }

        // Конвертируем RUB -> USD
        double usdAmount = convertToUSD(initialRubles, usdRate);

        // Конвертируем USD -> EUR (используем обратный курс)
        double eurAmount = usdAmount * (usdRate / eurRate);

        // Конвертируем EUR -> RUB
        double finalRubles = eurAmount * eurRate;

        // Расчет потерь
        return initialRubles - finalRubles;
    }

    /**
     * Расчет потерь с учетом комиссии банка
     * @param initialRubles исходная сумма
     * @param usdRate курс доллара
     * @param eurRate курс евро
     * @param commission процент комиссии банка
     * @return общие потери с учетом комиссии
     */
    public double calculateConversionLossWithCommission(double initialRubles,
                                                        double usdRate, double eurRate,
                                                        double commission) {
        if (commission < 0 || commission > 100) {
            throw new IllegalArgumentException("Комиссия должна быть от 0 до 100%");
        }

        double conversionLoss = calculateConversionLoss(initialRubles, usdRate, eurRate);
        double commissionAmount = initialRubles * (commission / 100);

        return conversionLoss + commissionAmount;
    }
}