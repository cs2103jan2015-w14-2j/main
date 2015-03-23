package test;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

//@author A0121409R

/*
 * To let JUnit know a test fails due to a assertion violation within the
 * function: 
 * 1) Add this line to the class file: 
 *    @Rule public ExpectedFailure rule = new ExpectedFailure(); 
 * 2) Add this line to the test:
 *    @AssertionFailure.
 */
public class ExpectedFailure implements TestRule {
    public Statement apply(Statement base, Description description) {
        return statement(base, description);
    }

    private Statement statement(final Statement base,
                                final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate();
                } catch (Throwable e) {
                    if (description.getAnnotation(AssertionFailure.class) != null) {
                        System.err.println("Assertion Failure Caught.");
                    } else {
                        throw e;
                    }
                }
            }
        };
    }

}
