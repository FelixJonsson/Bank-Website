package isp.entity.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ AccountTest.class, TransactionTest.class })
public class AllTests {
	
}