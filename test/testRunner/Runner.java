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

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Main JUnit test runner - runs all JUnit tests and prints success/failures.
 *
 * @author Matthew Walters, Sivarjuen Ravichandran
 */
public class Runner {
	public static void main(String[] args) {


		PrintStream originalStream = System.out;

		PrintStream dummyStream = new PrintStream(new OutputStream(){
			public void write(int b) {
			}
		});

		int passed = 0;

		System.out.println(">> Running all tests... this will take around 5 minutes. Please Wait. <<");
		System.out.println("(Ignore exceptions)");

		System.out.println("\nRunning Integration tests...(1/8)");
		System.setOut(dummyStream);
		Result integration = JUnitCore.runClasses(IntegrationSuite.class);
		System.setOut(originalStream);

		for (Failure failure : integration.getFailures()) {
			System.out.println(failure.toString());
		}
		if (integration.wasSuccessful()) {
			System.out.println(">> All tests pass. <<");
			passed++;
		}
		else
			System.out.println(">> There were test failures. <<");



		System.out.println("\nRunning Logic tests...(2/8)");
		System.setOut(dummyStream);
		Result logic = JUnitCore.runClasses(LogicSuite.class);
		System.setOut(originalStream);

		for (Failure failure : logic.getFailures()) {
			System.out.println(failure.toString());
		}

		if (logic.wasSuccessful()){
			System.out.println(">> All tests pass. <<");
			passed++;
		}
		else
			System.out.println(">> There were test failures. <<");


		System.out.println("\nRunning Networking tests...(3/8)");
		System.setOut(dummyStream);
		Result networking = JUnitCore.runClasses(NetworkingSuite.class);
		System.setOut(originalStream);

		for (Failure failure : networking.getFailures()) {
			System.out.println(failure.toString());
		}
		if (networking.wasSuccessful()){
			System.out.println(">> All tests pass. <<");
			passed++;
		}
		else
			System.out.println(">> There were test failures. <<");


		System.out.println("\nRunning Physics tests...(4/8)");
		System.setOut(dummyStream);
		Result physics = JUnitCore.runClasses(PhysicsSuite.class);
		System.setOut(originalStream);

		for (Failure failure : physics.getFailures()) {
			System.out.println(failure.toString());
		}
		if (physics.wasSuccessful()){
			System.out.println(">> All tests pass. <<");
			passed++;
		}
		else
			System.out.println(">> There were test failures. <<");


		System.out.println("\nRunning AI tests...(5/8)");
		System.setOut(dummyStream);
		Result ai = JUnitCore.runClasses(AISuite.class);
		System.setOut(originalStream);

		for (Failure failure : ai.getFailures()) {
			System.out.println(failure.toString());
		}
		if (ai.wasSuccessful()){
			System.out.println(">> All tests pass. <<");
			passed++;
		}
		else
			System.out.println(">> There were test failures. <<");


		System.out.println("\nRunning Rendering tests...(6/8)");
		System.setOut(dummyStream);
		Result rendering = JUnitCore.runClasses(RenderingSuite.class);
		System.setOut(originalStream);

		for (Failure failure : rendering.getFailures()) {
			System.out.println(failure.toString());
		}
		if (rendering.wasSuccessful()){
			System.out.println(">> All tests pass. <<");
			passed++;
		}
		else
			System.out.println(">> There were test failures. <<");


		System.out.println("\nRunning Audio tests..(7/8)");
		System.setOut(dummyStream);
		Result audio = JUnitCore.runClasses(AudioSuite.class);
		System.setOut(originalStream);

		for (Failure failure : audio.getFailures()) {
			System.out.println(failure.toString());
		}
		if (audio.wasSuccessful()){
			System.out.println(">> All tests pass. <<");
			passed++;
		}
		else
			System.out.println(">> There were test failures. <<");


		System.out.println("\nRunning GUI tests...(8/8)");
		System.setOut(dummyStream);
		Result gui = JUnitCore.runClasses(GUISuite.class);
		System.setOut(originalStream);

		for (Failure failure : gui.getFailures()) {
			System.out.println(failure.toString());
		}
		if (gui.wasSuccessful()){
			System.out.println(">> All tests pass. <<");
			passed++;
		}
		else
			System.out.println(">> There were test failures. <<");

		System.out.println("\n\n" + passed + " out of 8 tests passed!");

		System.exit(0);
	}
}