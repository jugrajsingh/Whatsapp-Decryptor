import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;

public class HexDump {
	public static void hexDump(PrintStream outStream, File inputFile) throws IOException {
		InputStream inputStream = new FileInputStream(inputFile);
		while (inputStream.available() > 0) { 
			StringBuilder stringBuilder = new StringBuilder(); 
			for (int j = 0; j < 16; j++) {
				if (inputStream.available() > 0) {  
					int value = (int) inputStream.read();  
					stringBuilder.append(String.format("%02x", value));
				}    
			}
			outStream.print(stringBuilder);
		}
		inputStream.close(); 
		outStream.close();
	}
}