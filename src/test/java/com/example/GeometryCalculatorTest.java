package com.example;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeMethod;
import static org.testng.Assert.*;

public class GeometryCalculatorTest {

    private GeometryCalculator geometry;

    @BeforeMethod(groups = {"geometry", "fast", "smoke"})
    public void setUp() {
        geometry = new GeometryCalculator();
    }

    @Test(groups = {"geometry", "fast", "smoke"})
    public void testCalculateTruncatedConeVolume() {
        // Тестовый пример 1: R=5, r=3, h=4
        double volume = geometry.calculateTruncatedConeVolume(5, 3, 4);

        // Расчет вручную: V = (1/3) * π * 4 * (25 + 15 + 9) = (1/3) * π * 4 * 49
        double expected = (1.0/3.0) * Math.PI * 4 * (25 + 15 + 9);
        //вот эту строчку тоже раскомментируй для 6 задания, а строчку выше наоборот закомментируй
        //double expected = 100.0; // НЕПРАВИЛЬНОЕ ожидаемое значение
        assertEquals(volume, expected, 0.001, "Объем конуса рассчитан неверно");

        // Тестовый пример 2: R=8, r=4, h=6
        volume = geometry.calculateTruncatedConeVolume(8, 4, 6);
        expected = (1.0/3.0) * Math.PI * 6 * (64 + 32 + 16);
        assertEquals(volume, expected, 0.001, "Объем конуса рассчитан неверно");
    }

    @Test(groups = {"geometry", "fast"})
    public void testCalculateTruncatedConeLateralSurfaceArea() {
        // Тестовый пример: R=5, r=3, h=4
        double area = geometry.calculateTruncatedConeLateralSurfaceArea(5, 3, 4);

        // Расчет вручную: l = √(16 + 4) = √20 ≈ 4.4721
        // S_бок = π * (5+3) * 4.4721 ≈ π * 8 * 4.4721
        double l = Math.sqrt(16 + 4);
        double expected = Math.PI * (5 + 3) * l;
        assertEquals(area, expected, 0.001, "Площадь боковой поверхности рассчитана неверно");
    }

    @Test(groups = {"geometry"})
    public void testCalculateTruncatedConeTotalSurfaceArea() {
        // Тестовый пример: R=5, r=3, h=4
        double area = geometry.calculateTruncatedConeTotalSurfaceArea(5, 3, 4);

        // Расчет вручную: l = √(16 + 4) = √20 ≈ 4.4721
        // S_полн = π * (25 + 9 + 8*4.4721) = π * (34 + 35.7768)
        double l = Math.sqrt(16 + 4);
        double expected = Math.PI * (25 + 9 + (5 + 3) * l);
        assertEquals(area, expected, 0.001, "Полная площадь поверхности рассчитана неверно");
    }

    @Test(groups = {"geometry", "fast"})
    public void testCalculateGeneratrix() {
        // Тестовый пример: R=5, r=3, h=4
        double l = geometry.calculateGeneratrix(5, 3, 4);
        double expected = Math.sqrt(16 + 4); // √(h² + (R-r)²) = √(16 + 4)
        assertEquals(l, expected, 0.001, "Образующая рассчитана неверно");
    }

    @Test(groups = {"geometry"})
    public void testCalculateAllConeParameters() {
        GeometryCalculator.ConeResults results = geometry.calculateAllConeParameters(5, 3, 4);

        // Проверяем все значения
        double expectedVolume = (1.0/3.0) * Math.PI * 4 * (25 + 15 + 9);
        double expectedL = Math.sqrt(16 + 4);
        double expectedLateralArea = Math.PI * (5 + 3) * expectedL;
        double expectedTotalArea = Math.PI * (25 + 9 + (5 + 3) * expectedL);

        assertEquals(results.getVolume(), expectedVolume, 0.001, "Объем не совпадает");
        assertEquals(results.getGeneratrix(), expectedL, 0.001, "Образующая не совпадает");
        assertEquals(results.getLateralSurfaceArea(), expectedLateralArea, 0.001,
                "Площадь боковой поверхности не совпадает");
        assertEquals(results.getTotalSurfaceArea(), expectedTotalArea, 0.001,
                "Полная площадь не совпадает");
    }

