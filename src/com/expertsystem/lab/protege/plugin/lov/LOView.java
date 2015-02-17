package com.expertsystem.lab.protege.plugin.lov;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.apache.log4j.Logger;
import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.owl.model.selection.OWLSelectionModel;
import org.protege.editor.owl.model.selection.OWLSelectionModelListener;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.model.OWLEntity;

import com.expertsystem.lab.lov.ResultsListItem;

/**
 * 
 * View plugin which includes Linked Open Vocabularies (LOV) functionality in Protege
 * 
 * @author Nuria Garcia <ngarcia@isoco.com>
 * @author Ghislain Atemezing <ghislain.atemezing@gmail.com> 
 * @author boricles <boricles@gmail.com> 
 *
 */

public class LOView extends AbstractOWLViewComponent {
	private static final long serialVersionUID = 1505057428784911280L;
	private Logger logger = Logger.getLogger(LOView.class);
	private LOVSelectionPanel lsp;
	private LOVResultsPanel lrp;
	private JButton selectionButton;
	private OWLSelectionModel selectionModel;
	
	private JLabel prueba;
	private OWLSelectionModelListener listener = new OWLSelectionModelListener() {

		@Override
		public void selectionChanged() throws Exception {
			OWLEntity entity = getOWLWorkspace().getOWLSelectionModel().getSelectedEntity();
			updateSelection(entity);
		}
	};
	
	private ActionListener button_listener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == selectionButton){
				updateListLOV();
				lrp.updateLOVResults();
			}
			
		}

		private void updateListLOV() {
			prueba.setText("(1562 results Person)");			
		}
	};
	
	private ActionListener add_listener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (lrp.getList_results().getSelectedValue() instanceof ResultsListItem) {
				ResultsListItem item = (ResultsListItem) lrp.getList_results().getSelectedValue();
				handleAdd(item);
			}
		}

		private void handleAdd(ResultsListItem item) {
			prueba.setText("(1562 results Person) Entity selected " + item.getPrefix() + ":" + item.getName());					
		}
	};

	@Override
	protected void initialiseOWLView() throws Exception {
		logger.info("Initializing LOV view");
		setLayout(new BorderLayout());
		selectionButton = new JButton("Search...");
		selectionButton.setToolTipText("Search LOV...");
		selectionButton.addActionListener(button_listener);
		lsp = new LOVSelectionPanel(selectionButton);
		lrp = new LOVResultsPanel(add_listener);
		lsp.setBorder(ComponentFactory.createTitledBorder("LOV Selection Entity"));
		lrp.setBorder(ComponentFactory.createTitledBorder("LOV Results"));
		add(lsp, BorderLayout.NORTH);	
		add(lrp, BorderLayout.CENTER);
		prueba = new JLabel("OK Status ");
		add(prueba, BorderLayout.SOUTH);	
		selectionModel = getOWLWorkspace().getOWLSelectionModel();
		selectionModel.addListener(listener);		
	}
	
	@Override
	protected void disposeOWLView() {
		selectionModel.removeListener(listener);
		selectionButton.removeActionListener(button_listener);
	}
	
	public void updateSelection (OWLEntity e){
		if (e != null) {
			String type = e.getEntityType().toString();
			String entityName = e.getIRI().getFragment();
			String entityLabel = getOWLModelManager().getRendering(e);
			lsp.setLocal_name_value(entityName);
			lsp.setLabel_value(entityLabel);
			lsp.setLocal_name("Local Name: " + entityName);
			lsp.setLabel("         Label : " + entityLabel);
			lsp.setType(type);
			lrp.setLocal_name(entityName);
			lrp.setLabel_name(entityLabel);
			lrp.setType(type);
		}
		else {
			lsp.setLocal_name_value("Thing");
			lsp.setLabel_value("Thing");
			lsp.setLocal_name("Local Name: Thing");
			lsp.setLabel("         Label : Thing");
			lsp.setType("Class");
			lrp.setLocal_name("Thing");
			lrp.setLabel_name("Thing");
			lrp.setType("Class");
		}
	}	
}
