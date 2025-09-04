package com.hbm.nucleartech.util;

import com.hbm.nucleartech.capability.energy.WattHourStorage;
import com.ibm.icu.impl.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

//import static com.hbm.nucleartech.util.FloatingLong.shift;
import static com.hbm.nucleartech.block.custom.base.BaseHbmBlockEntity.WATT_HOUR_TO_FE;
import static com.hbm.nucleartech.util.FloatingLong.dbgFL;
import static org.junit.jupiter.api.Assertions.*;

class FloatingLongEdgeCasesTest {

    /* ---------------- Parsing / construction / toString ---------------- */

    @Test
    void parseSimpleIntegersAndToString() {
        FloatingLong a = FloatingLong.create("0");
        FloatingLong b = FloatingLong.create("123");
        FloatingLong c = FloatingLong.create("-9876543210");

        assertEquals("0.0E0", a.toString());
        assertEquals("123.0E0", b.toString());
        assertEquals("-9876543210.0E0", c.toString());
    }

    @Test
    void parseFractionalFormatsAndTrailingDot() {
        FloatingLong a = FloatingLong.create("123.456");
        FloatingLong b = FloatingLong.create("123.");   // treated by code as dec "0"
        assertEquals(123, a.intValue());
        assertEquals(123, b.intValue());
        // ensure decimal preservation: b should include ".0" in its canonical form based on constructor logic
        assertTrue(b.toString().startsWith("123"));
    }

    @Test
    void parseWithExponentUpperAndLowerCase() {
        FloatingLong a = FloatingLong.create("1.23E3");
        FloatingLong b = FloatingLong.create("1.23e3");
        assertEquals(0, a.compareTo(b));
        assertEquals(1230, a.intValue());
    }

//    @Test
//    void invalidFormatsShouldThrow() {
//        assertThrows(IllegalArgumentException.class, () -> FloatingLong.create(""));    // empty
//        assertThrows(IllegalArgumentException.class, () -> FloatingLong.create((String) null)); // null
//        // ".5" is invalid for the regex (requires leading digit)
//        assertThrows(IllegalArgumentException.class, () -> FloatingLong.create(".5"));
//        // letters
//        assertThrows(IllegalArgumentException.class, () -> FloatingLong.create("12a34"));
//    }

    /* ---------------- sign and -0 handling ---------------- */

    @Test
    void negativeZeroFractionSignBehavior() {
        FloatingLong negHalf = FloatingLong.create("-0.5");
        FloatingLong posHalf = FloatingLong.create("0.5");

        assertTrue(negHalf.compareTo(posHalf) < 0, "-0.5 should be less than 0.5");

        // negation of negative should produce positive and vice versa
        assertEquals(0, negHalf.negate().compareTo(FloatingLong.create("0.5")));
        assertEquals(0, posHalf.negate().compareTo(FloatingLong.create("-0.5")));
    }

    @Test
    void negatePreservesMinusZeroTextualCase() {
        FloatingLong a = FloatingLong.create("-0.123");
        FloatingLong neg = a.negate();
        assertEquals(0, neg.compareTo(FloatingLong.create("0.123")));
    }

    /* ---------------- equals / hashCode / compareTo invariants ---------------- */

    @Test
    void equalsHashCodeReflexiveSymmetricTransitive() {
        FloatingLong a = FloatingLong.create("1000");
        FloatingLong b = FloatingLong.create("1.00E3");
        FloatingLong c = FloatingLong.create("10000E-1"); // also 1000

        // reflexive
        assertTrue(a.equals(a));
        // symmetric
        assertTrue(a.equals(b) && b.equals(a));
        // transitive
        assertTrue(a.equals(b) && b.equals(c) && a.equals(c));

        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(b.hashCode(), c.hashCode());
    }

    @Test
    void compareMagnitudeVsSign() {
        FloatingLong negBig = FloatingLong.create("-1E20");
        FloatingLong posSmall = FloatingLong.create("1");
        assertTrue(negBig.compareTo(posSmall) < 0);
    }

    /* ---------------- fitsInLong / isSafe / utility ---------------- */

    @Test
    void fitsInLongEdgeCases() {
        assertTrue(FloatingLong.fitsInLong("9223372036854775807")); // Long.MAX_VALUE
        assertTrue(FloatingLong.fitsInLong("-9223372036854775808")); // abs(Long.MIN_VALUE)
        assertFalse(FloatingLong.fitsInLong("9223372036854775808")); // just over
        assertFalse(FloatingLong.fitsInLong("-9223372036854775809")); // just under
    }

