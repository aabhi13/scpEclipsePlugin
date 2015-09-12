package com.abhsinh2.scpplugin.ui.view;

import java.util.Comparator;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;

import com.abhsinh2.scpplugin.ui.actions.SCPViewFilterAction;
import com.abhsinh2.scpplugin.ui.contributors.EditLocationContributionItem;
import com.abhsinh2.scpplugin.ui.contributors.RemoveLocationContributionItem;
import com.abhsinh2.scpplugin.ui.contributors.ViewLocationContributionItem;
import com.abhsinh2.scpplugin.ui.handlers.EditLocationHandler;
import com.abhsinh2.scpplugin.ui.handlers.RemoveLocationHandler;
import com.abhsinh2.scpplugin.ui.handlers.ViewLocationHandler;
import com.abhsinh2.scpplugin.ui.model.SCPLocation;
import com.abhsinh2.scpplugin.ui.model.SCPLocationManager;
import com.abhsinh2.scpplugin.ui.model.local.ISCPLocalLocation;
import com.abhsinh2.scpplugin.ui.provider.SCPLocationTableLabelProvider;
import com.abhsinh2.scpplugin.ui.provider.SCPViewContentProvider;

public class SCPView extends ViewPart {

	public static final String ID = "com.abhsinh2.scpplugin.ui.SCPView";

	private TableViewer tableViewer;

	private TableColumn nameColumn;
	private TableColumn remoteAddressColumn;
	private TableColumn remoteLocationColumn;

	private LocationViewSorter sorter;
	private IMemento memento;
	private SCPViewFilterAction filterAction;

	private IHandler removeHandler;
	private IHandler editHandler;
	private IHandler viewHandler;
	
	private RemoveLocationContributionItem removeContributionItem;
	private EditLocationContributionItem editContributionItem;
	private ViewLocationContributionItem viewContributionItem;
	
	private ISelectionListener pageSelectionListener;

	public SCPView() {
	}

	public void createPartControl(Composite parent) {
		createTableViewer(parent);
		createTableSorter();
		createContributions();
		createContextMenu();
		createToolbarButtons();
		//createViewPulldownMenu();
		hookGlobalHandlers();
		createInlineEditor();
		hookMouse();
		hookPageSelection();
	}

	private void createTableViewer(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.MULTI | SWT.FULL_SELECTION);
		final Table table = tableViewer.getTable();

		TableColumnLayout layout = new TableColumnLayout();
		parent.setLayout(layout);

		nameColumn = new TableColumn(table, SWT.LEFT);
		nameColumn.setText("Name");
		layout.setColumnData(nameColumn, new ColumnWeightData(4));

		remoteAddressColumn = new TableColumn(table, SWT.LEFT);
		remoteAddressColumn.setText("Machine");
		layout.setColumnData(remoteAddressColumn, new ColumnWeightData(9));

		remoteLocationColumn = new TableColumn(table, SWT.LEFT);
		remoteLocationColumn.setText("Location");
		layout.setColumnData(remoteLocationColumn, new ColumnWeightData(18));

		table.setHeaderVisible(true);
		table.setLinesVisible(false);

		tableViewer.setContentProvider(new SCPViewContentProvider());
		tableViewer.setLabelProvider(new SCPLocationTableLabelProvider());
		tableViewer.setInput(SCPLocationManager.getManager());

