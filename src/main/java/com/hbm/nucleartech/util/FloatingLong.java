package com.hbm.nucleartech.util;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lightweight decimal+exponent number type that preserves fractional digits via LeadingLong.
 * - 'not' holds the canonical textual representation (trimmed).
 * - num: signed integer part (may be negative).
 * - dec: fractional digits preserved as LeadingLong (may be null).
 * - exp: exponent (power-of-10).
 * Numeric operations / comparisons use BigDecimal built from the canonical text (cached).
 */
public class FloatingLong extends Number implements Comparable<FloatingLong> {

    protected enum Type {

        integral,
        fractional,
        exponential
    }

    protected long num;
    protected LeadingLong dec;  // may be null if no decimal part
    protected long exp;

    protected Type type;

    protected String not;      // canonical textual representation, e.g. "-123.045E6"

    private static final Pattern FLOAT_STR_PATTERN =
            Pattern.compile("^([+-]?\\d+)(?:\\.(\\d*))?(?:[eE]([+-]?\\d+))?$");

    public static final FloatingLong ZERO = FloatingLong.create();

//    9223372036854775807.9223372036854775807E9223372036854775807
    public static final FloatingLong MAX_VALUE = FloatingLong.create(Long.MAX_VALUE-1, new LeadingLong(Long.MAX_VALUE-1), Long.MAX_VALUE-1);

//    -9223372036854775808.9223372036854775807E9223372036854775807
    public static final FloatingLong MIN_VALUE = FloatingLong.create(Long.MIN_VALUE+1, new LeadingLong(Long.MAX_VALUE-1), Long.MAX_VALUE-1);



    // the last thing to do is make it so that if a scientific notation is created, and it can fit into a long, convert it into a long.

    FloatingLong convertIfFits() {
        if (this.type != Type.exponential) return this;

        // use BigDecimal to get a plain (non-exponential) string if it's not absurdly large.
        // BigDecimal can parse scientific notation exactly from the textual form.
        try {
            BigDecimal bd = new BigDecimal(this.not);              // exact interpretation of textual representation
            bd = bd.stripTrailingZeros();                         // tidy it up (optional)
            String plain = bd.toPlainString();                    // plain decimal (no 'E' part)

            // Protect against extremely long plain strings that would blow memory / perf.
            final int MAX_PLAIN_LEN = 2000; // tweak as appropriate
            if (plain.isEmpty() || plain.length() > MAX_PLAIN_LEN) {
                return this; // can't safely convert
            }

            return new FloatingLong(plain);
        } catch (Exception e) {
            // If anything goes wrong, keep the original exponential form.
            return this;
        }
    }


    /** String constructor: parse formats like "123", "123.45", "123.45E6", "123E6", "0.005", "-1.0E3" */
    private FloatingLong(String input) {
        if (input == null) throw new IllegalArgumentException("input cannot be null");
        String trimmed = input.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("input cannot be empty");

        Matcher m = FLOAT_STR_PATTERN.matcher(trimmed);
        if (!m.matches()) {
            throw new IllegalArgumentException("Invalid floating-long format: " + input);
        }

        // Keep canonical textual form
        this.not = trimmed;

        String intPart = m.group(1);        // may include + or -
        String decPart = m.group(2);        // may be null or empty
        String expPart = m.group(3);        // may be null

        // parse integer part (signed)
        this.num = Long.parseLong(intPart);

        // decimal part: preserve presence; treat "123." as fraction "0"
        if (decPart != null) {
            // strip trailing zeros from the textual decimal part — "123.4500" -> "123.45"
            String trimmedDec = decPart.replaceFirst("0+$", "");
            if (trimmedDec.isEmpty()) {
                // treat an all-zero decimal as "no decimal part"
                this.dec = new LeadingLong(0);
            } else {
                this.dec = new LeadingLong(trimmedDec);
            }
        } else {
            this.dec = new LeadingLong(0);
        }

        // exponent
        this.exp = expPart != null ? Long.parseLong(expPart) : 0L;

        this.type = this.exp != 0L ? Type.exponential : this.dec.longValue() != 0L ? Type.fractional : Type.integral;

        this.not = (this.not.charAt(0) == '-' && this.not.charAt(1) == '0' ? "-" : "") + this.num + "." + this.dec.toString() + "E" + this.exp;
    }

    /**
     * Primary constructor from components.
     * Keeps num as signed (can be negative). LeadingLong should represent positive fractional digits only.
     */
    private FloatingLong(long integer, LeadingLong decimal, long exponent) {
        this.num = integer;
        this.dec = decimal;
        this.exp = exponent;

        // Build canonical textual representation (Long.toString handles sign)
        StringBuilder sb = new StringBuilder();
        sb.append(Long.toString(num));
        sb.append('.').append(dec.toString());
        if (exp != 0L) sb.append('E').append(exp);
        this.not = sb.toString();

        this.type = this.exp != 0L ? Type.exponential : this.dec.longValue() != 0L ? Type.fractional : Type.integral;

        this.not = (this.not.charAt(0) == '-' && this.not.charAt(1) == '0' ? "-" : "") + this.num + "." + this.dec.toString() + "E" + this.exp;
    }

    public FloatingLong copy() {

        return this;
    }

    public FloatingLong shaveDecimal() {

        return create(num, new LeadingLong(0), exp);
    }

