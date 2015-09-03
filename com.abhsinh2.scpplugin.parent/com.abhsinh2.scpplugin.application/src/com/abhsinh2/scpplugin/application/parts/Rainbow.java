package com.abhsinh2.scpplugin.application.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.services.internal.events.EventBroker;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.abhsinh2.scpplugin.application.IStringService;
import com.abhsinh2.scpplugin.application.StringService;

public class Rainbow {
	private static final Object[] rainbow = { "Red", "Orange", "Yellow",
			"Green", "Blue", "Indigo", "Violet" };

	@Inject
	private ESelectionService selectionService;
	
	@Inject
	EventBroker eventBroker;
	
	//@Inject
	//private StringService stringService;
	
	@Inject
	private IStringService stringService;

	@PostConstruct
	public void create(Composite parent) {
		ListViewer lv = new ListViewer(parent, SWT.NONE);
		lv.setContentProvider(new ArrayContentProvider());
		lv.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				//selectionService.setSelection(event.getSelection());
				
				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				Object colour = sel.getFirstElement();
				eventBroker.post("rainbow/colour",colour);
				stringService.process(colour.toString());
			}
		});
		lv.setInput(rainbow);
	}
}
