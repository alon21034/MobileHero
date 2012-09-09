package tw.edu.ntu.mobilehero;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FacebookLogin extends Activity {
	final static String SERVER_URL = "http://comicrelay.appspot.com";
	final static int CAPTURE_IMAGE_REQUEST_CODE = 0; 
	
	Uri imageUri;
	
	ProgressBar progressBar;
	
	class downloadTask extends AsyncTask<Integer, Integer, ArrayList<Comic>> {
		int attribute;		
		
		@Override  
        protected void onPreExecute() {
        }  
		
		@Override
		protected ArrayList<Comic> doInBackground(Integer... attributes) {
			attribute = attributes[0];
			
			ArrayList<Comic> results =  new ArrayList<Comic>();
	    	try {
				HttpClient client = new DefaultHttpClient();			
		        HttpPost post = new HttpPost(SERVER_URL + "/sort");  
		        List<NameValuePair> params = new ArrayList<NameValuePair>();
		        params.add(new BasicNameValuePair("attribute", String.valueOf(attribute)));
		        params.add(new BasicNameValuePair("user", Utils.user));
			    UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			    post.setEntity(reqEntity);
			    
		        HttpResponse response = client.execute(post);
		        Log.d("Status Code of Sorting Comics(" + attribute + ")", String.valueOf(response.getStatusLine().getStatusCode()));
		        if (response.getStatusLine().getStatusCode() == 200) {  
		            HttpEntity resEntity = response.getEntity();  
		            try {
		            	JSONArray jsonArray = new JSONArray(EntityUtils.toString(resEntity));
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							
							Comic comic = new Comic(jsonObject.getString("identifier"),
									jsonObject.getString("blobkey1"), 
									jsonObject.getString("blobkey2"), 
									jsonObject.getString("blobkey3"), 
									jsonObject.getString("blobkey4"), 
									jsonObject.getString("creator1"), 
									jsonObject.getString("creator2"),
									jsonObject.getString("creator3"),
									jsonObject.getString("creator4"),
									Integer.parseInt(jsonObject.getString("views")));
							if (attribute == Utils.INVITATIONS) {
								comic.setInviter(jsonObject.getString("inviter"));
								Log.d("inviter", jsonObject.getString("inviter"));
							}
							
							results.add(comic);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
		        }
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	
	    	return results;
		}
		
		@Override
		protected void onPostExecute(ArrayList<Comic> results) {			
	        switch(attribute) {
	        case Utils.MOST_VIEWS:
	        	for (Comic comic : results) {
		        	Log.d("MOST_VIEWS", comic.getIdentifier());
		        	/*
		        	 * Info about the Nth Panel (N = {1, 2, 3, 4})
		        	 * 	-IMAGE URL				: comic.getPanel(n).getUrl()
		        	 * 	-CREATOR'S FACEBOOK ID	: comic.getPanel(n).getCreator()
		        	 */
		        }
	        	break;
	        case Utils.MY_WORKS:
	        	for (Comic comic : results) {
		        	Log.d("MY_WORKS", comic.getIdentifier());
		        }
	        	break;
	        case Utils.INVITATIONS:
	        	for (Comic comic : results) {
		        	Log.d("INVITATIONS", comic.getIdentifier());
		        }
	        	break;
	        	
        	default:
        		break;
	        }
	    }
	}

	class uploadTask extends AsyncTask<String, Integer, Integer> {
		@Override  
        protected void onPreExecute() {
        }
		
		@Override
		protected Integer doInBackground(String... params) {
			File file = new File("mnt/sdcard", params[0]);
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());  
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			byte[] byteArray = out.toByteArray();
		
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(SERVER_URL + "/upload");
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("file", new ByteArrayBody(byteArray, params[0]));
			try {
				entity.addPart("identifier", new StringBody(params[1]));
				entity.addPart("order", new StringBody(params[2]));
				entity.addPart("creator", new StringBody(Utils.user));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			post.setEntity(entity);
	        
			HttpResponse response;
			try {
				response = client.execute(post);
				Log.d("Status Code of Uploading", String.valueOf(response.getStatusLine().getStatusCode()));
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer integer) {
		}
	}
}