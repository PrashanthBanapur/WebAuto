package org.seltest.core;

public class TestCase {
	private static ThreadLocal<String> testName = new ThreadLocal<String>();
	private static ThreadLocal<String> author = new ThreadLocal<String>();
	private static ThreadLocal<String> date = new ThreadLocal<String>();
	private static ThreadLocal<String> version = new ThreadLocal<String>();
	private static ThreadLocal<Integer> sleep = new ThreadLocal<Integer>();

	public static void setSleepTime(int val) {
		sleep.set(val);
	}

	public static int getSleepTime() {
		return sleep.get();
	}

	public static String getTestName() {
		return testName.get();
	}

	public static void setTestName(String name) {
		testName.set(name);
	}

	public static String getAuthor() {
		return author.get();
	}

	public static void setAuthor(String name) {
		author.set(name);
	}

	public static String getDate() {
		return date.get();
	}

	public static void setDate(String name) {
		date.set(name);
	}

	public static String getVersion() {
		return version.get();
	}

	public static void setVersion(String name) {
		version.set(name);
	}

}