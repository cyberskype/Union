package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapterComprobanteVenta;
import union.union_vr1.Sqlite.CursorAdapter_Autorizacion_Cobros;
import union.union_vr1.Sqlite.CursorAdapter_Man_Can_Dev;
import union.union_vr1.Sqlite.CursorAdapter_Man_Cbrz;
import union.union_vr1.Sqlite.DBAdapter_Temp_Autorizacion_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Canjes_Devoluciones;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Cobro;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta;
import union.union_vr1.Sqlite.DbAdapter_Comprob_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Utils.MyApplication;

import static union.union_vr1.R.layout.prompts_cobros;
import static union.union_vr1.R.layout.prompts_cobros_fecha;

public class VMovil_Venta_Comprob extends Activity {


    private DbAdapter_Temp_Session session;
    private int idEstablec;
    private DbAdapter_Comprob_Venta dbHelper;
    private CursorAdapterComprobanteVenta cursorAdapterComprobanteVenta;
    private DbAdapter_Comprob_Venta_Detalle dbHelper_Comp_Venta_Detalle;
    private DbAdapter_Stock_Agente dbHelper_Stock_Agente;
    private DbAdapter_Comprob_Cobro dbHelper_Comprob_Cobro;
    private DbAdapter_Canjes_Devoluciones dbHelper_Canjes_Dev;
    private DBAdapter_Temp_Autorizacion_Cobro dbAutorizaciones;
    private int idComprobante;
    private int idAgente;


    TabHost tH;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.princ_venta_comprob);

        session = new DbAdapter_Temp_Session(this);
        session.open();


        //-----------borrar luego
         DbAdapter_Histo_Venta_Detalle k = new DbAdapter_Histo_Venta_Detalle(getApplication());
        k.open();
        dbAutorizaciones = new DBAdapter_Temp_Autorizacion_Cobro(this);
        dbAutorizaciones.open();
        //k.deleteAllHistoVentaDetalle();
        //k.insertSomeHistoVentaDetalle();
        //-----------------


        dbHelper = new DbAdapter_Comprob_Venta(this);
        dbHelper.open();
        dbHelper_Comp_Venta_Detalle = new DbAdapter_Comprob_Venta_Detalle(this);
        dbHelper_Comp_Venta_Detalle.open();
        dbHelper_Stock_Agente = new DbAdapter_Stock_Agente(this);
        dbHelper_Stock_Agente.open();
        dbHelper_Comprob_Cobro = new DbAdapter_Comprob_Cobro(this);
        dbHelper_Comprob_Cobro.open();


        //---
        dbHelper_Canjes_Dev = new DbAdapter_Canjes_Devoluciones(this);
        dbHelper_Canjes_Dev.open();

        //idAgente=((MyApplication) this.getApplication()).getIdAgente();
        idAgente = session.fetchVarible(1);

        tH = (TabHost) findViewById(R.id.tabMante);
        tH.setup();
        //idEstablec = ((MyApplication) this.getApplication()).getIdEstablecimiento();

        idEstablec =session.fetchVarible(2);


        //Item1
        TabHost.TabSpec spec = tH.newTabSpec("1");
        spec.setContent(R.id.comprob);
        spec.setIndicator("Comprobantes");
        displayListView();
        tH.addTab(spec);
        // Item2
        TabHost.TabSpec spec2 = tH.newTabSpec("2");
        spec2.setContent(R.id.cobranza);
        spec2.setIndicator("Cobranza");
        listarCobranzas();
        tH.addTab(spec2);
//Item 3
        TabHost.TabSpec spec3 = tH.newTabSpec("3");
        spec3.setContent(R.id.canje);
        spec3.setIndicator("Canje/Devoluciones");
        listarCanjes_devoluciones(idEstablec);
        tH.addTab(spec3);
