/**
 * Created on Nov 5, 2004
 */
package com.domainlanguage.testutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import junit.framework.AssertionFailedError;

public class SerializationTest {
	private static final String TEST_FILE = "test.txt";

	public static void assertSerializationWorks(Object toSerialize) throws AssertionFailedError {
		if (!(toSerialize instanceof Serializable)) 
			throw new AssertionFailedError("Object doesn't implement java.io.Serializable interface"); 

		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		File serFile = null;
		try {
			serFile = new File(TEST_FILE);
			out = new ObjectOutputStream(new FileOutputStream(serFile));
			out.writeObject(toSerialize);
			out.flush();
			in = new ObjectInputStream(new FileInputStream(serFile));
			Object deserialized = in.readObject();
			if (!toSerialize.equals(deserialized)) 
				throw new AssertionFailedError("Reconstituted object is expected to be equal to serialized"); 
		} catch (AssertionFailedError e) {
			throw e;
		} catch (Exception e) {
			throw new AssertionFailedError("Exception while serializing: " + e);
		} finally {
			try {
				if (out != null) 
					out.close();
				if (in != null) 
					in.close();
				serFile.delete();
			} catch (IOException ignore) {
			}
		}
	}

}