package com.mdbytes.demo.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Jpa Hibernate Mappings: Test Suite")
@SelectClasses({OneToOneTests.class, OneToManyTests.class, OneToManyUniTests.class, ManyToManyTests.class})
class ApplicationTests {


}
