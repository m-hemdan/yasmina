package com.example.yasmina;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Showall extends AppCompatActivity {
    int PICK_IMAGE_REQUEST = 2;
    private Uri filePath;
    ImageView imageView;
    TextView textView, result1, result2;
    EditText nameEdit;
    Button pickImage, uploadBtn, rotateLeftBtn, rotateRightBut;
    private DatabaseReference mDatabase;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
    StorageReference storageRef, imagesRef, spaceRef;
    Bitmap bitmap, rotatedBitmap;
    int imageHeight, imageWidth;
    List arrImage;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showall);
        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference ref = firebaseDatabase.getReference("HairTable");
        imageView = (ImageView) findViewById(R.id.setImageView);
        storage = FirebaseStorage.getInstance("gs://databasetest2-32b6b.appspot.com");
        storageRef = storage.getReference();
        pickImage = (Button) findViewById(R.id.picture);
        textView = (TextView) findViewById(R.id.tt);
        String email = getIntent().getStringExtra("email");
        textView.setText(email);
        nameEdit = (EditText) findViewById(R.id.name_img);
        result1 = (TextView) findViewById(R.id.result1);
        result2 = (TextView) findViewById(R.id.result2);
        uploadBtn = (Button) findViewById(R.id.upload);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        arrImage = new ArrayList<>();
        arrImage.add("gs://databasetest2-32b6b.appspot.com/hairColor_section/imag5.webp");
        arrImage.add("gs://databasetest2-32b6b.appspot.com/hairColor_section/image1.webp");
        arrImage.add("gs://databasetest2-32b6b.appspot.com/hairColor_section/image2.webp");
        arrImage.add("gs://databasetest2-32b6b.appspot.com/hairColor_section/image3.jpg");
        arrImage.add("gs://databasetest2-32b6b.appspot.com/hairColor_section/image4.jpg");
        arrImage.add("gs://databasetest2-32b6b.appspot.com/hairColor_section/image6.jpg");
        arrImage.add("gs://databasetest2-32b6b.appspot.com/hairColor_section/image9.jpg");
        //  final String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(Showall.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    StorageReference ref = storageRef.child("face_liner/" + UUID.randomUUID().toString());
                    Bitmap bmp = null;
                    try {
                        bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                    byte[] data = baos.toByteArray();
                    //uploading the image
                    UploadTask uploadTask2 = ref.putBytes(data);
                    uploadTask2
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!urlTask.isSuccessful()) ;
                                    Uri downloadUrl = urlTask.getResult();
                                    SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");
                                    String now = ISO_8601_FORMAT.format(new Date());
                                    Content content = new Content(nameEdit.getText().toString(), downloadUrl.toString(), arrImage , 1, "subText", "0",1);
                                    mDatabase.child("faceSection").push().setValue(content);
                                    progressDialog.dismiss();
                                    Toast.makeText(Showall.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Showall.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //imageView.setImageBitmap(bitmap);
                Bitmap rotatedBitmap = rotateImageIfRequired(Showall.this, bitmap, filePath);
                imageView.setImageBitmap(rotatedBitmap);
                imageHeight = bitmap.getHeight();
                imageWidth = bitmap.getWidth();
                Toast.makeText(getApplicationContext(), "image succ upload", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "image faild upload", Toast.LENGTH_LONG).show();

            }
        }
    }

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
}
