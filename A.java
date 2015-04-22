package get_zhaole365_events_json;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

/*@author: Huang Yan
 *@date: 2015/4/15 */
public class A {
	public static void main(String[] args) throws IOException{
		String string = null;
		File f = new File("events.json");
		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
		BufferedWriter writer = new BufferedWriter(write);
		
		try {
			string = getString("http://www.zhaole365.com:8983/solr/events/select?q=*%3A*&rows=5&wt=json&indent=true");
			JSONObject data = new JSONObject(string);
			JSONObject response = data.getJSONObject("response");
	        JSONArray docs = response.getJSONArray("docs");
	        
	        for(int i = 0; i < docs.length(); ++i){
	        	//writer.write(docs.getString(i) + "\n");
	        	docs.getJSONObject(0).getJSONArray("location_description").get(0);
	        	System.out.println(docs.getJSONObject(0).getJSONArray("location_description").get(0).toString());
	        	
	        	System.out.println(docs.getJSONObject(0).getString("eventtime"));
	        }
			
	        
		} catch (Exception e) {
			e.printStackTrace();
		}		
		writer.close();
		//System.out.println(string);
	}
	public static String getString(String urlpath) throws Exception {
		URL url = new URL(urlpath);
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(6 * 1000);
			if (conn.getResponseCode() == 200) {
				InputStream inStream = conn.getInputStream();
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = -1;
				while ((len = inStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				outStream.close();
				inStream.close();
				byte[] data = outStream.toByteArray();
				return new String(data, "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

