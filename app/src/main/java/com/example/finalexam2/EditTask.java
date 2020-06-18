package com.example.finalexam2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditTask extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private TextView _tv;
    private EditText _et;
    private LinearLayout _layout;
    private String _date, _name, _type;
    private ImageView _img;
    private CheckBox _ch;
    private ArrayList<Task> _tasks;
    private int _position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);
        this._ch = findViewById(R.id.taskchecked);
        this._tv = findViewById(R.id.etaskdate);
        this._et = findViewById(R.id.etaskname);
        this._img = findViewById(R.id.image);
        this._layout = findViewById(R.id.miLayout);

        _layout.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                swiping(false);


            }

            public void onSwipeLeft() {

                swiping(true);

            }

            public void onSwipeBottom() {
            }

        });

        if (getIntent().getExtras() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("image"), 0, getIntent().getByteArrayExtra("image").length);
            this._img.setImageBitmap(bmp);
            this._name = getIntent().getStringExtra("name");
            this._date = getIntent().getStringExtra("date");
            this._position = getIntent().getIntExtra("posicion", 0);
            this._et.setText(this._name);
            if (getIntent().getStringExtra("done").equals("true")) {
                this._ch.setChecked(true);
            }
            this._type = "edit";
            this._tasks = getIntent().getParcelableExtra("tascas");

        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm a");
            _date = simpleDateFormat.format(new Date());
            this._ch.setEnabled(false);
            this._type = "create";
        }
        this._tv.setText(_date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_task_menu, menu);
        return true;
    }

    public void save(MenuItem mi) {
        _name = _et.getText().toString();
        if (_name.equals("")) {
            Toast.makeText(EditTask.this, "No has insertat totes les dades", Toast.LENGTH_SHORT).show();
        } else {

            Intent resultIntent = new Intent();
            Drawable d = _img.getDrawable();
            resultIntent.putExtra("image", imgToByte(d));

            resultIntent.putExtra("name", _name);

            if (_type.equals("edit")) {
                if (this._ch.isChecked()) {
                    resultIntent.putExtra("done", "true");

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm a");
                    _date = simpleDateFormat.format(new Date());

                } else {
                    resultIntent.putExtra("done", "false");
                }
                resultIntent.putExtra("id", getIntent().getIntExtra("id", 0));
            }
            resultIntent.putExtra("date", _date);
            setResult(RESULT_OK, resultIntent);

            finish();

        }
    }

    public void close(MenuItem mi) {
        onBackPressed();
    }

    public byte[] imgToByte(Drawable draw) {
        Bitmap bitmap = ((BitmapDrawable) draw).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        return bitmapdata;
    }

    public void cambiarImagen(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = findViewById(R.id.image);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void swiping(boolean derecha) {
        if (derecha) {
            if (_position < _tasks.size()) {
                Intent i = new Intent(this, EditTask.class);
                i.putExtra("id", _tasks.get(_position + 1).getId());
                i.putExtra("image", _tasks.get(_position + 1).getImage());
                i.putExtra("name", _tasks.get(_position + 1).getName());
                i.putExtra("date", _tasks.get(_position + 1).getDate());
                i.putExtra("done", _tasks.get(_position + 1).getDone());
                i.putExtra("tascas", _tasks);
                i.putExtra("posicion", _position + 1);
                startActivityForResult(i, 2);
                Toast.makeText(this, "SUUUU HE CAMBIAO DERECHA 1" + _position, Toast.LENGTH_SHORT).show();//Toast per a avisar a l'usuari
            } else {

                Intent i = new Intent(this, EditTask.class);
                i.putExtra("id", _tasks.get(0).getId());
                i.putExtra("image", _tasks.get(0).getImage());
                i.putExtra("name", _tasks.get(0).getName());
                i.putExtra("date", _tasks.get(0).getDate());
                i.putExtra("done", _tasks.get(0).getDone());
                i.putExtra("tascas", _tasks);
                i.putExtra("posicion", 0);
                startActivityForResult(i, 2);
                Toast.makeText(this, "SUUUU HE CAMBIAO DERECHA 2" + _position, Toast.LENGTH_SHORT).show();//Toast per a avisar a l'usuari
            }
        } else {

            if (_position > 0) {

                Intent i = new Intent(this, EditTask.class);
                i.putExtra("id", _tasks.get(_position - 1).getId());
                i.putExtra("image", _tasks.get(_position - 1).getImage());
                i.putExtra("name", _tasks.get(_position - 1).getName());
                i.putExtra("date", _tasks.get(_position - 1).getDate());
                i.putExtra("done", _tasks.get(_position - 1).getDone());
                i.putExtra("tascas", _tasks);
                i.putExtra("posicion", _position - 1);
                startActivityForResult(i, 2);
                Toast.makeText(this, "SUUUU HE CAMBIAO IZQUIERDA 1 " + _position, Toast.LENGTH_SHORT).show();//Toast per a avisar a l'usuari

            } else {

                Intent i = new Intent(this, EditTask.class);
                i.putExtra("id", _tasks.get(_tasks.size()).getId());
                i.putExtra("image", _tasks.get(_tasks.size()).getImage());
                i.putExtra("name", _tasks.get(_tasks.size()).getName());
                i.putExtra("date", _tasks.get(_tasks.size()).getDate());
                i.putExtra("done", _tasks.get(_tasks.size()).getDone());
                i.putExtra("tascas", _tasks);
                i.putExtra("posicion", _position);
                startActivityForResult(i, 2);
                Toast.makeText(this, "SUUUU HE CAMBIAO IZQUIERDA 2 " + _position, Toast.LENGTH_SHORT).show();//Toast per a avisar a l'usuari

            }
        }
    }
}
