package test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//@author A0121409R

/*
 * To let JUnit know a test fails due to a assertion violation within the
 * function: 
 * 1) Add this line to the class file: 
 *    @Rule public ExpectedFailure rule = new ExpectedFailure(); 
 * 2) Add this line to the test:
 *    @AssertionFailure.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AssertionFailure {

}
