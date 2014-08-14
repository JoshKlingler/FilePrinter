import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextArea;
import javax.swing.JTextField;



@SuppressWarnings("serial")
public class FilePrinter extends JFrame {
	
	private JPanel contentPane;
	private JFileChooser fileChooser = new JFileChooser();
	private JLabel lblFolderName = new JLabel("No folder selected.");
	private Desktop desktop = Desktop.getDesktop();
	private JTextArea textArea = new JTextArea();
	private JTextField tfNumPrint;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		// Change look and feel of application
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FilePrinter frame = new FilePrinter();
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
	public FilePrinter() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 373, 309);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblFolder = new JLabel("Folder:");
		lblFolder.setBounds(30, 36, 56, 16);
		contentPane.add(lblFolder);
		
		lblFolderName.setBounds(185, 36, 235, 16);
		contentPane.add(lblFolderName);
		
		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openSaveDialog(fileChooser, lblFolderName, true);
			}
		});
		btnBrowse.setBounds(76, 32, 97, 25);
		contentPane.add(btnBrowse);
		
		JLabel lblNumberOfCopies = new JLabel("Number of copies:");
		lblNumberOfCopies.setBounds(30, 65, 143, 16);
		contentPane.add(lblNumberOfCopies);
		
		JButton btnPrint = new JButton("Print");
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tfNumPrint.getText();
				System.out.println(fileChooser.getSelectedFile());
				printFilesInFolder(fileChooser.getSelectedFile(), Integer.parseInt( tfNumPrint.getText() ));
			}
		});
		btnPrint.setBounds(84, 196, 204, 53);
		contentPane.add(btnPrint);
		
		textArea.setBounds(69, 97, 235, 86);
		contentPane.add(textArea);
		
		tfNumPrint = new JTextField();
		tfNumPrint.setBounds(153, 65, 56, 22);
		contentPane.add(tfNumPrint);
		tfNumPrint.setColumns(10);
	}

	/**
	 * Open save dialog and change label to name of directory if a directory was chosen
	 * @param saveLoc Stores location for file to be saved to.
	 * @param label Label that will change to show the selected file path.
	 */
	private void openSaveDialog(JFileChooser fileChooser, JLabel label, boolean directoryOnly){
		// If selecting a save location for either the comment sheet or new spreadsheet,
		// the user can only select a folder. If selecting an existing spreadsheet they 
		// can only select the CSV file.
		if(directoryOnly){
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		else{
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
			fileChooser.setFileFilter(filter);
		}

		// Open file dialog window
		int returnVal = fileChooser.showOpenDialog(this);

		if(returnVal == JFileChooser.APPROVE_OPTION){
			File saveLoc = fileChooser.getSelectedFile();
			int pathLength = saveLoc.getPath().length();
			String labelMessage;

			// If path is too large to fit, cut off
			// text to the left and replace with "..."
			if(pathLength > 25){ 
				labelMessage = "...";
				labelMessage += saveLoc.getPath().substring(pathLength - 25);
			}
			else{
				labelMessage = saveLoc.getPath();
			}
			label.setText(labelMessage);
		}
	}
	
	/**
	 * Prints every file in a folder.
	 * @param folder Folder containing files to print
	 * @param numPrints number of copies to print
	 */
	private void printFilesInFolder(File folder, int numPrints){
		File[] files = folder.listFiles();
		
		for(File f:files){							// Print each file...
			for(int i = 0; i < numPrints; i++){     // ...specified number of times
				try {
					textArea.setText("Printing " + f.getName() + "...");
					System.out.println(f.getName());
					desktop.print(f);
					Thread.sleep(3000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		textArea.setText("Done printing.");
	}
}
