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
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests regarding formatting of sets of temporal objects.
 */
public class TemporalsSetTest {
    
    private final Temporals temporals = new Temporals(Locale.ENGLISH, ZoneOffset.UTC);

    @Test
    public void testSetFormat() {
        Set<Temporal> set = new TreeSet<>(asList(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31)));
        Set<String> expected = new TreeSet<>(asList("January 1, 2015 12:00:00 AM Z", "December 31, 2015 12:00:00 AM Z"));
        assertEquals(expected, temporals.setFormat(set));
    }

    @Test
    public void testSetFormatWithPattern() {
        Set<Temporal> set = new TreeSet<>(asList(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31)));
        String pattern = "yyyy-MM-dd";
        Set<String> expected = new TreeSet<>(asList("2015-01-01", "2015-12-31"));
        assertEquals(expected, temporals.setFormat(set, pattern));
    }

    @Test
    public void testSetDay() {
        Set<Temporal> set = new TreeSet<>(asList(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31)));
        Set<Integer> expected = new TreeSet<>(asList(1, 31));
        assertEquals(expected, temporals.setDay(set));
    }
    
    @Test
    public void testSetMonth() {
        Set<Temporal> set = new TreeSet<>(asList(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31)));
        Set<Integer> expected = new TreeSet<>(asList(1, 12));
        assertEquals(expected, temporals.setMonth(set));
    }
    
    @Test
    public void testSetMonthName() {
        Set<Temporal> set = new TreeSet<>(asList(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31)));
        Set<String> expected = new TreeSet<>(asList("January", "December"));
        assertEquals(expected, temporals.setMonthName(set));
    }
    
    @Test
    public void testSetMonthNameShort() {
        Set<Temporal> set = new TreeSet<>(asList(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 12, 31)));
        Set<String> expected = new TreeSet<>(asList("Jan", "Dec"));
        assertEquals(expected, temporals.setMonthNameShort(set));
    }
    
    @Test
    public void testSetYear() {
        Set<Temporal> set = new TreeSet<>(asList(LocalDate.of(2014, 1, 1), LocalDate.of(2015, 12, 31)));
        Set<Integer> expected = new TreeSet<>(asList(2014, 2015));
        assertEquals(expected, temporals.setYear(set));
    }

    @Test
    public void testSetDayOfWeek() {
        Set<Temporal> set = new TreeSet<>(asList(LocalDate.of(2014, 1, 1), LocalDate.of(2015, 12, 31)));
        Set<Integer> expected = new TreeSet<>(asList(3, 4));
        assertEquals(expected, temporals.setDayOfWeek(set));
    }

    @Test
    public void testSetDayOfWeekName() {
        Set<Temporal> set = new TreeSet<>(asList(LocalDate.of(2014, 1, 1), LocalDate.of(2015, 12, 31)));
        Set<String> expected = new TreeSet<>(asList("Wednesday", "Thursday"));
        assertEquals(expected, temporals.setDayOfWeekName(set));
    }

    @Test
    public void testSetDayOfWeekNameShort() {
        Set<Temporal> set = new TreeSet<>(asList(LocalDate.of(2014, 1, 1), LocalDate.of(2015, 12, 31)));
        Set<String> expected = new TreeSet<>(asList("Wed", "Thu"));
        assertEquals(expected, temporals.setDayOfWeekNameShort(set));
    }

    @Test
    public void testSetHour() {
        Set<Temporal> set = new TreeSet<>(asList(
            LocalDateTime.of(2015, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2015, 12, 31, 23, 59, 10, 9)));
        Set<Integer> expected = new TreeSet<>(asList(1, 23));
        assertEquals(expected, temporals.setHour(set));
    }

    @Test
    public void testSetMinute() {
        Set<Temporal> set = new TreeSet<>(asList(
            LocalDateTime.of(2015, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2015, 12, 31, 23, 59, 10, 9)));
        Set<Integer> expected = new TreeSet<>(asList(1, 59));
        assertEquals(expected, temporals.setMinute(set));
    }

    @Test
    public void testSetSecond() {
        Set<Temporal> set = new TreeSet<>(asList(
            LocalDateTime.of(2015, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2015, 12, 31, 23, 59, 10, 9)));
        Set<Integer> expected = new TreeSet<>(asList(1, 10));
        assertEquals(expected, temporals.setSecond(set));
    }

    @Test
    public void testSetNanosecond() {
        Set<Temporal> set = new TreeSet<>(asList(
            LocalDateTime.of(2015, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2015, 12, 31, 23, 59, 10, 9)));
        Set<Integer> expected = new TreeSet<>(asList(1, 9));
        assertEquals(expected, temporals.setNanosecond(set));
    }

    @Test
    public void testSetFormatISO() {
        Set<Temporal> set = new TreeSet<>(asList(
            LocalDateTime.of(2015, 1, 1, 1, 1, 1, 1), LocalDateTime.of(2015, 12, 31, 23, 59, 10, 9)));
        Set<String> expected = new TreeSet<>(asList("2015-01-01T01:01:01.000+0000", "2015-12-31T23:59:10.000+0000"));
        assertEquals(expected, temporals.setFormatISO(set));
    }
    
}
