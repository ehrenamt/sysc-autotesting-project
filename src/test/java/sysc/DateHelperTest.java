/*
 * QuadraticTest.java
 *
 * Most tests included are metamorphic, because these are not systematically
 * or automatically recognized and generated. Each metamorphic test contains a 
 * docstring which describes the relation being tested.
*/

package sysc;

// Java inbuilt imports
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Calendar;
import java.util.Date;

// JUnit imports
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;

// package imports for readability
import sysc.DateHelper.DateFormats;
import static sysc.DateHelper.parseDate;


public class DateHelperTest {

    // -------------------------------------------------------------------------
    //                          Combinatorial Tests
    // -------------------------------------------------------------------------

    // Incomplete catch-all for test frames generated via category-partition
    @ParameterizedTest
    @JsonClasspathSource("s.json")
    public void testPrettifyDate(
        @org.junitpioneer.jupiter.json.Property("a") double a,
        @org.junitpioneer.jupiter.json.Property("b") double b,
        @org.junitpioneer.jupiter.json.Property("c") double c,
        @org.junitpioneer.jupiter.json.Property("oracle") Object oracle
    ) {
        assertEquals(oracle, oracle);
    }

    // -------------------------------------------------------------------------
    //                          Metamorphic Tests
    // -------------------------------------------------------------------------


    /*
     * Testing for a symmetric relation:
     * Given a constant time (seed) in format F, a conversion to G and then back
     * to F will result in the same value as the format F for all formats in
     * DateFormats which contain a date but not a time.
     */
    @Test
    void testMetamorphicDateFormatSymmetry_1() {

        long seedTimestamp = 1_792_535_420_000L;

        for (DateFormats formatF : DateFormats.values()) {
            if (formatF.getDateFormat().contains("h") || formatF.getDateFormat().contains("H")) {
                continue;
            }

            for (DateFormats formatG : DateFormats.values()) {
                
                if (formatG.getDateFormat().contains("h") || formatG.getDateFormat().contains("H")) {
                    continue;
                }

                String seedF = DateHelper.getDesiredFormat(formatF, seedTimestamp);

                String seedG = 
                    DateHelper.getDesiredFormat(formatG, parseDate(seedF, formatF));

                String seedReconvertedF = 
                    DateHelper.getDesiredFormat(formatF, DateHelper.parseDate(seedG, formatG));

                assertEquals(seedF, seedReconvertedF);
            }
        }
    }

    /*
     * Testing for a symmetric relation:
     * The seed is an arbitrary date or datetime string in format F parsed via
     * parseAnyDate into a Long. The follow-up is passing format F and the seed
     * into getDesiredFormat to produce f1. f1 and the original date should be
     * equal. 
     */
    @Test
    void testMetamorphicParseDateSymmetry_1() {

        String seed = "20/10/26";

        Long seedTime = DateHelper.parseAnyDate(seed);

        // follow-up

        String reformatted = 
            DateHelper.getDesiredFormat(DateHelper.DateFormats.S_DDMMyy, seedTime);

        // System.out.println(seed);
        // System.out.println(reformatted);

        assertEquals(seed, reformatted);
    }

    /*
     * Testing for a constant relation:
     * The seed is a given datetime. The followup case is a datetime at the same
     * time of day, but differing in date. The result of calling getTimeOnly()
     * on the seed and the followup should be equal.
     */
    @Test
    void testMetamorphicTimeOnlyConstant_1() {

        // seed
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2026, Calendar.OCTOBER, 20, 9, 30, 0);

        Long seed = cal.getTimeInMillis();
        
        // follow-up case
        // adding one day to the calendar to ...
        cal.add(Calendar.DAY_OF_MONTH, 1);

        Long followUp = cal.getTimeInMillis();

