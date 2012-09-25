package tw.edu.ntu.mobilehero.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import tw.edu.ntu.mobilehero.Utils;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

public final class PictureFiles {
    // Methods to load pictures from various sources. These all block, which
    // means in almost all cases you should be calling them from a thread
    // other than the main thread.
    //
    // Anything that loads or saves a private picture operates on exactly
    // the provided image data. However, anything that loads a public picture
    // will scale it and create a private copy. Public pictures can be opened
    // from several possible sources; private images can only be opened from
    // filenames.
    //
    // Unlike previous implementations and the standard Android image handling
    // API, you cannot pass null filenames/URIs/etc. into these functions -
    // they will crash. You will also never get a null back from them - they
    // will raise a PictureFiles.Exception.

    private static String APP_NAME;
    private static File PUBLIC_ROOT;
    private static File PRIVATE_ROOT;
    private static File CACHE_ROOT;
    private static long ONE_DAY = 1000 * 60 * 60 * 24;

    public static class Exception extends java.lang.Exception {
        private static final long serialVersionUID = -156321536299199476L;
        public Throwable cause;
        Exception(Throwable cause) {
            super(cause.getMessage());
            this.cause = cause;
        }
        Exception(String cause) {
            this(new Throwable(cause));
        }
    }

    // Return type for functions that open public files. It contains
    // both the requested bitmap, appropriately sized, and a private
    // file where the resized bitmap has been saved.
    public static class BitmapFile {
        public BitmapDrawable bmp;
        public File file;
        protected BitmapFile(BitmapDrawable bmp, File file) {
            this.bmp = bmp;
            this.file = file;
        }
    }