    @Test
    void isSafeOverflowDetection() {
        // a + b overflow check approximation
        assertEquals(1, FloatingLong.isSafe(Long.MAX_VALUE - 1, 10)); // would overflow
        assertEquals(0, FloatingLong.isSafe(100, 200));
        assertEquals(-1, FloatingLong.isSafe(Long.MIN_VALUE + 1, -10));
    }

    /* ---------------- intValue / longValue truncation & clamping ---------------- */

    @Test
    void intValueTruncationAndClamping() {
        assertEquals(1234, FloatingLong.create("1234.99").intValue());
        assertEquals(-1234, FloatingLong.create("-1234.99").intValue());

        // clamp to Integer.MAX_VALUE
        assertEquals(Integer.MAX_VALUE, FloatingLong.create("1E20").intValue());
        // tiny magnitude -> zero
        assertEquals(0, FloatingLong.create("1E-20").intValue());
    }

    @Test
    void longValueTruncationAndClamping() {
        assertEquals(123456789012345L, FloatingLong.create("123456789012345.999").longValue());
        assertEquals(Long.MAX_VALUE, FloatingLong.create("1E100").longValue());
        assertEquals(Long.MIN_VALUE, FloatingLong.create("-1E100").longValue());
    }

    /* ---------------- float/double conversion edge behaviors ---------------- */

    @Test
    void floatAndDoubleClampingTiny() {
        assertEquals(Float.MAX_VALUE, FloatingLong.create("1E50").floatValue());
        assertEquals(Double.MAX_VALUE, FloatingLong.create("1E400").doubleValue());

        // very tiny values -> treated as 0.0
        assertEquals(0.0, FloatingLong.create("1E-400").doubleValue());
        assertEquals(0.0f, FloatingLong.create("1E-1000").floatValue());
    }

    /* ---------------- add / subtract behavior ---------------- */

    @Test
    void addDominanceWhenShiftsDifferGreatly() {
        FloatingLong huge = FloatingLong.create("1E50");
        FloatingLong small = FloatingLong.create("1.0E10");
        // huge + small == huge (small ignored)
        assertEquals(0, huge.add(small).compareTo(huge));
        // small + huge == huge
        assertEquals(0, small.add(huge).compareTo(huge));
    }

    @Test
    void addExactSameMagnitude() {
        FloatingLong a = FloatingLong.create("123.45");
        FloatingLong b = FloatingLong.create("876.55");
        assertEquals(0, a.add(b).compareTo(FloatingLong.create("1000.0")));
    }

    @Test
    void subtractBasic() {
        FloatingLong a = FloatingLong.create("5.75");
        FloatingLong b = FloatingLong.create("2.25");
        assertEquals(0, a.subtract(b).compareTo(FloatingLong.create("3.5")), "result: " + a.subtract(b));
    }

    /* ------------- convert to other values ------------- */

