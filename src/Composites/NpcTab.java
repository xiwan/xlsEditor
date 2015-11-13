package Composites;

import libs.XlsTable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import xlsEditor.MainApp;

public class NpcTab extends Composite {
	private Label label;
	
	private Table xlsTable;
	private Button btnSave;
	private Button buttonSelectFile;

	private String selectedFile;
	private String defaultDirPath = "/Users/xi.a.wan/Documents/dev/svn/projectX/策划文件";
	private String fileSelectedPath = "";
	
	private String sheetName = "AIConfig";
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public NpcTab(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new GridLayout(1, false));
		this.setSize(650, 600);
		
		final XlsTable xlsTableParser = new XlsTable();

	    label = new Label(this, SWT.BORDER | SWT.WRAP);
	    label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
	    //label.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
	    label.setText("Select a dir/file by clicking the buttons below.");
	    
	    buttonSelectFile = new Button(this, SWT.PUSH);
	    buttonSelectFile.setText("Select a file/multiple files");
	    buttonSelectFile.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				FileDialog fileDialog = new FileDialog(MainApp.shell, SWT.SINGLE);
				fileDialog.setFilterPath(defaultDirPath);
				fileDialog.setFilterExtensions(new String[]{"*.xls", "*.xlsx"});
		        fileDialog.setFilterNames(new String[]{ "xls Document", "xlsx Document"});
		        
		        String firstFile = fileDialog.open();
		        if(firstFile != null) {
		        	//defaultDirPath = fileDialog.getFilterPath();
		            String[] selectedFiles = fileDialog.getFileNames();
		            StringBuffer sb = new StringBuffer("Selected files under dir " + fileDialog.getFilterPath() +  ": \n");
		            for(int i=0; i<selectedFiles.length; i++) {
		            	sb.append(selectedFiles[i] + "\n");
		            }
		            label.setText(sb.toString());
		            selectedFile = selectedFiles[0];
		            fileSelectedPath = fileDialog.getFilterPath() + '/' + selectedFile;
		            
		            xlsTable.clearAll(); 
		            xlsTableParser.importContents(fileSelectedPath);
		            xlsTableParser.loadToTable(xlsTable);
		    	    
		        }    
			}
	    });
	    
	    Button defaultFile = new Button(this, SWT.PUSH);
	    defaultFile.setText("load defaultFile");
	    defaultFile.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				xlsTable.clearAll(); 
	            xlsTableParser.importContentsBySheet(sheetName);
	            xlsTableParser.loadToTable(xlsTable);
			}
	    });
	    new Label(this, SWT.NONE);
	    
	    xlsTable = new Table(this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION | SWT.VIRTUAL);
	    xlsTable.setHeaderVisible(true);
	    xlsTable.setLinesVisible(true);
	    xlsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	    new Label(this, SWT.NONE);
	    
	    btnSave = new Button(this, SWT.NONE);
	    btnSave.setText("save");
	    new Label(this, SWT.NONE);
	    btnSave.addListener(SWT.MouseUp, new Listener(){
			@Override
			public void handleEvent(Event evt) {
				// TODO Auto-generated method stub
				if (selectedFile != null && !selectedFile.isEmpty()) {
					String outputPath = "/tmp/" + selectedFile;
					xlsTableParser.exportContents(outputPath);
					label.setText("file has been saved to " + outputPath);					
				}

			}
	    });
	    new Label(this, SWT.NONE);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
