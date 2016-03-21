import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

import javax.swing.JOptionPane;

public class Cutter {
	public static void ByteCutter(File input,File output,int start,int end){
		try {
			byte[] b = Files.readAllBytes(input.toPath());
			byte[] o = new byte[end-start+1];
			int j=0;
			for(int i = start-1;i<end;i++){
				o[j] = b[i];
				j++;
			}
			FileOutputStream fos = new FileOutputStream(output);
			fos.write(o);
			fos.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
}
