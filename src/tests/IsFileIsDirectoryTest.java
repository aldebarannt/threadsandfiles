package tests;

import java.io.File;

public class IsFileIsDirectoryTest {
	
	public static void main(String[] args) {
		
		final File file = new File("d:\\dev\\error.log");
		final File directory = new File("d:\\dev");
		
		int count = 100000;
		
		long start = System.nanoTime();
		for (int i=0; i<count; i++) {
			file.isFile();
		}
		long end = System.nanoTime();
		System.out.println("file is file: " + (end - start));
		
		start = System.nanoTime();
		for (int i=0; i<count; i++) {
			file.isDirectory();
		}
		end = System.nanoTime();
		System.out.println("file is directory: " + (end - start));

		start = System.nanoTime();
		for (int i=0; i<count; i++) {
			directory.isFile();
		}
		end = System.nanoTime();
		System.out.println("directory is file: " + (end - start));

		start = System.nanoTime();
		for (int i=0; i<count; i++) {
			directory.isDirectory();
		}
		end = System.nanoTime();
		System.out.println("directory is directory: " + (end - start));

	}
	
}
