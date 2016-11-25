package com.example.degreeapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkcor.guidelib.GuideView;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String path;

    /**
     * 判断系统中是否存在可以启动的相机应用
     *
     * @return 存在返回true，不存在返回false
     */
    public boolean hasCamera(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        HighLightGuideView.builder(this) .addNoHighLightGuidView(R.mipmap.listlead) .setMaskColor(getResources().getColor(R.color.mask_color)) .show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tv = (TextView) findViewById(R.id.textView);
        Button bt1 = (Button) findViewById(R.id.button);
        Button bt2 = (Button) findViewById(R.id.button2);
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ECO";
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        GuideView.builder(this)
                .addHighLightGuidView(tv, "上传下载报告")
                .addHighLightGuidView(bt1, "上传下载报告")
                .addHighLightGuidView(bt2, "上传下载报告")
                .addHighLightGuidView(fab, "上传下载报告")
                .setHighLightStyle(GuideView.VIEWSTYLE_CIRCLE).show();
//        HighLightGuideView.builder(this)
//                .addHighLightGuidView(tv, R.mipmap.dmtext)
//                .addHighLightGuidView(bt1,R.mipmap.dmtext)
//                .addHighLightGuidView(bt2, R.mipmap.dmtext)
//                .addHighLightGuidView(fab, R.mipmap.dmtext)
//                .setHighLightStyle(HighLightGuideView.VIEWSTYLE_CIRCLE).show();
//        HighLightGuideView h = HighLightGuideView.builder(this).addHighLightGuidView(fab, R.mipmap.dmtext).setHighLightStyle(HighLightGuideView.VIEWSTYLE_CIRCLE);
//        h.show();
//        h.setOnDismissListener(new HighLightGuideView.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
//            }
//        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //1、调用相机
                if (!hasCamera(MainActivity.this)) {
                    return;
                }
                File f = new File(path);
                if (!f.exists()) {
                    f.mkdirs();
                }
                File mPhotoFile = new File(path, "test.jpg");
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri fileUri = Uri.fromFile(mPhotoFile);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(captureIntent, 9527);
            }
        });
    }

    //2、拿到照片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9527 && resultCode == RESULT_OK) {

//            File photoFile = mCapturePhotoHelper.getPhoto();//获取拍完的照片
//            if (photoFile != null) {
//                PhotoPreviewActivity.preview(this, photoFile);//跳转到预览界面
//            }
            ImageView img = (ImageView) findViewById(R.id.img);
            Bitmap bm = BitmapFactory.decodeFile(path + File.separator + "test.jpg");
            int degree = getBitmapDegree(path + File.separator + "test.jpg");
            Bitmap bitmap = rotateBitmapByDegree(bm, degree);
            img.setImageBitmap(bitmap);
//            if (!bm.isRecycled()) {
//                bm.recycle();
//            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取图片的旋转角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照指定的角度进行旋转
     *
     * @param bitmap 需要旋转的图片
     * @param degree 指定的旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(@NonNull Bitmap bitmap, int degree) {
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
