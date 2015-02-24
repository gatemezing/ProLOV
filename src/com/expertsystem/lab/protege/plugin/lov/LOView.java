package com.expertsystem.lab.protege.plugin.lov;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.apache.log4j.Logger;
import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.owl.model.entity.OWLEntityCreationException;
import org.protege.editor.owl.model.entity.OWLEntityCreationSet;
import org.protege.editor.owl.model.selection.OWLSelectionModel;
import org.protege.editor.owl.model.selection.OWLSelectionModelListener;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.util.OWLEntityRenamer;

import com.expertsystem.lab.lov.LOVConnector;
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
	private LOVConnector connector;
	private JButton selectionButton;
	private OWLSelectionModel selectionModel;	
	private JLabel status;
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
			}			
		}

		private void updateListLOV() {
			String type_lov = lsp.getType();
			if(type_lov.equals("objectproperty")){
				type_lov = "property";
			}
			if(type_lov.equals("dataproperty")){
				type_lov = "datatype";
			}
			if(type_lov.equals("namedindividual")){
				type_lov = "instance";
			}			
			List<ResultsListItem> r = connector.parseTerms(connector.searchTerm(lsp.getLocal_name_value(), type_lov));
			status.setText("Total Results (" + lsp.getLocal_name_value() + "): " + connector.getTotal_results());	
			lrp.updateLOVResults(r);
			lrp.updateUI();
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
			status.setText("Total Results (" + lsp.getLocal_name_value() + "): " + connector.getTotal_results());
			status.setText(status.getText() + " | Entity selected " + item.getPrefix() + ":" + item.getName());	

			OWLEntity selectedEntity = getOWLWorkspace().getOWLSelectionModel().getSelectedEntity();
			IRI baseIRI = IRI.create(item.getUrl_vocab().substring(0, item.getUrl_vocab().length() - item.getName().length()));

			OWLEntity parent = null;			

			if(selectedEntity.isOWLClass()){
				OWLClass selected = selectedEntity.asOWLClass();
				Set<OWLClassExpression> setParent  = selected.getSuperClasses(getOWLModelManager().getActiveOntology());
				//Set<OWLClassExpression> setParent = (Set<OWLClassExpression>) getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass().getSuperClasses(getOWLModelManager().getActiveOntology());
				Iterator<OWLClassExpression> it = setParent.iterator();
				if(it.hasNext()){
					parent = it.next().asOWLClass();
				}				
			}

			if(selectedEntity.isOWLObjectProperty()){
				Set<OWLObjectPropertyExpression> setParent = (Set<OWLObjectPropertyExpression>) getOWLWorkspace().getOWLSelectionModel().getLastSelectedObjectProperty().getSuperProperties(getOWLModelManager().getActiveOntology());
				Iterator<OWLObjectPropertyExpression> it = setParent.iterator();
				if(it.hasNext()){
					parent = it.next().asOWLObjectProperty();
				}	
			}

			if(selectedEntity.isOWLDataProperty()){
				Set<OWLDataPropertyExpression> setParent = (Set<OWLDataPropertyExpression>) getOWLWorkspace().getOWLSelectionModel().getLastSelectedDataProperty().getSuperProperties(getOWLModelManager().getActiveOntology());
				Iterator<OWLDataPropertyExpression> it = setParent.iterator();
				if(it.hasNext()){
					parent = it.next().asOWLDataProperty();
				}
			}

			try {
				@SuppressWarnings("unchecked")
				OWLEntityCreationSet<OWLEntity> set = (OWLEntityCreationSet<OWLEntity>) getOWLModelManager().getOWLEntityFactory().createOWLEntity(selectedEntity.getClass(), item.getName(), baseIRI);
				//List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
				if (set != null){
					if(parent == null){
						//OWLEntity newEntity = set.getOWLEntity();
						getOWLModelManager().applyChanges(set.getOntologyChanges());
						//getOWLWorkspace().getOWLSelectionModel().setSelectedEntity(newEntity);
					}
					else{
						createSubEntity(parent, set);
					}
					//Building the equivalent assertion
					createEquivalentEntity(selectedEntity, set);
				}
			} catch (OWLEntityCreationException e) {				
				e.printStackTrace();
			}

			/*try {
				if(selectedEntity.isOWLClass()){
					OWLEntityCreationSet<OWLClass> set = (OWLEntityCreationSet<OWLClass>) getOWLModelManager().getOWLEntityFactory().createOWLClass(item.getName(), baseIRI);
					createOWLclass(set);
				}
			} catch (OWLEntityCreationException e) {
				e.printStackTrace();
			}*/
		}

		/*private void createOWLclass(OWLEntityCreationSet<OWLClass> set){
			OWLObjectHierarchyProvider<OWLClass> hclass = getOWLModelManager().getOWLHierarchyManager().getOWLClassHierarchyProvider();
			OWLObjectTree<OWLClass> tree = new OWLModelManagerTree<OWLClass>(getOWLEditorKit(), hclass);

			@SuppressWarnings("unchecked")
			OWLObjectTreeNode<OWLClass> parentNode 
			= (OWLObjectTreeNode<OWLClass>) tree.getSelectionPath().getParentPath().getLastPathComponent();
			if (parentNode == null || parentNode.getOWLObject() == null) {
				return;
			}
			OWLClass parentCls = parentNode.getOWLObject();

			List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
            changes.addAll(set.getOntologyChanges());
            OWLModelManager mngr = getOWLModelManager();
            OWLDataFactory df = mngr.getOWLDataFactory();
            if (!df.getOWLThing().equals(parentCls)) {
                changes.add(new AddAxiom(mngr.getActiveOntology(), df.getOWLSubClassOfAxiom(set.getOWLEntity(), parentCls)));
            }
            mngr.applyChanges(changes);
            // Select the new class
            tree.setSelectedOWLObject(set.getOWLEntity());
		}*/
	};

	private ActionListener edit_listener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (lrp.getList_results().getSelectedValue() instanceof ResultsListItem) {
				ResultsListItem item = (ResultsListItem) lrp.getList_results().getSelectedValue();
				handleEdit(item);
			}
		}

		private void handleEdit(ResultsListItem item) {	
			OWLEntity selectedEntity = getOWLWorkspace().getOWLSelectionModel().getSelectedEntity();
			OWLEntityRenamer owlEntityRenamer = new OWLEntityRenamer(getOWLModelManager().getOWLOntologyManager(),
					getOWLModelManager().getOntologies());
			try {
				final IRI iri = IRI.create(new URI(item.getUrl_vocab()));
				if (iri == null) {
					return;
				}
				final List<OWLOntologyChange> changes;
				changes = owlEntityRenamer.changeIRI(selectedEntity, iri);
				getOWLModelManager().applyChanges(changes);
				getOWLWorkspace().getOWLSelectionModel().setSelectedEntity(getOWLDataFactory().getOWLEntity(selectedEntity.getEntityType(), iri));			

			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	};	

	private ActionListener subclass_listener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (lrp.getList_results().getSelectedValue() instanceof ResultsListItem) {
				ResultsListItem item = (ResultsListItem) lrp.getList_results().getSelectedValue();
				handleSubEntity(item);
			}
		}

		private void handleSubEntity(ResultsListItem item) {
			IRI baseIRI = IRI.create(item.getUrl_vocab().substring(0, item.getUrl_vocab().length() - item.getName().length()));
			OWLEntity parent = getOWLWorkspace().getOWLSelectionModel().getSelectedEntity();

			try {
				@SuppressWarnings("unchecked")
				OWLEntityCreationSet<OWLEntity> set = (OWLEntityCreationSet<OWLEntity>) getOWLModelManager().getOWLEntityFactory().createOWLEntity(parent.getClass(), item.getName(), baseIRI);
				createSubEntity(parent, set);				
			} catch (OWLEntityCreationException e) {
				e.printStackTrace();
			}			
		}		 
	};

	public void createSubEntity(OWLEntity parent, OWLEntityCreationSet<OWLEntity> set){
		List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
		OWLDataFactory df = getOWLModelManager().getOWLDataFactory();
		OWLEntity child = set.getOWLEntity();
		if (parent != null && parent.isOWLClass()){
			changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(),
					df.getOWLSubClassOfAxiom((OWLClass)child, (OWLClass)parent)));
		}	
		if (parent != null && parent.isOWLObjectProperty()){
			changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(),
					df.getOWLSubObjectPropertyOfAxiom((OWLObjectProperty)child, (OWLObjectProperty) parent)));
		}
		if (parent != null && parent.isOWLDataProperty()){				
			changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(),
					df.getOWLSubDataPropertyOfAxiom((OWLDataProperty)child, (OWLDataProperty) parent)));
		}	
		if (parent == null || parent.isOWLNamedIndividual()){
			getOWLModelManager().applyChanges(set.getOntologyChanges());
		}

		getOWLModelManager().applyChanges(changes);
		getOWLWorkspace().getOWLSelectionModel().setSelectedEntity(child);
	}
	
	public void createEquivalentEntity(OWLEntity entityA, OWLEntityCreationSet<OWLEntity> set){
		List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
		OWLDataFactory df = getOWLModelManager().getOWLDataFactory();
		OWLEntity entityB = set.getOWLEntity();
		if (entityA != null && entityA.isOWLClass()){
			changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(),
					df.getOWLEquivalentClassesAxiom((OWLClass)entityB, (OWLClass)entityA)));
		}	
		if (entityA != null && entityA.isOWLObjectProperty()){
			changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(),
					df.getOWLEquivalentObjectPropertiesAxiom((OWLObjectProperty)entityB, (OWLObjectProperty) entityA)));
		}
		if (entityA != null && entityA.isOWLDataProperty()){				
			changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(),
					df.getOWLEquivalentDataPropertiesAxiom((OWLDataProperty)entityB, (OWLDataProperty) entityA)));
		}				

		getOWLModelManager().applyChanges(changes);
		getOWLModelManager().refreshRenderer();
		getOWLWorkspace().getOWLSelectionModel().setSelectedEntity(entityA);
	}

	@Override
	protected void initialiseOWLView() throws Exception {
		logger.info("Initializing LOV view");
		setLayout(new BorderLayout());
		connector = new LOVConnector();
		selectionButton = new JButton("Search...");
		selectionButton.setToolTipText("Search LOV...");
		selectionButton.addActionListener(button_listener);
		lsp = new LOVSelectionPanel(selectionButton);
		lrp = new LOVResultsPanel(add_listener, edit_listener, subclass_listener);		
		lsp.setBorder(ComponentFactory.createTitledBorder("LOV Selection Entity"));
		lrp.setBorder(ComponentFactory.createTitledBorder("LOV Results"));
		add(lsp, BorderLayout.NORTH);	
		add(lrp, BorderLayout.CENTER);
		status = new JLabel("OK Status ");
		add(status, BorderLayout.SOUTH);	
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
			lsp.setType(type.toLowerCase());
			lrp.setLocal_name(entityName);
			lrp.setLabel_name(entityLabel);
			lrp.setType(type);
		}
		else {
			lsp.setLocal_name_value("Thing");
			lsp.setLabel_value("Thing");
			lsp.setLocal_name("Local Name: Thing");
			lsp.setLabel("         Label : Thing");
			lsp.setType("class");
			lrp.setLocal_name("Thing");
			lrp.setLabel_name("Thing");
			lrp.setType("class");
		}		
		lrp.init();
		status.setText("OK Status ");
	}	
}
