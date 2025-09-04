package com.hbm.nucleartech.util;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Represents a group of decimal digits while preserving leading zeros.
 * Example: "005" => digits="005", value=5, length=3.
 */
public final class LeadingLong extends Number implements Comparable<LeadingLong> {

    private final String digits;   // full digits, preserves leading zeros (e.g. "005")
    private final long value;      // numeric value (5)
    private final int length;      // number of digits (3)

    /**
     * Construct from a numeric value (no leading zeros).
     * digits will be the plain decimal representation of value.
     */
    public LeadingLong(long value) {

        if (value < 0) throw new IllegalArgumentException("value must be >= 0");
        String digits = Long.toString(value);
        if(digits.length() > 4)
            digits = digits.substring(0, 4);
        this.digits = digits;
        this.value = Long.parseLong(this.digits);
        this.length = this.digits.length();
    }

    /**
     * Construct from a string of digits (may include leading zeros).
     * Example: new LeadingLong("005") preserves leading zeros.
     */
    public LeadingLong(@NotNull String digits) {

//        System.out.println(digits);

        if (digits == null || digits.isEmpty()) {

            throw new IllegalArgumentException("digits must be a non-empty string of 0-9 characters");
        }
        if (!digits.matches("\\d+")) {

            throw new IllegalArgumentException("digits must contain only numeric characters (0-9)");
        }
        if(digits.length() > 4)
            digits = digits.substring(0, 4);
        this.digits = digits;
        // parse numeric value (will be 0 for "000", etc.)
        try {

            this.value = Long.parseLong(this.digits);
        } catch (NumberFormatException e) {

            // If digits are longer than Long can hold, you can switch to BigInteger here.

            throw new IllegalArgumentException("digit string too large to parse to long; use smaller groups or adapt code", e);
        }
        this.length = this.digits.length();
    }

    /**
     * Construct from an explicit leading-zero string and a numeric tail value.
     * Example: lead = "00", value = 5 => digits = "005".
     */
    public LeadingLong(@NotNull String lead, long tailValue) {

        if (!lead.matches("\\d*")) throw new IllegalArgumentException("lead must contain only digits (usually zeros)");
        String tail = Long.toString(Math.max(0, tailValue));
        this.digits = lead + tail;
        try {

            this.value = Long.parseLong(this.digits);
        } catch (NumberFormatException e) {

            throw new IllegalArgumentException("combined digits too large for long", e);
        }
        this.length = this.digits.length();
    }

    /** Number of digits in the original string. */
    public int length() {

        return length;
    }

    /* Number implementations (safe fallbacks) */
    @Override
    public int intValue() {

        return (int) value;
    }

    @Override
    public long longValue() {

        return value;
    }

    @Override
    public float floatValue() {

        return Float.parseFloat("0." + digits);
    }

    @Override
    public double doubleValue() {

        return Double.parseDouble("0." + digits);
    }

    @Override
    public String toString() {

        return digits;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof LeadingLong that)) return false;
        return this.length == that.length && this.digits.equals(that.digits);
    }

    @Override
    public int hashCode() {

        return Objects.hash(digits, length);
    }

    /**
     * Compare two LeadingLong groups numerically by aligning their digit widths.
     * Example: compare "05" (length=2, value=5) vs "005" (length=3, value=5):
     *   "05" -> 050 (value 50), "005" -> 005 (value 5) scaled appropriately by aligning to max length.
     */
    @Override
    public int compareTo(@NotNull LeadingLong o) {

        if (this.equals(o)) return 0;

        if (this.digits.equals(o.digits) && this.length == o.length) return 0;

        int maxLen = Math.max(this.length, o.length);

        // scale both values to the same width: value * 10^(maxLen - length)
        BigInteger a = BigInteger.valueOf(this.value).multiply(BigInteger.TEN.pow(maxLen - this.length));
        BigInteger b = BigInteger.valueOf(o.value).multiply(BigInteger.TEN.pow(maxLen - o.length));

        return a.compareTo(b);
    }
}