//Item 4
        TabHost.TabSpec spec4 = tH.newTabSpec("4");
        spec4.setContent(R.id.autoriz);
        spec4.setIndicator("Autorización");
        displayAutorizaciones();
        tH.addTab(spec4);


        /*Bundle bundle = getIntent().getExtras();
        valIdEstabX=bundle.getString("idEstabX");
        dbHelper = new DbAdapter_Comprob_Venta(this);
        dbHelper.open();
        dbHelpery = new DbAdapter_Comprob_Venta_Detalle(this);
        dbHelpery.open();
        //Generate ListView from SQLite Database
        displayListView();
        */
    }
    private void listarCanjes_devoluciones(int idEstabl){


        Cursor cr = dbHelper_Canjes_Dev.listarCanjesDev(idEstabl);
        cr.moveToFirst();
        CursorAdapter_Man_Can_Dev adapterCanjes_Dev = new CursorAdapter_Man_Can_Dev(getApplicationContext(),cr);
        ListView listaCanjes_Dev = (ListView) findViewById(R.id.listarCanjDev);
        listaCanjes_Dev.setAdapter(adapterCanjes_Dev);
    }

    private void listarCobranzas() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Cursor cursor = dbHelper_Comprob_Cobro.listarComprobantesToCobrosMante("" + idEstablec);
        CursorAdapter_Man_Cbrz cAdapter_Cbrz_Man = new CursorAdapter_Man_Cbrz(this, cursor);
        final ListView listCbrz = (ListView) findViewById(R.id.VVCO_cbrz);
        listCbrz.setAdapter(cAdapter_Cbrz_Man);


        listCbrz.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cr2 = (Cursor) listCbrz.getItemAtPosition(i);
                if (cr2.getString(7).equals("Cobrado")) {
                    String idCompro = cr2.getString(0);
                    String factura = cr2.getString(1);
                    String hora = cr2.getString(5);
                    String fecha = cr2.getString(9);
                    Double monto = Double.parseDouble(cr2.getString(6));

                    dialog(idCompro, factura, hora, fecha, monto);
                } else {
                    Toast.makeText(getApplicationContext(), "Ya se Encuentra Anulado", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void dialog(final String idCompro, String factura, final String hora, final String fecha, final Double monto) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Anular");

        // set dialog message
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("¿Desea Anular?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dbHelper_Comprob_Cobro.updateComprobCobrosMan(idCompro, fecha, hora, monto, "1");
                        Toast.makeText(getApplicationContext(),
                                "Actualizado", Toast.LENGTH_SHORT).show();

                        Intent w = new Intent(getApplicationContext(), VMovil_Evento_Establec.class);
                        w.putExtra("idEstab", ""+idEstablec);
                        w.putExtra("idAgente", idAgente);

                        startActivity(w);
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),
                                "Cancelo", Toast.LENGTH_SHORT).show();
                    }
                });
        listarCobranzas();

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    private void displayAutorizaciones(){

        Cursor cursor = dbAutorizaciones.listarAutorizaciones(idEstablec);
        CursorAdapter_Autorizacion_Cobros adapterAutorizacion = new CursorAdapter_Autorizacion_Cobros(getApplicationContext(),cursor);
        ListView listAuCobros = (ListView) findViewById(R.id.listAutorizacionCobros);
        listAuCobros.setAdapter(adapterAutorizacion);
        listAuCobros.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor crCobros = (Cursor)adapterView.getItemAtPosition(i);
                crCobros.moveToPosition(i);
                String id = crCobros.getString(crCobros.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_autorizacion_cobro));
                String estado = crCobros.getString(crCobros.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_id_estado_solicitud));
                String idDetalleCobro = crCobros.getString(crCobros.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_montoCredito));
                String idComprobante = crCobros.getString(crCobros.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_id_comprobante));
                String fecha = crCobros.getString(crCobros.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_fechaLimite));
                if(estado.equals("1")){

                    Toast.makeText(getApplicationContext(),"Solicitud Aun por Aprobarse",Toast.LENGTH_SHORT).show();
                }if(estado.equals("2")){
                    select(crCobros,fecha);

                }if(estado.equals("4")){
                    selectEliminar(id,idDetalleCobro,idComprobante);
                    //Toast.makeText(getApplicationContext(),"Solicitud Anulada",Toast.LENGTH_SHORT).show();

                }if(estado.equals("5")){

                    Toast.makeText(getApplicationContext(),"Ya Ejecutada"+id+"-"+idDetalleCobro+"",Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
    public void selectEliminar(final String idAutorizacion, final String idDetalleCobro, final String idComprobante){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Cancelar el Proceso");

        // set dialog message
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("Se ha Negado la Prologa de Credito, tiene que Anular el Proceso.")
                .setCancelable(false)
                .setPositiveButton("Anular", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       // boolean up = dbAutorizaciones.anularAutorizacion(idAutorizacion, idDetalleCobro, idComprobante);
                        if (true) {
                            back();
                            Toast.makeText(getApplicationContext(), "Anulado Correctamente", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Ocurrio un Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }
    public long calcularFecha(String fecha){
        long startDate=0;
        Log.d("FECHA","  fecha:"+fecha);
        try {
            String dateString = fecha;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(dateString);
            startDate = date.getTime();
            Log.d("FECHA","lon:"+startDate+"  fecha:"+fecha);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return  startDate;
    }
    public long calcularFechaHoy(){
        long startDate=0;

        try {
            String dateString = getDatePhone();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(dateString);
            startDate = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return  startDate;
    }

    public void cambiarFecha(Cursor cr, final String fecha){
        final int idAutorizacion = cr.getInt(cr.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_id_autorizacion_cobro));
        final int idAEstablecimiento = cr.getInt(cr.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_establec));
        final String idComprobanteCobro = cr.getString(cr.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_id_comprobante));
        final Double montoCredito = cr.getDouble(cr.getColumnIndexOrThrow(DBAdapter_Temp_Autorizacion_Cobro.temp_vigencia_credito));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle("Confirmar Prologa de Pagos");
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout_cobros = inflater.inflate(prompts_cobros_fecha, null);
        final DatePicker dp = (DatePicker) layout_cobros.findViewById(R.id.fechakel);
        Log.d("PARAMS",""+idComprobanteCobro+"-"+montoCredito);
        dp.setMaxDate(calcularFecha(fecha));
        dp.setMinDate(calcularFechaHoy());
        alertDialogBuilder.setView(layout_cobros);
        // set dialog message
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("La Feha Limite es: "+fecha)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int day = dp.getDayOfMonth();
                        int month = dp.getMonth()+1;
                        int year = dp.getYear();
                        String fechaRenovada = day + "/" + month + "/" + year;
                        int aut = dbAutorizaciones.updateAutorizacionCobro_Au(idAutorizacion, 5, idAEstablecimiento, fecha);
                        int cobr = dbHelper_Comprob_Cobro.updateComprobCobros_Auto(idComprobanteCobro, montoCredito, fechaRenovada);

                        if (cobr == 1 && aut==1) {
                            Toast.makeText(getApplicationContext(), "Inserto Correctamente", Toast.LENGTH_SHORT).show();
                            back();

                        } else {
                            Toast.makeText(getApplicationContext(), "Ocurrio un Error", Toast.LENGTH_SHORT).show();
                        }

                    }

                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),
                                "Cancelo", Toast.LENGTH_SHORT).show();
                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    public void select(final Cursor cr,final String fecha){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle("Confirmar Prologa de Pagos");

            // set dialog message
            AlertDialog.Builder builder = alertDialogBuilder
                    .setMessage("Aprobado, la Fecha Limite para el cobro es: " + fecha + "")
                    .setCancelable(false)
                    .setPositiveButton("Ir", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // boolean up = dbAutorizaciones.updateAutorizacionAprobado(idAutorizacion, idDetalleCobro);
                            if (true) {
                                cambiarFecha(cr,fecha);
                                Toast.makeText(getApplicationContext(), "Guardado Correctamente", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Ocurrio un Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });


            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

    }

private void back(){
    Intent w = new Intent(getApplicationContext(), VMovil_Evento_Establec.class);
    w.putExtra("idEstab", ""+idEstablec);
    w.putExtra("idAgente", idAgente);

    startActivity(w);
}


    private void displayListView() {

        Cursor cursor = dbHelper.fetchAllComprobVenta();
        cursorAdapterComprobanteVenta = new CursorAdapterComprobanteVenta(this, cursor);


        ListView listView = (ListView) findViewById(R.id.VVCO_listar);
        listView.setAdapter(cursorAdapterComprobanteVenta);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                idComprobante =
                        Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_id_comprob)));

                int estado = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Comprob_Venta.CV_estado_comp)));


                switch (estado) {
                    case 0:
                        Toast.makeText(getApplicationContext(),
                                "El comprobante ya se encuentra anulado", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        dialogCambiarEstado(idComprobante);
                        break;
                    default:

                        break;

                }

            }
        });


        EditText myFilter = (EditText) findViewById(R.id.VVCO_buscar);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                cursorAdapterComprobanteVenta.getFilter().filter(s.toString());
            }
        });

        cursorAdapterComprobanteVenta.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchComprobVentaByName(constraint.toString());
            }
        });

    }


    public void dialogCambiarEstado(final int idComprobante) {

        final String[] items = {"Anular"};
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Anular comprobante");
        dialogo.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {


                Cursor cursorComprobanteVentaDetalle = dbHelper_Comp_Venta_Detalle.fetchAllComprobVentaDetalleByIdComp(idComprobante);
                cursorComprobanteVentaDetalle.moveToFirst();

                int id_producto;
                int cantidad;
                int precioUnitario;
                int costeVenta;

                cursorComprobanteVentaDetalle.moveToFirst();
                if (cursorComprobanteVentaDetalle.getCount() > 0) {
                    do {
                        id_producto = cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_id_producto));
                        cantidad = cursorComprobanteVentaDetalle.getInt(cursorComprobanteVentaDetalle.getColumnIndex(DbAdapter_Comprob_Venta_Detalle.CD_cantidad));

                        dbHelper_Stock_Agente.updateStockAgenteCantidad(id_producto, cantidad);

                    } while (cursorComprobanteVentaDetalle.moveToNext());
                } else {
                    Toast.makeText(getApplicationContext(), "No ha registros de este comprobante de venta : ", Toast.LENGTH_LONG).show();
                }

                dbHelper.updateComprobante(idComprobante, 0);

                finish();
                Intent intent2 = new Intent(getApplicationContext(), VMovil_Venta_Comprob.class);
                startActivity(intent2);

                Toast.makeText(getApplicationContext(), "Comprobante Anulado ", Toast.LENGTH_LONG).show();

            }
        });
        dialogo.create();
        dialogo.show();
    }

