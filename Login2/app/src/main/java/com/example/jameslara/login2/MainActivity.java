package com.example.jameslara.login2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {


    EditText editTextUsuario, editTextContrasenia;
    HttpURLConnection con;
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsuario = (EditText) findViewById(R.id.editTextUser);
        editTextContrasenia = (EditText) findViewById(R.id.editTextPassword);

    }


    public void CheckLogin(View view){
        String usuario, contrasenia;
        usuario = editTextUsuario.getText().toString();
        contrasenia = editTextContrasenia.getText().toString();
        try {
            //Comprobar si la conexión a la red es posible
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                new GetUsuarioTask().execute(
                new URL("http://192.168.55.2:8080/estadias/get_usuario.php?user=" +
                        usuario + "&password=" + contrasenia ));
            } else {
                Toast.makeText(this, "Error de conexion", Toast.LENGTH_LONG).show();
            }
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
    }

    public class GetUsuarioTask extends AsyncTask<URL, Void, Usuario >{
        @Override
        protected Usuario doInBackground( URL ... urls){
            Usuario user = null;
            try{
                con = (HttpURLConnection) urls[0].openConnection();
                int statusCode = con.getResponseCode();
                if (statusCode != 200) {
                    user = new Usuario("-",0);
                    return user;
                }
                else{
                    InputStream in = new BufferedInputStream(con.getInputStream());
                    JSONUsuarioParser parser = new JSONUsuarioParser();
                    user = parser.readJsonStream(in);
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                con.disconnect();
            }
            return user;
        }

        @Override
        protected void onPostExecute( Usuario u)
        {
            if (u.getId() != 0 ) {
                //Datos correctos
                Intent intent = new Intent(getBaseContext(), MainActivity2.class);
                intent.putExtra(EXTRA_MESSAGE, u.getNombre());

                startActivity(intent);
            }
            else
                Toast.makeText(getBaseContext(),
                        "Nombre de usuario o contraseña incorrectos",
                        Toast.LENGTH_LONG).show();
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
}
