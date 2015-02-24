package com.expertsystem.lab.lov;

import org.protege.editor.core.ui.list.MListItem;

public class ResultsListItem implements MListItem {

	private String name;
	private String prefix;
	private String url_vocab;
	private String num_ocurrences;
	private String num_datasets;
	private String comment;
	private String label;
	private double confidence;

	public ResultsListItem(String name){
		this.name = name;
		this.prefix = "";
		this.url_vocab = "";
		this.num_ocurrences = "0";
		this.num_datasets = "0";
		this.comment = "";
		this.label = "";
		this.confidence = 0.0;
	}	
	
	public ResultsListItem(String name, String prefix, String url_vocab, String num_ocurrences, String num_datasets, 
			String comment, String label, double confidence){
		this.name = name;
		this.prefix = prefix;
		this.url_vocab = url_vocab;
		this.num_ocurrences = num_ocurrences;
		this.num_datasets = num_datasets;
		this.comment = comment;
		this.label = label;
		this.confidence = confidence;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getUrl_vocab() {
		return url_vocab;
	}

	public void setUrl_vocab(String url_vocab) {
		this.url_vocab = url_vocab;
	}

	public String getNum_ocurrences() {
		return num_ocurrences;
	}

	public void setNum_ocurrences(String num_ocurrences) {
		this.num_ocurrences = num_ocurrences;
	}

	public String getNum_datasets() {
		return num_datasets;
	}

	public void setNum_datasets(String num_datasets) {
		this.num_datasets = num_datasets;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public void handleEdit() {
		// do nothing			
	}

	@Override
	public boolean isDeleteable() {
		return false;
	}       

	@Override
	public boolean handleDelete() {           
		return false;
	}

	@Override
	public String getTooltip() {
		return prefix + ":" + name;
	}
}
