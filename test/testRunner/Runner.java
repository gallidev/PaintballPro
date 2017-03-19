package testRunner;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import testSuites.NetworkingSuite;

/**
 * Main JUnit test runner - runs all JUnit tests and prints success/failures.
 * 
 * @author Matthew Walters
 */
public class Runner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(NetworkingSuite.class);

      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
		
      if(result.wasSuccessful())
    	  System.out.println("All tests passes.");
      else
    	  System.out.println("There were test failures.");
   }
} 