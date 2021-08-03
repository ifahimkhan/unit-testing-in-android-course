package com.techyourchance.unittestingfundamentals.exercise1;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NegativeNumberValidatorTest {
    NegativeNumberValidator SUT;

    @Before
    public void setup() {
        SUT = new NegativeNumberValidator();
    }

    @Test
    public void isNegative_returns_false() {
        boolean flag = SUT.isNegative(0);
        assertThat(flag, is(false));
    }

    @Test
    public void isNegative_returns_true() {
        boolean flag = SUT.isNegative(-1);
        assertThat(flag, is(true));
    }

    @Test
    public void isNegative_returns_false1() {
        boolean flag = SUT.isNegative(1);
        assertThat(flag, is(false));
    }


}