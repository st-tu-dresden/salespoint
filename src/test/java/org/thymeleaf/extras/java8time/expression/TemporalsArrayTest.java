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
import java.time.temporal.Temporal;
import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests regarding formatting of arrays of temporal objects.
 */
public class TemporalsArrayTest {
    
    private final Temporals temporals = new Temporals(Locale.ENGLISH, ZoneOffset.UTC);

    @Test
    public void testArrayFormat() {
        Temporal[] array = {LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31)};
        String[] expected = {"January 1, 2015 12:00:00 AM Z", "December 31, 2015 12:00:00 AM Z"};
        assertArrayEquals(expected, temporals.arrayFormat(array));
    }

    @Test
    public void testArrayFormatWithPattern() {
        Temporal[] array = {LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31)};
        String pattern = "yyyy-MM-dd";
        String[] expected = {"2015-01-01", "2015-12-31"};
        assertArrayEquals(expected, temporals.arrayFormat(array, pattern));
    }

    @Test
    public void testArrayDay() {
        Temporal[] array = {LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31)};
        Integer[] expected = {1, 31};
        assertArrayEquals(expected, temporals.arrayDay(array));
    }
    
    @Test
    public void testArrayMonth() {
        Temporal[] array = {LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31)};
        Integer[] expected = {1, 12};
        assertArrayEquals(expected, temporals.arrayMonth(array));
    }
    
    @Test
    public void testArrayMonthName() {
        Temporal[] array = {LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31)};
        String[] expected = {"January", "December"};
        assertArrayEquals(expected, temporals.arrayMonthName(array));
    }

    @Test
    public void testArrayMonthNameShort() {
        Temporal[] array = {LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31)};
        String[] expected = {"Jan", "Dec"};
        assertArrayEquals(expected, temporals.arrayMonthNameShort(array));
    }

    @Test
    public void testArrayYear() {
        Temporal[] array = {LocalDate.of(2014, 1, 1), LocalDate.of(2015, 12, 31)};
        Integer[] expected = {2014, 2015};
        assertArrayEquals(expected, temporals.arrayYear(array));
    }

    @Test
    public void testArrayDayOfWeek() {
        Temporal[] array = {LocalDate.of(2014, 1, 1), LocalDate.of(2015, 12, 31)};
        Integer[] expected = {3, 4};
        assertArrayEquals(expected, temporals.arrayDayOfWeek(array));
    }

    @Test
    public void testArrayDayOfWeekName() {
        Temporal[] array = {LocalDate.of(2014, 1, 1), LocalDate.of(2015, 12, 31)};
        String[] expected = {"Wednesday", "Thursday"};
        assertArrayEquals(expected, temporals.arrayDayOfWeekName(array));
    }

    @Test
    public void testArrayDayOfWeekNameShort() {
        Temporal[] array = {LocalDate.of(2014, 1, 1), LocalDate.of(2015, 12, 31)};
        String[] expected = {"Wed", "Thu"};
        assertArrayEquals(expected, temporals.arrayDayOfWeekNameShort(array));
    }

    @Test
    public void testArrayHour() {
        Temporal[] array = {LocalDateTime.of(2015, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2015, 12, 31, 23, 59, 10, 9)};
        Integer[] expected = {1, 23};
        assertArrayEquals(expected, temporals.arrayHour(array));
    }

    @Test
    public void testArrayMinute() {
        Temporal[] array = {LocalDateTime.of(2015, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2015, 12, 31, 23, 59, 10, 9)};
        Integer[] expected = {1, 59};
        assertArrayEquals(expected, temporals.arrayMinute(array));
    }

    @Test
    public void testArraySecond() {
        Temporal[] array = {LocalDateTime.of(2015, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2015, 12, 31, 23, 59, 10, 9)};
        Integer[] expected = {1, 10};
        assertArrayEquals(expected, temporals.arraySecond(array));
    }

    @Test
    public void testArrayNanosecond() {
        Temporal[] array = {LocalDateTime.of(2015, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2015, 12, 31, 23, 59, 10, 9)};
        Integer[] expected = {1, 9};
        assertArrayEquals(expected, temporals.arrayNanosecond(array));
    }

    @Test
    public void testFormatISO() {
        Temporal[] array = {
            LocalDateTime.of(2015, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2015, 12, 31, 23, 59, 10, 9)};
        String[] expected = {"2015-01-01T01:01:01.000+0000", "2015-12-31T23:59:10.000+0000"};
        assertArrayEquals(expected, temporals.arrayFormatISO(array));
    }

}
