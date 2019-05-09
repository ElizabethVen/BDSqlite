package com.example.basededatosaltas;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText et_codigo, et_descripcion, et_precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_codigo = (EditText)findViewById(R.id.txt_codigo);
        et_descripcion = (EditText)findViewById(R.id.descripcion);
        et_precio = (EditText)findViewById(R.id.precio);
    }

    //Metodo para dar de alta los productos
    public void Registrar(View view){
        //crear un objeto de la clase creada previamente
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);

        //abrir BD en modo lectura y escritura
        SQLiteDatabase BaseDeDatos =admin.getWritableDatabase();

        //trabajar con los datos que el usuario vaya proporcionando
        String codigo = et_codigo.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        String precio = et_precio.getText().toString();

        //!codigo.isEmpty = diferente de vacio
        if(!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){
            //Guardar los datos de la BD que el usuario ha escrito
            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);
            //Insertar los datos en la tabla

            BaseDeDatos.insert("articulos", null, registro);
            BaseDeDatos.close();

            et_codigo.setText("");
            et_descripcion.setText("");
            et_precio.setText("");
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para consultar un artìculo o producto
    public void Buscar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDataBase = admin.getWritableDatabase();
        String codigo = et_codigo.getText().toString();

        if(!codigo.isEmpty()){
            Cursor fila = BaseDeDataBase.rawQuery
                    ("select descripcion, precio from articulos where codigo =" + codigo, null);

            if(fila.moveToFirst()){
                et_descripcion.setText(fila.getString(0));
                et_precio.setText(fila.getString(1));
                BaseDeDataBase.close();
            }else{
                Toast.makeText(this,"No existe el articulo", Toast.LENGTH_SHORT).show();
                BaseDeDataBase.close();
            }

        }else {
            Toast.makeText(this,"Debes introducir el codigo del articulo", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para eliminar un producto o articulo

    public void Eliminar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);

        //creamos objeto para abrir BD en modo lectura-escritura
        SQLiteDatabase BaseDataBase = admin.getWritableDatabase();
        //recuperar dato a eliminar de la BD
        String codigo = et_codigo.getText().toString();

        if(!codigo.isEmpty()){

            //Borrar nuestro articulo, delete retorna un entero con la cantidad de articulos borrados

            int cantidad = BaseDataBase.delete("articulos", "codigo=" + codigo, null);
            BaseDataBase.close();

            et_codigo.setText("");
            et_precio.setText("");
            et_descripcion.setText("");

            if(cantidad ==1){
                Toast.makeText(this, "Articulo eliminado exitosamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "El articulo no existe", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Debes de introducir el codigo del articulo", Toast.LENGTH_LONG).show();
        }
    }

    //Metodo para modoficar un articulo o producto

    public void Modificar (View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase BaseDataBase = admin.getWritableDatabase();

        String codigo = et_codigo.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        String precio = et_precio.getText().toString();

        // Validamos que los campos no esten vacios

        if(!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){

            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);
            //Nos permite modificar los valores en nuestra BD
            int cantidad = BaseDataBase.update("articulos", registro, "codigo=" + codigo, null);

            BaseDataBase.close();
            et_codigo.setText("");
            et_precio.setText("");
            et_descripcion.setText("");

            if(cantidad==1){
                Toast.makeText(this, "Articulo modificado correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "El articulo no existe", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }

    }
}