    public static FloatingLong create() {

        return new FloatingLong("0");
    }
    public static FloatingLong create(String input) {

        if(input.isEmpty())
            input = "0";

        return new FloatingLong(input.replaceAll(",", "").toUpperCase()).convertIfFits();
    }
    public static FloatingLong create(long integer, LeadingLong decimal, long exponent) {

//        System.out.println(integer + "." + decimal + "E" + exponent);
        return new FloatingLong(integer, decimal, exponent).convertIfFits();
    }
    public static FloatingLong create(long input) {

        return new FloatingLong(input, new LeadingLong(0), 0);
    }
    public static FloatingLong create(float input) {
        // BigDecimal.valueOf preserves full double value and gives a plain string if you want:
        return new FloatingLong(BigDecimal.valueOf(input).toString().replaceAll(",", "").toUpperCase()).convertIfFits();
    }
    public static FloatingLong create(int input) {

        return new FloatingLong(input, new LeadingLong(0), 0);
    }
    public static FloatingLong create(double input) {
        // BigDecimal.valueOf preserves full double value and gives a plain string if you want:
        return new FloatingLong(BigDecimal.valueOf(input).toString().replaceAll(",", "").toUpperCase()).convertIfFits();
    }

    /** Simple accessor for the canonical string form. */
    @Override
    public String toString() {
        return not;
    }

    // safer digit counting for longs
    private static int safeDigitsCount(long v) {
        if (v == 0L) return 1;
        if (v == Long.MIN_VALUE) return 19;
        return Long.toString(Math.abs(v)).length();
    }

    // Build an *unsigned* unscaled BigInteger (no sign) from components (handles Long.MIN_VALUE)
    private static BigInteger buildUnscaledBigIntegerUnsigned(long num, LeadingLong dec) {
        StringBuilder s = new StringBuilder();
        if (num == Long.MIN_VALUE) {
            s.append("9223372036854775808");
        } else {
            s.append(Long.toString(Math.abs(num)));
        }
        if (dec != null && dec.length() > 0) s.append(dec.toString());

//        System.out.println(s.toString());
        return new BigInteger(s.toString());
    }

    /** Build signed unscaled BigInteger by concatenating abs(num) and dec digits (if any).
     *  Handles Long.MIN_VALUE correctly by using the string "9223372036854775808". */
    private static BigInteger buildSignedUnscaledBigInteger(long num, LeadingLong dec) {
        StringBuilder sb = new StringBuilder();
        String absStr;
        if (num == Long.MIN_VALUE) {
            // abs(Long.MIN_VALUE) = 9223372036854775808 (can't represent as positive long)
            absStr = "9223372036854775808";
        } else {
            absStr = Long.toString(Math.abs(num));
        }
        sb.append(absStr);
        if (dec != null && dec.length() > 0) {
            sb.append(dec.toString()); // LeadingLong preserves leading zeros
        }
        BigInteger bi = new BigInteger(sb.toString());
        if (num < 0) bi = bi.negate();
        return bi;
    }

//    this shit ain't gonna work for you ;3
//    you need to store and do everything without BigDecimal because BigDecimal can't handle how big your numbers are~

