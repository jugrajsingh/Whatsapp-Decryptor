import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.JOptionPane;

public class GZIP {
	public static void decompressGzipFile(String archiveFile, String outputFile) {
		try {
			FileInputStream fileInputStream = new FileInputStream(archiveFile);
			GZIPInputStream archiveInputStream = new GZIPInputStream(fileInputStream);
			FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
			byte[] buffer = new byte[1024];
			int len;
			while((len = archiveInputStream.read(buffer)) != -1){
				fileOutputStream.write(buffer, 0, len);
			}
			fileOutputStream.close();
			archiveInputStream.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
	public static void compressGzipFile(String inputfile, String archiveFile) {
		try {
			FileInputStream fileInputStream = new FileInputStream(inputfile);
			FileOutputStream fileOutputStream = new FileOutputStream(archiveFile);
			GZIPOutputStream archiveOutputStream = new GZIPOutputStream(fileOutputStream);
			byte[] buffer = new byte[1024];
			int len;
			while((len=fileInputStream.read(buffer)) != -1){
				archiveOutputStream.write(buffer, 0, len);
			}
			archiveOutputStream.close();
			fileOutputStream.close();
			fileInputStream.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
}