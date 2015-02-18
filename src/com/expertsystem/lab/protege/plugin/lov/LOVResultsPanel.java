package com.expertsystem.lab.protege.plugin.lov;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.protege.editor.core.ui.list.MList;
import org.protege.editor.core.ui.list.MListAddButton;
import org.protege.editor.core.ui.list.MListButton;
import org.protege.editor.core.ui.util.ComponentFactory;

import com.expertsystem.lab.lov.ResultsListItem;

/**
 * 
 * Linked Open Vocabularies (LOV) API wrapper 
 * 
 * @author Nuria Garcia <ngarcia@isoco.com>
 * @author Ghislain Atemezing <ghislain.atemezing@gmail.com> 
 * @author boricles <boricles@gmail.com> 
 *
 */

public class LOVResultsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel text;
	private MList list_results;	
	private MListAddButton add_button;
	private String local_name;
	private String label_name;
	private String type;	

	public LOVResultsPanel (ActionListener add_listener){		
		this.local_name = "Thing";
		this.label_name = "Thing";
		this.type = "Class";
		setLayout(new BorderLayout());
		add_button = new MListAddButton(add_listener);
		text = new JLabel();		
		text.setFont(new Font(text.getFont().getName(), Font.PLAIN, 10));
		text.setText("<html> <img src='http://lov.okfn.org/img/LOV.png' height='47' width='47' style='float:left;margin:0 5px 0 0;'/> "
				+ "LOV stands for Linked Open Vocabularies. <br> "
				+ "This name is derived from LOD, standing for Linked Open Data. <br>"
				+ "Let's assume that the reader is somehow familiar with the latter concept, <br>"
				+ "otherwise a visit to http://linkeddata.org/ or http://www.w3.org/2013/data/ <br>"
				+ "will help to figure it before further reading.</html>");
		text.setOpaque(true);
		text.setBackground(Color.WHITE);
		text.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,
				0,
				1,
				0,
				Color.LIGHT_GRAY),
				BorderFactory.createEmptyBorder(2, 35, 5, 2)));
		add(text, BorderLayout.CENTER);		
	}

	public String getLocal_name() {
		return local_name;
	}

	public void setLocal_name(String local_name) {
		this.local_name = local_name;
	}

	public String getLabel_name() {
		return label_name;
	}

	public void setLabel_name(String label_name) {
		this.label_name = label_name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	

	public MList getList_results() {
		return list_results;
	}

	public void setList_results(MList list_results) {
		this.list_results = list_results;
	}

	public void updateLOVResults(){
		this.removeAll();
		
		List<ResultsListItem> results = new ArrayList<ResultsListItem>();
		results.add(new ResultsListItem(local_name, "foaf", "http://xmlns.com/foaf/0.1/Person", "2,320,027", "72", "A person", "Person", 0.707));
		results.add(new ResultsListItem(local_name, "bbccore", "http://www.bbc.co.uk/ontologies/coreconcepts/Person", "", "", "", "Person @en-gb", 0.556));
		results.add(new ResultsListItem(local_name, "sport", "http://www.bbc.co.uk/ontologies/sport/Person", "", "", "An athlete or other person with typically a @en-gb", "Person @en-gb", 0.526));
		results.add(new ResultsListItem(local_name, "schema", "http://schema.org/Person", "980,153", "2", "A person (alive, dead, undead, or fictional). ", "Person", 0.507));

		list_results = new MList(){			
			private static final long serialVersionUID = 1L;

			protected List<MListButton> getButtons(Object value) {
				List<MListButton> buttons  = new ArrayList<MListButton>(super.getButtons(value));
				buttons.add(add_button);            
				return buttons;
			}
		};

		list_results.setListData(results.toArray());
		list_results.setCellRenderer(new ResultsListCellRenderer());

		JScrollPane sp = ComponentFactory.createScrollPane(list_results);
		add(sp, BorderLayout.CENTER);			
	}	

	private class ResultsListCellRenderer extends DefaultListCellRenderer{

		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			JPanel r = new JPanel();
			r.setLayout(new BorderLayout());
			JLabel label = new JLabel();

			ResultsListItem item = (ResultsListItem) value;
			String text = "<html><div style='font-size: 9px;'><b>" + item.getPrefix() + ":" +item.getName() 
					+ "</b> (" + item.getPrefix() + ")&nbsp;&nbsp;&nbsp;&nbsp;" + item.getConfidence() + "</div> <br>";

			if(!item.getNum_ocurrences().equals("") && !item.getNum_datasets().equals("")){
				text += item.getNum_ocurrences() + " occurrences in "+ item.getNum_datasets() + " LOD datasets <br>";
			}

			text += "<a href='" + item.getUrl_vocab() + "'>" + item.getUrl_vocab() + "</a> <br>"; 

			if(!item.getComment().equals("")){
				text += "<font color='green'> rdfs:comment </font>" + item.getComment() + "<br>";
			}

			if(!item.getLabel().equals("")){
				text += "<font color='green'> rdfs:label </font>" + item.getLabel() + "<br>";
			}

			text += "<font color='green'> localName </font>" + item.getName();
			text += "</html>";
			
			label.setText(text);
			label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 10));
			r.add(label, BorderLayout.WEST);

			return r;			
		}		 
	}
}
