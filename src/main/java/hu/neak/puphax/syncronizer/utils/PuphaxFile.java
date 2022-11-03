package hu.neak.puphax.syncronizer.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PuphaxFile {

	public static void writeFile(String fileName, String data, boolean createNew) {
		Path file = Paths.get(fileName.concat(".json"));
		try {
			if(createNew) {
				Files.write(file, "".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} else {
				Files.write(file, data.getBytes(), StandardOpenOption.APPEND);
			}
			System.out.println("file write success!");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
}
