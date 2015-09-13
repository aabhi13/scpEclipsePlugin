package com.abhsinh2.scpplugin.ui.contributors;

import java.util.Collections;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * Context Menu on View to show View Button. View Button is used to view
 * selected location details.
 * 
 * @author abhsinh2
 * 
 */
public class ViewLocationContributionItem extends ContributionItem {
	private final IViewSite viewSite;
	private final IHandler handler;
	boolean enabled = false;
	private MenuItem menuItem;
	private ToolItem toolItem;

	public ViewLocationContributionItem(IViewSite viewSite, IHandler handler) {
		this.handler = handler;
		this.viewSite = viewSite;
		viewSite.getSelectionProvider().addSelectionChangedListener(
				new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						ISelection selection = event.getSelection();
						if (selection instanceof IStructuredSelection) {
							IStructuredSelection strucSel = (IStructuredSelection) selection;

							if (!strucSel.isEmpty() && strucSel.size() < 2) {
								enabled = true;
								updateEnablement();
							}
						}
					}
				});
	}

	public void fill(Menu menu, int index) {
		menuItem = new MenuItem(menu, SWT.NONE, index);
		menuItem.setText("View");
		menuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				run();
			}
		});
		updateEnablement();
	}

	public void fill(ToolBar parent, int index) {
		toolItem = new ToolItem(parent, SWT.NONE, index);
		toolItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				run();
			}
		});
		updateEnablement();
	}

	private void updateEnablement() {
		Image image = PlatformUI
				.getWorkbench()
				.getSharedImages()
				.getImage(
						enabled ? ISharedImages.IMG_TOOL_COPY
								: ISharedImages.IMG_TOOL_DELETE_DISABLED);
		if (menuItem != null) {
			// menuItem.setImage(image);
			menuItem.setEnabled(enabled);
		}

		if (toolItem != null) {
			// toolItem.setImage(image);
			toolItem.setEnabled(enabled);
		}
	}

	public void run() {
		// Setup execution context
		final IHandlerService handlerService = (IHandlerService) viewSite
				.getService(IHandlerService.class);
		IEvaluationContext evaluationContext = handlerService
				.createContextSnapshot(true);
		ExecutionEvent event = new ExecutionEvent(null, Collections.EMPTY_MAP,
				null, evaluationContext);

		try {
			handler.execute(event);
		} catch (ExecutionException e) {

		}
	}
}
