package com.example;

/**
 * Задание 3
 * Создать программу для решения задачи по геометрии.
 * Заданы R, r, h – измерения усеченного конуса.
 * Вычислить площадь поверхности и объем усеченного конуса.
 */
public class GeometryCalculator {

    /**
     * Вычисление объема усеченного конуса
     * Формула: V = (1/3) * π * h * (R² + R*r + r²)
     * @param R радиус большего основания
     * @param r радиус меньшего основания
     * @param h высота конуса
     * @return объем усеченного конуса
     */

    public double calculateTruncatedConeVolume(double R, double r, double h) {
        validateConeParameters(R, r, h);
        return (1.0/3.0) * Math.PI * h * (R*R + R*r + r*r);
        //Поля, вот это разкомментируй для 6 задания, а строчку  выше наоборот закоменти, ну либо 3.0 поменяй на 2.0.
        //return (1.0/2.0) * Math.PI * h * (R*R + R*r + r*r); // ОШИБКА: 1/2 вместо 1/3
    }
    /**
     * Вычисление площади боковой поверхности усеченного конуса
     * Формула: S_бок = π * (R + r) * l, где l - образующая
     * l = √(h² + (R - r)²)
     * @param R радиус большего основания
     * @param r радиус меньшего основания
     * @param h высота конуса
     * @return площадь боковой поверхности
     */
    public double calculateTruncatedConeLateralSurfaceArea(double R, double r, double h) {
        validateConeParameters(R, r, h);
        double l = calculateGeneratrix(R, r, h); // образующая
        return Math.PI * (R + r) * l;
    }

    /**
     * Вычисление полной площади поверхности усеченного конуса
     * Формула: S_полн = π * (R² + r² + (R + r) * l)
     * @param R радиус большего основания
     * @param r радиус меньшего основания
     * @param h высота конуса
     * @return полная площадь поверхности
     */
    public double calculateTruncatedConeTotalSurfaceArea(double R, double r, double h) {
        validateConeParameters(R, r, h);
        double l = calculateGeneratrix(R, r, h); // образующая
        return Math.PI * (R*R + r*r + (R + r) * l);
    }

    /**
     * Вычисление площади основания (большего)
     * @param R радиус большего основания
     * @return площадь большего основания
     */
    public double calculateBaseArea(double R) {
        if (R <= 0) {
            throw new IllegalArgumentException("Радиус основания должен быть положительным");
        }
        return Math.PI * R * R;
    }

    /**
     * Вычисление образующей усеченного конуса
     * Формула: l = √(h² + (R - r)²)
     * @param R радиус большего основания
     * @param r радиус меньшего основания
     * @param h высота конуса
     * @return длина образующей
     */
    public double calculateGeneratrix(double R, double r, double h) {
        validateConeParameters(R, r, h);
        return Math.sqrt(h*h + (R - r)*(R - r));
    }

    /**
     * Проверка параметров конуса на валидность
     */
    private void validateConeParameters(double R, double r, double h) {
        if (R <= 0 || r <= 0 || h <= 0) {
            throw new IllegalArgumentException("Все параметры конуса должны быть положительными");
        }
        if (R <= r) {
            throw new IllegalArgumentException("Радиус большего основания R должен быть больше радиуса меньшего основания r");
        }
    }

    /**
     * Класс для хранения всех результатов вычислений для конуса
     */
    public static class ConeResults {
        private final double volume;
        private final double lateralSurfaceArea;
        private final double totalSurfaceArea;
        private final double generatrix;

        public ConeResults(double volume, double lateralSurfaceArea,
                           double totalSurfaceArea, double generatrix) {
            this.volume = volume;
            this.lateralSurfaceArea = lateralSurfaceArea;
            this.totalSurfaceArea = totalSurfaceArea;
            this.generatrix = generatrix;
        }

        public double getVolume() { return volume; }
        public double getLateralSurfaceArea() { return lateralSurfaceArea; }
        public double getTotalSurfaceArea() { return totalSurfaceArea; }
        public double getGeneratrix() { return generatrix; }

        @Override
        public String toString() {
            return String.format("Объем: %.2f, Площадь бок. поверхности: %.2f, " +
                            "Полная площадь: %.2f, Образующая: %.2f",
                    volume, lateralSurfaceArea, totalSurfaceArea, generatrix);
        }
    }

    /**
     * Полный расчет всех параметров конуса
     */
    public ConeResults calculateAllConeParameters(double R, double r, double h) {
        validateConeParameters(R, r, h);

        double volume = calculateTruncatedConeVolume(R, r, h);
        double lateralArea = calculateTruncatedConeLateralSurfaceArea(R, r, h);
        double totalArea = calculateTruncatedConeTotalSurfaceArea(R, r, h);
        double generatrix = calculateGeneratrix(R, r, h);

        return new ConeResults(volume, lateralArea, totalArea, generatrix);
    }
}