package com.expertsystem.lab.lov;

/**
 * 
 * Linked Open Vocabularies (LOV) Connector Constants 
 * 
 * @author Nuria Garcia <ngarcia@expertsystem.com>
 * @author Ghislain Atemezing <ghislain.atemezing@gmail.com> 
 * @author boricles <boricles@gmail.com> 
 *
 */

public interface Constants {
	
	String LOV_QUERY_STRING = "http://lov.okfn.org/dataset/lov/api/v2/term/search?q=";
	String LOV_API_STRING= "http://lov.okfn.org/dataset/lov/api/v2/";
	String LOV_API_VOCAB= LOV_API_STRING + "vocabulary/" ;
	String LOV_AUTO_STRING = "http://lov.okfn.org/dataset/lov/api/v2/term/autocomplete?q=";

}
