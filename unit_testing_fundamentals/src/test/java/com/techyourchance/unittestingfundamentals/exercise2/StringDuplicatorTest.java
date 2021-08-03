package com.techyourchance.unittestingfundamentals.exercise2;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringDuplicatorTest {

    StringDuplicator SUT;

    @Before
    public void setup() {
        SUT = new StringDuplicator();

    }

    @Test
    public void duplicate_SingleChar_TwoCharsReturned() {

        String duplicatedString = SUT.duplicate("A");
        Assert.assertThat(duplicatedString, CoreMatchers.is("AA"));
    }

    @Test
    public void duplicate_longString_doubleLongStringReturned() {
        String duplicatedString = SUT.duplicate("MOHAMMED FAHIM MOHAMMED NAIM KHAN");
        Assert.assertThat(duplicatedString, CoreMatchers.is("MOHAMMED FAHIM MOHAMMED NAIM KHANMOHAMMED FAHIM MOHAMMED NAIM KHAN"));

    }

    @Test
    public void duplicate_emptyString_emptyStringReturned() throws Exception {
        String duplicatedString = SUT.duplicate("");
        Assert.assertThat(duplicatedString, CoreMatchers.is(""));

    }
}