    @DataProvider(name = "coneData")
    public Object[][] coneDataProvider() {
        return new Object[][] {
                // R, r, h
                {5.0, 3.0, 4.0},
                {8.0, 4.0, 6.0},
                {10.0, 6.0, 8.0},
                {7.5, 4.5, 5.0},
                {12.0, 8.0, 10.0}
        };
    }

    @Test(dataProvider = "coneData", groups = {"geometry", "data-provider"})
    public void testConeCalculationsWithDataProvider(double R, double r, double h) {
        // Проверяем, что все методы возвращают положительные значения
        double volume = geometry.calculateTruncatedConeVolume(R, r, h);
        double lateralArea = geometry.calculateTruncatedConeLateralSurfaceArea(R, r, h);
        double totalArea = geometry.calculateTruncatedConeTotalSurfaceArea(R, r, h);
        double generatrix = geometry.calculateGeneratrix(R, r, h);

        // Проверяем положительность результатов
        assertTrue(volume > 0, "Объем должен быть положительным");
        assertTrue(lateralArea > 0, "Площадь боковой поверхности должна быть положительной");
        assertTrue(totalArea > 0, "Полная площадь должна быть положительной");
        assertTrue(generatrix > 0, "Образующая должна быть положительной");

        // Проверяем соотношения
        assertTrue(totalArea > lateralArea,
                "Полная площадь должна быть больше площади боковой поверхности");
        assertTrue(generatrix > h,
                "Образующая должна быть больше высоты (по теореме Пифагора)");

        // Проверяем формулу связи площадей
        double baseAreaR = Math.PI * R * R;
        double baseAreaRr = Math.PI * r * r;
        assertEquals(totalArea, lateralArea + baseAreaR + baseAreaRr, 0.001,
                "Полная площадь должна равняться сумме боковой площади и площадей оснований");
    }

    @Test(groups = {"geometry", "exceptions"},
            expectedExceptions = IllegalArgumentException.class)
    public void testNegativeRadius() {
        geometry.calculateTruncatedConeVolume(-5, 3, 4);
    }

    @Test(groups = {"geometry", "exceptions"},
            expectedExceptions = IllegalArgumentException.class)
    public void testZeroHeight() {
        geometry.calculateTruncatedConeVolume(5, 3, 0);
    }

    @Test(groups = {"geometry", "exceptions"},
            expectedExceptions = IllegalArgumentException.class)
    public void testRSmallerThanR() {
        geometry.calculateTruncatedConeVolume(3, 5, 4); // R < r
    }

    @Test(groups = {"geometry", "boundary"})
    public void testCalculateBaseArea() {
        double area = geometry.calculateBaseArea(5);
        double expected = Math.PI * 25;
        assertEquals(area, expected, 0.001, "Площадь основания рассчитана неверно");
    }

    @Test(groups = {"geometry", "exceptions"},
            expectedExceptions = IllegalArgumentException.class)
    public void testBaseAreaWithZeroRadius() {
        geometry.calculateBaseArea(0);
    }

    @Test(groups = {"geometry", "boundary"})
    public void testVerySmallCone() {
        // Конус с очень маленькими размерами
        double R = 0.1;
        double r = 0.05;
        double h = 0.01;

        GeometryCalculator.ConeResults results = geometry.calculateAllConeParameters(R, r, h);

        assertTrue(results.getVolume() > 0, "Объем должен быть положительным даже для маленького конуса");
        assertTrue(results.getTotalSurfaceArea() > 0,
                "Площадь должна быть положительной даже для маленького конуса");
    }
}