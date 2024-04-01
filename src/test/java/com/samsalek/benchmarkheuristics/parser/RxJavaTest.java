package com.samsalek.benchmarkheuristics.parser;

// Figure out what to do with this test class later
/*
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RxJavaTest {

    private static MethodParser PARSER;

    @BeforeClass
    public static void init() {
        PARSER = new MethodParser(Integer.MAX_VALUE, "src\\main\\java\\", "src\\test\\java\\", "rxjava",
                new File("src\\main\\java"),
                new File("src\\test\\java"));
    }

    @Test
    public void packageAccessesTest_dispose3() {
        Map<String, Integer> expectedValues = new HashMap<>();
        expectedValues.put("io.reactivex.rxjava3.plugins", 27);
        expectedValues.put("java.lang", 31);
        expectedValues.put("java.util.concurrent",10);
        expectedValues.put("java.util.concurrent.atomic", 6);
        expectedValues.put("io.reactivex.rxjava3.internal.schedulers", 4);
        expectedValues.put("io.reactivex.rxjava3.exceptions", 1);
        expectedValues.put("org.junit", 2);

        String filePath = new File("src\\test\\java\\io\\reactivex\\rxjava3\\internal\\schedulers\\InstantPeriodicTaskTest.java").getAbsolutePath();
        String methodName = "dispose3";
        PARSER.parse(filePath, methodName);
        Map<String, Integer> actualValues = PARSER.getParsedMethod().getPackageAccesses();

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void numConditionalTest_dispose3() {
        String filePath = new File("src\\test\\java\\io\\reactivex\\rxjava3\\internal\\schedulers\\InstantPeriodicTaskTest.java").getAbsolutePath();
        String methodName = "dispose3";
        PARSER.parse(filePath, methodName);

        int expectedValue = 32;
        int actualValue = PARSER.getParsedMethod().getNumConditionals();

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void numLoopsTest_dispose3() {
        String filePath = new File("src\\test\\java\\io\\reactivex\\rxjava3\\internal\\schedulers\\InstantPeriodicTaskTest.java").getAbsolutePath();
        String methodName = "dispose3";
        PARSER.parse(filePath, methodName);

        int expectedValue = 2;
        int actualValue = PARSER.getParsedMethod().getNumLoops();

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void numNestedLoopsTest_dispose3() {
        String filePath = new File("src\\test\\java\\io\\reactivex\\rxjava3\\internal\\schedulers\\InstantPeriodicTaskTest.java").getAbsolutePath();
        String methodName = "dispose3";
        PARSER.parse(filePath, methodName);

        int expectedValue = 0;
        int actualValue = PARSER.getParsedMethod().getNumNestedLoops();

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void numMethodCallsTest_dispose3() {
        String filePath = new File("src\\test\\java\\io\\reactivex\\rxjava3\\internal\\schedulers\\InstantPeriodicTaskTest.java").getAbsolutePath();
        String methodName = "dispose3";
        PARSER.parse(filePath, methodName);

        int expectedValue = 50;
        int actualValue = PARSER.getParsedMethod().getNumMethodCalls();

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void LocTest_dispose3() {
        String filePath = new File("src\\test\\java\\io\\reactivex\\rxjava3\\internal\\schedulers\\InstantPeriodicTaskTest.java").getAbsolutePath();
        String methodName = "dispose3";
        PARSER.parse(filePath, methodName);

        int expectedValue = 141;
        int actualValue = PARSER.getParsedMethod().getLinesOfCode();

        assertEquals(expectedValue, actualValue);
    }
}
*/
