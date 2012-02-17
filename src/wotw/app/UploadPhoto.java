package wotw.app;
//Taken from http://stackoverflow.com/questions/2169649/open-an-image-in-androids-built-in-gallery-app-programmatically
import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class UploadPhoto extends Activity {
	
	
	 private static final int SELECT_PICTURE = 1;
	 private String selectedImagePath;
	    //ADDED
	    private String filemanagerstring;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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

        }
    
    
    //UPDATED
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                //OI FILE Manager
                filemanagerstring = selectedImageUri.getPath();

                //MEDIA GALLERY
                selectedImagePath = getPath(selectedImageUri);

                //DEBUG PURPOSE - you can delete this if you want
                if(selectedImagePath!=null)
                {
                    addImage(selectedImagePath);
                }

                else  if(filemanagerstring!=null)
                {
                    System.out.println(filemanagerstring);
                    addImage(filemanagerstring);
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

    }
    
    
