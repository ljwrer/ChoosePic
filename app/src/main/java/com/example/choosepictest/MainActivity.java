package com.example.choosepictest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MainActivity extends Activity {
    private Button takePhoto;
    private Button choose;
    private ImageView image;
    Uri imageUri;
    public static final int TAKE_PHOTO=1;
    public static final int CROP_PHOTO=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        takePhoto=(Button)findViewById(R.id.take_photo);
        image=(ImageView)findViewById(R.id.picture);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"image.jpg");
                if(file.exists())
                {
                    file.delete();
                }
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri=Uri.fromFile(file);
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,TAKE_PHOTO);

            }
        });
        choose=(Button)findViewById(R.id.choose_from_album);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"image.jpg");
                if(file.exists())
                {
                    file.delete();
                }
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri=Uri.fromFile(file);
//                Intent intent=new Intent("Intent.ACTION_PICK");
//                intent.setType("image/*");
//                intent.putExtra("crop", true);
//                intent.putExtra("scale", true);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//
                Intent intent = new Intent("android.intent.action.PICK");
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                intent.putExtra("output", imageUri);
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
//                intent.putExtra("aspectX", 1);// 裁剪框比例
//                intent.putExtra("aspectY", 1);
//                intent.putExtra("outputX", crop);// 输出图片大小
//                intent.putExtra("outputY", crop);
                 startActivityForResult(intent, CROP_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case TAKE_PHOTO:
                Intent intent=new Intent("com.android.camera.action.CROP");//裁剪
                intent.setDataAndType(imageUri, "image/*");//设置路径与MIME类型
                intent.putExtra("scale", true);//保持纵横比
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//输出地址
                startActivityForResult(intent, CROP_PHOTO);
                break;
            case CROP_PHOTO:
                try {
                    Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    image.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
