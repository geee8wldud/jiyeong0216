package com.autorecotest.ict.autorecotest;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;


public class MainActivity extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "opencv";
    private CameraBridgeViewBase mOpenCvCameraView;

    private boolean isbasic;
    private int mRoiX;
    private int mRoiY;
    private int Width;
    private int Height;

    private Mat matInput;
    private Mat m_matRoi;
    private Mat m_matResult = new Mat();

    private Rect mRectRoi;
    private Bitmap bmp_result;

    private Button mBtnCamera;
    private TextView mText;
    private SurfaceView mSurfaceRoi;
    private SurfaceView mSurfaceRoiBorder;
    private android.widget.RelativeLayout.LayoutParams mRelativeParams;

    /* 현재 회전 상태 (하단 Home 버튼의 위치)*/
    private enum mOrientHomeButton {Right, Bottom, Left, Top}
    private OrientationEventListener mOrientEventListener;
    private mOrientHomeButton mCurrOrientHomeButton = mOrientHomeButton.Bottom;

    //**********************************
    //web으로부터 받을 uuid를 저장할 변수
    private String data;
    //**********************************

    static {
        System.loadLibrary("opencv_java3");
    }

    /* 소켓 생성 */
    private Socket socket;  //소켓생성
    BufferedReader in;      //서버로부터 온 데이터를 읽음
    OutputStream os;
    ObjectOutputStream oos;

    /* 카메라 세팅 관련 메소드 */
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* 화면 캡쳐 방지 */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        // 상태바 숨김
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 화면 켜진 상태 유지
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /* 소켓 통신 권한 설정 */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //퍼미션 상태 확인
            if (!hasPermissions(PERMISSIONS)) {
                //퍼미션 허가 안되어있다면 사용자에게 요청
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }

        /* 카메라 설정 */
        mOpenCvCameraView = (CameraBridgeViewBase)findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(0); // front-camera(1),  back-camera(0)
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        /* 뷰 선언 */
        mBtnCamera = (Button) findViewById(R.id.btncamera);
        mText = (TextView) findViewById(R.id.frametext);
        mSurfaceRoi = (SurfaceView) findViewById(R.id.surface_roi);
        mSurfaceRoiBorder = (SurfaceView) findViewById(R.id.surface_roi_border);


        mOrientEventListener = new OrientationEventListener(this,
                SensorManager.SENSOR_DELAY_NORMAL) {

            @Override
            public void onOrientationChanged(int arg0) {

                // 방향센서값에 홈버튼 위치 인식
                // 0˚
                if (arg0 >= 340 || arg0 < 20) {
                    rotateViews(270);
                    mCurrOrientHomeButton = mOrientHomeButton.Bottom;
                    // 90˚
                } else if (arg0 >= 70 && arg0 < 110) {
                    rotateViews(180);
                    mCurrOrientHomeButton = mOrientHomeButton.Left;
                    // 180˚
                } else if (arg0 >= 160 && arg0 < 200) {
                    rotateViews(90);
                    mCurrOrientHomeButton = mOrientHomeButton.Top;
                    // 270˚
                } else if (arg0 >= 250 && arg0 < 290) {
                    rotateViews(0);
                    mCurrOrientHomeButton = mOrientHomeButton.Right;
                }


                //ROI 선 조정
                mRelativeParams = new android.widget.RelativeLayout.LayoutParams(Width + 5, Height + 5);
                mRelativeParams.setMargins(mRoiX, mRoiY, 0, 0);
                mRelativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mRelativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
                mSurfaceRoiBorder.setLayoutParams(mRelativeParams);

                //ROI 영역 조정
                mRelativeParams = new android.widget.RelativeLayout.LayoutParams(Width - 5, Height - 5);
                mRelativeParams.setMargins(mRoiX + 5, mRoiY + 5, 0, 0);
                mRelativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mRelativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
                mSurfaceRoi.setLayoutParams(mRelativeParams);

            }
        };

        //방향센서 핸들러 활성화
        mOrientEventListener.enable();


        //방향센서 인식 오류 시, Toast 메시지 출력 후 종료
        if (!mOrientEventListener.canDetectOrientation()) {
            Toast.makeText(this, "Can't Detect Orientation",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }


    public void rotateViews(int degree) {
        mBtnCamera.setRotation(degree);
        mText.setRotation(degree);

        switch (degree) {
            // 가로
            case 0:

                isbasic = true;

                /* 안내 TextView 위치 조정 */
                mRelativeParams = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mRelativeParams.setMargins(0, 0, 0, 0);
                mRelativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                mText.setLayoutParams(mRelativeParams);


                /* 버튼 View 위치 조정 */
                mRelativeParams = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mRelativeParams.setMargins(0, 0, convertDpToPixel(20), 0);
                mRelativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
                mRelativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                mBtnCamera.setLayoutParams(mRelativeParams);

                break;

            case 180:

                isbasic = true;

                /* 안내 TextView 위치 조정 */
                mRelativeParams = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mRelativeParams.setMargins(0, 0, 0, 0);
                mRelativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                mText.setLayoutParams(mRelativeParams);


                /* 버튼 View 위치 조정 */
                mRelativeParams = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mRelativeParams.setMargins(0, 0, convertDpToPixel(20), 0);
                mRelativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
                mRelativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                mBtnCamera.setLayoutParams(mRelativeParams);

                break;


            // 세로
            case 90:

                isbasic = false;

                /* 안내 TextView 위치 조정 */
                mRelativeParams = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mRelativeParams.setMargins(0, 0, convertDpToPixel(50), 0);
                mRelativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
                mRelativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                mText.setLayoutParams(mRelativeParams);


                /* 버튼 View 위치 조정 */
                mRelativeParams = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mRelativeParams.setMargins(convertDpToPixel(20), 0, 0, 0);
                mRelativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
                mBtnCamera.setLayoutParams(mRelativeParams);

                break;

            case 270:

                isbasic = false;

                /* 안내 TextView 위치 조정 */
                mRelativeParams = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mRelativeParams.setMargins(convertDpToPixel(50), 0, 0, 0);
                mRelativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
                mText.setLayoutParams(mRelativeParams);


                /* 버튼 View 위치 조정 */
                mRelativeParams = new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mRelativeParams.setMargins(0, 0, convertDpToPixel(20), 0);
                mRelativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
                mRelativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                mBtnCamera.setLayoutParams(mRelativeParams);

                break;
        }
    }

    //dp 단위로 입력하기 위한 변환 함수 (px 그대로 사용 시 기기마다 화면 크기가 다르기 때문에 다른 위치에 가버림)
    public int convertDpToPixel(float dp) {

        Resources resources = getApplicationContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;

    }

    /* 비트맵 회전 */
    public synchronized static Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != b2) {
                    bitmap = b2;
                }
            } catch (OutOfMemoryError ex) {
            }
        }

        return bitmap;
    }

    /* 비트맵 -> 바이트 배열 변환 */
    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    /* [통신용 스레드] : 비트맵 이미지를 바이트 배열로 변환하여 전송 */
    class worker extends Thread {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {
            /* 비트맵으로 받은 변환된 이미지의 크기를 800X500으로 조절 */
            Bitmap image_bitmap_copy = Bitmap.createScaledBitmap(bmp_result, 800, 500, true);  //크기 조절 (용량 줄이기)

            //************************************************************
            // web으로부터 uuid를 받음
            Intent intent = getIntent();
            data = intent.getData().getQueryParameter("session_id");
            Log.e("[REST]","넘겨 받은 uuid : "+data);
            //************************************************************

            /* 변환된 이미지를 비트맵 -> 바이트배열로 변환하여 b에 저장 */
            byte[] b = bitmapToByteArray(image_bitmap_copy);
            Log.e("[REST]1", ""+b.length);
            //Log.e("[REST]2", ""+encodeb.length);
            byte[] encodeb = Base64.encode(b,0);         //********************* 수정
//            byte[] encodeb = Base64.getEncoder().encode(b);  //********************** -> api 24에서 오류남

            Log.e("[REST]2", ""+encodeb.length);
            String json = new String(encodeb);
            Log.e("[REST]3", ""+json.length());
            JSONObject js = new JSONObject();
            try {
                js.put("image", json);
                Log.e("[REST]", "put image to json complete");
                Log.e("[REST]4", ""+json.length());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Log.e("[REST]5", ""+js.getString("image").length());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            URL url = null;
            try {
                url = new URL("http://192.168.12.152:8080/info");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                //**************************************************************************
                // web에서 받은 uuid를 header에 넣어줌
                urlConnection.setRequestProperty("uuid", data);
                Log.e("[REST]","보낸 uuid : "+data);
                //**************************************************************************
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(js.toString());
                //urlConnection.getOutputStream().write(b);
                urlConnection.connect();
                int response = urlConnection.getResponseCode();
                urlConnection.disconnect();
                Log.e("[REST]5", "response code :" + response);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /* 소켓 통신 후 어플 종료 */
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
        }

    }


    public void onClickButton(View v) throws IOException {

        bmp_result = Bitmap.createBitmap(m_matRoi.cols(), m_matRoi.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(m_matRoi, bmp_result);

        /* Orientation에 따라 Bitmap 회전 (landscape일 때는 미수행) */
        if (mCurrOrientHomeButton != mOrientHomeButton.Right) {
            switch (mCurrOrientHomeButton) {
                case Bottom:
                    bmp_result = GetRotatedBitmap(bmp_result, 90);
                    break;
                case Left:
                    bmp_result = GetRotatedBitmap(bmp_result, 180);
                    break;
                case Top:
                    bmp_result = GetRotatedBitmap(bmp_result, 270);
                    break;
            }
        }

        worker w = new worker(); //작업 스레드 생성
        w.setDaemon(true); //메인스레드와 종료 동기화
        w.start(); //http 통신 스레드 시작



    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "onResume :: Internal OpenCV library not found.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "onResum :: OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();

        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        matInput = inputFrame.rgba();
        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();


        /* 박스의 가로 세로 비율에 따른 사이즈 설정 */
        Height = (int) display.getHeight() * 8 / 10;
        if (Height > 1080) Height = 1080;

        if (isbasic) Width = (int) Height * 43 / 27;    /* 핸드폰이 가로인 경우 */
        else Width = (int) Height * 27 / 43;            /* 핸드폰이 세로인 경우 */

        mRoiX = (int) (matInput.size().width - Width) / 2;
        mRoiY = (int) (matInput.size().height - Height) / 2;


        /* ROI 영역 생성 */
        mRectRoi = new Rect(mRoiX, mRoiY, Width, Height);

        m_matRoi = matInput.submat(mRectRoi);
        m_matRoi.copyTo(matInput.submat(mRectRoi));

        return matInput;
    }


    /* 퍼미션 관련 메소드 */
    static final int PERMISSIONS_REQUEST_CODE = 1000;
    String[] PERMISSIONS  = {"android.permission.CAMERA"};


    private boolean hasPermissions(String[] permissions) {
        int result;
        /* 스트링 배열에 있는 퍼미션들의 허가 여부 확인 */
        for (String perms : permissions){
            result = ContextCompat.checkSelfPermission(this, perms);
            if (result == PackageManager.PERMISSION_DENIED){ /* 허가 안된 퍼미션이 있는 경우 */
                return false;
            }
        }
        /* 모든 퍼미션이 허가된 경우 */
        return true;
    }



    /* 퍼미션 요청에 따른 결과 */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraPermissionAccepted = grantResults[0]
                            == PackageManager.PERMISSION_GRANTED;
                    if (!cameraPermissionAccepted)
                        showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");
                }
                break;
        }
    }

    /* 퍼미션 요청 다이얼로그 생성 */
    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id){
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.create().show();
    }

}