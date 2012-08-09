package wotw.app;
/*Based on code seen @ the following urls: 
http://stackoverflow.com/questions/2169649/open-an-image-in-androids-built-in-gallery-app-programmatically
http://stackoverflow.com/questions/2935946/sending-images-using-http-post
*/

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class UploadImage extends Activity {


private static final int SELECT_PICTURE = 1;
private String selectedImagePath;
//ADDED
private String filemanagerstring;
private ImageUploader imageUploader;
private String filePath;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadimage);
        final Context myContext = this;
        ((Button) findViewById(R.id.selectPhotoBtn)).setOnClickListener(new OnClickListener()
       {
         public void onClick(View arg0) {

                // in onCreate or any event where your want the user to
                // select a file
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
        
        ((Button) findViewById(R.id.uploadBtn)).setOnClickListener(new OnClickListener()
        {
        public void onClick(View arg0)
        {
        	if(filePath != "")
        	{
	        	imageUploader = new ImageUploader(myContext);
	        	imageUploader.execute(new String[] {filePath});
        	}

        	
        }
        	
        });
    }
    
    

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                //OI FILE Manager
                filemanagerstring = selectedImageUri.getPath();

                //MEDIA GALLERY
                selectedImagePath = getPath(selectedImageUri);

                if(selectedImagePath!=null)
                {
                    addImage(selectedImagePath);
                    filePath =  selectedImagePath;
                }

                else if(filemanagerstring!=null)
                {
                    addImage(filemanagerstring);
                    filePath = filemanagerstring;
                }

            }
        }
    }
    public void addImage(String filePath)
    {
     File fileToSend = new File(filePath);
     Bitmap photoToSend = BitmapFactory.decodeFile(fileToSend.getAbsolutePath());
     ((ImageView) findViewById(R.id.bathroomPhoto)).setImageBitmap(photoToSend);
    
    
    
    }
    //UPDATED!
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }
    private class ImageUploader extends AsyncTask<String, Void, Boolean>{	
    	
    	private Context context;
    	
    	public ImageUploader(Context context)
    	{
    		this.context = context;
    		
    	}
    	private final String url = "http://www.scope-resolution.org/android/image_process.php";
    	
    	@Override
    	protected Boolean doInBackground(String... arg0) {
    		Boolean success = uploadImage(arg0[0]);
    		return success;
    	}
    	
    	@Override
    	 protected void onPostExecute(Boolean result) {
            if(result.equals(true))
            	new AlertDialog.Builder(context).setTitle("First Run").setMessage("Tremendous sucess in your endeavor.").setNeutralButton("OK", null).show();
            else
            	new AlertDialog.Builder(context).setTitle("First Run").setMessage("Failure, oh noes!").setNeutralButton("OK", null).show();
         }

 
    	
    	
    	private Boolean uploadImage(String imagePath)
    	{
    	    HttpClient httpClient = new DefaultHttpClient();
    	    HttpContext localContext = new BasicHttpContext();
    	    HttpPost httpPost = new HttpPost(url);

    	        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                // If the key equals to "image", we use FileBody to transfer the data
                entity.addPart("image", new FileBody(new File (imagePath)));
    	        httpPost.setEntity(entity);

    	        try {
    				HttpResponse response = httpClient.execute(httpPost, localContext);
    			} catch (ClientProtocolException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				return false;
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				return false;
    			}


    	       return true;
    		
    		
    	}

    }

    }
    