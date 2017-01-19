package com.jianboke.utils;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("FileReadAndWriteUtils")
public class FileReadAndWriteUtils {
	private static final Logger log = LoggerFactory.getLogger(FileReadAndWriteUtils.class);
	
	public boolean writeFile(String filename, byte[] data) {
		
		return false;
	}
}
