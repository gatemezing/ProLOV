package com.expertsystem.lab.lov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
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

	public int total_results;

	public LOVConnector(){
		this.total_results = 0;
	}	

	public int getTotal_results() {
		return total_results;
	}

	public void setTotal_results(int total_results) {
		this.total_results = total_results;
	}

	/**
	 * Method to search a term through LOV API
	 * @param q: Query string
	 * @param type: Type of the term [class, property, datatype, instance]
	 * @return JSONObject: Results from the query in json format
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

			json = new JSONObject(response.toString());
			//System.out.println(response.toString());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;		
	}

	public List<ResultsListItem> parseTerms(JSONObject json){
		List<ResultsListItem> results = new ArrayList<ResultsListItem>();

		this.total_results = json.getInt("total_results");
		//System.out.println(total_results);
		
		if(total_results > 0){
			JSONArray terms = (JSONArray) json.get("results");
			for (int i=0; i < terms.length(); i++) {
				JSONObject data = (JSONObject) terms.get(i);
				
				//Local Name
				String prefixName = data.getJSONArray("prefixedName").get(0).toString();
				String local_name = prefixName.substring(prefixName.lastIndexOf(':') + 1);
				//System.out.println(local_name);
				
				//Prefix
				String prefix = data.getJSONArray("vocabulary.prefix").get(0).toString();
				//System.out.println(prefix);
				
				//Occurrences
				String ocurrences = data.getJSONArray("metrics.occurrencesInDatasets").get(0).toString();
				//System.out.println(ocurrences);
				
				//Datasets
				String datasets = data.getJSONArray("metrics.reusedByDatasets").get(0).toString();
				//System.out.println(datasets);
				
				//Uri
				String uri = data.getJSONArray("uri").get(0).toString();
				//System.out.println(uri);
				
				//Confidence
				double score = data.getDouble("score");
				//System.out.println(score);
				
				JSONObject highlight = data.getJSONObject("highlight");
				//Comment
				String comment = "";
				if(!highlight.isNull("http://www.w3.org/2000/01/rdf-schema#comment")){
					comment = highlight.getJSONArray("http://www.w3.org/2000/01/rdf-schema#comment").get(0).toString();
					//comment = comment.replaceAll("\"", " ");
					//comment = comment.replace('"', '\"');
					//System.out.println(comment);
					
				}
				
				//Label
				String label = "";
				if(!highlight.isNull("http://www.w3.org/2000/01/rdf-schema#label")){
					label = highlight.getJSONArray("http://www.w3.org/2000/01/rdf-schema#label").get(0).toString();
					//System.out.println(label);
				}
				
				ResultsListItem item = new ResultsListItem(local_name, prefix, uri, ocurrences, datasets, comment, label, score);
				results.add(item);
			}
		}
		return results;
	}

	/*
	 * ex:http://lov.okfn.org/dataset/lov/api/v2/term/autocomplete?q=foaf:p&type=property
	 * string = URI or prefixed URI to complete (from 1 character).
	 * type can be class or property. 
	 */
	public JSONObject autocompleteTerm(String q, String type){
		JSONObject json = null;
		HttpURLConnection connection;
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
		//s += URLEncoder.encode("UTF-8");
		s += URLEncoder.encode(s, "UTF-8");
		URL url = new URL(s);

		// read from the URL
		Scanner scan = new Scanner(url.openStream());
		String str = new String();
		while (scan.hasNext())
			str += scan.nextLine();
		scan.close();

		// build a JSON object
		obj = new JSONObject(str);
		if (! obj.getString("status").equals("OK"))
			return null;
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
		//st += URLEncoder.encode("UTF-8");
		st += URLEncoder.encode(st, "UTF-8");
		URL url = new URL(st);

		// read from the URL
		Scanner scan = new Scanner(url.openStream());
		String str = new String();
		while (scan.hasNext())
			str += scan.nextLine();
		scan.close();

		// build a JSON object
		result = new JSONObject(str);
		if (! result.getString("status").equals("OK"))
			return null;


		return result;
	}

	/*
	 * Any other necessary method to get data from the LOV API ? Maybe for agent
	 */


	public static void main(String[] args){
		//LOVConnector lovapi = new LOVConnector();
		//lovapi.searchTerm("Scale", "class");
		//lovapi.parseTerms(lovapi.searchTerm("Scale", "class"));
		
		/*String uri = "http://www.aktors.org/ontology/portal#Person";
		String name = "Person";
		
		String base_uri = uri.substring(0, uri.length() - name.length());
		System.out.println(base_uri);*/
	}
}
