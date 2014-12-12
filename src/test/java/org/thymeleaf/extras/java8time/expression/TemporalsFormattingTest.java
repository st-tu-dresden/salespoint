/*
 * Copyright 2014 The THYMELEAF team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thymeleaf.extras.java8time.expression;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests regarding formatting of temporal objects.
 */
public class TemporalsFormattingTest {
    
    private final Temporals temporals = new Temporals(Locale.ENGLISH, ZoneOffset.UTC);

    @Test
    public void testFormat() {
        Temporal time = ZonedDateTime.of(2015, 12, 31, 23, 59, 45, 0, ZoneOffset.UTC);
        assertEquals("December 31, 2015 11:59:45 PM Z", temporals.format(time));
    }

    @Test
    public void testFormatWithPattern() {
        Temporal time = LocalDateTime.of(2015, 12, 31, 23, 59);
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String expectd = "2015-12-31 23:59:00";
        assertEquals(expectd, temporals.format(time, pattern));
    }

    @Test
    public void testDay() {
        Temporal time = LocalDate.of(2015, 12, 31);
        assertEquals(31, temporals.day(time).intValue());
    }

    @Test
    public void testMonth() {
        Temporal time = LocalDate.of(2015, 12, 31);
        assertEquals(12, temporals.month(time).intValue());
    }
    
    @Test
    public void testMonthName() {
        Temporal time = LocalDate.of(2015, 12, 31);
        assertEquals("December", temporals.monthName(time));
    }

    @Test
    public void testMonthNameShort() {
        Temporal time = LocalDate.of(2015, 12, 31);
        assertEquals("Dec", temporals.monthNameShort(time));
    }

    @Test
    public void testYear() {
        Temporal time = LocalDate.of(2015, 12, 31);
        assertEquals(2015, temporals.year(time).intValue());
    }

    @Test
    public void testDayOfWeek() {
        Temporal time = LocalDate.of(2015, 12, 31);
        assertEquals(4, temporals.dayOfWeek(time).intValue());
    }
    
    @Test
    public void testDayOfWeekName() {
        Temporal time = LocalDate.of(2015, 12, 31);
        assertEquals("Thursday", temporals.dayOfWeekName(time));
    }
    
    @Test
    public void testDayOfWeekNameShort() {
        Temporal time = LocalDate.of(2015, 12, 31);
        assertEquals("Thu", temporals.dayOfWeekNameShort(time));
    }
    
    @Test
    public void testHour() {
        Temporal time = LocalDateTime.of(2015, 12, 31, 23, 59, 45, 1);
        assertEquals(23, temporals.hour(time).intValue());
    }

    @Test
    public void testMinute() {
        Temporal time = LocalDateTime.of(2015, 12, 31, 23, 59, 45, 1);
        assertEquals(59, temporals.minute(time).intValue());
    }

    @Test
    public void testSecond() {
        Temporal time = LocalDateTime.of(2015, 12, 31, 23, 59, 45, 1);
        assertEquals(45, temporals.second(time).intValue());
    }

    @Test
    public void testNanosecond() {
        Temporal time = LocalDateTime.of(2015, 12, 31, 23, 59, 45, 1);
        assertEquals(1, temporals.nanosecond(time).intValue());
    }

    @Test
    public void testFormatISO() {
        Temporal time = LocalDateTime.of(2015, 12, 31, 23, 59, 45, 1).atZone(ZoneOffset.MAX);
        assertEquals("2015-12-31T23:59:45.000+1800", temporals.formatISO(time));
    }

}
