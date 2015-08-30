package com.abhsinh2.scpplugin.ui.view;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import com.abhsinh2.scpplugin.ui.provider.SCPLocationTableLabelProvider;

public class SCPView extends ViewPart {

	public static final String ID = "com.abhsinh2.plugin.scp.SCPView";

	private TableViewer viewer;
	
	private TableColumn nameColumn;
	private TableColumn remoteAddressColumn;	
	private TableColumn remoteLocationColumn;

	public SCPView() {
	}

	public void createPartControl(Composite parent) {
		createTableViewer(parent);
	}

	private void createTableViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.MULTI | SWT.FULL_SELECTION);
		final Table table = viewer.getTable();

		TableColumnLayout layout = new TableColumnLayout();
		parent.setLayout(layout);

		nameColumn = new TableColumn(table, SWT.LEFT);
		nameColumn.setText("Name");
		layout.setColumnData(nameColumn, new ColumnWeightData(4));
		
		remoteAddressColumn = new TableColumn(table, SWT.LEFT);
		remoteAddressColumn.setText("Machine");
		layout.setColumnData(remoteAddressColumn, new ColumnPixelData(18));		

		remoteLocationColumn = new TableColumn(table, SWT.LEFT);
		remoteLocationColumn.setText("Location");
		layout.setColumnData(remoteLocationColumn, new ColumnWeightData(9));

		table.setHeaderVisible(true);
		table.setLinesVisible(false);

		viewer.setLabelProvider(new SCPLocationTableLabelProvider());

		getSite().setSelectionProvider(viewer);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
