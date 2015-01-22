package union.union_vr1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import union.union_vr1.Conexion.JSONParser;
import union.union_vr1.JSONParser.ParserAgente;
import union.union_vr1.Objects.Agente;
import union.union_vr1.RestApi.StockAgenteRestApi;
import union.union_vr1.Sqlite.DbAdapter_Agente;
import union.union_vr1.Utils.MyApplication;
import union.union_vr1.Vistas.VMovil_Evento_Indice;
import union.union_vr1.Vistas.VMovil_Online_Pumovil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Login extends Activity implements OnClickListener{


    private Login loginClass;
    private boolean succesLogin;
    ProgressDialog prgDialog;
	private EditText user, pass;
	private Button mSubmit, mSalirs;
    private EditText Txt;
	 // Progress Dialog
    private ProgressDialog pDialog;
    private String pru;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private String var1 = "";
    private DbAdapter_Agente dbAdapter_agente;

    public void setVar1(String var1){
        this.var1=var1;
    }

    public String getVar1(){
        return this.var1;
    }

    //public void modificarValorVar1(){
    //    this.var1 = "pruebas XD";
    //}

    //php login script location:
    
    //localhost :  
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
   // private static final String LOGIN_URL = "http://xxx.xxx.x.x:1234/webservice/login.php";
    
    //testing on Emulator:
    private static final String LOGIN_URL = "http://192.168.0.158:8083/produnion/login.php";
    //private static final String LOGIN_URL = "http://192.168.0.158:8081/webservice/login.php";
  //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/login.php";
    
    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_NOMBRE  = "name";

    //JSON elementos para la tabla m_agente
    private static final String TAG_id_agente = "id_agente";
    private static final String TAG_id_agente_venta = "id_agente_venta";
    private static final String TAG_id_empresa = "id_empresa";
    private static final String TAG_id_usuario = "id_usuario";
    private static final String TAG_nombre_agente = "nombre_agente";
    private static final String TAG_nombre_usuario = "nombre_usuario";
    private static final String TAG_pass_usuario = "pass_usuario";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

        loginClass = this;

        dbAdapter_agente = new DbAdapter_Agente(this);
        dbAdapter_agente.open();


		//setup input fields
		user = (EditText)findViewById(R.id.username);
		pass = (EditText)findViewById(R.id.password);

		//setup buttons
		mSubmit = (Button)findViewById(R.id.login);
        mSalirs = (Button)findViewById(R.id.salir);
		
		//register listeners
		mSubmit.setOnClickListener(this);
        mSalirs.setOnClickListener(this);
        //estaConectado();
        //if(isOnline()){
        //    user.setText("exito");
        //}else{
        //    user.setText("error");
        //}

	}

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }


    protected Boolean estaConectado(){
        if(conectadoWifi()){
            user.setText("Conexion a Wifi");
            return true;
        }else{
            if(conectadoRedMovil()){
                user.setText("Conexion a Movil");
                return true;
            }else{
                user.setText("No Tiene Conexion a Internet");
                return false;
            }
        }
    }

    protected Boolean conectadoWifi(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean conectadoRedMovil(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login:

            succesLogin = false;


            Cursor mCursorAgente = dbAdapter_agente.login(user.getText().toString(), pass.getText().toString());
            if (mCursorAgente!=null){
                mCursorAgente.moveToFirst();
                succesLogin=true;
            }
            if(mCursorAgente.getCount()==0){
                succesLogin=false;
            }

            if (succesLogin){
                ((MyApplication) loginClass.getApplication()).setIdAgente(mCursorAgente.getInt(mCursorAgente.getColumnIndexOrThrow(dbAdapter_agente.AG_id_agente_venta)));
                ((MyApplication) loginClass.getApplication()).setIdLiquidacion(mCursorAgente.getInt(mCursorAgente.getColumnIndexOrThrow(dbAdapter_agente.AG_liquidacion)));
                ((MyApplication) loginClass.getApplication()).setDisplayedHistorialComprobanteAnterior(false);
                Toast.makeText(getApplicationContext(), "Login correcto", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Login.this, VMovil_Online_Pumovil.class);
                finish();
                startActivity(i);
            }else{
                if (conectadoRedMovil()||conectadoWifi()){
                    new LoginRest().execute();
                }else{
                    Toast.makeText(getApplicationContext(), "Necesita estar conectado a internet la primera vez", Toast.LENGTH_SHORT).show();
                }
            }

			//new AttemptLogin().execute();
			break;
		case R.id.salir:
            finish();
			break;
		default:
			break;
		}
	}

    class LoginRest extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            StockAgenteRestApi api = new StockAgenteRestApi();
            ArrayList<Agente> agenteLista = null;
            JSONObject jsonObjAgente = null;
            try {
                jsonObjAgente = api.GetAgenteVenta(user.getText().toString(),pass.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ParserAgente parserAgente = new ParserAgente();

            publishProgress(""+25);
            agenteLista = parserAgente.parserAgente(jsonObjAgente);
            Log.d("JSON OBJECT AGENTE", ""+jsonObjAgente.toString());


            publishProgress(""+50);
            if (agenteLista.size()>0){
                succesLogin = true;
                for (int i = 0; i < agenteLista.size() ; i++) {
                    Log.d("Agente"+i, "Nombre : "+agenteLista.get(i).getNombreAgente());

                    //VARIABLE GLOBAL, PARA OBTENERLA DESDE CUALQUIER SITIO DE LA APLICACIÓN
                    ((MyApplication) loginClass.getApplication()).setIdAgente(agenteLista.get(i).getIdAgenteVenta());
                    ((MyApplication) loginClass.getApplication()).setIdLiquidacion(agenteLista.get(i).getLiquidacion());
                    ((MyApplication) loginClass.getApplication()).setDisplayedHistorialComprobanteAnterior(false);

                    agenteLista.get(i).getIdAgenteVenta();
                    boolean existe = dbAdapter_agente.existeAgentesById(agenteLista.get(i).getIdAgenteVenta());
                    Log.d("EXISTE ", ""+existe);
                    if (existe){
                        dbAdapter_agente.updateAgente(agenteLista.get(i));
                    }else {
                        //NO EXISTE ENTONCES CREEMOS UNO NUEVO
                        dbAdapter_agente.createAgente(agenteLista.get(i));
                    }
                }
            }
            publishProgress(""+75);


            return null;
        }

        @Override
        protected void onPreExecute() {
            createProgressDialog();
        }

        @Override
        protected void onPostExecute(String s) {
            prgDialog.setProgress(100);
            prgDialog.dismiss();

            if (succesLogin){

                Toast.makeText(getApplicationContext(), "Login correcto", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Login.this, VMovil_Online_Pumovil.class);
                finish();
                startActivity(i);
            }else{
                Toast.makeText(getApplicationContext(), "Usuario y/o password incorrecto", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            prgDialog.setProgress(Integer.parseInt(values[0]));
        }
    }

	class AttemptLogin extends AsyncTask<String, String, String> {

		 /**
         * Before starting background thread Show Progress Dialog
         * */
		boolean failure = false;
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
            int success;
            String nombre = "";
            String username = user.getText().toString();
            String password = pass.getText().toString();
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
 
                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                       LOGIN_URL, "POST", params);
 
                // check your log for json response
                Log.d("Login attempt", json.toString());
 
                // json success tag
                success = json.getInt(TAG_SUCCESS);
                nombre = json.getString(TAG_SUCCESS);
                //user.setText(String.valueOf(nombre));
                //modificarValorVar1();
                //setVar1(String.valueOf(success));
                //pass.setText(String.valueOf(success));
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                	Intent i = new Intent(Login.this, VMovil_Online_Pumovil.class);

                    i.putExtra("putPassUsuario", json.getString(TAG_pass_usuario));
                    i.putExtra("putNombreAgente", json.getString(TAG_nombre_agente));
                    i.putExtra("putNombreUsuario", json.getString(TAG_nombre_usuario));
                    i.putExtra("putIdAgenteVenta", json.getString(TAG_id_agente_venta));
                    i.putExtra("putIdEmpresa", json.getString(TAG_id_empresa));
                    i.putExtra("putIdUsuario", json.getString(TAG_id_usuario));


                	finish();
    				startActivity(i);
                	return json.getString(TAG_MESSAGE);
                }
                if (success == 0) {
                	Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
			
		}
		/**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
            	Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}

    public void createProgressDialog(){
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Logeando...");
        prgDialog.setIndeterminate(false);
        prgDialog.setMax(100);
        prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        prgDialog.setCancelable(false);
        prgDialog.show();

    }
		 

}
