package com.leetcode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BestTimeToBuyAndSellStockWithTransactionFeeTest {

    private BestTimeToBuyAndSellStockWithTransactionFee solution;

    @BeforeEach
    void setUp() {
        solution = new BestTimeToBuyAndSellStockWithTransactionFee();
    }

    @Test
    @DisplayName("Example 1: standard case from problem statement")
    void testExample1() {
        assertEquals(8, solution.maxProfit(new int[] { 1, 3, 2, 8, 4, 9 }, 2));
    }

    @Test
    @DisplayName("Example 2: monotonically decreasing prices — no profit possible")
    void testDecreasingPrices() {
        assertEquals(0, solution.maxProfit(new int[] { 9, 8, 7, 6, 5 }, 1));
    }

    @Test
    @DisplayName("Single element — no transaction possible")
    void testSingleElement() {
        assertEquals(0, solution.maxProfit(new int[] { 5 }, 1));
    }

    @Test
    @DisplayName("Fee equals price spread — no profitable transaction")
    void testFeeEqualsPriceSpread() {
        // spread = 9 - 1 = 8, fee = 8 → net = 0
        assertEquals(0, solution.maxProfit(new int[] { 1, 9 }, 8));
    }

    @Test
    @DisplayName("Fee exceeds price spread — no profitable transaction")
    void testFeeExceedsPriceSpread() {
        assertEquals(0, solution.maxProfit(new int[] { 1, 9 }, 10));
    }

    @Test
    @DisplayName("Zero fee — degenerate to unlimited transactions")
    void testZeroFee() {
        // Sum all upward moves: (3-1)+(8-2)+(9-4) = 2+6+5 = 13
        assertEquals(13, solution.maxProfit(new int[] { 1, 3, 2, 8, 4, 9 }, 0));
    }

    @Test
    @DisplayName("All prices equal — no profitable transaction")
    void testAllPricesEqual() {
        assertEquals(0, solution.maxProfit(new int[] { 5, 5, 5, 5, 5 }, 1));
    }

    @Test
    @DisplayName("Two elements with profitable trade")
    void testTwoElements() {
        // profit = 10 - 1 - 2 = 7
        assertEquals(7, solution.maxProfit(new int[] { 1, 10 }, 2));
    }

    @Test
    @DisplayName("Multiple transactions are better than one")
    void testMultipleTransactionsBetter() {
        // Buy@1 sell@5 (profit=3), buy@1 sell@5 (profit=3) → total 6
        // vs single buy@1 sell@5 → 3
        assertEquals(6, solution.maxProfit(new int[] { 1, 5, 1, 5 }, 1));
    }

    @Test
    @DisplayName("Large input — monotonically increasing prices")
    void testMonotonicallyIncreasing() {
        // Optimal: buy on day 0, sell on last day → 100 - 1 - 1 = 98
        assertEquals(98, solution.maxProfit(new int[] { 1, 2, 3, 50, 100 }, 1));
    }
}