    public static boolean isInitialized() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
            && PRIVATE_ROOT != null
            && PUBLIC_ROOT != null
            && CACHE_ROOT != null;
    }

    public static boolean initialize(Context ctx) {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state))
            return false;

        try {
            PackageManager pm = ctx.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(ctx.getPackageName(), 0);
            APP_NAME = (String)pm.getApplicationLabel(ai);
            PUBLIC_ROOT = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_NAME);
        } catch (NameNotFoundException exc) {
            return false;
        } catch (NullPointerException exc) {
            return false;
        }
        File pictures = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        PRIVATE_ROOT = new File(pictures, "Database");
        CACHE_ROOT = ctx.getExternalCacheDir();

        try {
            File noMedia = new File(pictures, ".nomedia");
            PRIVATE_ROOT.mkdirs();
            CACHE_ROOT.mkdirs();
            PUBLIC_ROOT.mkdirs();
            if (PRIVATE_ROOT.isDirectory() && PUBLIC_ROOT.isDirectory() && CACHE_ROOT.isDirectory()
                && (noMedia.exists() || noMedia.createNewFile())) {
                return true;
            } else {
                PRIVATE_ROOT = PUBLIC_ROOT = CACHE_ROOT = null;
                return false;
            }
        } catch (IOException exc) {
            PRIVATE_ROOT = PUBLIC_ROOT = CACHE_ROOT = null;
            return false;
        } catch (NullPointerException exc) {
            PRIVATE_ROOT = PUBLIC_ROOT = CACHE_ROOT = null;
            return false;
        }
    }
    
    private static Bitmap scaleExact(String source, Bitmap bmp) throws Exception {
        // inSampleSize is good for saving memory on load but is too crude
        // to cut an image of almost 2x screen size down to screen size, or
        // sometimes you request a sampleSize but get the nearest power-of-2
        // instead of what you requested. This will scale the image to
        // exactly the correct size.
        // FIXME(jfw): This is seriously unlocalizable.
        if (bmp == null)
            throw new Exception("Unable to load " + source);
        float scale = (float)400f / Math.max(bmp.getWidth(), bmp.getHeight());
        if (scale < 0.99) {
            int newWidth = Math.round(Math.max(scale * bmp.getWidth(), 1));
            int newHeight = Math.round(Math.max(scale * bmp.getHeight(), 1));
            bmp = ThumbnailUtils.extractThumbnail(bmp, newWidth, newHeight, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            bmp.getWidth();
            bmp.getHeight();
        }
        return bmp;
    }

    private static BitmapDrawable BitmapDrawable(String source, Bitmap bmp) throws Exception {
        // FIXME(jfw): This is seriously unlocalizable.
        if (bmp == null)
            throw new Exception("Unable to load " + source);
        BitmapDrawable draw = new BitmapDrawable(bmp);
        return draw;
    }

    // Generate a public filename. In some cases we may wish to generate
    // a public image using an external app, like the camera, so this
    // method is public.
    public static File PublicFile(String hint, String ext) {
        assert(isInitialized());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
        return new File(PUBLIC_ROOT, hint + " " + formatter.format(new Date()) + "." + ext);
    }

    // Generate a private filename. It shouldn't be necessary to make
    // this function public since all private file saving should be done
    // via this class.
    private static File PrivateFile(String ext) {
        return new File(PRIVATE_ROOT, UUID.randomUUID().toString() + "." + ext);
    }

    private static File CacheFile(String ext) {
        return new File(CACHE_ROOT, UUID.randomUUID().toString() + "." + ext);
    }

    public static void cleanCache() {
        assert(isInitialized());
        Time now = new Time();
        now.setToNow();
        long lastToKeep = now.toMillis(true) - ONE_DAY;
        for (File cache : CACHE_ROOT.listFiles()) {
            if (cache.lastModified() < lastToKeep) {
                cache.delete();
            }
        }
    }

    private static File savePicture(File name, Bitmap data) {
        Log.d("!!","save picture: " + name.toString());
        Bitmap.CompressFormat format = name.toString().toLowerCase().endsWith("png") ? CompressFormat.PNG : CompressFormat.JPEG;

        try {
            Log.d("!!","!! start");
            
            File wallpaperDirectory = new File("/sdcard/MobileHero/");
         // have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs();
         // create a File object for the output file
            File outputFile = new File(wallpaperDirectory, name.toString());
            
            FileOutputStream fos = new FileOutputStream(outputFile);
            boolean success = data.compress(format, 90, fos);
            fos.close();

            if (!success) {
                name.delete();
                Log.d("!!","1");
                return null;
            } else {
                return name;
            }
        } catch (FileNotFoundException e) {
            Log.d("!!","file not found");
            name.delete();
            return null;
        } catch (IOException e) {
            name.delete();
            return null;
        }
        
    }

    public static File savePublicPicture(String hint, String ext, Bitmap data, Context ctx) {
        assert(isInitialized());
        File name = PublicFile(hint, ext);
        File saved = savePicture(name, data);
        MediaScannerConnection.scanFile(ctx, new String[] { saved.toString() }, null, null);
        return saved;
    }

    public static File savePublicPicture(String hint, String ext, BitmapDrawable data, Context ctx) {
        assert(isInitialized());
        return savePublicPicture(hint, ext, data.getBitmap(), ctx);
    }
    
    public static File copyToPublicPicture(String hint, File src, Context ctx) {
        assert(isInitialized());
        if(src == null)
            return null;

        File dst;
        if (src.toString().toLowerCase().endsWith("png"))
            dst = PublicFile(hint, "png");
        else
            dst = PublicFile(hint, "jpg");
        try {
            Utils.copyFile(src, dst);
            MediaScannerConnection.scanFile(ctx, new String[] { dst.toString() }, null, null);
            return dst;
        } catch (IOException exc) {
            dst.delete();
            return null;
        }
    }

    public static File savePrivatePicture(Bitmap data) {
        assert(isInitialized());
        File name = PrivateFile("png");
        return savePicture(name, data);
    }

    public static File savePrivatePicture(BitmapDrawable data) {
        assert(isInitialized());
        return savePrivatePicture(data.getBitmap());
    }

    public static File saveCachePicture(Bitmap data) {
        assert(isInitialized());
        return savePicture(CacheFile("png"), data);
    }

    public static File copyToCachePicture(File src) {
        assert(isInitialized());
        if (src == null) {
            return null;
        }

        File dst;
        if (src.toString().toLowerCase().endsWith("png"))
            dst = CacheFile("png");
        else
            dst = CacheFile("jpg");
        try {
            Utils.copyFile(src, dst);
            return dst;
        } catch (IOException exc) {
            dst.delete();
            return null;
        }
    }

    public static boolean isPrivatePicture(File f) {
        assert(isInitialized());
        return f.getParentFile().equals(PRIVATE_ROOT);
    }

    private static BitmapFactory.Options LoadOptions(int sampleSize) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        // We would like to o.inPreferredConfig = null here so images with
        // alpha load with alpha, and devices with true color screens load
        // true color images. But the HTC Rhyme, and probably other devices,
        // will crash when given a null inPreferredConfig.
        o.inPreferredConfig = Bitmap.Config.RGB_565;
        o.inSampleSize = sampleSize;
        o.inDither = sampleSize > 1;
        return o;
    }

    private static BitmapFactory.Options BoundsOptions() {
        BitmapFactory.Options o = LoadOptions(1);
        o.inPreferredConfig = Bitmap.Config.RGB_565;
        o.inJustDecodeBounds = true;
        return o;
    }

    private static BitmapFile BitmapFile(BitmapDrawable draw) throws Exception {
        File f = savePrivatePicture(draw);
        if (f == null)
            throw new Exception("Unable to load picture.");
        return new BitmapFile(draw, f);
    }

    public static BitmapFile BitmapFile(Bitmap bmp) throws Exception {
        return BitmapFile(BitmapDrawable("bitmap", bmp));
    }

    public static BitmapFile loadPublicPicture(File f) throws Exception {
        assert(isInitialized());
        try {
            BitmapFactory.Options o = BoundsOptions();
            BitmapFactory.decodeFile(f.toString(), o);
            int sampleSize = (int)Math.floor(Math.max(o.outHeight, o.outWidth) / (double)400f);

            o = LoadOptions(sampleSize);
            Bitmap bmp = BitmapFactory.decodeFile(f.toString(), o);
            bmp = scaleExact(f.toString(), bmp);
            BitmapDrawable draw = BitmapDrawable(f.toString(), bmp);
            return BitmapFile(draw);
        } catch (OutOfMemoryError exc) {
            throw new Exception(exc);
        }
    }

    private static BitmapFile downloadPicture(Uri uri) throws Exception {
        // Downloaded pictures are a bit more complicated than other types.
        // We need to save them to a file, import them as a private picture,
        // and then deleted the original file.

        final AndroidHttpClient client = AndroidHttpClient.newInstance(APP_NAME);
        HttpEntity entity = null;

        try {
            URL url = new URL(uri.toString());
            URI correctUri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());

            final HttpGet request = new HttpGet(correctUri.toURL().toString());
            final HttpResponse response = client.execute(request);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK)
                throw new Exception(response.getStatusLine().toString());

            entity = response.getEntity();
            if (entity == null)
                throw new Exception(response.getStatusLine().toString());
            String contentType;
            if (entity.getContentType() == null)
                contentType = "image/jpeg";
            else
                contentType = entity.getContentType().getValue().toLowerCase();

            return loadPublicPicture(entity.getContent(), contentType);
        } catch (IOException e) {
            throw new Exception(e);
        } catch (IllegalStateException e) {
            throw new Exception(e);
        } catch (URISyntaxException e) {
            throw new Exception(e);
        } finally {
            client.close();
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                }
            }
        }
    }

    public static BitmapFile loadPublicPicture(Context ctx, Uri uri) throws Exception {
        assert(isInitialized());

        String scheme = uri.getScheme();
        if (scheme != null && (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
            return downloadPicture(uri);
        }

        try {
            InputStream is = ctx.getContentResolver().openInputStream(uri);
            BitmapFactory.Options o = BoundsOptions();
            BitmapFactory.decodeStream(is, null, o);
            is.close();
            int sampleSize = (int)Math.floor(Math.max(o.outHeight, o.outWidth) / (double)400f);

            o = LoadOptions(sampleSize);
            is = ctx.getContentResolver().openInputStream(uri);

            System.gc();
            Bitmap bmp = BitmapFactory.decodeStream(is, null, o);
            bmp = scaleExact(uri.toString(), bmp);
            BitmapDrawable draw = BitmapDrawable(uri.toString(), bmp);
            return BitmapFile(draw);
        } catch (OutOfMemoryError exc) {
            throw new Exception(exc);
        } catch (FileNotFoundException exc) {
            throw new Exception(exc);
        } catch (IOException exc) {
            throw new Exception(exc);
        }
    }
    
    public static BitmapFile loadPublicPicture(InputStream is, String mimeType) throws Exception{
        assert(isInitialized());
        File cached = null;

        try{
            if (mimeType.equals("image/png"))
                cached = PictureFiles.CacheFile("png");
            else if (mimeType.equals("image/gif"))
                cached = PictureFiles.CacheFile("gif");
            else
                cached = PictureFiles.CacheFile("jpg");

            byte buf[] = new byte[1 << 16];
            int read;
            FileOutputStream fos = new FileOutputStream(cached);
            while ((read = is.read(buf)) != -1)
                fos.write(buf, 0, read);
            fos.close();

            return PictureFiles.loadPublicPicture(cached);
        }catch (FileNotFoundException exc){
            throw new Exception(exc);
        }catch (IOException exc){
            throw new Exception(exc);
        }finally{
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            
            if(cached != null){
                cached.delete();
            }
        }
    }

    public static BitmapDrawable loadPrivatePicture(File f) throws Exception {
        assert(isInitialized());
        assert(f.getParentFile().equals(PRIVATE_ROOT));
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inPreferredConfig = Bitmap.Config.RGB_565;
        o.inDither = true;

        try {
            System.gc();
            Bitmap bmp = BitmapFactory.decodeFile(f.toString(), o);
            return BitmapDrawable(f.toString(), bmp);
        } catch (OutOfMemoryError exc) {
            throw new Exception(exc);
        }
    }

    /**
     * Resource pictures are a special case. We need to make a private copy so
     * they stay the same even if the resource changes, but we've already baked
     * them as part of the build process so we shouldn't need to scale them at
     * runtime.
     * 
     * @throws Exception
     */
    public static BitmapFile loadResourcePicture(Context context, int id) throws Exception {
        assert (isInitialized());
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
            BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
            return BitmapFile(drawable);
        } catch (OutOfMemoryError error) {
            throw new Exception(error);
        }
    }

    public static BitmapFile loadMemoryPicture(Bitmap bmp) throws Exception {
        assert(isInitialized());
        // TODO(jfw): Right now this is just used to load pictures from camera
        // intents that don't support output URIs; these are tiny bitmaps so
        // we don't need to scale them. However to use this more generally it
        // needs to be able to scale the bitmap down before saving.
        //
        // It's also used for changing backgrounds, which are already scaled
        // to screen size.
        return BitmapFile(bmp);
    }

    // Delete a file only if it is a private file. We should never be deleting
    // non-private files; by definition, those are the files the user knows
    // about and wants to keep.
    public static boolean deletePrivatePicture(File file) {
        assert (isInitialized());
        if (file != null && file.getParentFile() != null && file.getParentFile().equals(PRIVATE_ROOT)) {
            return file.delete();
        } else {
            return file == null;
        }
    }

    // Delete any pictures not found in the given picture database.
    // Obviously, this can't be called if there's any chance something
    // changed the database between generating the list and this
    // function's execution.
    public static int cleanPrivatePictures(HashSet<File> allowed) {
        assert(isInitialized());
        int deleted = 0;
        for (File f : PRIVATE_ROOT.listFiles())
            if (!allowed.contains(f) && f.delete())
                deleted++;
        return deleted;
    }
}