    @Test
    void shredderMath() {

        var capacity = FloatingLong.create(1.0E3);
        var maxReceive = FloatingLong.create(1.0E2);
        var maxExtract = FloatingLong.create(1.0E3);
        var initialCapacity = FloatingLong.create(9.0E2);

        var input = FloatingLong.create(50E3);

        dbgFL("initialCapacity", initialCapacity);
        dbgFL("maxExtract", maxExtract);

        var translateWatts = input.divide(1000).divide(20);
        dbgFL("translateWatts", translateWatts);

        var wattHoursReceived = WattHourStorage.Min(capacity.subtract(initialCapacity), WattHourStorage.Min(maxReceive, translateWatts));

//        assertEquals(0, initialCapacity.add(wattHoursReceived).compareTo(FloatingLong.create(902.5)));

        FloatingLong inner = WattHourStorage.Min(maxExtract, translateWatts);
        dbgFL("inner(min)", inner);

        wattHoursReceived = WattHourStorage.Min(initialCapacity, inner);
        dbgFL("wattHoursReceived", wattHoursReceived);

        initialCapacity = initialCapacity.subtract(wattHoursReceived);
        dbgFL("initialCapacity", initialCapacity);

        inner = WattHourStorage.Min(maxExtract, translateWatts);
        dbgFL("inner(min)", inner);

        wattHoursReceived = WattHourStorage.Min(initialCapacity, inner);
        dbgFL("wattHoursReceived", wattHoursReceived);

        initialCapacity = initialCapacity.subtract(wattHoursReceived);
        dbgFL("initialCapacity", initialCapacity);

        inner = WattHourStorage.Min(maxExtract, translateWatts);
        dbgFL("inner(min)", inner);

        wattHoursReceived = WattHourStorage.Min(initialCapacity, inner);
        dbgFL("wattHoursReceived", wattHoursReceived);

        initialCapacity = initialCapacity.subtract(wattHoursReceived);
        dbgFL("initialCapacity", initialCapacity);

        inner = WattHourStorage.Min(maxExtract, translateWatts);
        dbgFL("inner(min)", inner);

        wattHoursReceived = WattHourStorage.Min(initialCapacity, inner);
        dbgFL("wattHoursReceived", wattHoursReceived);

        initialCapacity = initialCapacity.subtract(wattHoursReceived);
        dbgFL("initialCapacity", initialCapacity);

        assertEquals(0, initialCapacity.compareTo(FloatingLong.create(890)));

        assertEquals(FloatingLong.create(711.111111111111111111).toString(), FloatingLong.create(639.999999999999999999).add(FloatingLong.create(71.111111111111111111)).toString(), "result: " + FloatingLong.create(639.999999999999999999).add(FloatingLong.create(71.111111111111111111)).toString());

        assertEquals(0, FloatingLong.create(2000).divide(WATT_HOUR_TO_FE).multiply(WATT_HOUR_TO_FE).compareTo(FloatingLong.create(2000)), "result: " + FloatingLong.create(2000).divide(WATT_HOUR_TO_FE).multiply(WATT_HOUR_TO_FE));
    }

    /* ---------------- multiply / divide ---------------- */

    @Test
    void multiplyBasicCases() {
        // multiply basic -> 2.5 * 4 = 10
//        System.out.println("multiply basic -> 2.5 * 4 = 10");
        FloatingLong r = FloatingLong.create("2.5").multiply(FloatingLong.create("4"));
        assertEquals(0, r.compareTo(FloatingLong.create("10")));
        // sign handling
//        System.out.println("sign handling");
        assertEquals(0, FloatingLong.create("-2").multiply(FloatingLong.create("3")).compareTo(FloatingLong.create("-6")));
        assertEquals(0, FloatingLong.create("-2").multiply(FloatingLong.create("-3")).compareTo(FloatingLong.create("6")));
        // integer * integer
//        System.out.println("int * int");
        assertEquals(0, FloatingLong.create("123456789").multiply(FloatingLong.create("0")).compareTo(FloatingLong.ZERO));
    }

    @Test
    void multiplyLargeExponents() {
        FloatingLong a = FloatingLong.create("1E200");
        FloatingLong b = FloatingLong.create("1E150");
        FloatingLong product = a.multiply(b);
        // product should be 1E350; doubleValue will clamp but compareTo should see exponent
        assertEquals(0, product.compareTo(FloatingLong.create("1E350")));
    }

    @Test
    void divideBasicValidationAndBehavior() {
        // divide by >1 accepted
        FloatingLong a = FloatingLong.create("10");
        FloatingLong out = a.divide(2.0);
        assertEquals(0, out.compareTo(FloatingLong.create("5")));

        // divide by <= 1 should throw according to code's guard
//        assertThrows(IllegalArgumentException.class, () -> a.divide(1.0));
//        assertThrows(IllegalArgumentException.class, () -> a.divide(0.5));
//        assertThrows(IllegalArgumentException.class, () -> a.divide(-1.0));
    }

    /* ---------------- MAX/MIN behaviours and absurd cases ---------------- */

    @Test
    void maxMinRoundtripAndArithmetic() {
        FloatingLong max = FloatingLong.MAX_VALUE;
        FloatingLong min = FloatingLong.MIN_VALUE;

        // Adding max to zero yields max
        assertEquals(0, max.add(FloatingLong.ZERO).compareTo(max));
        // Subtracting max from max yields zero
        assertEquals(0, max.subtract(max).compareTo(FloatingLong.ZERO));

        // Dividing MAX by 2 and adding small should be equal to the divided result per existing tests
        FloatingLong half = max.divide(2.0);
        FloatingLong small = FloatingLong.create("3E303");
        FloatingLong sum = half.add(small);
        assertEquals(0, sum.compareTo(half),  half + " : " + small + " : " + sum);
    }

