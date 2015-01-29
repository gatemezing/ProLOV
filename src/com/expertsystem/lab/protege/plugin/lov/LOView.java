package com.expertsystem.lab.protege.plugin.lov;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import org.apache.log4j.Logger;
import org.protege.editor.owl.model.selection.OWLSelectionModel;
import org.protege.editor.owl.model.selection.OWLSelectionModelListener;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.model.OWLEntity;

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
	private JLabel label;
	private OWLSelectionModel selectionModel;
	private OWLSelectionModelListener listener = new OWLSelectionModelListener() {

		@Override
		public void selectionChanged() throws Exception {
			OWLEntity entity = getOWLWorkspace().getOWLSelectionModel().getSelectedEntity();
			updateView(entity);
		}
	};

	@Override
	protected void initialiseOWLView() throws Exception {
		logger.info("Initializing LOV view");
		label = new JLabel("Hello world");
		setLayout(new BorderLayout());
		add(label, BorderLayout.CENTER);
		selectionModel = getOWLWorkspace().getOWLSelectionModel();
		selectionModel.addListener(listener);
	}
	
	@Override
	protected void disposeOWLView() {
		selectionModel.removeListener(listener);
	}

	private void updateView(OWLEntity e) {
		if (e != null) {
			String type = e.getEntityType().toString();
			String entityName = e.getIRI().getFragment();
			String entityLabel = getOWLModelManager().getRendering(e);
			label.setText("Hello World! Selected entity = [Name:] " +  
					entityName + ", [Label:] " + entityLabel + ", [Type:] " + type);
		}
		else {
			label.setText("Hello World!");
		}
	}
}
