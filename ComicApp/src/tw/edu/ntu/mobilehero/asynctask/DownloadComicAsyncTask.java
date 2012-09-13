package tw.edu.ntu.mobilehero.asynctask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import tw.edu.ntu.mobilehero.Comic;
import tw.edu.ntu.mobilehero.Utils;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadComicAsyncTask extends AsyncTask<Integer, Throwable, ArrayList<Comic>>{

    private final static String SERVER_URL = "http://comicrelay.appspot.com";
    
    @Override
    protected ArrayList<Comic> doInBackground(Integer... attributes) {
        int attribute = attributes[0];
        
        ArrayList<Comic> results =  new ArrayList<Comic>();
        try {
            HttpClient client = new DefaultHttpClient();            
            HttpPost post = new HttpPost(SERVER_URL + "/query");  
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("attribute", String.valueOf(attribute)));
            params.add(new BasicNameValuePair("user", Utils.user));
            params.add(new BasicNameValuePair("offset", String.valueOf(0)));
            UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(reqEntity);
            
            HttpResponse response = client.execute(post);
            Log.d("Status Code of Sorting Comics(" + attribute + ")", String.valueOf(response.getStatusLine().getStatusCode()));
            if (response.getStatusLine().getStatusCode() == 200) {  
                HttpEntity resEntity = response.getEntity();  
                if (resEntity != null) {
                    final InputStream stream = resEntity.getContent();
                    final String json = convertStreamToString(stream);
                    Log.d("!!!", json);
                    JSONArray jsonArray = new JSONArray(json);
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
                }
            }
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return results;
    }
    
    private String convertStreamToString(InputStream stream) throws Throwable {
        final InputStreamReader isr = new InputStreamReader(stream);
        final BufferedReader reader = new BufferedReader(isr);
        final StringBuilder builder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            builder.append(line + "\n");
        }

        return builder.toString();
    }
}
