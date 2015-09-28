package br.com.marquesapps.receitas.utils;

import org.apache.commons.io.IOUtils;

class StreamUtil {
	
	public static final String SUFFIX = ".tmp";
	public static File stream2file (String filename, InputStream inp) throws IOException {
		final File tempFile = File.createTempFile(filename, SUFFIX);
		tempFile.deleteOnExit();
		try {
				FileOutputStream out = new FileOutputStream(tempFile)
				IOUtils.copy(inp, out);
		} catch (Exception e) {
			return null;
		}
			return tempFile;
	}
}