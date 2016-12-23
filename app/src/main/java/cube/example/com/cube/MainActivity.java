package cube.example.com.cube;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.IntegerRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cs.min2phase.Search;
import com.unity3d.player.UnityPlayer;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private Mat mat;
    private CameraBridgeViewBase cameraBridgeViewBase;
    private Bitmap bitmap;
    private Bitmap afterModify;
    private TextView textView;
    private Button makePrase;
    private String result;
    private TextView[][] facelet;
    private final int[] COLORS = {Color.parseColor("#ffee00") ,Color.parseColor("#008018"),Color.parseColor("#ff0000"),Color.parseColor("#ffffff"),Color.parseColor("#0000ff"),Color.parseColor("#ff8c00")};
    //红，绿，橙，蓝，白，黄
    private int nowColor;
    private String nowColorName;

    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    private LinearLayout unityView;

    private boolean isFirstStep;
    private String []step;
    private int count;
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//
//            String res = msg.getData().getString("res");
//            Log.v("Handler OpenCV Result", res);
//            textView.setText(res);
//        }
//    };
//
//    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
//        @Override
//        public void onManagerConnected(int status) {
//            super.onManagerConnected(status);
//            switch (status) {
//                case LoaderCallbackInterface.SUCCESS:
//                {
//
//                    Log.i("OpenCV", "OpenCV loader successful");
//        //            cameraBridgeViewBase.enableView();
//                   // mat.convertTo(mat, );
//                    // jni解析图片同样也比较费时, 要再开一个线程
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Mat mat = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8U );
//
//                            Utils.bitmapToMat(bitmap, mat);
//                            Log.i("OpenCV", "The convert is success");
//                            // 调用jni
//                            JniUtils jniUtils = new JniUtils();
//                            // 返回的是色块的坐标, 颜色你自己取吧, 有坐标mat可以直接读出来的
//                            String ret = jniUtils.findSquares(mat.getNativeObjAddr());
//                            ret=ret.replace("\n","");
//                            ret=ret.replace(" ","");
//                            ret=ret.replace("),","");
//                            ret=ret.replace(")","");
//                            ret=ret.replace("(","#");
//                            String[] sArray = ret.split("#");
//                            for(int i = 1;i<10;i++){
//                                String[] s = sArray[i].split(",");
//                                int x = Integer.parseInt(s[0]);
//                                int y = Integer.parseInt(s[1]);
//                                int pixel = bitmap.getPixel(x,y);
//                                int red = Color.red(pixel); // same as (pixel >> 16) &0xff
//                                int green = Color.green(pixel); // same as (pixel >> 8) &0xff
//                                int blue = Color.blue(pixel); // same as (pixel & 0xff)
//                                int alpha = Color.alpha(pixel); // same as (pixel >>> 24)
//                                int z = y;
//                                y = z;
//                            }
//                            Message message = new Message();
//                            Bundle b = new Bundle();
//                            b.putString("res", ret);
//                            message.setData(b);
//                            handler.sendMessage(message);
//                        }
//                    }).start();
//     //               textView.setText(ret);
//
//                    //mat = mat.t();
//                    //afterModify = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
//                    //Utils.matToBitmap(mat, afterModify);
//                    Log.i("OpenCV", "The convert is success");
//
//
//                }  break;
//                default:{
//                    super.onManagerConnected(status);
//                } break;
//            }
//        }
//    };
//    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isFirstStep = true;
        mUnityPlayer = new UnityPlayer(this);
        mUnityPlayer.requestFocus();
        unityView = (LinearLayout) findViewById(R.id.unity_layout);
        unityView.addView(mUnityPlayer);
        facelet = new TextView[6][9];
        facelet[0][0] = (TextView)findViewById(R.id.U1);
        facelet[0][1] = (TextView)findViewById(R.id.U2);
        facelet[0][2] = (TextView)findViewById(R.id.U3);
        facelet[0][3] = (TextView)findViewById(R.id.U4);
        facelet[0][4] = (TextView)findViewById(R.id.U5);
        facelet[0][5] = (TextView)findViewById(R.id.U6);
        facelet[0][6] = (TextView)findViewById(R.id.U7);
        facelet[0][7] = (TextView)findViewById(R.id.U8);
        facelet[0][8] = (TextView)findViewById(R.id.U9);

        facelet[1][0] = (TextView)findViewById(R.id.R1);
        facelet[1][1] = (TextView)findViewById(R.id.R2);
        facelet[1][2] = (TextView)findViewById(R.id.R3);
        facelet[1][3] = (TextView)findViewById(R.id.R4);
        facelet[1][4] = (TextView)findViewById(R.id.R5);
        facelet[1][5] = (TextView)findViewById(R.id.R6);
        facelet[1][6] = (TextView)findViewById(R.id.R7);
        facelet[1][7] = (TextView)findViewById(R.id.R8);
        facelet[1][8] = (TextView)findViewById(R.id.R9);

        facelet[2][0] = (TextView)findViewById(R.id.F1);
        facelet[2][1] = (TextView)findViewById(R.id.F2);
        facelet[2][2] = (TextView)findViewById(R.id.F3);
        facelet[2][3] = (TextView)findViewById(R.id.F4);
        facelet[2][4] = (TextView)findViewById(R.id.F5);
        facelet[2][5] = (TextView)findViewById(R.id.F6);
        facelet[2][6] = (TextView)findViewById(R.id.F7);
        facelet[2][7] = (TextView)findViewById(R.id.F8);
        facelet[2][8] = (TextView)findViewById(R.id.F9);

        facelet[3][0] = (TextView)findViewById(R.id.D1);
        facelet[3][1] = (TextView)findViewById(R.id.D2);
        facelet[3][2] = (TextView)findViewById(R.id.D3);
        facelet[3][3] = (TextView)findViewById(R.id.D4);
        facelet[3][4] = (TextView)findViewById(R.id.D5);
        facelet[3][5] = (TextView)findViewById(R.id.D6);
        facelet[3][6] = (TextView)findViewById(R.id.D7);
        facelet[3][7] = (TextView)findViewById(R.id.D8);
        facelet[3][8] = (TextView)findViewById(R.id.D9);

        facelet[4][0] = (TextView)findViewById(R.id.L1);
        facelet[4][1] = (TextView)findViewById(R.id.L2);
        facelet[4][2] = (TextView)findViewById(R.id.L3);
        facelet[4][3] = (TextView)findViewById(R.id.L4);
        facelet[4][4] = (TextView)findViewById(R.id.L5);
        facelet[4][5] = (TextView)findViewById(R.id.L6);
        facelet[4][6] = (TextView)findViewById(R.id.L7);
        facelet[4][7] = (TextView)findViewById(R.id.L8);
        facelet[4][8] = (TextView)findViewById(R.id.L9);

        facelet[5][0] = (TextView)findViewById(R.id.B1);
        facelet[5][1] = (TextView)findViewById(R.id.B2);
        facelet[5][2] = (TextView)findViewById(R.id.B3);
        facelet[5][3] = (TextView)findViewById(R.id.B4);
        facelet[5][4] = (TextView)findViewById(R.id.B5);
        facelet[5][5] = (TextView)findViewById(R.id.B6);
        facelet[5][6] = (TextView)findViewById(R.id.B7);
        facelet[5][7] = (TextView)findViewById(R.id.B8);
        facelet[5][8] = (TextView)findViewById(R.id.B9);


        for(int i= 0;i <6;i++){
            for(int j = 0 ;j<9;j++){
                facelet[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView et = (TextView)v;
                        et.setBackgroundColor(nowColor);
                        et.setText(nowColorName);
                    }
                });
            }
        }
        TextView colorChangeU = (TextView)findViewById(R.id.U);
        colorChangeU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowColor = COLORS[0];
                nowColorName = "0";
            }
        });
        TextView colorChangeR = (TextView)findViewById(R.id.R);
        colorChangeR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowColor = COLORS[1];
                nowColorName = "1";
            }
        });
        TextView colorChangeF = (TextView)findViewById(R.id.F);
        colorChangeF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowColor = COLORS[2];
                nowColorName = "2";
            }
        });
        TextView colorChangeD = (TextView)findViewById(R.id.D);
        colorChangeD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowColor = COLORS[3];
                nowColorName = "3";
            }
        });
        TextView colorChangeL = (TextView)findViewById(R.id.L);
        colorChangeL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowColor = COLORS[4];
                nowColorName = "4";
            }
        });
        TextView colorChangeB = (TextView)findViewById(R.id.B);
        colorChangeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowColor = COLORS[5];
                nowColorName = "5";
            }
        });

        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Click","MakeState");
                String cubeString = getCubeString();
                setRotaSpeed("30");
                String rota = getRota(cubeString,true);
                setRota(rota);
                isFirstStep = true;
            }
        });
        Button button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Click2","SolveCube");
                String cubeString = getCubeString();
                setRotaSpeed("3");
                String rota = getRota(cubeString,false);
                setRota(rota);
            }
        });

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0;i<6;i++)
                    for(int j =0;j<9;j++){
                        facelet[i][j].setBackgroundColor(Color.BLACK);
                        facelet[i][j].setText(i+"");
                        isFirstStep = true;
                    }
