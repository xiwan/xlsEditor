package Composites;

import java.io.File;
import java.util.ArrayList;

import libs.XlsTable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;

import xlsEditor.MainApp;

public class MainTab extends Composite {
	private Label label;
	
	private String selectedDir;
	private String defaultDirPath = "/Users/xi.a.wan/Documents/dev/svn/projectX";
	private static ArrayList<String> filteredFiles = new ArrayList<String>();
	
	public static ArrayList<String> getFilteredFiles() {
		return filteredFiles;
	}

	public static void setFilteredFiles(ArrayList<String> filteredFiles) {
		MainTab.filteredFiles = filteredFiles;
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MainTab(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new GridLayout(1, false));
		this.setSize(650, 600);
		
		label = new Label(this, SWT.BORDER | SWT.WRAP);
	    label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
	    label.setText("Select a director by clicking the buttons below.");
	    
	    Button button = new Button(this, SWT.PUSH);
	    button.setText("Browse...");
	    button.addSelectionListener(new SelectionAdapter(){
	    	@Override
			public void widgetSelected(SelectionEvent e) {
	    		DirectoryDialog dirDialog = new DirectoryDialog(MainApp.shell);
	    		dirDialog.setFilterPath(defaultDirPath);
	    		dirDialog.setMessage("选择工作文件夹");

	    		String dir = dirDialog.open();
	    		if(dir != null) {
	    			filteredFiles.clear();
	    			dirDialog.setText(dir);
	    			selectedDir = dirDialog.getFilterPath();
	    			traverse(new File(selectedDir));
	    			MainApp.filteredFiles = filteredFiles;
	    			XlsTable.loadXlsToMem(filteredFiles);
	    		}
	    	};
	    });
	}
	
	public void traverse(File dir){
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; children != null && i < children.length; i++) {
	            traverse(new File(dir, children[i]));
	        }
	    }
	    if (dir.isFile()) {
	        if (dir.getName().endsWith(".xls")) {
	            filteredFiles.add(dir.getAbsolutePath());
	        }
	    }
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
