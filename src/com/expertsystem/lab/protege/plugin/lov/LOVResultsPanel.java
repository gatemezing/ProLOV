package com.expertsystem.lab.protege.plugin.lov;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
	private MListEditButton edit_button;
	private MListSubEntityButton subEntity_button;
	private String local_name;
	private String label_name;
	private String type;	

	public LOVResultsPanel (ActionListener add_listener, ActionListener edit_listener, ActionListener subclass_listener){		
		this.local_name = "Thing";
		this.label_name = "Thing";
		this.type = "class"; 
		setLayout(new BorderLayout());
		add_button = new MListAddButton(add_listener);		
		edit_button = new MListEditButton(edit_listener);
		subEntity_button = new MListSubEntityButton(subclass_listener);
		init();	
	}

	public void init(){
		this.removeAll();
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
				Color.WHITE),
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

	public void updateLOVResults(List<ResultsListItem> results){
		this.removeAll();

		list_results = new MList(){			
			private static final long serialVersionUID = 1L;

			protected List<MListButton> getButtons(Object value) {
				List<MListButton> buttons  = new ArrayList<MListButton>(super.getButtons(value));
				buttons.add(subEntity_button);				
				buttons.add(add_button); 		
				buttons.add(edit_button);
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

			if(!item.getNum_ocurrences().equals("0") && !item.getNum_datasets().equals("0")){
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

	private class MListSubEntityButton extends MListButton {

		protected MListSubEntityButton(ActionListener subentity_listener) {
			super("Add Sub-entity", Color.DARK_GRAY.darker(), subentity_listener);
		}

		public void paintButtonContent(Graphics2D g) {
			int w = getBounds().width;
			int h = getBounds().height;
			int x = getBounds().x;
			int y = getBounds().y;
			g.drawOval(x + 3, y + 3, 6, 6);
			g.drawLine(x + 8, y + 8, x + w - 5, y + h - 5);
		}
	}

	private class MListAddButton extends MListButton {

		protected MListAddButton(ActionListener actionListener) {
			super("Add Entity and Equivalent Axiom", Color.GREEN.darker(), actionListener);
		}

		public void paintButtonContent(Graphics2D g) {
			int size = getBounds().height;
			int thickness = (Math.round(size / 8.0f) / 2) * 2;

			int x = getBounds().x;
			int y = getBounds().y;

			int insetX = size / 4;
			int insetY = size / 4;
			int insetHeight = size / 2;
			int insetWidth = size / 2;
			g.fillRect(x + size / 2  - thickness / 2, y + insetY, thickness, insetHeight);
			g.fillRect(x + insetX, y + size / 2 - thickness / 2, insetWidth, thickness);
		}
	}
	
	private class MListEditButton extends MListButton {

	    protected MListEditButton(ActionListener actionListener) {
	        super("Reuse Directly", new Color(20, 80, 210), actionListener);
	    }

	    public void paintButtonContent(Graphics2D g) {
	        Rectangle bounds = getBounds();
	        int x = bounds.x;
	        int y = bounds.y;
	        int size = bounds.width;
	        int quarterSize = (Math.round(bounds.width / 4.0f) / 2) * 2;
	        g.fillOval(x + size / 2 - quarterSize, y + size / 2 - quarterSize, 2 * quarterSize, 2 * quarterSize);
	        g.setColor(getBackground());
	        g.fillOval(x + size / 2 - quarterSize / 2, y + size / 2 - quarterSize / 2, quarterSize, quarterSize);
	    }
	}
}
