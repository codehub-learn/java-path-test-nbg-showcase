package com.acme.eshop.service.example;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConditionalTestingTest {

    @Test
    @Disabled
    void test(){
        assertTrue(true);
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void test1(){
        assertTrue(true);
    }

    @Test
    @EnabledOnOs({OS.WINDOWS, OS.LINUX})
    void test2(){
        assertTrue(true);
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void test3(){
        assertTrue(true);
    }

    @Test
    @EnabledOnJre({JRE.JAVA_11, JRE.JAVA_17})
    void test4(){
        assertTrue(true);
    }

    @Test
    @EnabledForJreRange(min = JRE.JAVA_8, max = JRE.JAVA_13)
    public void shouldOnlyRunOnJava8UntilJava13() {
        // Only enabled on Java 8, 9, 10, 11, 12, and 13.
    }

    @Test
    @DisabledForJreRange(min = JRE.JAVA_14, max = JRE.JAVA_15)
    public void shouldNotBeRunOnJava14AndJava15() {
        // Enabled for all except for Java 14 and 15.
    }

    @Test
    @DisabledOnJre(JRE.OTHER)
    public void thisTestOnlyRunsWithUpToDateJREs() {
        // Only enabled on Java 8, 9, 10, and 11.
    }

    @Test
    @EnabledIfSystemProperty(named = "java.vm.vendor", matches = "Oracle.*")
    public void test5() {
    }

    @Test
    @DisabledIfSystemProperty(named = "file.separator", matches = "[/]")
    public void disabledIfFileSeperatorIsSlash() {
        // Disabled if the system running supports "/" as its file separator
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "GDMSESSION", matches = "ubuntu")
    public void onlyRunOnUbuntuServer() {
        // Only enabled if the environment variable "GDMSESSION" equals "ubuntu"
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "LC_TIME", matches = ".*UTF-8.")
    public void shouldNotRunWhenTimeIsNotUTF8() {
        //  Disabled if the environment variable "LC_TIME" does not equal "*UTF-8.". Practically means it will not
        //  run if the time is not based on UTF8
    }
}
