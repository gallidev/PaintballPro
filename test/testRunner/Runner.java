package testRunner;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import testSuites.AISuite;
import testSuites.AudioSuite;
import testSuites.GUISuite;
import testSuites.IntegrationSuite;
import testSuites.LogicSuite;
import testSuites.NetworkingSuite;
import testSuites.PhysicsSuite;
import testSuites.RenderingSuite;

/**
 * Main JUnit test runner - runs all JUnit tests and prints success/failures.
 *
 * @author Matthew Walters
 */
public class Runner {
   public static void main(String[] args) {

      Result result = JUnitCore.runClasses(IntegrationSuite.class, LogicSuite.class, NetworkingSuite.class, PhysicsSuite.class, AISuite.class, RenderingSuite.class, AudioSuite.class, GUISuite.class);


      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }

      if(result.wasSuccessful())
    	  System.out.println(">> All tests pass. <<");
      else
    	  System.out.println(">> There were test failures. <<");

      System.exit(0);
   }
}