/*
    private void eleccion(String idComprobDet){
        //cursor = manager.BuscarAgentes(tv.getText().toString());
        cursorx = dbHelpery.fetchAllComprobVentaDetalleByIdComp(idComprobDet);
        //Nos aseguramos de que existe al menos un registro
        if (cursorx.moveToFirst()) {
            txtResultado = "";
            //Recorremos el cursor hasta que no haya más registros
            do {
                String val01 = cursorx.getString(3);
                String val02 = cursorx.getString(2);
                String val03 = cursorx.getString(4);
                String val04 = cursorx.getString(5);
                //txtResultado += val01 + " - " + val02 + " - " + val03 + " - " + "\n";
                if(val02.length()>17) {
                    txtResultado += String.format("%-6s", val01) + String.format("%-19s", val02.substring(0, 17) + ".") + String.format("%7s", val03) + String.format("%7s", val04) + "\n";
                }else{
                    txtResultado += String.format("%-6s", val01) + String.format("%-19s", val02) + String.format("%7s", val03) + String.format("%7s", val04) + "\n";
                }
            } while(cursorx.moveToNext());
        }
        Toast.makeText(getApplicationContext(), txtResultado, Toast.LENGTH_SHORT).show();
        elec();
    }

    private void elec(){
        //Intent i = new Intent(this, VMovil_Evento_Establec.class);
        //i.putExtra("idEstab", idEstabl);
        //startActivity(i);
        final String[] items = {"Buscar", "Abrir", "Imprimir", "Cerrar"};
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("ACCION");
        dialogo.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(item == 0){
                    findBT();
                }
                if(item == 1){
                    try{
                        openBT();
                    } catch (IOException ex) {

                    }
                }
                if(item == 2){
                    try{
                        sendData();
                    } catch (IOException ex) {

                    }
                }
                if(item == 3){
                    try{
                        closeBT();
                    } catch (IOException ex) {

                    }
                }

                Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_LONG).show();
            }
        });
        dialogo.create();
        dialogo.show();

    }

    private void displayListView() {

        Cursor cursor = dbHelper.fetchAllComprobVentaByEstable(valIdEstabX);

        // The desired columns to be bound
        String[] columns = new String[] {
                DbAdapter_Comprob_Venta.CV_serie,
                DbAdapter_Comprob_Venta.CV_num_doc,
                DbAdapter_Comprob_Venta.CV_base_imp,
                DbAdapter_Comprob_Venta.CV_igv,
                DbAdapter_Comprob_Venta.CV_total
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.VVCO_seriedinf,
                R.id.VVCO_nrodocinf,
                R.id.VVCO_basimpinf,
                R.id.VVCO_igvvalinf,
                R.id.VVCO_totalvinf,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_venta_comprob,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.VVCO_listar);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                // Get the state's capital from this row in the database.
                idComprob = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                //Toast.makeText(getApplicationContext(),
                //        idComprob, Toast.LENGTH_SHORT).show();
                eleccion(idComprob);
            }
        });

        EditText myFilter = (EditText) findViewById(R.id.VVCO_buscar);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchComprobVentaByName(constraint.toString());
            }
        });

    }

    // This will find a bluetooth printer device
    public void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                //titulo.setText("No bluetooth adapter available");
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                    .getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // MP300 is the name of the bluetooth printer device
                    if (device.getName().equals("Star Micronics")) {
                        mmDevice = device;
                        break;
                    }
                }
            }
            //titulo.setText("Bluetooth Device Found");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tries to open a connection to the bluetooth printer device
    public void openBT() throws IOException {
        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            //titulo.setText("Bluetooth Opened");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // After opening a connection to bluetooth printer device,
    // we have to listen and check if a data were sent to be printed.
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // This is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()
                            && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length);
                                        final String data = new String(
                                                encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
                                                //titulo.setText(data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendData() throws IOException {
        try {

            String texto = ".\n";
            texto += "    UNIVERSIDAD PERUANA UNION   \n";
            texto += "     Cent.aplc. Prod. Union     \n";
            texto += "   C. Central Km 19 Villa Union \n";
            texto += " Lurigancho-Chosica Fax: 6186311\n";
            texto += "      Telf: 6186309-6186310     \n";
            texto += " Casilla 3564, Lima 1, LIMA PERU\n";
            texto += "         RUC: 20138122256       \n";
            texto += "--------------------------------\n";
            //texto += "Factura Nro. 030-000212\n";
            //texto += "Fecha: 12/11/2014\n";
            //texto += "Cajero: Juan Perez Perez\n";
            //texto += "Cliente: Perico Palotes Palotes\n";
            //texto += "DNI: 47678934\n";
            //texto += "Direccion: Alameda Nro 2039 - Chosica\n";
            //texto += "--------------------------------\n";
            //texto += "Cant. Producto           Importe\n";
            //texto += "--------------------------------\n";
            //texto += String.format("%-6s", 5) + String.format("%-19s", "Pan Americano Mediano Union".substring(0, 17) + ".") + String.format("%7s", 40.50) + "\n";
            //texto += String.format("%-6s", 5) + String.format("%-19s", "Rollo de caneladddddddddd".substring(0, 17) + ".") + String.format("%7s", 30.50) + "\n";
            //texto += String.format("%-6s", 5) + String.format("%-19s", "Paneton Unioneeeeeeeeee".substring(0, 17) + ".") + String.format("%7s", 10.50) + "\n";
            //texto += String.format("%-6s", 5) + String.format("%-19s", "Paneton Super Bom".substring(0, 17) + ".") + String.format("%7s", 3.40) + "\n";
            //texto += String.format("%-6s", 5) + String.format("%-19s", "Pan Americano Sandwich".substring(0, 17) + ".") + String.format("%7s", 100.3) + "\n";
            //texto += String.format("%-6s", 5) + String.format("%-19s", "Pan Americano Mediano Union".substring(0, 17) + ".") + String.format("%7s", 2.34) + "\n";
            //texto += String.format("%-25s", "SUB TOTAL:") + String.format("%7s", 1000.00) + "\n";
            //texto += String.format("%-25s", "IGV:") + String.format("%7s", 180.00) + "\n";
            //texto += String.format("%-25s", "TOTAL:") + String.format("%7s", 1800.00) + "\n";

            mmOutputStream.write(txtResultado.getBytes());

            // tell the user data were sent
            //titulo.setText("Data Sent");

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Close the connection to bluetooth printer.
    public void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            //titulo.setText("Bluetooth Closed");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
}