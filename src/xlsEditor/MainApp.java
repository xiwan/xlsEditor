package xlsEditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.layout.FillLayout;

import Composites.NpcTab;

public class MainApp {

	public static Shell shell;

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
		
		TabItem npcTab = new TabItem(tabs, SWT.NONE);
		npcTab.setText("npc");
		
		NpcTab npc = new NpcTab(display, tabs, SWT.NONE);
		npcTab.setControl(npc);
		
	}

}
