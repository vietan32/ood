/**
Leetcode 1044. Longest Duplicate Substring
Given a string s, consider all duplicated substrings: (contiguous) substrings of s that occur 2 or more times. The occurrences may overlap.
Return any duplicated substring that has the longest possible length. If s does not have a duplicated substring, the answer is "".

Example 1:
Input: s = "banana"
Output: "ana"

Example 2:
Input: s = "abcd"
Output: ""

Constraints:
2 <= s.length <= 3 * 104
s consists of lowercase English letters.
 */
package com.leetcode;

import java.util.HashSet;
import java.util.Set;

class LongestDuplicateSubstring {
    private static final long MOD1 = 1_000_000_007L;
    private static final long MOD2 = 1_000_000_009L;
    private long[] pow1;
    private long[] pow2;

    public String longestDupSubstring(String s) {
        int n = s.length();
        pow1 = new long[n + 1];
        pow2 = new long[n + 1];
        pow1[0] = 1;
        pow2[0] = 1;
        for (int i = 1; i <= n; i++) {
            pow1[i] = (pow1[i - 1] * 26) % MOD1;
            pow2[i] = (pow2[i - 1] * 26) % MOD2;
        }

        int l = 0, r = n;
        int startIdx = -1;
        int maxLen = 0;

        while (l < r) {
            int mid = (l + r + 1) / 2;
            int idx = check(s, mid);
            if (idx != -1) {
                l = mid;
                startIdx = idx;
                maxLen = mid;
            } else {
                r = mid - 1;
            }
        }
        return startIdx != -1 ? s.substring(startIdx, startIdx + maxLen) : "";
    }

    private int check(String s, int window) {
        Set<Long> set = new HashSet<>();
        int n = s.length();
        long h1 = 0, h2 = 0;

        for (int i = 0; i < n; i++) {
            int val = s.charAt(i) - 'a';
            h1 = (h1 * 26 + val) % MOD1;
            h2 = (h2 * 26 + val) % MOD2;

            if (i >= window) {
                int prevVal = s.charAt(i - window) - 'a';
                h1 = (h1 - pow1[window] * prevVal) % MOD1;
                if (h1 < 0)
                    h1 += MOD1;

                h2 = (h2 - pow2[window] * prevVal) % MOD2;
                if (h2 < 0)
                    h2 += MOD2;
            }

            if (i >= window - 1) {
                long combinedHash = (h1 << 32) | h2;
                if (set.contains(combinedHash)) {
                    return i - window + 1;
                }
                set.add(combinedHash);
            }
        }
        return -1;
    }
}
