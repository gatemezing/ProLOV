package com.expertsystem.lab.lov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONObject;

/**
 * 
 * Linked Open Vocabularies (LOV) API wrapper 
 * 
 * @author Nuria Garcia <ngarcia@isoco.com>
 * @author Ghislain Atemezing <ghislain.atemezing@gmail.com> 
 * @author boricles <boricles@gmail.com> 
 *
 */

public class LOVConnector implements Constants {

	/**
	 * Method to search a term through LOV API
	 * @param q: Query string
	 * @param type: Type of the term [class, property, datatype, instance]
	 * @return JSONObject: Results from the query in json format
	 */
	
	/*
	 * Maybe instead returning a JSON object, we could make a POJO class with general
	 * data that we need and filling the information parsing the JSON string 
	 */
	public JSONObject searchTerm(String q, String type){
		HttpURLConnection connection;
		JSONObject json = null;
		String query = LOV_QUERY_STRING + q + ""
				+ "&type=" + type;

		URL url;
		try {
			url = new URL(query);
			connection = (HttpURLConnection) url.openConnection();						
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			InputStream is = connection.getInputStream();						
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));				
			StringBuffer response = new StringBuffer();					
			String str;			
			while ((str = rd.readLine()) != null) {
				response.append(str);
			}
			rd.close();	
			
			System.out.println(response.toString());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;		
	}
	
	public JSONObject autocompleteTerm(String q, String type){
		JSONObject json = null;
		return json;
	}
	
	
	/*
	 * Any other necessary method to get data from the LOV API 
	 */
	
	
	
	
	/*public static void main(String[] args){
		LOVApi lovapi = new LOVApi();
		lovapi.searchTerm("Person", "class");
	}*/
}
