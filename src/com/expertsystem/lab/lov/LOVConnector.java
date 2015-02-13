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
	
	/*
	 * ex:http://lov.okfn.org/dataset/lov/api/v2/term/autocomplete?q=foaf:p&type=property
	 * string = URI or prefixed URI to complete (from 1 character).
	 * type can be class or property. 
	 */
	public JSONObject autocompleteTerm(String q, String type){
		JSONObject json = null;
		HttpURLConnection connection;
		JSONObject json = null;
		String query = LOV_AUTO_STRING + q + ""
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
	
	
	
	
	/* listing all the vocabularies
	 * in LOV ecosystem. Results composed of {titles[{lang,value}], nsp, prefix, uri} 
	 * 
	 */
	public JSONObject listVocab() throws Exception
		{
		JSONObject obj = null;
		
		// build the string and URL
		String s = LOV_API_VOCAB + "list";
	    s += URLEncoder.encode("UTF-8");
	    URL url = new URL(s);
	 
	    // read from the URL
	    Scanner scan = new Scanner(url.openStream());
	    String str = new String();
	    while (scan.hasNext())
	        str += scan.nextLine();
	    scan.close();
	 
	    // build a JSON object
	    JSONObject obj = new JSONObject(str);
	    if (! obj.getString("status").equals("OK"))
	        return;
		/* todo: parse the result prefix, title + uri + description
	    JSONArray arr = obj.getJSONArray("titles");
	    for (int i = 0; i < arr.length(); i++)
	    	// print the result
	        System.out.println(arr.getJSONObject(i));
	        */
		
	    return obj;
	}
	
	
	
	/*get http://lov.okfn.org/dataset/lov/api/v2/vocabulary/info?vocab={string}
	*example: http://lov.okfn.org/dataset/lov/api/v2/vocabulary/info?vocab=skos
	* input: string representing Prefix, URI or Namespace of a vocabulary in LOV. 
	* This parameter is mandatory
	*output: uri, titles, descriptions, tags (domaine)
	*/
	public JSONObject infoVocab(String vocab)  throws Exception{
		JSONObject result = null;
		// build the string and URL
		String st = LOV_API_VOCAB + "info"  + "?vocab="
				+ "" + vocab;
		st += URLEncoder.encode("UTF-8");
		URL url = new URL(st);
		
		// read from the URL
	    Scanner scan = new Scanner(url.openStream());
	    String str = new String();
	    while (scan.hasNext())
	        str += scan.nextLine();
	    scan.close();
	 
	    // build a JSON object
	    JSONObject result = new JSONObject(str);
	    if (! result.getString("status").equals("OK"))
	        return;
	    
		
		return result;
		
		
		
	}
	
	/*
	 * Any other necessary method to get data from the LOV API ? Maybe for agent
	 */
	
	
	/*public static void main(String[] args){
		LOVApi lovapi = new LOVApi();
		lovapi.searchTerm("Person", "class");
	} */
}
