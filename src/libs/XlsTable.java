package libs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import xlsEditor.MainApp;
//import jxl.Cell;
//import jxl.CellType;
//import jxl.Sheet;
//import jxl.Workbook;
//import jxl.WorkbookSettings;
//import jxl.read.biff.BiffException;
//import jxl.write.Label;
//import jxl.write.WritableSheet;
//import jxl.write.WritableWorkbook;
//import jxl.write.WriteException;
//import jxl.write.biff.RowsExceededException;

public class XlsTable {
	
    private Vector headers = new Vector();
    private Vector data = new Vector(); 
    private String sheetName;
    
    public static HashMap<String, Vector> sheetsMap = new HashMap<String, Vector>();
 
    public Vector getHeaders() {
		return headers;
	}

	public int getHeadersSize() {
		return this.headers.size();
	}

//	public void setHeaders(Vector headers) {
//		this.headers = headers;
//	}

	public Vector getData(int row) {
		return (Vector) this.data.get(row);
	}
	
	public int getDataSize() {
		return this.data.size();
	}
	
	public void setData(int row, Vector dd) {
		this.data.set(row, dd);
		this.sheetsMap.put(this.sheetName, this.data);
	}
	
	public void setData(int row, int col, String update) {
		//this.data = data;
		Vector dd = (Vector) this.data.get(row);
  	  	dd.set(col, update);
  	  	this.data.set(row, dd);
  	  	this.sheetsMap.put(this.sheetName, this.data);
	}
	
	public void appendData(Vector dd) {
		this.data.add(dd);
		this.sheetsMap.put(this.sheetName, this.data);
	}
	
	public void removeData(int row) {
		this.data.remove(row);
		this.sheetsMap.put(this.sheetName, this.data);
	}
	
