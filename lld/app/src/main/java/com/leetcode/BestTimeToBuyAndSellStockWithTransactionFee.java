package com.leetcode;

/**
 * Best Time to Buy and Sell Stock with Transaction Fee
 * You are given an array prices where prices[i] is the price of a given stock
 * on the ith day, and an integer fee representing a transaction fee.
 * 
 * Find the maximum profit you can achieve. You may complete as many
 * transactions as you like, but you need to pay the transaction fee for each
 * transaction.
 * 
 * Note:
 * 
 * A transaction is a buy followed by a sell.
 * You may not engage in multiple transactions simultaneously (i.e., you must
 * sell the stock before you buy again).
 * The transaction fee is only charged once for each transaction (not per buy
 * and per sell).
 * 
 * Example 1:
 * Input: prices = [1,3,2,8,4,9], fee = 2
 * Output: 8
 * Explanation: The maximum profit can be achieved by buying on day 0 (price =
 * 1), selling on day 3 (price = 8), buying on day 4 (price = 4), and selling on
 * day 5 (price = 9). The total profit is ((8 - 1) - 2) + ((9 - 4) - 2) = 8.
 * 
 * Constraints:
 * 1 <= prices.length <= 5 * 10^4
 * 1 <= prices[i] < 5 * 10^4
 * 0 <= fee < 5 * 10^4
 */

public class BestTimeToBuyAndSellStockWithTransactionFee {
    public int maxProfit(int[] prices, int fee) {
        int cash = 0;
        int hold = -prices[0];

        for (int i = 1; i < prices.length; i++) {
            cash = Math.max(cash, hold + prices[i] - fee);
            hold = Math.max(hold, cash - prices[i]);
        }

        return cash;
    }

    public static void main(String[] args) {
        BestTimeToBuyAndSellStockWithTransactionFee solution = new BestTimeToBuyAndSellStockWithTransactionFee();

        // Test 1: Standard case
        int res1 = solution.maxProfit(new int[] { 1, 3, 2, 8, 4, 9 }, 2);
        System.out.println("Test 1 (Expected: 8): " + res1);

        // Test 2: Decreasing prices
        int res2 = solution.maxProfit(new int[] { 9, 8, 7, 6, 5 }, 1);
        System.out.println("Test 2 (Expected: 0): " + res2);

        // Test 3: Fee equals price spread
        int res3 = solution.maxProfit(new int[] { 1, 9 }, 8);
        System.out.println("Test 3 (Expected: 0): " + res3);

        // Test 4: Zero fee
        int res4 = solution.maxProfit(new int[] { 1, 3, 2, 8, 4, 9 }, 0);
        System.out.println("Test 4 (Expected: 13): " + res4);

        // Test 5: Monotonically increasing prices
        int res5 = solution.maxProfit(new int[] { 1, 2, 3, 50, 100 }, 1);
        System.out.println("Test 5 (Expected: 98): " + res5);
    }
}
