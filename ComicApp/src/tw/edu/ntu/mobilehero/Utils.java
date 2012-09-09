package tw.edu.ntu.mobilehero;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class Utils {
	final static int MOST_VIEWS = 0;
	final static int MY_WORKS = 1;
	final static int INVITATIONS = 2;
	
	final static String SERVER_URL = "http://comicrelay.appspot.com";
	
	static String user;
	static SharedPreferences mPrefs;
	
	final static Facebook facebook = new Facebook("311185528978877");
	static AsyncFacebookRunner mAsyncRunner;
	
	static Activity currentActivity;

	static class inviteTask extends AsyncTask<JSONArray, Integer, Integer> {
		@Override  
        protected void onPreExecute() {
        }
		
		@Override
		protected Integer doInBackground(JSONArray... arrays) {
			try {
				String identifier = null;
				try {
					identifier = arrays[0].getString(arrays[0].length() - 1);
					Log.d("identifier", identifier);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				HttpClient client = new DefaultHttpClient();
		        HttpPost post = new HttpPost(SERVER_URL + "/invite");
		        List<NameValuePair> params = new ArrayList<NameValuePair>();
		        params.add(new BasicNameValuePair("inviter", user));
		        params.add(new BasicNameValuePair("invitees", arrays[0].toString()));
		        params.add(new BasicNameValuePair("identifier", identifier));
			    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			    post.setEntity(entity);
			    
		        HttpResponse response = client.execute(post);
		        Log.d("Status Code of Inviting Friends", String.valueOf(response.getStatusLine().getStatusCode()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer integer) {
		}
	}
	
	public static void login(Activity activity) {
		currentActivity = activity;
		mPrefs = currentActivity.getPreferences(Context.MODE_PRIVATE);
		user = mPrefs.getString("id", null);
		String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
        	facebook.setAccessExpires(expires);
        }
        
        if(!facebook.isSessionValid()) {
        	facebook.authorize(currentActivity, new String[] {}, new DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("access_token", facebook.getAccessToken());
                    editor.putLong("access_expires", facebook.getAccessExpires());
                    editor.commit();
                    
                    Bundle params = new Bundle();
                    params.putString("fields", "id");
            		mAsyncRunner = new AsyncFacebookRunner(facebook);
                    mAsyncRunner.request("me", params, new RequestListener() {
            			@Override
            			public void onComplete(String response, Object state) {
            				try {
            					JSONObject jsonObject = new JSONObject(response.toString());
            					SharedPreferences.Editor editor = mPrefs.edit();
                                editor.putString("id", jsonObject.getString("id"));
                                editor.commit();
                                
                                mPrefs = currentActivity.getPreferences(Context.MODE_PRIVATE);
                        		user = mPrefs.getString("id", null);
            				} catch (JSONException e) {
            					e.printStackTrace();
            				}
            			}

						@Override
						public void onIOException(IOException e, Object state) {}

						@Override
						public void onFileNotFoundException(FileNotFoundException e, Object state) {}

						@Override
						public void onMalformedURLException(MalformedURLException e, Object state) {}

						@Override
						public void onFacebookError(FacebookError e, Object state) {}
                    });
                }

				@Override
				public void onFacebookError(FacebookError e) {}

				@Override
				public void onError(DialogError e) {}

				@Override
				public void onCancel() {}
            });
        }
	}
	
	public static void logout(Activity activity) {
		currentActivity = activity;
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		mAsyncRunner.logout(currentActivity, new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {}
			  
			@Override
			public void onIOException(IOException e, Object state) {}
			  
			@Override
			public void onFileNotFoundException(FileNotFoundException e, Object state) {}
			  
			@Override
			public void onMalformedURLException(MalformedURLException e, Object state) {}
			  
			@Override
			public void onFacebookError(FacebookError e, Object state) {}
		});
		mPrefs = currentActivity.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.remove("access_token");
		editor.remove("access_expires");
        editor.commit();
	}
   
    public static void invite(Context context, final String identifier) {    	
    	Bundle params = new Bundle();
        params.putString("title", "邀請朋友");
        params.putString("message", "和我一起來完成這幅四格漫畫吧!");
        //params.putString("filters", "app_users");
        params.putString("filters", "all");
        facebook.dialog(context, "apprequests", params, new DialogListener() {
    		@Override
    		public void onComplete(Bundle values) {
    			JSONArray inviteesArray = new JSONArray();
    			Set<String> keys = values.keySet();
    			for (int i = 0; i < keys.size() - 1; i++) {
    				JSONObject jsonObject = new JSONObject();
    				try {
    					jsonObject.put("invitee", values.getString("to[" + String.format("%1$d", i) + "]"));
    					Log.d("invitee", values.getString("to[" + String.format("%1$d", i) + "]"));
    				} catch (JSONException e) {
    					e.printStackTrace();
    				}
    				inviteesArray.put(jsonObject);
    			}
    			inviteesArray.put(identifier);
    			
    			new inviteTask().execute(inviteesArray);
    		}

    		@Override
    		public void onFacebookError(FacebookError e) {
    			e.printStackTrace();
    		}

    		@Override
    		public void onError(DialogError e) {
    			e.printStackTrace();
    		}

    		@Override
    		public void onCancel() {}
        });
    }
    
    public static void share(Context context, String identifier) {
    	Bundle params = new Bundle();
    	params.putString("caption", "分享創作");
    	params.putString("description", "我的作品喔!");
    	params.putString("picture", 
        		SERVER_URL + "/serve?blob-key=AMIfv96zugySBsMgsqtQgjQIBGtxlnVyfTK7wih3gx2Uj7KHcBlrvjCV_jbIwhpwD4dx31n4qfUoJx3sVvEnZSO3uCGkFlrYIqLkq2F3xcM49KY5ZvH60DsTsIR_tk3Upthmwf72SfBt0qqiAWoknx81swCLh9e8hg");
        facebook.dialog(context, "feed", params, new DialogListener() {
			@Override
			public void onComplete(Bundle values) {}

			@Override
			public void onFacebookError(FacebookError e) {
				e.printStackTrace();
			}

			@Override
			public void onError(DialogError e) {
				e.printStackTrace();
			}

			@Override
			public void onCancel() {}
        });    	
    }
}