package gr.codelearn.eshop.service;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuiteDisplayName("RunAll Suite")
@SelectPackages("gr.codelearn.eshop.service")
//@ExcludeTags()
//@IncludeClassNamePatterns("^.*Test$")
//@SelectClasses({RepeatedTest.class, TimeoutTesting.class})
public class RunAllSuite {
}
