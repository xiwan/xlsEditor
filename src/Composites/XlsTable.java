package Composites;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class XlsTable {
	
    private Vector headers = new Vector();
    private Vector data = new Vector();
    
    public void importContents(String fileSelectedPath, final Table table) {
    	try {
            File inputWorkbook = new File(fileSelectedPath);
            Workbook w = Workbook.getWorkbook(inputWorkbook);
            Sheet sheet = w.getSheet(0);
            int rows = sheet.getRows();
            int cols = sheet.getColumns();
            
            headers.clear(); 
            data.clear();

            for (int i = 0; i < rows; i++) {
            	Vector d = new Vector();
            	for (int j = 0; j < cols; j++) {
            		Cell cell = sheet.getCell(j, i);
            		CellType type = cell.getType();
            		if (type == CellType.LABEL) {
            			d.add(cell.getContents().toString());
            		}
            		if (type == CellType.NUMBER) {
            			d.add(cell.getContents().toString());
            		}			            		
	            	if (i == 3) {
	            		headers.add(cell.getContents());
	            	}
            	}
            	data.add(d);	
            }
            
            // fill the headers and data
            for (int i = 0; i < headers.size(); i++) {
            	TableColumn column = new TableColumn(table, SWT.NONE, i);
            	column.setText(headers.get(i).toString());
            	table.getColumn(i).pack();
            } 
            for (int i = 0; i < sheet.getRows(); i++) {
            	TableItem item = new TableItem(table, SWT.NONE, i);
            	for (int j = 0; j < sheet.getColumns(); j++) {
            		Cell cell = sheet.getCell(j, i);
	            	item.setText(j, cell.getContents());
	            	//item.addListener(eventType, listener);
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
						                	  Vector dd = (Vector) data.get(row);
						                	  dd.set(column, text.getText());
						                	  data.set(row, dd);
						                	  //System.out.println(column + " "+ row + " = " + dd.get(column));
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
            

        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    }
    
    public void exportContents(String outputPath){
    	try { 
	        DefaultTableModel model = new DefaultTableModel(data, headers);
	        WritableWorkbook workbook = Workbook.createWorkbook(new File(outputPath));
	        WritableSheet sheet = workbook.createSheet("First Sheet", 0);
	        for (int i=0; i < model.getRowCount(); i++) {
				for (int j=0; j < model.getColumnCount(); j++) {
					if (model.getValueAt(i, j) != null) {
						Label content = new Label(j, i, model.getValueAt(i, j).toString());
						sheet.addCell(content);
					}
				}
	        } 

//            JTable table = new JTable();
            
//            table.setModel(model);
//            table.setAutoCreateRowSorter(true);
//            model = new DefaultTableModel(data, headers);
//            table.setModel(model);
//            JScrollPane scroll = new JScrollPane(table);
//            JFrame f = new JFrame();
//            f.getContentPane().add(scroll);
//            f.setSize(400, 200);
//            f.setResizable(true);
//            f.setVisible(true);
	        
			workbook.write();
			workbook.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          	
    };


}
