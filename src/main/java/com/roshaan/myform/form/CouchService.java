package com.roshaan.myform.form;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class CouchService {

	public final String DATABASE_NAME = "users";

	void createDatabase(String dbName) throws ClientProtocolException, IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPut httpPut = new HttpPut("http://127.0.0.1:5984/" + dbName.toLowerCase());

		System.out.println("Executing request " + httpPut.getRequestLine());

		// Create a custom response handler
		ResponseHandler<String> responseHandler = response -> {
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				return entity != null ? EntityUtils.toString(entity) : null;
			} else {
				throw new ClientProtocolException("Unexpected response status creating db: " + status);
			}
		};

		String responseBody = httpclient.execute(httpPut, responseHandler);
		System.out.println("creating database ----------------------------------------");
		System.out.println(responseBody);

	}

	void addData(String firstName, String lastName) throws ClientProtocolException, IOException {

		JSONObject person = new JSONObject();
		person.put("firstName", firstName);
		person.put("lastName", lastName);

		String documentKey = getKey();

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPut httpPut = new HttpPut("http://127.0.0.1:5984/" + DATABASE_NAME + "/" + documentKey);
//            this is body
		httpPut.setEntity(new StringEntity(person.toString()));

		System.out.println("Executing request " + httpPut.getRequestLine());

		// Create a custom response handler
		ResponseHandler<String> responseHandler = response -> {
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				return entity != null ? EntityUtils.toString(entity) : null;
			} else {
				throw new ClientProtocolException("Unexpected response status addData : " + status);
			}
		};

		String responseBody = httpclient.execute(httpPut, responseHandler);
		System.out.println("add data----------------------------------------");
		System.out.println(responseBody);

	}

	String getKey() throws ClientProtocolException, IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpPut = new HttpGet("http://127.0.0.1:5984/_uuids");

		System.out.println("Executing request " + httpPut.getRequestLine());

		// Create a custom response handler
		ResponseHandler<String> responseHandler = response -> {
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				return entity != null ? EntityUtils.toString(entity) : null;
			} else {
				throw new ClientProtocolException("Unexpected response status getting key: " + status);
			}
		};

//		getting response
		String responseBody = httpclient.execute(httpPut, responseHandler);
		System.out.println("getting key----------------------------------------");
		System.out.println(responseBody);

//		fetching key
		JSONObject responseJson = new JSONObject(responseBody);

		JSONArray key = responseJson.getJSONArray("uuids");

		return key.getString(0);
	}

	boolean checkDbExist(String databaseName) throws ClientProtocolException, IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpPut = new HttpGet("http://127.0.0.1:5984/_all_dbs");

		System.out.println("Executing request " + httpPut.getRequestLine());

		// Create a custom response handler
		ResponseHandler<String> responseHandler = response -> {
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				return entity != null ? EntityUtils.toString(entity) : null;
			} else {
				throw new ClientProtocolException("Unexpected response status checking db : " + status);
			}
		};

		String responseBody = httpclient.execute(httpPut, responseHandler);
//  		printing response
		System.out.println("checking db----------------------------------------");
		System.out.println(responseBody);

//            checking response for database
		JSONArray databases = new JSONArray(responseBody);

		for (int i = 0; i < databases.length(); i++) {
			if (databases.getString(i).equals(databaseName))
				return true;
		}

		return false;

	}
}
