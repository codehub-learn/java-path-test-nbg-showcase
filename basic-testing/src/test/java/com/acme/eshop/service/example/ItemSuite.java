package com.acme.eshop.service.example;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuiteDisplayName("Item Suite")
@SelectPackages("com.acme.eshop.service")
@IncludeTags("development")
//@ExcludeTags()
//@IncludeClassNamePatterns("^.*Test$")
//@SelectClasses({RepeatedTest.class, TimeoutTesting.class})
public class ItemSuite {
}