		getSite().setSelectionProvider(tableViewer);
	}

	private void createTableSorter() {
		Comparator<ISCPLocalLocation> nameComparator = new Comparator<ISCPLocalLocation>() {
			public int compare(ISCPLocalLocation i1, ISCPLocalLocation i2) {
				return i1.getName().compareTo(i2.getName());
			}
		};

		sorter = new LocationViewSorter(tableViewer,
				new TableColumn[] { nameColumn },
				new Comparator[] { nameComparator });

		if (memento != null)
			sorter.init(memento);
		tableViewer.setSorter(sorter);
	}

	private void createContributions() {
		removeHandler = new RemoveLocationHandler();
		editHandler = new EditLocationHandler();
		viewHandler = new ViewLocationHandler();
		
		removeContributionItem = new RemoveLocationContributionItem(
				getViewSite(), removeHandler);
		editContributionItem = new EditLocationContributionItem(
				getViewSite(), editHandler);
		viewContributionItem = new ViewLocationContributionItem(
				getViewSite(), viewHandler);
	}

	private void createContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager m) {
				SCPView.this.fillContextMenu(m);
			}
		});
		Menu menu = menuMgr.createContextMenu(tableViewer.getControl());
		tableViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, tableViewer);
	}

	private void fillContextMenu(IMenuManager menuMgr) {
		//menuMgr.add(new Separator(""));
		menuMgr.add(viewContributionItem);
		menuMgr.add(new Separator("edit"));
		menuMgr.add(editContributionItem);
		//menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void createToolbarButtons() {
		IToolBarManager toolBarMgr = getViewSite().getActionBars()
				.getToolBarManager();
		//toolBarMgr.add(new GroupMarker("edit"));
		toolBarMgr.add(removeContributionItem);
	}

	private void createViewPulldownMenu() {
		IMenuManager menu = getViewSite().getActionBars().getMenuManager();
		filterAction = new SCPViewFilterAction(tableViewer, "Filter...");
		if (memento != null)
			filterAction.init(memento);
		menu.add(filterAction);
	}

	@Override
	public void setFocus() {
		tableViewer.getControl().setFocus();
	}

	public void dispose() {
		if (pageSelectionListener != null)
			getSite().getPage().removePostSelectionListener(
					pageSelectionListener);
		super.dispose();
	}

	/**
	 * Saves the view state such as sort order and filter state within a
	 * memento.
	 */
	public void saveState(IMemento memento) {
		super.saveState(memento);
		//sorter.saveState(memento);
		//filterAction.saveState(memento);
	}

	private void hookPageSelection() {
		pageSelectionListener = new ISelectionListener() {
			public void selectionChanged(IWorkbenchPart part,
					ISelection selection) {
				pageSelectionChanged(part, selection);
			}
		};
		getSite().getPage().addPostSelectionListener(pageSelectionListener);
	}

	protected void pageSelectionChanged(IWorkbenchPart part,
			ISelection selection) {
		if (part == this)
			return;
		if (!(selection instanceof IStructuredSelection))
			return;
		IStructuredSelection sel = (IStructuredSelection) selection;
		ISCPLocalLocation[] items = SCPLocationManager.getManager()
				.existingLocationFor(sel.iterator());
		if (items.length > 0)
			tableViewer.setSelection(new StructuredSelection(items), true);
	}

	private void createInlineEditor() {
		TableViewerColumn column = new TableViewerColumn(tableViewer, nameColumn);

		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return ((SCPLocation) element).getName();
			}
		});

		column.setEditingSupport(new EditingSupport(tableViewer) {
			TextCellEditor editor = null;

			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				if (editor == null) {
					Composite table = (Composite) tableViewer.getControl();
					editor = new TextCellEditor(table);
				}
				return editor;
			}

			protected Object getValue(Object element) {
				return ((ISCPLocalLocation) element).getName();
			}

			protected void setValue(Object element, Object value) {
				((ISCPLocalLocation) element).setName((String) value);
				tableViewer.refresh(element);
			}
		});

		tableViewer.getColumnViewerEditor().addEditorActivationListener(
				new ColumnViewerEditorActivationListener() {
					public void beforeEditorActivated(
							ColumnViewerEditorActivationEvent event) {
						if (event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION) {
							if (!(event.sourceEvent instanceof MouseEvent))
								event.cancel = true;
							else {
								MouseEvent mouseEvent = (MouseEvent) event.sourceEvent;
								if ((mouseEvent.stateMask & SWT.ALT) == 0)
									event.cancel = true;
							}
						} else if (event.eventType != ColumnViewerEditorActivationEvent.PROGRAMMATIC)
							event.cancel = true;
					}

					public void afterEditorActivated(
							ColumnViewerEditorActivationEvent event) {
					}

					public void beforeEditorDeactivated(
							ColumnViewerEditorDeactivationEvent event) {
					}

					public void afterEditorDeactivated(
							ColumnViewerEditorDeactivationEvent event) {
					}
				});
	}

	private void hookGlobalHandlers() {
		final IHandlerService handlerService = (IHandlerService) getViewSite()
				.getService(IHandlerService.class);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			private IHandlerActivation removeActivation;

			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection().isEmpty()) {
					if (removeActivation != null) {
						handlerService.deactivateHandler(removeActivation);
						removeActivation = null;
					}
				} else {
					if (removeActivation == null) {
						removeActivation = handlerService.activateHandler(
								IWorkbenchActionDefinitionIds.DELETE,
								removeHandler);
					}
				}
			}
		});
	}

	private void hookMouse() {
		tableViewer.getTable().addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				com.abhsinh2.scpplugin.ui.util.EditorUtil.openEditor(getSite()
						.getPage(), tableViewer.getSelection());
			}
		});
	}

	/**
	 * Initializes this view with the given view site. A memento is passed to
	 * the view which contains a snapshot of the views state such as sort order
	 * and filter state from a previous session. In our case, the sort and
	 * filter state cannot be initialized immediately, so we cache the memento
	 * for later.
	 */
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);	
		this.memento = memento;
	}

	/**
	 * Answer the currently selected favorite items
	 */
	public IStructuredSelection getSelection() {
		return (IStructuredSelection) tableViewer.getSelection();
	}

	/**
	 * Report selection changes to the specified listener
	 * 
	 * @param selectionListener
	 *            the listener (not <code>null</code>)
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		tableViewer.addSelectionChangedListener(listener);
	}
}
