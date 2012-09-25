package tw.edu.ntu.mobilehero.asynctask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import tw.edu.ntu.mobilehero.Comic;
import tw.edu.ntu.mobilehero.Utils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class UploadComicAsyncTask extends AsyncTask<String, Throwable, Comic>{

    private final static String SERVER_URL = "http://comicrelay.appspot.com";
    
    @Override
    protected Comic doInBackground(String... params) {
        File file = new File("mnt/sdcard/Pictures/ComicRelays", params[0]);
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
        
        return null;
    }
}