	public static void loadXlsToMem(ArrayList<String> files) {
		try {
			sheetsMap.clear();
			for(String filePath: files) {
				FileInputStream fis = new FileInputStream(new File(filePath));
				XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
				int sheetLength = myWorkBook.getNumberOfSheets();
				//Workbook w = Workbook.getWorkbook(inputWorkbook);
				//Sheet[] sheets = myWorkBook.gets.getSheets();
				
				
				for (int idx = 0; idx < sheetLength; idx ++) {
	            	//Sheet sheet = w.getSheet(idx);
	            	XSSFSheet sheet = myWorkBook.getSheetAt(idx);
	            	String sheetName = myWorkBook.getSheetName(idx);
	            	Vector sheetData = new Vector();
	            	
	            	// Traversing over each row of XLSX file
	            	Iterator<Row> rowIterator = sheet.iterator();
	            	while (rowIterator.hasNext()) {
	            		Row row = rowIterator.next();
	            		Vector d = new Vector();
	            		// For each row, iterate through each columns
	            		Iterator<Cell> cellIterator = row.cellIterator();
	            		while (cellIterator.hasNext()) {
	            			Cell cell = cellIterator.next();
	            			switch (cell.getCellType()) {
	            			case Cell.CELL_TYPE_STRING:
	            				d.add(cell.getStringCellValue());
	            				break;
	            			case Cell.CELL_TYPE_NUMERIC:
	            				d.add(cell.getNumericCellValue());
	            				break;
	            			case Cell.CELL_TYPE_BOOLEAN:
	            				d.add(cell.getBooleanCellValue());
	            				break;
	            			case Cell.CELL_TYPE_BLANK:
	            				d.add("");
	            				break;
	            			default:
	            				d.add("");
	            			}	
	            		}
	            		sheetData.add(d);	
	            	}
	            	sheetsMap.put(sheetName, sheetData);
	            }
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadToTable(final Table table) {
		if (data == null || data.size() == 0) {
			MessageBox dialog = new MessageBox(MainApp.shell, SWT.OK);
			dialog.setText("没有数据");
			dialog.setMessage("有可能没有设置xls的工作目录!");
			dialog.open();
			return;
		};
		// fill the headers and data
        for (int i = 0; i < headers.size(); i++) {
        	TableColumn column = new TableColumn(table, SWT.NONE, i);
        	column.setText(headers.get(i).toString());
        	table.getColumn(i).pack();
        }
        
        for (int i = 0; i < data.size(); i++) {
        	TableItem item = new TableItem(table, SWT.NONE, i);
        	Vector dd = (Vector)data.get(i);
        	for (int j = 0; j < headers.size(); j++) {
        		System.out.println(dd);
        		System.out.println(dd.size() + " " + j + " " + i);
            	item.setText(j, dd.get(j).toString());
        	}	
        }
        
        final TableEditor editor = new TableEditor(table);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;        
        table.addListener(SWT.MouseDoubleClick, new Listener(){
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
	    		Rectangle clientArea = table.getClientArea();
	    		Point pt = new Point(event.x, event.y);
	            int index = table.getTopIndex();
	            while (index < table.getItemCount()) {
	            	boolean visible = false;
	            	final int row = index;
	            	final TableItem item = table.getItem(index);
	            	for (int i = 0; i < table.getColumnCount(); i++) {
	            		Rectangle rect = item.getBounds(i);
	            		if (rect.contains(pt)) {
	            			final int column = i;
	                        final Text text = new Text(table, SWT.NONE);
	                        Listener textListener = new Listener() {
								@Override
								public void handleEvent(Event evt) {
									// TODO Auto-generated method stub
									switch (evt.type) {
					                  case SWT.FocusOut:
					                	  item.setText(column, text.getText());
					                	  XlsTable.this.setData(row, column, text.getText());;
					                	  text.dispose();
					                	  break;
					                  case SWT.Traverse:
					                	  switch (evt.detail) {
					                	  case SWT.TRAVERSE_RETURN:
					                		  item.setText(column, text.getText());
					                		  // FALL THROUGH
					                	  case SWT.TRAVERSE_ESCAPE:
					                		  text.dispose();
					                		  evt.doit = false;
					                	  }
					                  break;
									}
								}
	                 
	                        };
	                        text.addListener(SWT.FocusOut, textListener);
	                        text.addListener(SWT.Traverse, textListener);
	                        editor.setEditor(text, item, i);
	                        text.setText(item.getText(i));
	                        text.selectAll();
	                        text.setFocus();
	                        return;
	            		}
	            		if (!visible && rect.intersects(clientArea)) {
	            			visible = true;
	            		}
	            	}
	            	if (!visible) return;
	            	index++;
	            }
			}
        	
        });        
	}
	
	public void importContentsBySheet(String name) {
		if (sheetsMap.get(name) == null || sheetsMap.get(name).isEmpty()) {
			return;
		}
		System.out.println("bingo " + name);
        this.sheetName = name;
		this.data = sheetsMap.get(name);
		if (data == null) {
			return;
		}
		this.headers = (Vector)data.get(3);
	}

	public void importContents(String fileSelectedPath) {
    	try {
    		sheetsMap.clear();
			FileInputStream fis = new FileInputStream(new File(fileSelectedPath));
			XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
			int sheetLength = myWorkBook.getNumberOfSheets();
			
            String sheetName = "";
            
            for (int idx = 0; idx < sheetLength; idx ++) {
            	XSSFSheet sheet = myWorkBook.getSheetAt(idx);
            	sheetName = myWorkBook.getSheetName(idx);
            	Vector sheetData = new Vector();
            	
            	// Traversing over each row of XLSX file
            	Iterator<Row> rowIterator = sheet.iterator();
            	while (rowIterator.hasNext()) {
            		Row row = rowIterator.next();
            		Vector d = new Vector();
            		// For each row, iterate through each columns
            		Iterator<Cell> cellIterator = row.cellIterator();
            		while (cellIterator.hasNext()) {
            			Cell cell = cellIterator.next();

            			switch (cell.getCellType()) {
            			case Cell.CELL_TYPE_STRING:
            				d.add(cell.getStringCellValue());
            				break;
            			case Cell.CELL_TYPE_NUMERIC:
            				d.add(cell.getNumericCellValue());
            				break;
            			case Cell.CELL_TYPE_BOOLEAN:
            				d.add(cell.getBooleanCellValue());
            				break;
            			case Cell.CELL_TYPE_BLANK:
            				d.add("");
            				break;
            			default:
            				d.add("");
            			}	
            		}
            		sheetData.add(d);	
            	}

                sheetsMap.put(sheetName, sheetData);
            }
    		
            this.sheetName = sheetName;
    		this.data = sheetsMap.get(sheetName);
    		if (data == null) {
    			return;
    		}
    		this.headers = (Vector)data.get(3);
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    }
	
	
    
    public void exportContents(String outputPath){
    	try { 
    		//FileInputStream fis = new FileInputStream(new File(outputPath));
    		XSSFWorkbook workbook = new XSSFWorkbook ();
    		int idx = 0;
    		for (Entry<String, Vector> entry : sheetsMap.entrySet()) {
    			Vector dd = entry.getValue();
    			DefaultTableModel model = new DefaultTableModel(dd, (Vector)data.get(3));
    			XSSFSheet sheet = workbook.createSheet(entry.getKey());
    			for (int i=0; i < model.getRowCount(); i++) {
    				XSSFRow row = sheet.createRow(i);
    				for (int j=0; j < model.getColumnCount(); j++) {
    					XSSFCell rlc = row.createCell(j);
    					if (model.getValueAt(i, j) != null) {
    						rlc.setCellValue(model.getValueAt(i, j).toString());
    					}
    				}
    	        } 
    			
    		}
    		
    		// open an OutputStream to save written data into XLSX file
            FileOutputStream fos = new FileOutputStream(new File(outputPath));
            workbook.write(fos);
            fos.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
          	
    };


}