    @Override
    public int intValue() {
        // Fast zero check
//        if (this.num == 0L && (this.dec == null || this.dec.length() == 0 || this.dec.longValue() == 0L) && this.exp == 0L) {
//            return 0;
//        }

        final BigInteger INT_MAX_BI = BigInteger.valueOf(Integer.MAX_VALUE);
        final BigInteger INT_MIN_BI = BigInteger.valueOf(Integer.MIN_VALUE);

        int decLen = this.dec.length();
        int numDigits = safeDigitsCount(this.num); // safe for Long.MIN_VALUE
        long unscaledDigits = (long) numDigits + (long) decLen;
        long shift = this.exp - decLen;

        // approxDigits = number of digits in integer part of the final value
        long approxDigits = getApproxDigits(unscaledDigits, shift);

        // Quick clamps:
        if (approxDigits > 10L) { // definitely larger than Integer.MAX_VALUE (10 digits)
            return (this.num >= 0) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        if (approxDigits <= 0L) { // magnitude < 1 -> truncates to zero
            return 0;
        }

        // Now safe to do exact operations: approxDigits is <= 10 so pow() will be small
        BigInteger unscaled = buildSignedUnscaledBigInteger(this.num, this.dec);

        if (shift > 0) {
            unscaled = unscaled.multiply(BigInteger.TEN.pow((int) shift));
        } else if (shift < 0) {
            unscaled = unscaled.divide(BigInteger.TEN.pow((int)(-shift))); // truncating toward zero
        }

        if (unscaled.compareTo(INT_MAX_BI) > 0) return Integer.MAX_VALUE;
        if (unscaled.compareTo(INT_MIN_BI) < 0) return Integer.MIN_VALUE;
        return unscaled.intValue();
    }

    // ---------------- longValue() ----------------
    @Override
    public long longValue() {
        // Fast zero check
        if (this.num == 0L && (this.dec.length() == 0 || this.dec.longValue() == 0L) && this.exp == 0L) {
            return 0L;
        }

        final BigInteger LONG_MAX_BI = BigInteger.valueOf(Long.MAX_VALUE);
        final BigInteger LONG_MIN_BI = BigInteger.valueOf(Long.MIN_VALUE);

        int decLen = this.dec.length();
        int numDigits = safeDigitsCount(this.num);
        long unscaledDigits = (long) numDigits + (long) decLen;
        long shift = this.exp - decLen;

        long approxDigits = getApproxDigits(unscaledDigits, shift);

        // Quick clamps:
        if (approxDigits > 19L) { // definitely larger than Long.MAX_VALUE (19 digits)
            return (this.num >= 0) ? Long.MAX_VALUE : Long.MIN_VALUE;
        }
        if (approxDigits <= 0L) { // magnitude < 1 -> truncates to zero
            return 0L;
        }

        // safe to build exact integer representation
        BigInteger unscaled = buildSignedUnscaledBigInteger(this.num, this.dec);

        if (shift > 0) {
            unscaled = unscaled.multiply(BigInteger.TEN.pow((int) shift));
        } else if (shift < 0) {
            unscaled = unscaled.divide(BigInteger.TEN.pow((int) (-shift))); // truncating toward zero
        }

        if (unscaled.compareTo(LONG_MAX_BI) > 0) return Long.MAX_VALUE;
        if (unscaled.compareTo(LONG_MIN_BI) < 0) return Long.MIN_VALUE;
        return unscaled.longValue();
    }

    private static long getApproxDigits(long unscaledDigits, long shift) {

        long approxDigits; // digits in integer part
        switch(isSafe(unscaledDigits, shift)) {

            case -1 -> {

                approxDigits = Long.MIN_VALUE;
            }
            case 0  -> {

                approxDigits = unscaledDigits + shift;
            }
            case 1  -> {

                approxDigits = Long.MAX_VALUE;
            }
            default -> {

                approxDigits = 0;
            }
        }
        return approxDigits;
    }
    private static int getApproxDigits(int unscaledDigits, int shift) {

        int approxDigits; // digits in integer part
        switch(isSafe(unscaledDigits, shift)) {

            case -1 -> {

                approxDigits = Integer.MIN_VALUE;
            }
            case 0  -> {

                approxDigits = unscaledDigits + shift;
            }
            case 1  -> {

                approxDigits = Integer.MAX_VALUE;
            }
            default -> {

                approxDigits = 0;
            }
        }
        return approxDigits;
    }
    private static float getApproxDigits(float unscaledDigits, float shift) {

        float approxDigits; // digits in integer part
        switch(isSafe(unscaledDigits, shift)) {

            case -1 -> {

                approxDigits = -Float.MAX_VALUE;
            }
            case 0  -> {

                approxDigits = unscaledDigits + shift;
            }
            case 1  -> {

                approxDigits = Float.MAX_VALUE;
            }
            default -> {

                approxDigits = 0;
            }
        }
        return approxDigits;
    }
    private static double getApproxDigits(double unscaledDigits, double shift) {

        double approxDigits; // digits in integer part
        switch(isSafe(unscaledDigits, shift)) {

            case -1 -> {

                approxDigits = -Double.MAX_VALUE;
            }
            case 0  -> {

                approxDigits = unscaledDigits + shift;
            }
            case 1  -> {

                approxDigits = Double.MAX_VALUE;
            }
            default -> {

                approxDigits = 0;
            }
        }
        return approxDigits;
    }

    // ---------------- floatValue() ----------------
    @Override
    public float floatValue() {
        // Fast zero check
        if (this.num == 0L && (this.dec.length() == 0 || this.dec.longValue() == 0L) && this.exp == 0L) {
            return 0.0f;
        }

        // float practical limits
        final int FLOAT_MAX_DIGITS = 38;
        final int FLOAT_MIN_DIGITS = -45;

        int decLen = this.dec.length();
        int numDigits = safeDigitsCount(this.num);
        long unscaledDigits = (long) numDigits + (long) decLen;
        long shift = this.exp - decLen;

        long approxDigits = getApproxDigits(unscaledDigits, shift);

        if (approxDigits > FLOAT_MAX_DIGITS) {
            return (this.num >= 0) ? Float.MAX_VALUE : -Float.MAX_VALUE;
        }
        if (approxDigits <= FLOAT_MIN_DIGITS) {
            return 0.0f;
        }

        // Build unscaled BigInteger (signed)
        BigInteger unscaled = buildSignedUnscaledBigInteger(this.num, this.dec);

        // Use BigDecimal scaling to preserve fractional part when shift < 0
        try {
            BigDecimal bd = new BigDecimal(unscaled);
            // scaleByPowerOfTen accepts int; safe because approxDigits constrained above
            if (shift != 0L) {
                if (Math.abs(shift) > Integer.MAX_VALUE) {
                    // absurd shift; fallback to clamps already handled above
                    return (this.num >= 0) ? Float.MAX_VALUE : -Float.MAX_VALUE;
                }
                bd = bd.scaleByPowerOfTen((int) shift);
            }
            float f = bd.floatValue(); // converts with rounding
            if (Float.isInfinite(f)) {
                return (f > 0) ? Float.MAX_VALUE : -Float.MAX_VALUE;
            }
            return f;
        } catch (Exception ex) {
            // Fallback to previous (less accurate) approach on unexpected errors
            if (shift > 0) {
                unscaled = unscaled.multiply(BigInteger.TEN.pow((int) shift));
            } else if (shift < 0) {
                unscaled = unscaled.divide(BigInteger.TEN.pow((int) (-shift)));
            }
            float f = unscaled.floatValue();
            if (Float.isInfinite(f)) {
                return (f > 0) ? Float.MAX_VALUE : -Float.MAX_VALUE;
            }
            return f;
        }
    }


    // ---------------- doubleValue() ----------------
    @Override
    public double doubleValue() {
        // Fast zero check
        if (this.num == 0L && (this.dec.length() == 0 || this.dec.longValue() == 0L) && this.exp == 0L) {
            return 0.0;
        }

        final int DOUBLE_MAX_DIGITS = 308;
        final int DOUBLE_MIN_DIGITS = -324;

        int decLen = this.dec.length();
        int numDigits = safeDigitsCount(this.num);
        long unscaledDigits = (long) numDigits + (long) decLen;
        long shift = this.exp - decLen;

        long approxDigits = getApproxDigits(unscaledDigits, shift);

        if (approxDigits > DOUBLE_MAX_DIGITS) {
            return (this.num >= 0) ? Double.MAX_VALUE : -Double.MAX_VALUE;
        }
        if (approxDigits <= DOUBLE_MIN_DIGITS) {
            return 0.0;
        }

        BigInteger unscaled = buildSignedUnscaledBigInteger(this.num, this.dec);

        try {
            BigDecimal bd = new BigDecimal(unscaled);
            if (shift != 0L) {
                if (Math.abs(shift) > Integer.MAX_VALUE) {
                    return (this.num >= 0) ? Double.MAX_VALUE : -Double.MAX_VALUE;
                }
                bd = bd.scaleByPowerOfTen((int) shift);
            }
            double d = bd.doubleValue();
            if (Double.isInfinite(d)) {
                return (d > 0) ? Double.MAX_VALUE : -Double.MAX_VALUE;
            }
            return d;
        } catch (Exception ex) {
            if (shift > 0) {
                unscaled = unscaled.multiply(BigInteger.TEN.pow((int) shift));
            } else if (shift < 0) {
                unscaled = unscaled.divide(BigInteger.TEN.pow((int) (-shift)));
            }
            double d = unscaled.doubleValue();
            if (Double.isInfinite(d)) {
                return (d > 0) ? Double.MAX_VALUE : -Double.MAX_VALUE;
            }
            return d;
        }
    }

    // ---------------- End of value() section ----------------

    /** true if the numeric value is negative (handles cases like "-0.5" where num == 0). */
    private boolean isNegative() {
        // prefer explicit `not` string sign because parsing " -0.5 " produces num==0 but original string had '-'
        if (this.num < 0) return true;
        // if integer part was 0 but original string started with '-', treat as negative
        return this.num == 0 && this.not != null && this.not.startsWith("-");
    }

    @Override
    public int compareTo(@NotNull FloatingLong o) {
        if (this == o) return 0;
        // Fast textual equality
        if (Objects.equals(this.not, o.not)) return 0;

        // Get dec lengths & shifts
        int decLenA = this.dec.length();
        int decLenB = (o.dec == null) ? 0 : o.dec.length();

        long shiftA = this.exp - decLenA;
        long shiftB = o.exp - decLenB;

        // Build unsigned unscaled BigIntegers once (used for magnitude and for exact compare later).
        BigInteger unscaledA_unsigned = buildUnscaledBigIntegerUnsigned(this.num, this.dec);
        BigInteger unscaledB_unsigned = buildUnscaledBigIntegerUnsigned(o.num, o.dec);

        // compute significant digit counts (trim leading zeros)
        int unscaledDigitsA = unscaledA_unsigned.equals(BigInteger.ZERO) ? 1 : unscaledA_unsigned.toString().replaceFirst("^0+", "").length();
        int unscaledDigitsB = unscaledB_unsigned.equals(BigInteger.ZERO) ? 1 : unscaledB_unsigned.toString().replaceFirst("^0+", "").length();

        long magA = (long) unscaledDigitsA + shiftA;
        long magB = (long) unscaledDigitsB + shiftB;

        boolean negA = this.isNegative();
        boolean negB = o.isNegative();

        // Different signs: positive > negative
        if (negA != negB) {
            return negA ? -1 : 1;
        }

        // Same sign: compare magnitudes
        if (magA != magB) {
            if (!negA) return Long.compare(magA, magB);
            return Long.compare(magB, magA); // negative numbers: larger magnitude => smaller numeric value
        }

        // magnitudes equal -> exact compare
        // Reuse the unsigned BigIntegers we already built
        BigInteger unscaledA = unscaledA_unsigned;
        BigInteger unscaledB = unscaledB_unsigned;

        long diff = shiftA - shiftB;
        if (diff == 0L) {
            BigInteger signedA = negA ? unscaledA.negate() : unscaledA;
            BigInteger signedB = negB ? unscaledB.negate() : unscaledB;
            return signedA.compareTo(signedB);
        }

        // Guard against absurd diff
        if (Math.abs(diff) > 1000) {
            return 0;
        }

        if (diff > 0) {
            unscaledA = unscaledA.multiply(BigInteger.TEN.pow((int) diff));
        } else {
            unscaledB = unscaledB.multiply(BigInteger.TEN.pow((int) (-diff)));
        }

        if (negA) unscaledA = unscaledA.negate();
        if (negB) unscaledB = unscaledB.negate();

        return unscaledA.compareTo(unscaledB);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FloatingLong other)) return false;
        return this.compareTo(other) == 0;
    }

    @Override
    public int hashCode() {
        // canonicalize by computing absUnscaled and shift, then remove trailing zeros from unscaled and adjust shift
        int decLen = this.dec.length();
        long shift = this.exp - decLen;
        BigInteger absUnscaled = buildUnscaledBigIntegerUnsigned(this.num, this.dec);
        // remove trailing zeros: while divisible by 10, divide and increment shift
        while (!absUnscaled.equals(BigInteger.ZERO) && absUnscaled.mod(BigInteger.TEN).equals(BigInteger.ZERO)) {
            absUnscaled = absUnscaled.divide(BigInteger.TEN);
            shift++;
        }
        int hash = 1;
        hash = 31 * hash + absUnscaled.hashCode();
        hash = 31 * hash + Long.hashCode(shift);
        // include sign
        hash = 31 * hash + Long.hashCode(isNegative() ? -1L : 1L);
        return hash;
    }


    public FloatingLong getValue() {
        return this;
    }

    public FloatingLong negate() {
        if (this.not != null) {
            if (this.not.startsWith("-")) {
                return new FloatingLong(this.not.substring(1));
            } else {
                return new FloatingLong("-" + this.not);
            }
        }
        // fallback: construct from components
        StringBuilder sb = new StringBuilder();
        if (this.num == 0 && this.not != null && this.not.startsWith("-")) {
            // cover special "-0.xxx" textual case if not preserved
            sb.append('-').append("0");
        } else {
            sb.append(Long.toString(-this.num));
        }
        if (this.dec != null) sb.append('.').append(this.dec.toString());
        if (this.exp != 0L) sb.append('E').append(this.exp);
        return new FloatingLong(sb.toString());
    }

    public static boolean fitsInLong(@NotNull String str) {
        String s = str.startsWith("-") ? str.substring(1) : str;
        s = s.replaceFirst("^0+(?!$)", ""); // strip leading zeros

        // Quick length check
        if (s.length() < 19) return true;
        if (s.length() > 19) return false;

        // Now length == 19 → compare to limits
        String max = str.startsWith("-") ? "9223372036854775808" // abs(Long.MIN_VALUE)
                : "9223372036854775807";
        return s.compareTo(max) <= 0;
    }

    public static void dbgFL(String tag, FloatingLong f) {
        System.err.println(tag + ": not=" + f.toString() + "  num=" + f.num + " dec=" + (f.dec==null?"<null>":f.dec.toString()) + " exp=" + f.exp);
    }

    public FloatingLong shift(long shift) {
        // No-op
        if (shift == 0L) return this;

        // Fast zero check
        BigInteger unscaledUnsigned = buildUnscaledBigIntegerUnsigned(this.num, this.dec);
        if (unscaledUnsigned.equals(BigInteger.ZERO)) return FloatingLong.ZERO;

        // Reject absurd shifts that would allocate huge strings
        final long MAX_SHIFT_SAFE = 1_000_000L;
        if (shift > MAX_SHIFT_SAFE || shift < -MAX_SHIFT_SAFE) return this;

        // Build combined digit string (absolute)
        String decStr = this.dec.toString();
        String absNumStr = (this.num == Long.MIN_VALUE) ? "9223372036854775808"
                : Long.toString(Math.abs(this.num));
        String combined = absNumStr + decStr;
        int combinedLen = combined.length();
        if (combinedLen == 0) return this; // defensive

        // Determine new integer-digit count after shifting
        long newIntLenLong = (long) combinedLen - shift; // positive -> digits left of point inside/after combined

        // Guard unrealistic newIntLen
        if (newIntLenLong < -MAX_SHIFT_SAFE || newIntLenLong > combinedLen + MAX_SHIFT_SAFE) return this;

        String intPart;
        String fracPart;

        if (newIntLenLong > combinedLen) {
            // decimal point moved to the right of all digits -> append zeros to integer part
            int addZeros = (int) (newIntLenLong - combinedLen); // safe because capped
            if (addZeros < 0 || addZeros > 1_000_000) return this; // safety
            intPart = combined + "0".repeat(addZeros);
            fracPart = "";
        } else if (newIntLenLong > 0) {
            // decimal point inside combined digits
            int newIntLen = (int) newIntLenLong; // safe: newIntLenLong in [1, combinedLen]
            intPart = combined.substring(0, newIntLen);
            fracPart = combined.substring(newIntLen);
        } else {
            // newIntLenLong <= 0: decimal point to left of all digits
            int leadingZeros = (int) (-newIntLenLong); // number of zeros before combined in fraction
            if (leadingZeros < 0 || leadingZeros > 1_000_000) return this; // safety
            intPart = "0";
            fracPart = "0".repeat(leadingZeros) + combined;
        }

        // Normalize: remove leading zeros on integer part (leave at least "0")
        intPart = intPart.replaceFirst("^0+(?!$)", "");

        // Trim trailing zeros in fractional part
        fracPart = fracPart.replaceFirst("0+$", "");

        // New textual exponent
        long newExp = this.exp + shift;

        // Prepare signed integer candidate for fitsInLong check
        String signPrefix = this.isNegative() ? "-" : "";
        String signedIntCandidate = signPrefix + (intPart.isEmpty() ? "0" : intPart);

        // If integer part won't fit in a long, move low-order integer digits into fractional part
        while (!fitsInLong(signedIntCandidate)) {
            if (intPart.length() <= 1) return this; // can't make it fit safely
            int keep = Math.min(18, intPart.length());
            String left = intPart.substring(0, keep);
            String moved = intPart.substring(keep);
            intPart = left;
            fracPart = moved + fracPart;
            signedIntCandidate = signPrefix + intPart;
        }

        // Ensure exponent is representable textually
        if (!fitsInLong(Long.toString(newExp))) return this;

        // Trim fractional component if it's too large to be represented
        while (!fracPart.isEmpty() && !fitsInLong(fracPart)) {
            fracPart = fracPart.substring(0, fracPart.length() - 1);
        }

        // Compose result string
        StringBuilder sb = new StringBuilder();
        if (this.isNegative()) sb.append('-');
        sb.append(intPart.isEmpty() ? "0" : intPart);
        if (!fracPart.isEmpty()) sb.append('.').append(fracPart);
        if (newExp != 0L) sb.append('E').append(newExp);

        try {
            return FloatingLong.create(sb.toString());
        } catch (Exception ex) {
            return this;
        }
    }

    public static byte isSafe(long a, long b) {
        if ((b > 0) && (a > Long.MAX_VALUE - b)) {
            return 1;
        }
        if ((b < 0) && (a < Long.MIN_VALUE - b)) {
            return -1;
        }
        return 0;
    }
    public static byte isSafe(int a, int b) {
        if ((b > 0) && (a > Integer.MAX_VALUE - b)) {
            return 1;
        }
        if ((b < 0) && (a < Integer.MIN_VALUE - b)) {
            return -1;
        }
        return 0;
    }
    public static byte isSafe(float a, float b) {
        if ((b > 0) && (a > Float.MAX_VALUE - b)) {
            return 1;
        }
        if ((b < 0) && (a < -Float.MAX_VALUE - b)) {
            return -1;
        }
        return 0;
    }
    public static byte isSafe(double a, double b) {
        if ((b > 0) && (a > Double.MAX_VALUE - b)) {
            return 1;
        }
        if ((b < 0) && (a < -Double.MAX_VALUE - b)) {
            return -1;
        }
        return 0;
    }

    /* ===================== Arithmetic ===================== */

    private static final int ADD_ALIGN_THRESHOLD = 19; // if shifts differ by > this many digits, larger operand dominates
    private static final int DIV_SCALE = 30; // decimal places to compute for divide (adjust as desired)

    public FloatingLong add(FloatingLong other) {
        // Fast path: zero handling
        if (this.num == 0 && (this.dec == null || this.dec.longValue() == 0) && this.exp == 0) return other;
        if (other.num == 0 && (other.dec == null || other.dec.longValue() == 0) && other.exp == 0) return this;

        // Get decimal lengths
        int decLenA = this.dec.length();
        int decLenB = (other.dec == null) ? 0 : other.dec.length();

        // Compute shifts (exp - decLen) and unscaled digit counts
        long shiftA = this.exp - decLenA;
        long shiftB = other.exp - decLenB;

        int numDigitsA = safeDigitsCount(this.num);
        int numDigitsB = safeDigitsCount(other.num);
        long unscaledDigitsA = (long) numDigitsA + decLenA;
        long unscaledDigitsB = (long) numDigitsB + decLenB;


        // magnitude measure: approximate number of integer digits in the value
        long magA = 0L;
        switch(isSafe(unscaledDigitsA, shiftA)) {

            case -1 -> {

                magA = Long.MIN_VALUE;
            }
            case 0  -> {

                magA = unscaledDigitsA + shiftA;
            }
            case 1  -> {

                magA = Long.MAX_VALUE;
            }
        }
        long magB = 0L;
        switch(isSafe(unscaledDigitsB, shiftB)) {

            case -1 -> {

                magB = Long.MIN_VALUE;
            }
            case 0  -> {

                magB = unscaledDigitsB + shiftB;
            }
            case 1  -> {

                magB = Long.MAX_VALUE;
            }
        }
//        System.err.println("A: unS: " + unscaledDigitsA + " - sA: " + shiftA + ", B: unS: " + unscaledDigitsB + " - sB: " + shiftB + " : results = [magA: " + magA + "], [magB: " + magB + "]");

        long diffShift = Math.abs(shiftA - shiftB);

        // If exponents (shifts) differ by a lot, the larger-magnitude operand dominates.
        // Use ADD_ALIGN_THRESHOLD and compare *overall* magnitude (not just shift).
        if (diffShift > ADD_ALIGN_THRESHOLD) {
            // If magnitudes are different, return the larger-magnitude operand.
            if (magA > magB) return this;
            if (magB > magA) return other;
            // If magnitudes somehow equal (very unlikely with huge diff), fall through to exact add.
        }

        // Align both to the smaller (more-negative) shift so we can add integer unscaled values.
        // Choosing the minimum shift avoids creating fractional unscaled values.
        long commonShift = Math.min(shiftA, shiftB);

        BigInteger unscaledA = buildUnscaledBigIntegerUnsigned(this.num, this.dec);
        BigInteger unscaledB = buildUnscaledBigIntegerUnsigned(other.num, other.dec);

        // apply signs
        if (this.isNegative()) unscaledA = unscaledA.negate();
        if (other.isNegative()) unscaledB = unscaledB.negate();

        // If an operand has a larger shift than commonShift, multiply it by 10^(shiftX - commonShift)
        // so its integer representation matches the common shift.
        if (shiftA > commonShift) {
            unscaledA = unscaledA.multiply(BigInteger.TEN.pow((int) (shiftA - commonShift)));
        }
        if (shiftB > commonShift) {
            unscaledB = unscaledB.multiply(BigInteger.TEN.pow((int) (shiftB - commonShift)));
        }

        BigInteger resultUnscaled = unscaledA.add(unscaledB);
        if (resultUnscaled.signum() == 0) {
            // Avoid returning weird textual zero forms; use canonical ZERO
            return FloatingLong.ZERO;
        }

        // Convert absolute result into textual components
        boolean resultNeg = resultUnscaled.signum() < 0;
        BigInteger absResult = resultUnscaled.abs();
        String raw = absResult.toString(); // decimal digits, most-significant first

        long resultNum;
        LeadingLong resultDec = new LeadingLong(0);

        // If too many digits, put lower-order digits into decimal part (preserve precision)
        if (raw.length() > 18) {
            String numPart = raw.substring(0, 18);
            String decPart = raw.substring(18);
            resultNum = Long.parseLong(numPart);
            // trim trailing zeros if you want (optional)
            String decFinal = decPart.replaceFirst("0+$", "");
            resultDec = decFinal.isEmpty() ? new LeadingLong(0) : new LeadingLong(decFinal);

            // The number of digits moved into fractional part:
            int movedDigits = decFinal.length();
            commonShift += movedDigits;           // <<--- IMPORTANT: raise exponent
        } else {
            resultNum = Long.parseLong(raw);
        }

        // If the computed result should be negative, reapply sign to `num`.
        if (resultNeg) resultNum = -resultNum;

        // Use commonShift as the exponent (this mirrors the previous logic — result numeric value =
        // (resultNum[.resultDec]) * 10^commonShift ).
        return FloatingLong.create(resultNum, resultDec, commonShift);
    }

    public FloatingLong subtract(FloatingLong other) {

        return this.add(other.negate());
    }

