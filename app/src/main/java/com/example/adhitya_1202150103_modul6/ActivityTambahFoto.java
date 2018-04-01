package com.example.adhitya_1202150103_modul6;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

;

public class ActivityTambahFoto extends AppCompatActivity implements View.OnClickListener {

    //Deklarasi View
    @BindView(R.id.postBTN) //@BindView declare sekaligus inisialisasi view dengan menggunakan library ButterKnife
    FloatingActionButton postBTN;
    @BindView(R.id.titleTV) //@BindView declare sekaligus inisialisasi view dengan menggunakan library ButterKnife
    TextInputEditText titleTV;
    @BindView(R.id.postTV) //@BindView declare sekaligus inisialisasi view dengan menggunakan library ButterKnife
    TextInputEditText postTV;
    @BindView(R.id.photoIMG) //@BindView declare sekaligus inisialisasi view dengan menggunakan library ButterKnife
    ImageView photoIMG;
    @BindView(R.id.chooseBTN) //@BindView declare sekaligus inisialisasi view dengan menggunakan library ButterKnife
    Button chooseBTN;
    private StorageReference refPhotoProfile;
    private Uri URLphoto;
    private ProgressDialog progBarDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        ButterKnife.bind(this); //Binding ButterKnife pada activity
        chooseBTN.setOnClickListener(this);
        postBTN.setOnClickListener(this);
        progBarDialog = new ProgressDialog(this);
    }

    //method handling onClickListener
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chooseBTN: //tombol choose (pilih gambar)
                //ImagePicker library untuk menampilkan dialog memilih gambar pada gallery/camera
                ImagePicker.create(this)
                        .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                        .folderMode(true) // folder mode (false by default)
                        .toolbarFolderTitle("Folder") // folder selection title
                        .toolbarImageTitle("Tap to select") // image selection title
                        .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
                        .single() // single mode
                        .limit(1) // max images can be selected (99 by default)
                        .showCamera(true) // show camera or not (true by default)
                        .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                        .enableLog(false) // disabling log
                        .start(); // start image picker activity with request code
                break;
            case R.id.postBTN:
                //validasi kosong
                if(titleTV.getText().toString().isEmpty()) {
                    titleTV.setError("Required");
                    return;
                }
                //validasi kosong
                if(postTV.getText().toString().isEmpty()) {
                    postTV.setError("Required");
                    return;
                }
                //validasi gambar sudah dipilih
                if(!isPicChange) {
                    Toast.makeText(this, "Choose The Photo!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progBarDialog.setMessage("Uploading..");
                progBarDialog.setIndeterminate(true);
                progBarDialog.show();

                //melakukan proses update foto
                refPhotoProfile = Constant.storageRef.child("gambar/" + System.currentTimeMillis() + ".jpg"); //akses path dan filename storage di firebase untuk menyimpan gambar
                StorageReference photoImagesRef = Constant.storageRef.child("gambar/" + System.currentTimeMillis() + ".jpg");
                refPhotoProfile.getName().equals(photoImagesRef.getName());
                refPhotoProfile.getPath().equals(photoImagesRef.getPath());

                //mengambil gambar dari imageview yang sudah di set menjadi selected image sebelumnya
                photoIMG.setDrawingCacheEnabled(true);
                photoIMG.buildDrawingCache();
                Bitmap bitmap = photoIMG.getDrawingCache(); //convert imageview ke bitmap
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); //convert bitmap ke bytearray
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = refPhotoProfile.putBytes(data); //upload image yang sudah dalam bentuk bytearray ke firebase storage
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        URLphoto = taskSnapshot.getDownloadUrl(); //setelah selesai upload, ambil url gambar
                        String key = Constant.refPhoto.push().getKey(); //ambil key dari node firebase

                        //push atau insert data ke firebase database
                        Constant.refPhoto.child(key).setValue(new FotoModel(
                                key,
                                URLphoto.toString(),
                                titleTV.getText().toString(),
                                postTV.getText().toString(),
                                Constant.currentUser.getEmail().split("@")[0],
                                Constant.currentUser.getEmail()));
                        progBarDialog.dismiss();
                        Toast.makeText(ActivityTambahFoto.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
        }
    }

    boolean isPicChange = false;

    //method untuk handling result dari activity lain contoh disini adalah imagepicker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) { // jika ada data dipilih
            Image image = ImagePicker.getFirstImageOrNull(data); //ambil first image
            File imgFile = new File(image.getPath()); // dapatkan lokasi gambar yang dipilih
            if(imgFile.exists()){ //jika ditemukan
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath()); //convert file ke bitmap
                photoIMG.setImageBitmap(myBitmap); //set imageview dengan gambar yang dipilih
                isPicChange = true; // ubah state menjadi true untuk menandakan gambar telah dipilih
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
