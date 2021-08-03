package com.techyourchance.unittestingfundamentals.exercise3;

import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector SUT;

    @Before
    public void setup() {
        SUT = new IntervalsAdjacencyDetector();
    }


    @Test
    public void isAdjacent_Interval1AdjacentToInterval2_trueReturned() {
        Interval interval1 = new Interval(1, 5);
        Interval interval2 = new Interval(5, 10);
        boolean flag = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(flag, CoreMatchers.is(true));
    }

    @Test
    public void isAdjacent_Interval1AdjacentToInterval2SomeDifference_falseReturned() {
        Interval interval1 = new Interval(1, 5);
        Interval interval2 = new Interval(6, 10);
        boolean flag = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(flag, CoreMatchers.is(false));
    }

    @Test
    public void isAdjacent_Interval1AndInterval2AreSame_falseReturned() {
        Interval interval1 = new Interval(1, 5);
        Interval interval2 = new Interval(1, 5);
        boolean flag = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(flag, CoreMatchers.is(false));
    }

    @Test
    public void isAdjacent_Interval1ContainsInterval2_falseReturned() {
        Interval interval1 = new Interval(1, 5);
        Interval interval2 = new Interval(2, 4);
        boolean flag = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(flag, CoreMatchers.is(false));
    }
    @Test
    public void isAdjacent_Interval2ContainsInterval1_falseReturned() {
        Interval interval1 = new Interval(2, 4);
        Interval interval2 = new Interval(1, 5);
        boolean flag = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(flag, CoreMatchers.is(false));
    }
    @Test
    public void isAdjacent_Interval1OverlapInterval2_falseReturned() {
        Interval interval1 = new Interval(1, 5);
        Interval interval2 = new Interval(2, 10);
        boolean flag = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(flag, CoreMatchers.is(false));
    }
    @Test
    public void isAdjacent_Interval2OverlapInterval1_falseReturned() {
        Interval interval1 = new Interval(2, 10);
        Interval interval2 = new Interval(1, 5);
        boolean flag = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(flag, CoreMatchers.is(false));
    }
    @Test
    public void isAdjacent_Interval2AjacentToInterval1_falseReturned(){
        Interval interval1 = new Interval(5, 10);
        Interval interval2 = new Interval(1, 5);
        boolean flag = SUT.isAdjacent(interval1, interval2);
        Assert.assertThat(flag, CoreMatchers.is(false));
    }
}