//    multiplication/division seems to be the only issues left
//    MAX_VALUE / 2 returns ~0.46, for instance.

    public FloatingLong multiply(double other) {
        // create a FloatingLong from the exact double representation
        return this.multiply(FloatingLong.create(BigDecimal.valueOf(other).toPlainString()));
    }

    // helper: convert current FloatingLong to BigDecimal exactly (using internal unscaled + scale)
    private BigDecimal toBigDecimal() {
        int decLen = this.dec.length();
        BigInteger unscaled = buildSignedUnscaledBigInteger(this.num, this.dec);
        // value = unscaled * 10^(exp - decLen)
        // BigDecimal(unscaledVal, scale) represents unscaledVal * 10^-scale
        // so choose scale = decLen - exp  (which may be negative)
        int scale = decLen - (int) this.exp; // cast ok if exponents are reasonable
        return new BigDecimal(unscaled, scale);
    }

    public FloatingLong multiply(FloatingLong other) {
        if (other == null) throw new IllegalArgumentException("other cannot be null");

        // Fast zero check
        if ((this.num == 0 && (this.dec == null || this.dec.length() == 0 || this.dec.longValue() == 0L) && this.exp == 0L) ||
                (other.num == 0 && (other.dec == null || other.dec.length() == 0 || other.dec.longValue() == 0L) && other.exp == 0L)) {
            return FloatingLong.ZERO;
        }

        // Use BigDecimal for robust decimal multiplication
        BigDecimal a = this.toBigDecimal();
        BigDecimal b = other.toBigDecimal();

        // Choose precision large enough to avoid truncation artifacts (adjust if needed)
        final int PREC = 4;
        MathContext mc = new MathContext(PREC, RoundingMode.HALF_UP);

        BigDecimal prod = a.multiply(b, mc);

        // convert to plain string (no scientific notation), strip trailing zeros sensibly
        BigDecimal normalized = prod.stripTrailingZeros();

        // Use toPlainString so FloatingLong.create(...) gets decimal digits (no E notation)
        String out = normalized.toString();

        // create a FloatingLong from the decimal string. Your create(String) already parses BigDecimal-style input.
        return FloatingLong.create(out);
    }

