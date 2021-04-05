package com.ac75.springboot.app.manuales.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.springframework.stereotype.Component;

@Component
public class FileWriter {

	public Path write(Path path, byte[] content) throws IOException {
		return Files.write(path, content, StandardOpenOption.CREATE);
	}
}
