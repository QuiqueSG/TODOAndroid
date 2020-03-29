package org.udg.pds.todoandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.entity.IdObject;
import org.udg.pds.todoandroid.entity.Objecte;
import org.udg.pds.todoandroid.rest.TodoApi;
import org.udg.pds.todoandroid.util.Global;

import java.io.InputStream;
import java.net.Inet4Address;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/*

Classe on esta implementada l'Activitat per la creació d'un objecte i afegir-lo a la BD

 */
public class AddObjecte extends AppCompatActivity implements Callback<IdObject> {

    TodoApi mTodoService;
    private EditText nom;
    private EditText des;
    private ImageView img;

    public static final int PICK_IMAGE = 1;


    @Override
    public void onResponse(Call<IdObject> call, Response<IdObject> response) {
        if (response.isSuccessful()) {
            finish();
        } else {
            Toast.makeText(this, "Error al afegir l'objecte", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<IdObject> call, Throwable t) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objecte_formulari);

        mTodoService = ((TodoApp)this.getApplication()).getAPI();

        //Primer botó per escollir la imatge de la galeria d'Android
        Button escollirImg = (Button) findViewById(R.id.BuscarImatge);

        escollirImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CREEM UN INTENT PERQUÈ L'USUARI AGAFI LA IMATGE QUE VOL
                Intent buscarImatge = new Intent();
                buscarImatge.setType("image/*");
                //Ens assegurem que sigui JPG o PNG (imatge)
                String[] mimeTypes = {"image/jpeg", "image/png"};
                buscarImatge.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                buscarImatge.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(buscarImatge, "Selecciona una imatge"), PICK_IMAGE);

            }
        });

        //Segon botó on cridem per guardar els resultats
        Button guardarObjecte = (Button) findViewById(R.id.GUARDAR);

        guardarObjecte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddObjecte.this.nom = (EditText) findViewById(R.id.ValorNomObjecte);
                AddObjecte.this.des = (EditText) findViewById(R.id.ValorDesObjecte);
                //ImageView img = (ImageView) findViewById(R.id.ImatgeObjecte);

                try {
                    Objecte obj = new Objecte();
                    obj.setDescripcio(des.getText().toString());
                    obj.setNom(nom.getText().toString());

                    Call<IdObject> call = mTodoService.addObjecte(obj);
                    call.enqueue(AddObjecte.this);
                } catch (Exception ex) {
                    return;
                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Si l'usuari selecciona una imatge
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE){
                //data.getData retorna el contingut que hi ha la adresa URI
                Uri selectedImage = data.getData();

                //Ens interessa el path absolut
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                //Moguem el cursor
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                // Ens movem a la primera fila
                cursor.moveToFirst();
                //Obtenim el index de la columna de MediaStore.Images.Media.DATA
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //Obtenim el String
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                //Fem SET per modificar la imatge
                img.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

            }
    }

}