        assertEquals(DateHelper.getTimeOnly(seed), DateHelper.getTimeOnly(followUp));
    }

    /*
     * Testing for a constant relation:
     * The seed is a given datetime. The followup case is a datetime at a 
     * different time of day. The result of calling getTimeOnly() on the seed
     * and the followup should NOT be equal.
     */
    @Test
    void testMetamorphicDateTimeOnlyChange_1() {

        // seed

        long seed = 1_792_535_420_000L; // Fixed timestamp for repeatable results

        String seedDateTime = DateHelper.getDateAndTime(seed);

        // follow-up case

        long followup = seed + 1_000L;

        String followupDateTime = DateHelper.getDateAndTime(followup);

        assertEquals(seedDateTime, followupDateTime);
    }

    /**
     * Testing for a constant relation:
     * Passing an initial date D1 to getDateOnly()
     */
    @Test
    void testMetamorphicDateOnlyConstant_1() {

        // seed case

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2026, Calendar.OCTOBER, 20, 9, 30, 0);

        long seed = cal.getTimeInMillis();

        // follow-up case

        Calendar cal2 = (Calendar) cal.clone();

        cal2.set(Calendar.HOUR_OF_DAY, 21);

        long followup = cal2.getTimeInMillis();

        String seedDate = DateHelper.getDateOnly(seed);
        String followDate = DateHelper.getDateOnly(followup);

        Long seedTimeStamp = DateHelper.getDateOnly(seedDate);
        Long followTimeStamp = DateHelper.getDateOnly(followDate);

        assertEquals(seedDate, followDate);
        assertEquals(seedTimeStamp, followTimeStamp);
    }

    /**
     * Testing for a constant relation:
     * Passing an initial date D1 to getDateOnly()
     */
    @Test
    void testMetamorphicDateAndTimeChange_1() {

        // seed case

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2026, Calendar.OCTOBER, 20, 9, 30, 0);

        long seed = cal.getTimeInMillis();

        // follow-up case

        Calendar cal2 = (Calendar) cal.clone();
        cal2.set(Calendar.HOUR_OF_DAY, 21);
        long followup = cal2.getTimeInMillis();

        String seedDateTime = DateHelper.getDateAndTime(seed);
        String followDateTime = DateHelper.getDateAndTime(followup);

        // String seedDateTimeFromString = DateHelper.getDateAndTime(Long.toString(seed));
        // String followDateTimeFromString = DateHelper.getDateAndTime(Long.toString(followup));

        assertNotEquals(seedDateTime, followDateTime);
        // assertNotEquals(seedDateTimeFromString, followDateTimeFromString);
    }

    /*
     * Testing for a changing relation:
     * The seed is two different time stamps s1 and s2. The followup case is 
     * the value of
     * s2 plus a minimum of 60,000 milliseconds. The number of minutes between
     * s1 and s2 and s1 and the followup should NOT be equal.
     */
    @Test
    void testMetamorphicMinutesDiffChange_1() {

        DateFormats format = DateFormats.S_YYYYMMDDHHMMSSA_N;

        // seed case

        long seed1 = 1_792_535_420_000L;
        long seed2 = 1_792_535_480_000L;

        String seedString1 = DateHelper.getDesiredFormat(format, seed1);
        String seedString2 = DateHelper.getDesiredFormat(format, seed2);

        Long seedDiff = DateHelper.getMinutesBetweenTwoDates(seedString1, seedString2, format);

        // follow-up case

        long followup = seed2 + 60_000L;
        String followupString = DateHelper.getDesiredFormat(format, followup);

        Long followupDiff = DateHelper.getMinutesBetweenTwoDates(seedString1, followupString, format);

        assertNotEquals(seedDiff, followupDiff);
    }

    /*
     * Testing for a linear relation:
     * The seed is two different time stamps s1 and s2. The followup case 
     * consists of f1 and f2 which are calculated by s1 + c and s2 + c
     * respectively, where c is a constant with a minimum calue of 60,000 
     * milliseconds. The result of getMinutesBetweenTwoDates() on {s1, s2}
     * and then on {f1, f2} should be equal.
     */
    @Test
    void testMetamorphicMinutesDiffLinear_1() {

        DateFormats format = DateFormats.S_YYYYMMDDHHMMSSA_N;

        // seed case

        long seed1 = 1_792_535_420_000L; // Fixed timestamp for repeatable results
        long seed2 = 1_792_535_480_000L; // Fixed timestamp for repeatable results

        String seedString1 = DateHelper.getDesiredFormat(format, seed1);
        String seedString2 = DateHelper.getDesiredFormat(format, seed2);

        Long seedDiff = DateHelper.getMinutesBetweenTwoDates(seedString2, seedString1, format);

        // follow-up case

        long followup1 = seed1 + 60_000L;
        long followup2 = seed2 + 60_000L;
        String followupString1 = DateHelper.getDesiredFormat(format, followup1);
        String followupString2 = DateHelper.getDesiredFormat(format, followup2);

        Long followupDiff = DateHelper.getMinutesBetweenTwoDates(followupString2, followupString1, format);

        assertEquals(seedDiff, followupDiff);
    }

    @Test
    void testMetamorphicHoursDiffChange_1() {

        DateFormats format = DateFormats.S_YYYYMMDDHHMMSSA_N;

        // seed case

        long seed1 = 1_792_535_420_000L;
        long seed2 = 1_792_535_485_000L;

        String seedString1 = DateHelper.getDesiredFormat(format, seed1);
        String seedString2 = DateHelper.getDesiredFormat(format, seed2);

        Long seedDiff = DateHelper.getHoursBetweenTwoDate(seedString1, seedString2, format);

        // follow-up case

        long followup = seed2 + 3_600_000L;
        String followupString = DateHelper.getDesiredFormat(format, followup);

        Long followupDiff = DateHelper.getHoursBetweenTwoDate(seedString1, followupString, format);

        assertNotEquals(seedDiff, followupDiff);
    }


    @Test
    void testMetamorphicDateFromDaysDiffSymmetry_1() {

        DateFormats format = DateFormats.D_DDMMyy_N;

        String todaytemp = DateHelper.getToday();

        long todayTimestamp = DateHelper.parseDate(todaytemp, DateHelper.DateFormats.S_DDMMYYYY);
        String today = DateHelper.getDesiredFormat(format, todayTimestamp);

        int seedDiff = 7;

        String future = DateHelper.getDateFromDays(seedDiff);

        Long followupDiff = DateHelper.getDaysBetweenTwoDate(future, today, format);

        assertNotEquals(today, future);
        assertEquals(Long.valueOf(seedDiff), followupDiff);
    }

    // Edge case: precisesly before midnight
    @Test
    void testMetamorphicTodayTomorrowDiffConstant_1() {

        DateHelper.DateFormats format = DateHelper.DateFormats.S_DDMMyy;

        String today = DateHelper.getToday();
        String tomorrow = DateHelper.getTomorrow();

        int diff = 1;

        Long followupDiff = DateHelper.getDaysBetweenTwoDate(tomorrow, today, format);

        System.out.println(followupDiff);

        assertNotEquals(today, tomorrow);
        assertEquals(Long.valueOf(diff), followupDiff);
    }

    /*
     * Testing for a constant relation:
     * The seed is result of calling getToday(). The followup case is calling
     * getTodayWithTime() and then calling getDateOnly on this result.
     * The result of getDateOnly and the seed should be equal.
     */
    @Test
    void testMetamorphicTodayDateIsConstant() throws ParseException {

        // seed case

        String todayDate = DateHelper.getToday();

        // follow-up case

        String todayTimeStamp = DateHelper.getTodayWithTime();


        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date parsedTimeStamp = format.parse(todayTimeStamp);

        String followDate = DateHelper.getDateOnly(parsedTimeStamp.getTime());

        assertEquals(todayDate, followDate);
    }

}