//                Log.i("Camera","Take Picture Button Click");
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                }
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, 1);

            }
        });


        makePrase = (Button) findViewById(R.id.makeParse);
        makePrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, MainActivity.this, baseLoaderCallback);
                // 会调用baseLoaderCallback 在上面定义
                if(isFirstStep) {
                    String cubeString = getCubeString();
                    setRotaSpeed("1");
                    String rota = getRota(cubeString, false);
                    step = rota.split("#");
                    count = 0;
                    isFirstStep = false;
                }
                if (count<=step.length) {
                    setRota(step[count]);
                    count++;
                }
            }
        });

        textView = (TextView)findViewById(R.id.textView);


        /*
        cameraBridgeViewBase = (CameraBridgeViewBase)findViewById(R.id.OpenCvCamera);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        */
    }
    public void setRotaSpeed(String speed){
        UnityPlayer.UnitySendMessage("Rubik", "setSpeed", speed);
    }
    public String getCubeString(){
        StringBuffer s = new StringBuffer(54);

        for (int i = 0; i < 54; i++)
            s.insert(i, 'B');// default initialization

        for (int i = 0; i < 6; i++)
            // read the 54 facelets
            for (int j = 0; j < 9; j++) {

                if (facelet[i][j].getText().equals(facelet[0][4].getText()))
                    s.setCharAt(9 * i + j, 'U');
                if (facelet[i][j].getText().equals(facelet[1][4].getText()))
                    s.setCharAt(9 * i + j, 'R');
                if (facelet[i][j].getText().equals(facelet[2][4].getText()))
                    s.setCharAt(9 * i + j, 'F');
                if (facelet[i][j].getText().equals(facelet[3][4].getText()))
                    s.setCharAt(9 * i + j, 'D');
                if (facelet[i][j].getText().equals(facelet[4][4].getText()))
                    s.setCharAt(9 * i + j, 'L');
                if (facelet[i][j].getText().equals(facelet[5][4].getText()))
                    s.setCharAt(9 * i + j, 'B');
            }
        String cubeString = s.toString();
        return cubeString;
    }
    public String getRota( String cubeString,boolean inverse){
        Log.i("Search","Start to Search");
        Search search = new Search();
        if (!Search.isInited())
            Search.init();
        //String cubeString = "DDRDUFUBULRUURLDFUFRBUFBDRRRUFLDDBRRLLLFLFDBBFBFUBLBDL";
        boolean useSeparator = false;
        boolean showLength = false;
        int maxDepth =21;
        int maxTime = 10;

        int mask = 0;
        mask |= useSeparator ? Search.USE_SEPARATOR : 0;
        mask |= inverse ? Search.INVERSE_SOLUTION : 0;
        mask |= showLength ? Search.APPEND_LENGTH : 0;
        result = search.solution(cubeString, maxDepth, 100, 0, mask);
        result += 18;
//        textView = (TextView)findViewById(R.id.textView);
//        textView.setText(result);
        if(result.contains("Error")) {
            switch (result.charAt(result.length() - 1)) {
                case '1':
                    result = "There are not exactly nine facelets of each color!";
                    break;
                case '2':
                    result = "Not all 12 edges exist exactly once!";
                    break;
                case '3':
                    result = "Flip error: One edge has to be flipped!";
                    break;
                case '4':
                    result = "Not all 8 corners exist exactly once!";
                    break;
                case '5':
                    result = "Twist error: One corner has to be twisted!";
                    break;
                case '6':
                    result = "Parity error: Two corners or two edges have to be exchanged!";
                    break;
                case '7':
                    result = "No solution exists for the given maximum move number!";
                    break;
                case '8':
                    result = "Timeout, no solution found within given maximum time!";
                    break;
            }

        }
        textView.setText(result);
        return result;
    }
    void setRota(String rota){
        UnityPlayer.UnitySendMessage("Rubik", "setRota", rota);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
////            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
////                Bundle extras = data.getExtras();
////                bitmap = (Bitmap) extras.get("data");
////                ImageView imageView = (ImageView)findViewById(R.id.imageView);
////                imageView.setImageBitmap(bitmap);
////            }
//            Uri uri = data.getData();
//            Log.e("uri", uri.toString());
//            ContentResolver cr = this.getContentResolver();
//            try{
//                bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//                ImageView imageView = (ImageView)findViewById(R.id.imageView);
//                imageView.setImageBitmap(bitmap);
//            }catch (FileNotFoundException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    // Quit Unity
    @Override protected void onDestroy ()
    {
        mUnityPlayer.quit();
        super.onDestroy();
    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();
        mUnityPlayer.resume();
    }

    // This ensures the layout will be correct.


    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().

}
