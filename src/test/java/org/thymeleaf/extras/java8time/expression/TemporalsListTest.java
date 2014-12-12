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
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests regarding formatting of lists of temporal objects.
 */
public class TemporalsListTest {
    
    private final Temporals temporals = new Temporals(Locale.ENGLISH, ZoneOffset.UTC);

    @Test
    public void testListFormat() {
        List<Temporal> list = asList(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31));
        List<String> expected = asList("January 1, 2015 12:00:00 AM Z", "December 31, 2015 12:00:00 AM Z");
        assertEquals(expected, temporals.listFormat(list));
    }

    @Test
    public void testListFormatWithPattern() {
        List<Temporal> list = asList(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31));
        String pattern = "yyyy-MM-dd";
        List<String> expected = asList("2015-01-01", "2015-12-31");
        assertEquals(expected, temporals.listFormat(list, pattern));
    }

    @Test
    public void testListDay() {
        List<Temporal> list = asList(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31));
        List<Integer> expected = asList(1, 31);
        assertEquals(expected, temporals.listDay(list));
    }
    
    @Test
    public void testListMonth() {
        List<Temporal> list = asList(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31));
        List<Integer> expected = asList(1, 12);
        assertEquals(expected, temporals.listMonth(list));
    }
    
    @Test
    public void testListMonthName() {
        List<Temporal> list = asList(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31));
        List<String> expected = asList("January", "December");
        assertEquals(expected, temporals.listMonthName(list));
    }
    
    @Test
    public void testListMonthNameShort() {
        List<Temporal> list = asList(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31));
        List<String> expected = asList("Jan", "Dec");
        assertEquals(expected, temporals.listMonthNameShort(list));
    }
    
    @Test
    public void testListYear() {
        List<Temporal> list = asList(LocalDate.of(2014, 1, 1), LocalDate.of(2015, 12, 31));
        List<Integer> expected = asList(2014, 2015);
        assertEquals(expected, temporals.listYear(list));
    }

    @Test
    public void testListDayOfWeek() {
        List<Temporal> list = asList(LocalDate.of(2014, 1, 1), LocalDate.of(2015, 12, 31));
        List<Integer> expected = asList(3, 4);
        assertEquals(expected, temporals.listDayOfWeek(list));
    }

    @Test
    public void testListDayOfWeekName() {
        List<Temporal> list = asList(LocalDate.of(2014, 1, 1), LocalDate.of(2015, 12, 31));
        List<String> expected = asList("Wednesday", "Thursday");
        assertEquals(expected, temporals.listDayOfWeekName(list));
    }

    @Test
    public void testListDayOfWeekNameShort() {
        List<Temporal> list = asList(LocalDate.of(2014, 1, 1), LocalDate.of(2015, 12, 31));
        List<String> expected = asList("Wed", "Thu");
        assertEquals(expected, temporals.listDayOfWeekNameShort(list));
    }

    @Test
    public void testListHour() {
        List<Temporal> list = asList(LocalDateTime.of(2015, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2015, 12, 31, 23, 59, 10, 9));
        List<Integer> expected = asList(1, 23);
        assertEquals(expected, temporals.listHour(list));
    }

    @Test
    public void testListMinute() {
        List<Temporal> list = asList(LocalDateTime.of(2015, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2015, 12, 31, 23, 59, 10, 9));
        List<Integer> expected = asList(1, 59);
        assertEquals(expected, temporals.listMinute(list));
    }

    @Test
    public void testListSecond() {
        List<Temporal> list = asList(LocalDateTime.of(2015, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2015, 12, 31, 23, 59, 10, 9));
        List<Integer> expected = asList(1, 10);
        assertEquals(expected, temporals.listSecond(list));
    }

    @Test
    public void testListNanosecond() {
        List<Temporal> list = asList(LocalDateTime.of(2015, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2015, 12, 31, 23, 59, 10, 9));
        List<Integer> expected = asList(1, 9);
        assertEquals(expected, temporals.listNanosecond(list));
    }

    @Test
    public void testListFormatISO() {
        List<Temporal> list = asList(LocalDateTime.of(2015, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2015, 12, 31, 23, 59, 10, 9));
        List<String> expected = asList("2015-01-01T01:01:01.000+0000", "2015-12-31T23:59:10.000+0000");
        assertEquals(expected, temporals.listFormatISO(list));
    }
    
}
