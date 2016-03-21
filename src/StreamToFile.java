import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;

public class StreamToFile {
	public static void InputStreamToFile(InputStream inputStream,File outputFile) {
		OutputStream outputStream = null;
		try {	
			outputStream = new FileOutputStream(outputFile);
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e);
		}
		finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e);
				}
			}
			if (outputStream != null) {
				try { 	 
					outputStream.close(); 
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e);
				}
			}
		}
	}
}