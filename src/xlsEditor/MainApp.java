package xlsEditor;

import java.util.ArrayList;

import libs.XlsTable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.layout.FillLayout;

import Composites.MainTab;
import Composites.NpcTab;

public class MainApp {

	public static Shell shell;
	public static ArrayList<String> filteredFiles = new ArrayList<String>();

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainApp window = new MainApp();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents(display);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(Display display) {
		shell = new Shell(display);
		shell.setSize(850, 600);
		shell.setText("策划工具");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		TabFolder tabs = new TabFolder(shell, SWT.NONE);
		TabItem mainTab = new TabItem(tabs, SWT.NONE);
		mainTab.setText("main");
		
		MainTab main = new MainTab(tabs, SWT.NONE);
		mainTab.setControl(main);
		
		TabItem npcTab = new TabItem(tabs, SWT.NONE);
		npcTab.setText("npc");
		
		NpcTab npc = new NpcTab(tabs, SWT.NONE);
		npcTab.setControl(npc);
		
	}

}
