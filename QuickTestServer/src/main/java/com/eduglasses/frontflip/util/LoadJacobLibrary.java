package com.eduglasses.frontflip.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.swing.JOptionPane;

import org.springframework.stereotype.Component;

//@Component
public class LoadJacobLibrary {

	boolean isError = false;

	@PostConstruct
	public void loadJacobLibrary() {
		System.out.println("Inside loadJacobLibrary");
		copyAndLoadFile();
	}

	private void copyAndLoadFile() {

		String sysAech = System.getProperty("sun.arch.data.model");

		String dllFileLoc = System.getProperty("user.home")
				+ "\\Documents\\frontflip\\";

		if (sysAech.contains("64")) {
			copyDLLFile(dllFileLoc, "jacob-1.18-M1-x64.dll");
		} else {
			copyDLLFile(dllFileLoc, "jacob-1.18-M1-x86.dll");
		}

		addDLLToPath(dllFileLoc);

	}

	private void copyDLLFile(String dllFileLoc, String dllFileName) {

		InputStream ddlStream = LoadJacobLibrary.class.getClassLoader()
				.getResourceAsStream(dllFileName);

		FileOutputStream fos = null;

		try {

			File dllFileDir = new File(dllFileLoc);

			if (!dllFileDir.exists()) {
				dllFileDir.mkdirs();
			}

			File fileOut = new File(dllFileLoc + dllFileName);

			fos = new FileOutputStream(fileOut);

			byte[] buf = new byte[2048];
			int r = ddlStream.read(buf);

			while (r != -1) {
				fos.write(buf, 0, r);
				r = ddlStream.read(buf);
			}

		} catch (FileNotFoundException e) {
			isError = true;
			JOptionPane.showMessageDialog(null,
					"Could not find location to copy jacob.dll", "Error", 2);
		} catch (IOException e) {
			isError = true;
			JOptionPane.showMessageDialog(null, "Could not copy jacob.dll",
					"Error", 2);
		}
	}

	/**
	 * The java.library.path system property instructs the JVM where to search
	 * for native libraries. You have to specify it as a JVM argument using
	 * -Djava.library.path=/path/to/lib the JVM will search the library path for
	 * the specified library. If it cannot be found you will get an
	 * java.lang.UnsatisfiedLinkError exception. The java.library.path is read
	 * only once when the JVM starts up. If you change this property using
	 * System.setProperty, it won't make any difference. Here is the code from
	 * ClassLoader.loadLibrary which shows how the path is initialized:
	 * 
	 * if (sys_paths == null) { usr_paths = initializePath("java.library.path");
	 * sys_paths = initializePath("sun.boot.library.path"); }
	 * 
	 * So, how can you modify the library path at runtime? There are a couple of
	 * ways to do this, both involving reflection.
	 * 
	 * @param pathToAdd
	 */
	/*
	 * If you set sys_paths to null, the library path will be re-initialized
	 * when you try to load a library. The following code does this:
	 */
	public void setLibraryPath(String pathToAdd) {

		try {

			System.setProperty("java.library.path", pathToAdd);

			// set sys_paths to null
			final Field sysPathsField = ClassLoader.class
					.getDeclaredField("sys_paths");
			sysPathsField.setAccessible(true);
			sysPathsField.set(null, null);

		} catch (Exception e) {
			isError = true;
			JOptionPane.showMessageDialog(null,
					"Error while adding jacob.dll to user path", "Error", 2);
		}
	}

	/**
	 * Instead of having to re-evaluate the entire java.library.path and
	 * sun.boot.library.path as in Option 1, you can instead append your path to
	 * the usr_paths array.
	 * 
	 * @param pathToAdd
	 */
	private void addDLLToPath(String pathToAdd) {

		Field usrPathsField = null;

		// get array of paths
		String[] paths = null;

		try {

			usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
			usrPathsField.setAccessible(true);
			paths = (String[]) usrPathsField.get(null);

			// check if the path to add is already present
			for (String path : paths) {
				if (path.equals(pathToAdd)) {
					return;
				}
			}

			final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
			newPaths[newPaths.length - 1] = pathToAdd;

			usrPathsField.set(null, newPaths);

		} catch (Exception e) {
			isError = true;
			JOptionPane.showMessageDialog(null,
					"Error while adding jacob.dll to user path", "Error", 2);
		}
	}

}
