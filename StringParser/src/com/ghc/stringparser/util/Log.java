package com.ghc.stringparser.util;

public class Log {
	private static boolean sDebug = false;

	public static boolean isDebug(){
		return sDebug;
	}
	
	public static void enableDebug() {
		sDebug = true;
	}

	public static void disableDebug() {
		sDebug = false;
	}

	public static void d(String message) {
		if (sDebug) {
			System.out.printf("%s \n", message);
		}
	}
	
	public static void d(String message, Object... args) {
		if (sDebug) {
			System.out.printf(message, args);
		}
	}
}