    @Test
    void arithmeticWithOppositeSigns() {
        assertEquals(0, FloatingLong.create("100").add(FloatingLong.create("-50")).compareTo(FloatingLong.create("50")));
        assertEquals(0, FloatingLong.create("-100").add(FloatingLong.create("50")).compareTo(FloatingLong.create("-50")));
    }

    /* ---------------- hashCode canonicalization specifics ---------------- */

    @Test
    void hashCodeIgnoresTrailingZerosByDesign() {
        FloatingLong a = FloatingLong.create("1000.0");
        FloatingLong b = FloatingLong.create("1.000E3");
        assertEquals(0, a.compareTo(b));
        assertEquals(a.hashCode(), b.hashCode());
    }

    /* ---------------- Very small/very large decimal manipulation ---------------- */

    @Test
    void tinyNumbersUnderflowToZeroForDouble() {
        FloatingLong tiny = FloatingLong.create("1E-400");
        assertEquals(0.0, tiny.doubleValue());
    }

    @Test
    void extremelyLargeNumbersClampToMax() {
        FloatingLong huge = FloatingLong.create("9.9E9999");
        dbgFL("huge", huge);
        assertEquals(Double.MAX_VALUE, huge.doubleValue());
        assertEquals(Float.MAX_VALUE, huge.floatValue());
    }

    /* ---------------- compareTo edge cases ---------------- */

    @Test
    void compareToWhenMagnitudesEqualButDifferentShifts() {
        FloatingLong a = FloatingLong.create("100");
        FloatingLong b = FloatingLong.create("1E2");
        assertEquals(0, a.compareTo(b));

        FloatingLong c = FloatingLong.create("1000");
        FloatingLong d = FloatingLong.create("1E3");
        assertEquals(0, c.compareTo(d));
    }

    /* ---------------- Basic sanity: ZERO & identity ---------------- */

    @Test
    void zeroIdentityAndOperations() {
        assertEquals(0, FloatingLong.ZERO.compareTo(FloatingLong.create("0")));
        assertEquals(0, FloatingLong.ZERO.add(FloatingLong.ZERO).compareTo(FloatingLong.ZERO));
        assertEquals(0, FloatingLong.ZERO.multiply(FloatingLong.create("12345")).compareTo(FloatingLong.ZERO));
    }

    /* ---------------- string canonicality / create overloads ---------------- */

    @Test
    void createFromPrimitivesAndStringsAgree() {
        FloatingLong fromLong = FloatingLong.create(123L);
        FloatingLong fromInt = FloatingLong.create(123);
        FloatingLong fromString = FloatingLong.create("123");
        assertEquals(0, fromLong.compareTo(fromInt));
        assertEquals(0, fromInt.compareTo(fromString));

        FloatingLong fromDouble = FloatingLong.create(123.0);
        assertEquals(0, fromString.compareTo(fromDouble));
    }

    /* ---------------- ensure divide/multiply handle exponent normalization ---------------- */

    @Test
    void divideProducesNormalizedExponentAndMantissa() {
        FloatingLong a = FloatingLong.create(1.25E3); // 1250
        FloatingLong res = a.divide(2.5);               // expecting 500
        assertEquals(0, res.compareTo(FloatingLong.create(500)));
    }

    @Test
    void multiplyProducesNormalizedExponentAndMantissa() {
        FloatingLong a = FloatingLong.create("0.5E3"); // 500
        FloatingLong res = a.multiply(FloatingLong.create(2.5));               // expecting 1250
        assertEquals(0, res.compareTo(FloatingLong.create("1250")));
    }

    /* ---------------- sanity check for implementation gaps (expected failing tests warn) ---------------- */

    @Test
    void multiplyWithHugeOperandsKeepsExponentAdditive() {
        FloatingLong a = FloatingLong.create("9E150");
        FloatingLong b = FloatingLong.create("9E160");
        FloatingLong prod = a.multiply(b); // expected 81E310 -> normalized to 8.1E311
        assertEquals(0, prod.compareTo(FloatingLong.create("8.1E311")));
    }
}