//    public FloatingLong multiply(FloatingLong other) {
//        if (other == null) throw new IllegalArgumentException("other cannot be null");
//
//        // fast zero check (exact)
//        if ((this.num == 0 && (this.dec == null || this.dec.length() == 0 || this.dec.longValue() == 0L) && this.exp == 0L) ||
//                (other.num == 0 && (other.dec == null || other.dec.length() == 0 || other.dec.longValue() == 0L) && other.exp == 0L)) {
//            return FloatingLong.ZERO;
//        }
//
//        int decLenA = this.dec.length();
//        int decLenB = (other.dec == null) ? 0 : other.dec.length();
//
//        long shiftA = this.exp - decLenA;
//        long shiftB = other.exp - decLenB;
//
//        long resultShift = shiftA + shiftB; // keep as-is, no normalization
//
//        BigInteger unscaledA = buildSignedUnscaledBigInteger(this.num, this.dec);
//        BigInteger unscaledB = buildSignedUnscaledBigInteger(other.num, other.dec);
//
//        // preserve sign, work with absolute values
//        boolean negative = (unscaledA.signum() < 0) ^ (unscaledB.signum() < 0);
//        BigInteger absA = unscaledA.abs();
//        BigInteger absB = unscaledB.abs();
//
//        BigInteger product = absA.multiply(absB);
//
//        if (product.equals(BigInteger.ZERO)) {
//            return FloatingLong.ZERO;
//        }
//
//        // Convert product into (num, dec) using same 18-digit split as other methods
//        String pStr = product.toString();
//        long resultNum;
//        LeadingLong resultDec = null;
//
//        if (pStr.length() > 18) {
//            String numPart = pStr.substring(0, 18);
//            String decPart = pStr.substring(18);
//            if(decPart.length() > 18)
//                decPart = decPart.substring(0, 18);
//            resultNum = Long.parseLong(numPart);
//
//            // trim trailing zeros from decimal part for cleanliness (same behavior as divide)
//            String decFinal = decPart.replaceFirst("0+$", "");
//            if (!decFinal.isEmpty()) resultDec = new LeadingLong(decFinal);
//        } else {
//            resultNum = Long.parseLong(pStr);
//        }
//
//        // reapply sign (LeadingLong is always positive digits)
//        if (negative) resultNum = -resultNum;
//
//        return FloatingLong.create(resultNum, resultDec, resultShift);
//    }


    public FloatingLong divide(double other) {
        // create a FloatingLong from the exact double representation
        return this.divide(FloatingLong.create(BigDecimal.valueOf(other).toPlainString()));
    }

    public FloatingLong divide(FloatingLong other) {
        if (other == null) throw new IllegalArgumentException("other cannot be null");

        // divide-by-zero detection (exact)
        if (other.num == 0 && (other.dec == null || other.dec.length() == 0 || other.dec.longValue() == 0L) && other.exp == 0L) {
            throw new ArithmeticException("Division by zero");
        }

        // zero / anything -> zero
        if (this.num == 0 && (this.dec == null || this.dec.length() == 0 || this.dec.longValue() == 0L) && this.exp == 0L) {
            return FloatingLong.ZERO;
        }

        int decLenA = this.dec.length();
        int decLenB = (other.dec == null) ? 0 : other.dec.length();

        long shiftA = this.exp - decLenA;
        long shiftB = other.exp - decLenB;

        long resultShift = shiftA - shiftB; // keep this as-is (no normalization)

        BigInteger unscaledA = buildSignedUnscaledBigInteger(this.num, this.dec);
        BigInteger unscaledB = buildSignedUnscaledBigInteger(other.num, other.dec);

        // Preserve sign for later; work with absolute values for division
        boolean negative = (unscaledA.signum() < 0) ^ (unscaledB.signum() < 0);
        BigInteger absA = unscaledA.abs();
        BigInteger absB = unscaledB.abs();

        // integer quotient and remainder (truncating toward zero)
        BigInteger quotient = absA.divide(absB);
        BigInteger remainder = absA.remainder(absB);

        // If quotient is zero and no remainder -> exact zero
        if (quotient.equals(BigInteger.ZERO) && remainder.equals(BigInteger.ZERO)) {
            return FloatingLong.ZERO;
        }

        // Convert quotient -> (num, dec) without changing resultShift.
        // Keep up to 18 digits in `num` (like add() logic), put the rest into `dec`.
        String qStr = quotient.toString(); // decimal digits, most-significant first
        long resultNum;
        LeadingLong resultDec = new LeadingLong(0);

        // If quotient has more than 18 digits, steal the low-order digits into decimal "mantissa"
        if (qStr.length() > 18) {
            // first 18 digits form the integer `num`; the rest become the start of the decimal digits
            String numPart = qStr.substring(0, 18);
            String decPart = qStr.substring(18);
            if(decPart.length() > 18)
                decPart = decPart.substring(0, 18);
            resultNum = Long.parseLong(numPart);
            StringBuilder decBuilder = new StringBuilder(decPart);

            // Append some fractional digits computed from remainder to increase useful precision.
            int extraDigits = DIV_SCALE; // using DIV_SCALE constant (30) - adjust as desired.
            for (int i = 0; i < extraDigits && !remainder.equals(BigInteger.ZERO); i++) {
                remainder = remainder.multiply(BigInteger.TEN);
                BigInteger digit = remainder.divide(absB);
                decBuilder.append(digit.toString());
                remainder = remainder.remainder(absB);
            }

            // Trim trailing zeros (optional) — keep as LeadingLong expects the exact digits.
            String decFinal = decBuilder.toString().replaceFirst("0+$", "");
            if (!decFinal.isEmpty()) resultDec = new LeadingLong(decFinal);
        } else {
            // quotient fits within 18 digits -> put it entirely into `num`, compute fractional digits from remainder
            resultNum = Long.parseLong(qStr);

            if (!remainder.equals(BigInteger.ZERO)) {
                StringBuilder decBuilder = new StringBuilder();
                int extraDigits = DIV_SCALE; // e.g. 30 decimal digits
                for (int i = 0; i < extraDigits && !remainder.equals(BigInteger.ZERO); i++) {
                    remainder = remainder.multiply(BigInteger.TEN);
                    BigInteger digit = remainder.divide(absB);
                    decBuilder.append(digit.toString());
                    remainder = remainder.remainder(absB);
                }
                String decFinal = decBuilder.toString().replaceFirst("0+$", "");
                if (!decFinal.isEmpty()) resultDec = new LeadingLong(decFinal);
            }
        }

        // Reapply sign to resultNum (LeadingLong must be positive digits only)
        if (negative) resultNum = -resultNum;

        return FloatingLong.create(resultNum, resultDec, resultShift);
    }
}
