package union.union_vr1.Vistas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapterEstablecimientoColor;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Precio;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Barcode_Scanner;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;
import union.union_vr1.Sqlite.DbAdaptert_Evento_Establec;
import union.union_vr1.Utils.MyApplication;

public class VMovil_Menu_Establec extends Activity {


    private DbAdapter_Temp_Session session;
    private DbAdaptert_Evento_Establec dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private CursorAdapterEstablecimientoColor cursorAdapterEstablecimientoColor;
    private DbAdapter_Temp_Barcode_Scanner dbAdapter_temp_barcode_scanner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.princ_menu_establec);


        session = new DbAdapter_Temp_Session(this);
        session.open();


        dbHelper = new DbAdaptert_Evento_Establec(this);
        dbHelper.open();
        dbAdapter_temp_barcode_scanner = new DbAdapter_Temp_Barcode_Scanner(this);
        dbAdapter_temp_barcode_scanner.open();
        //Add some data

        //Generate ListView from SQLite Database
        displayListView();

    }

    private void eleccion(String idEstabl, int idAgente) {
        Intent i = new Intent(this, VMovil_Evento_Establec.class);

        dbAdapter_temp_barcode_scanner.deleteAll();
        //((MyApplication) this.getApplication()).setIdEstablecimiento(Integer.parseInt(idEstabl));
        session.deleteVariable(2);
        session.createTempSession(2,Integer.parseInt(idEstabl));
        dbAdapter_temp_barcode_scanner.createTempScanner(Integer.parseInt(idEstabl));
        session.deleteVariable(2);
        session.createTempSession(2, Integer.parseInt(idEstabl));

        finish();
        startActivity(i);
    }

    private void displayListView() {

        Cursor cursor = dbHelper.listarEstablecimientos();

        /*
        // The desired columns to be bound
        String[] columns = new String[] {
                DbAdaptert_Evento_Establec.EE_id_establec,
                DbAdaptert_Evento_Establec.EE_nom_establec,
                DbAdaptert_Evento_Establec.EE_nom_cliente,
                DbAdaptert_Evento_Establec.EE_doc_cliente
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.VME_codigo,
                R.id.VME_establec,
                R.id.VME_nombre,
                R.id.VME_docum,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.infor_menu_establec,
                cursor,
                columns,
                to,
                0);

*/
        cursorAdapterEstablecimientoColor = new CursorAdapterEstablecimientoColor(this, cursor);


        ListView listView = (ListView) findViewById(R.id.VME_listar);
        listView.setAdapter(cursorAdapterEstablecimientoColor);


        // Assign adapter to ListView

        //bindView();
        //View v = listView.getSelectedView();
        //v.setBackgroundColor(Color.BLUE);
        //listView.findViewById(0).setBackgroundColor(Color.BLUE);
        //v0.setBackgroundColor(Color.BLUE);
        //View v1 = listView.findViewById(0);
        //v1.setBackgroundColor(Color.GREEN);
        //View v2 = listView.findViewById(0);
        //v2.setBackgroundColor(Color.RED);
        //View v3 = listView.findViewById(0);
        //v3.setBackgroundColor(Color.YELLOW);
        //View v4 = listView.getSelectedView();
        //v4.setBackgroundColor(Color.BLUE);

        for (int i = 0; i < listView.getCount(); i++) {
            final View row = listView.getAdapter().getView(i, null, null);
            //Solo deseo colocar background color a las 3 primeras filas, pero
            //al ejecutar no pinta ninguna fila de azul
            row.setBackgroundColor(Color.BLUE);
        }

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String idEstablec =
                        cursor.getString(cursor.getColumnIndexOrThrow("idEstablecimiento"));

                int id_agente = cursor.getInt(cursor.getColumnIndexOrThrow("idAgente"));

                //if(idEstEst == 1){
                //    view.setBackgroundColor(Color.BLUE);
                //}
                //if(idEstEst == 2){
                //    view.setBackgroundColor(Color.GREEN);
                //}
                //if(idEstEst == 3){
                //    view.setBackgroundColor(Color.RED);
                //}
                //if(idEstEst == 4){
                //    view.setBackgroundColor(Color.YELLOW);
                //}


                Toast.makeText(getApplicationContext(),
                        idEstablec + "Aqui po" + id_agente, Toast.LENGTH_SHORT).show();
                eleccion(idEstablec, id_agente);
                //listView.setBackgroundColor(Color.GREEN);

            }
        });

        EditText myFilter = (EditText) findViewById(R.id.VME_buscar);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                cursorAdapterEstablecimientoColor.getFilter().filter(s.toString());
            }
        });

        cursorAdapterEstablecimientoColor.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.listarEstablecimientosPorNombre(constraint.toString());
            }
        });

    }

    public void bindView(View view, Context context, Cursor cur) {

        String color_picto = (cur.getString(cur.getColumnIndex("ee_in_estado_no_atencion")));

        if (color_picto.equals("0")) {

            view.setBackgroundColor(Color.parseColor("#f48905"));
        } else if (color_picto.equals("1")) {
            view.setBackgroundColor(Color.parseColor("#688f2b"));
        } else if (color_picto.equals("2")) {
            view.setBackgroundColor(Color.parseColor("#F781F3"));
        } else if (color_picto.equals("3")) {
            view.setBackgroundColor(Color.parseColor("#003366"));
        } else if (color_picto.equals("4")) {
            view.setBackgroundColor(Color.parseColor("#F7FE2E"));
        } else if (color_picto.equals("5")) {
            view.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }


}