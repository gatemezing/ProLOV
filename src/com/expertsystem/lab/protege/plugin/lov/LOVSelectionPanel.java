package com.expertsystem.lab.protege.plugin.lov;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LOVSelectionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel local_name;
	private String local_name_value;
	private JLabel label;
	private String label_value;
	private String type;
	
	public LOVSelectionPanel (JButton selectionButton){
		setLayout(new BorderLayout());
		local_name_value = "Thing";
		label_value = "Thing";	
		local_name = new JLabel("Local Name: " + local_name_value);
		label = new JLabel("         Label : " + label_value);
		type = "Class";
		add(local_name, BorderLayout.WEST);		
		add(label, BorderLayout.CENTER);	
		add(selectionButton, BorderLayout.EAST);
	}	
	
	public String getLocal_name() {
		return local_name.getText();
	}

	public void setLocal_name(String local_name) {
		this.local_name.setText(local_name);
	}

	public String getLabel() {
		return label.getText();
	}

	public void setLabel(String label) {
		this.label.setText(label);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLocal_name_value() {
		return local_name_value;
	}

	public void setLocal_name_value(String local_name_value) {
		this.local_name_value = local_name_value;
	}

	public String getLabel_value() {
		return label_value;
	}

	public void setLabel_value(String label_value) {
		this.label_value = label_value;
	}	

	/*public static void main(String[] args) {
		JFrame f = new JFrame();
		LOVSelectionPanel lsp = new LOVSelectionPanel();
		lsp.setBorder(ComponentFactory.createTitledBorder("LOV Selection Entity"));
		f.setContentPane(lsp);
		f.setSize(400, 400);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}*/
	
}
