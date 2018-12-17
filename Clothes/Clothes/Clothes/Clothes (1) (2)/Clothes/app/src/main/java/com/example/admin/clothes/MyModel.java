package com.example.admin.clothes;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyModel extends Activity {

    Button plus, closemodel;
    private String imagePath;
    final int PICK_FROM_CAMERA = 0;
    final int PICK_FROM_ALBUM = 1;
    final int CROP_FROM_IMAGE = 2;

    private String mCurrentPhotoPath;
    private static final int REQUEST_IMAGE_CAPTURE = 3;

    private Uri contentUri;


    private Uri mImageCaptureUri;
    private ImageView modelpicture;
    private String absoultePath;

    //앨범 비트맵
    public static Bitmap image_bitmap;
    //카메라 비트맵
    //static Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model);

        registerEvent();
    }



    public void registerEvent() {

        plus = (Button)findViewById(R.id.plus);
        closemodel=(Button)findViewById(R.id.closemodel);
        modelpicture = (ImageView) findViewById(R.id.modelpicture);


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSimple();
            }
        });
        closemodel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void DialogSimple() {
        final AlertDialog.Builder alt = new AlertDialog.Builder(this);
        alt.setMessage("전신사진을 등록하세요").setCancelable(false).setPositiveButton("앨범",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbumAction();


                    }
                }).setNegativeButton("카메라", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakePhotoAction();
            }
        }).setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alt.create();
        alert.setTitle("사진등록");
        alert.setIcon(R.drawable.icon);
        alert.show();
    }

    //카메라 앱이 있는지 없는지 확인
    private boolean isExistCameraApplication() {
        PackageManager packageManager = this.getPackageManager();
        Intent cameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        List CameraApps = packageManager.queryIntentActivities(cameraApp, PackageManager.MATCH_DEFAULT_ONLY);
        return CameraApps.size() > 0;
    }


    //카메라 사진촬영
    public void doTakePhotoAction() {
        if (isExistCameraApplication()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //  String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
            //  mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

            //   intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
            //  modelpicture.setImageURI(mImageCaptureUri);


        } else {
            Toast.makeText(this, "카메라 앱을 설치하세요", Toast.LENGTH_LONG);
        }
    }

    //앨범에서 사진 가져오기
    public void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != this.RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_ALBUM: {
             /*   mImageCaptureUri=data.getData();
                Log.d("SmartWheel",mImageCaptureUri.getPath().toString());
                Intent intent=new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                //crop할 이미지를 300*600 크기로 저장
                intent.putExtra("outputX",600);
                intent.putExtra("outputY",1100);
                intent.putExtra("aspectX",1);
                intent.putExtra("aspectY",1.5);
                intent.putExtra("scale",true);
                intent.putExtra("return-data",true);
                startActivityForResult(intent, CROP_FROM_IMAGE);*/

                if (resultCode == Activity.RESULT_OK) {
                    try {
                        //이미지 데이터를 비트맵으로 받아온다.
                        image_bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        //배치해놓은 ImageView에 set
                        modelpicture.setImageBitmap(image_bitmap);
                       // plus.setBackgroundResource(R.drawable.modelreset);
                        //plus.setVisibility(View.INVISIBLE);

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            }
            case PICK_FROM_CAMERA: {

                dispatchTakePictureIntent();

//                // modelpicture.setImageURI(data.getData());
//                Intent intent = new Intent("com.android.camera.action.CROP");
//                intent.setDataAndType(mImageCaptureUri, "image/*");
//
//                //crop할 이미지를 300*600 크기로 저장
//                intent.putExtra("outputX", 600);
//                intent.putExtra("outputY", 1100);
//                intent.putExtra("aspectX", 1);
//                intent.putExtra("aspectY", 1.5);
//                intent.putExtra("scale", true);
//                intent.putExtra("return-data", true);
//                startActivityForResult(intent, CROP_FROM_IMAGE);


            }
            case CROP_FROM_IMAGE: {
                //크롭된 이후의 이미지를 넘겨받는다.
                //이미지뷰에 이미지를 보여준다거나 부가적인 작업이후에 임시파일 삭제
                if (resultCode != this.RESULT_OK) {
                    return;
                }
                final Bundle extras = data.getExtras();

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel/" + System.currentTimeMillis() + ".jpg";

                if (extras != null) {
                    image_bitmap = extras.getParcelable("data");
                    modelpicture.setImageBitmap(image_bitmap);
                    plus.setBackgroundResource(R.drawable.modelreset);
                    storeCropImage(image_bitmap, filePath);
                    absoultePath = filePath;

                    break;
                }
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) {
                    f.delete();
                }
            }
            case REQUEST_IMAGE_CAPTURE: {
                //rotatePhoto();
                cropImage(contentUri);
                break;
            }
        }
    }

    private void storeCropImage(Bitmap bitmap, String filePath) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel";
        File directory_smartWheel = new File(dirPath);

        if (!directory_smartWheel.exists())
            directory_smartWheel.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
//            ...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                contentUri = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        Log.d("BoardFragment", "storageDir : " + storageDir);
        Log.d("BoardFragment", "fileName : " + imageFileName);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }
//
//    public void rotatePhoto() {
//        ExifInterface exif;
//        try {
//            if(mCurrentPhotoPath == null) {
//                mCurrentPhotoPath = contentUri.getPath();
//            }
//            exif = new ExifInterface(mCurrentPhotoPath);
//            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//            int exifDegree = exifOrientationToDegrees(exifOrientation);
//            if(exifDegree != 0) {
//                Bitmap bitmap = getBitmap();
//                Bitmap rotatePhoto = rotate(bitmap, exifDegree);
//                saveBitmap(rotatePhoto);
//            }
//        }
//        catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//
//    public int exifOrientationToDegrees(int exifOrientation)
//    {
//        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
//        {
//            return 90;
//        }
//        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
//        {
//            return 180;
//        }
//        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
//        {
//            return 270;
//        }
//        return 0;
//    }
//
//    public Bitmap getBitmap() {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inInputShareable = true;
//        options.inDither=false;
//        options.inTempStorage=new byte[32 * 1024];
//        options.inPurgeable = true;
//        options.inJustDecodeBounds = false;
//
//        File f = new File(mCurrentPhotoPath);
//
//        FileInputStream fs=null;
//        try {
//            fs = new FileInputStream(f);
//        } catch (FileNotFoundException e) {
//            //TODO do something intelligent
//            e.printStackTrace();
//        }
//
//        Bitmap bm = null;
//
//        try {
//            if(fs!=null) bm=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, options);
//        } catch (IOException e) {
//            //TODO do something intelligent
//            e.printStackTrace();
//        } finally{
//            if(fs!=null) {
//                try {
//                    fs.close();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//        return bm;
//    }


    private void cropImage(Uri contentUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri of image
        cropIntent.setDataAndType(contentUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, CROP_FROM_IMAGE);
    }
}
