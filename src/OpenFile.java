import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.ssl.OpenSSL;


public class OpenFile extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField DBHex;
	private JTextField KeyHex;
	private JFileChooser filechooser;
	private File AttachedDBFile;
	private File AttachedKeyFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OpenFile frame = new OpenFile();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public OpenFile() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		DBHex = new JTextField();
		DBHex.setEditable(false);
		DBHex.setColumns(10);
		
		JButton btnOpenDatabase = new JButton("Open Database");
		btnOpenDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DBHex.setText("Processing Please Wait...");
				filechooser = new JFileChooser();
				filechooser.addChoosableFileFilter(new FileFilter() {
					
					@Override
					public String getDescription() {
						 return "Whatsapp Databases";
					}
					
					@Override
					public boolean accept(File arg0) {
						if (arg0.isDirectory()) {
				            return true;
				        }

				        String extension = getExtension(arg0);
				        if (extension != null) {
				        	if (extension.equals("crypt8") || extension.equals("crypt7") || extension.equals("crypt5") || extension.equals("db")) {
				        		return true;
				            } else {
				                return false;
				            }
				        }
						return false;
					}
				});
				filechooser.setAcceptAllFileFilterUsed(false);
				int returnVal = filechooser.showDialog(null,"Select");
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					AttachedDBFile = filechooser.getSelectedFile();
					try {
						HexDump.hexDump(new java.io.PrintStream("dumpiv.txt"), new File(AttachedDBFile.getAbsolutePath()));
						Cutter.ByteCutter(new File("dumpiv.txt"), new File("iv.txt"), 103, 134);
						DBHex.setText(AttachedDBFile.getAbsolutePath());
						Runtime.getRuntime().exec("dd if=\""+AttachedDBFile.getAbsolutePath()+"\" of="+AttachedDBFile.getName()+".nohdr ibs=67 skip=1");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{
					DBHex.setText("File not Loaded");
				}
				filechooser.setSelectedFile(null);
			}
		});
		
		KeyHex = new JTextField();
		KeyHex.setEditable(false);
		KeyHex.setColumns(10);
		
		JButton btnOpenKey = new JButton("Open Key");
		btnOpenKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				KeyHex.setText("Processing Please Wait...");
				filechooser = new JFileChooser();
				filechooser.addChoosableFileFilter(new FileFilter() {
					
					@Override
					public String getDescription() {
						 return "Whatsapp Key";
					}
					
					@Override
					public boolean accept(File arg0) {
						if (arg0.isDirectory()) {
				            return true;
				        }

				        String key = arg0.getName();
				        if (key != null) {
				        	if (key.equals("key")) {
				        		return true;
				            } else {
				                return false;
				            }
				        }
						return false;
					}
				});
				filechooser.setAcceptAllFileFilterUsed(false);
				int returnVal = filechooser.showDialog(null,"Select");
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					AttachedKeyFile = filechooser.getSelectedFile();
					try {
						HexDump.hexDump(new java.io.PrintStream("dumpkey.txt"), new File(AttachedKeyFile.getAbsolutePath()));
						Cutter.ByteCutter(new File("dumpkey.txt"), new File("key.txt"), 253, 316);
						KeyHex.setText(AttachedKeyFile.getAbsolutePath());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else{
					KeyHex.setText("File not Loaded");
				}
				filechooser.setSelectedFile(null);
			}
		});
		
		JLabel lblIv = new JLabel("IV");
		
		JLabel lblKey = new JLabel("Key");
		
		JButton btnDecryptDb = new JButton("Decrypt DB");
		btnDecryptDb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					byte[] key = null;
					key = Files.readAllBytes(new File("key.txt").toPath());
					
					byte[] iv = null;
					iv = Files.readAllBytes(new File("iv.txt").toPath());
					
					//Decryption of Selected DB
					InputStream fout;
					fout = OpenSSL.decrypt("aes-256-cbc", key, iv, new FileInputStream(new File("msgstore.db.crypt8.nohdr")));
					StreamToFile.InputStreamToFile(fout, new File("msgstore.gz"));
					
					//Extracting Decrypted File to .db
					GZIP.decompressGzipFile("msgstore.gz", "msgstore.db");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblKey)
								.addComponent(lblIv)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(KeyHex, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
										.addComponent(DBHex, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE))
									.addGap(10)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(btnOpenKey, GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
										.addComponent(btnOpenDatabase, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(46)
							.addComponent(btnDecryptDb)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(21)
					.addComponent(lblIv)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(DBHex, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnOpenDatabase))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblKey)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(KeyHex, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnOpenKey))
					.addPreferredGap(ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
					.addComponent(btnDecryptDb)
					.addGap(45))
		);
		contentPane.setLayout(gl_contentPane);
	}
	public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
