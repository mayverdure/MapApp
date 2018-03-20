package com.example.a30032758.android.mapapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Uri _imageUri;
    //パーミッション許可ダイアログで許可を与えた後、すぐにカメラアプリに遷移
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == 2000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            ImageButton ivCamera = (ImageButton) findViewById(R.id.ivCamera);  // （1）
            onCameraImageClick(ivCamera);  // （2）
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(35.70683322, 139.56503562);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //メルボルンに青のマーカー//
//        LatLng MELBOURNE = new LatLng(-37.813, 144.962);
//        Marker melbourne = mMap.addMarker(new MarkerOptions()
//                .position(MELBOURNE)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

    }

    public void onCameraImageClick(View view) {
        //ストレージへのパーミッションチェックコード(if文)
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, 2000);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");  // （1）
        Date now = new Date(System.currentTimeMillis());  // （1）
        String nowStr = dateFormat.format(now);  // （1）
        String fileName = "UseCameraActivityPhoto_" + nowStr +".jpg";  // （1）

        ContentValues values = new ContentValues();  // （2）
        values.put(MediaStore.Images.Media.TITLE, fileName);  // （3）
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");  // （4）

        ContentResolver resolver = getContentResolver();  // （5）
        _imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  // （6）

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  // （7）
        intent.putExtra(MediaStore.EXTRA_OUTPUT, _imageUri);  // （8）
        startActivityForResult(intent, 200);  // （9）
    }

    //Uriを使う前のクリックイベント
//    public void onCameraImageClick(View view) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  // （1）
//        startActivityForResult(intent, 200);  // （2）
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 200 && resultCode == RESULT_OK) {
            ImageView ivCamera = (ImageView) findViewById(R.id.ivCamera);  // （2）
            ivCamera.setImageURI(_imageUri);  // （3）
        }
    }

    //画像を保存せず、そのまま持ってきて表示・すごく小さい画像しかできない。
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == 200 && resultCode == RESULT_OK) {  // （1）
//            Bitmap bitmap =  data.getParcelableExtra("data");  // （2）
//            ImageView ivCamera = (ImageView) findViewById(R.id.ivCamera);  // （3)
//            ivCamera.setImageBitmap(bitmap);  // （3）
//        }
//    }
}

