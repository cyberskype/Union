package union.union_vr1.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import union.union_vr1.Conexion.DbHelper;
import union.union_vr1.Objects.HistorialVentaDetalles;

public class DbAdapter_Histo_Venta_Detalle {

    public static final String HD_id_hventadet = "_id";
    public static final String HD_id_detalle = "hd_in_id_detalle";
    public static final String HD_id_comprob = "hd_in_id_comprob";
    public static final String HD_id_establec = "hd_in_id_establec";
    public static final String HD_id_producto = "hd_in_id_producto";
    public static final String HD_id_tipoper = "hd_in_id_tipoper";
    public static final String HD_orden = "hd_in_orden";
    public static final String HD_comprobante = "hd_te_comprobante";
    public static final String HD_nom_estab = "hd_te_nom_estab";
    public static final String HD_nom_producto = "hd_te_nom_producto";
    public static final String HD_cantidad = "hd_in_cantidad";
    public static final String HD_importe = "hd_re_importe";
    public static final String HD_fecha = "hg_te_fecha";
    public static final String HD_hora = "hd_te_hora";
    public static final String HD_valor_unidad = "hd_in_valor_unidad";
    public static final String HD_categoria_ope = "hd_in_categoria_ope";
    public static final String HD_forma_ope = "hd_in_forma_ope";
    public static final String HD_cantidad_ope = "hd_in_cantidad_ope";
    public static final String HD_importe_ope = "hd_re_importe_ope";
    public static final String HD_fecha_ope = "hg_te_fecha_ope";
    public static final String HD_hora_ope = "hd_te_hora_ope";
    public static final String HD_lote = "hd_te_lote";
    public static final String HD_Guia = "hd_te_idGuia";
    public static final String HD_estado = "hd_in_estado";
    public static final String HD_id_agente = "hd_in_id_agente";
    public static final String HD_cantidad_ope_dev = "hd_in_cantidad_ope_dev";
    public static final String HD_categoria_ope_dev = "hd_in_categoria_ope_dev";
    public static final String HD_importe_ope_dev = "hd_re_importe_ope_dev";
    public static final String HD_fecha_ope_dev = "hg_te_fecha_ope_dev";
    public static final String HD_hora_ope_dev = "hd_te_hora_ope_dev";
    public static final String estado_sincronizacion = "estado_sincronizacion";

    public static final String TAG = "Histo_Venta_Detalle";
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;

    //private static final int DATABASE_VERSION = 1;
    //private static final String DATABASE_NAME = "ProdUnion.sqlite";
    private static final String SQLITE_TABLE_Histo_Venta_Detalle = "m_histo_venta_detalle";
    private final Context mCtx;

    public static final String CREATE_TABLE_HISTO_VENTA_DETALLE =
            "create table "+SQLITE_TABLE_Histo_Venta_Detalle+" ("
                    +HD_id_hventadet+" integer primary key autoincrement,"
                    +HD_id_detalle+" text,"
                    +HD_id_comprob+" integer,"
                    +HD_id_establec+" integer,"
                    +HD_id_producto+" integer,"
                    +HD_id_tipoper+" integer,"
                    +HD_orden+" integer,"
                    +HD_comprobante+" text,"
                    +HD_nom_estab+" text,"
                    +HD_nom_producto+" text,"
                    +HD_cantidad+" integer,"
                    +HD_importe+" real,"
                    +HD_fecha+" text,"
                    +HD_hora+" text,"
                    +HD_valor_unidad+" integer,"
                    +HD_categoria_ope+" integer,"
                    +HD_forma_ope+" integer,"
                    +HD_cantidad_ope+" integer,"
                    +HD_importe_ope+" real,"
                    +HD_fecha_ope+" text,"
                    +HD_hora_ope+" text,"
                    +HD_lote+" text,"
                    +HD_Guia+" text,"
                    +HD_estado+" integer,"
                    +HD_id_agente+" integer,"
                    +HD_cantidad_ope_dev+" integer,"
                    +HD_categoria_ope_dev+" integer,"
                    +HD_importe_ope_dev+" real,"
                    +HD_fecha_ope_dev+" text,"
                    +HD_hora_ope_dev+" text, " +
                    estado_sincronizacion+" integer);";

    public static final String DELETE_TABLE_HISTO_VENTA_DETALLE = "DROP TABLE IF EXISTS " + SQLITE_TABLE_Histo_Venta_Detalle;

    public DbAdapter_Histo_Venta_Detalle(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter_Histo_Venta_Detalle open() throws SQLException {
        mDbHelper = new DbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createHistoVentaDetalle(
            int id_detalle, int id_comprob, int id_establec, int id_producto, int id_tipoper, int orden,
            String comprobante, String nom_estab, String nom_producto, int cantidad, double importe,
            String fecha, String hora, int valor_unidad, int categoria_ope, int forma_ope,
            int cantidad_ope, double importe_ope, String fecha_ope, String hora_ope, String lote,
            String lugar_registro, int estado, int id_agente,int cantidad_ope_dev,int categoria_ope_dev,double importe_ope_dev,String fecha_ope_dev,String hora_ope_dev){

        ContentValues initialValues = new ContentValues();
        initialValues.put(HD_id_detalle,id_detalle);
        initialValues.put(HD_id_comprob,id_comprob);
        initialValues.put(HD_id_establec,id_establec);
        initialValues.put(HD_id_producto,id_producto);
        initialValues.put(HD_id_tipoper,id_tipoper);
        initialValues.put(HD_orden,orden);
        initialValues.put(HD_comprobante,comprobante);
        initialValues.put(HD_nom_estab,nom_estab);
        initialValues.put(HD_nom_producto,nom_producto);
        initialValues.put(HD_cantidad,cantidad);
        initialValues.put(HD_importe,importe);
        initialValues.put(HD_fecha,fecha);
        initialValues.put(HD_hora,hora);
        initialValues.put(HD_valor_unidad,valor_unidad);
        initialValues.put(HD_categoria_ope,categoria_ope);
        initialValues.put(HD_forma_ope,forma_ope);
        initialValues.put(HD_cantidad_ope,cantidad_ope);
        initialValues.put(HD_importe_ope,importe_ope);
        initialValues.put(HD_fecha_ope,fecha_ope);
        initialValues.put(HD_hora_ope,hora_ope);
        initialValues.put(HD_lote,lote);
        initialValues.put(HD_Guia,lugar_registro);
        initialValues.put(HD_estado,estado);
        initialValues.put(HD_id_agente,id_agente);
        initialValues.put(HD_cantidad_ope_dev,cantidad_ope_dev);
        initialValues.put(HD_categoria_ope_dev,categoria_ope_dev);
        initialValues.put(HD_importe_ope_dev,importe_ope_dev);
        initialValues.put(HD_fecha_ope_dev,fecha_ope_dev);
        initialValues.put(HD_hora_ope_dev,hora_ope_dev);

        return mDb.insert(SQLITE_TABLE_Histo_Venta_Detalle, null, initialValues);
    }

    public long createHistoVentaDetalle(HistorialVentaDetalles historialVentaDetalles){

        ContentValues initialValues = new ContentValues();
        initialValues.put(HD_id_detalle,historialVentaDetalles.getIdDetalle());
        initialValues.put(HD_id_comprob,historialVentaDetalles.getIdComprobante());
        initialValues.put(HD_id_establec,historialVentaDetalles.getIdEstablecimiento());
        initialValues.put(HD_id_producto,historialVentaDetalles.getIdProducto());
        initialValues.put(HD_id_tipoper,historialVentaDetalles.getidTipoOperacion());
        initialValues.put(HD_orden,historialVentaDetalles.getOrden());
        initialValues.put(HD_comprobante,historialVentaDetalles.getComprobante());
        initialValues.put(HD_nom_estab,historialVentaDetalles.getNombreEstablecimiento());
        initialValues.put(HD_nom_producto,historialVentaDetalles.getNombreProducto());
        initialValues.put(HD_cantidad,historialVentaDetalles.getCantidad());
        initialValues.put(HD_importe,historialVentaDetalles.getImporte());
        initialValues.put(HD_fecha,historialVentaDetalles.getFecha());
        initialValues.put(HD_hora,historialVentaDetalles.getHora());
        initialValues.put(HD_valor_unidad,historialVentaDetalles.getValorUnidad());
        initialValues.put(HD_categoria_ope,historialVentaDetalles.getCategoriaOperacion());
        initialValues.put(HD_forma_ope,historialVentaDetalles.getFormaOperacion());
        initialValues.put(HD_cantidad_ope,historialVentaDetalles.getCantidadOperacion());
        initialValues.put(HD_importe_ope,historialVentaDetalles.getImporteOperacion());
        initialValues.put(HD_fecha_ope,historialVentaDetalles.getFechaOperacion());
        initialValues.put(HD_hora_ope,historialVentaDetalles.getHoraOperacion());
        initialValues.put(HD_lote, historialVentaDetalles.getLote());
        initialValues.put(HD_Guia,historialVentaDetalles.getLugarRegistro());
        initialValues.put(HD_estado, historialVentaDetalles.getEstado());
        initialValues.put(HD_id_agente,historialVentaDetalles.getIdAgente());
        initialValues.put(HD_cantidad_ope_dev,historialVentaDetalles.getCantidadOperacionesDevuelto());
        initialValues.put(HD_categoria_ope_dev,historialVentaDetalles.getCategoriaOperacionesDevuelto());
        initialValues.put(HD_importe_ope_dev,historialVentaDetalles.getImporteOperacionesDevuelto());
        initialValues.put(HD_fecha_ope_dev,historialVentaDetalles.getFechaOperacionesDevuelto());
        initialValues.put(HD_hora_ope_dev,historialVentaDetalles.getHoraOperacionesDevuelto());
        initialValues.put(Constants._SINCRONIZAR,historialVentaDetalles.getEstadoSincronizacion());

        return mDb.insert(SQLITE_TABLE_Histo_Venta_Detalle, null, initialValues);
    }

    public void updateHistoVentaDetalle(HistorialVentaDetalles historialVentaDetalles){
        ContentValues initialValues = new ContentValues();
        initialValues.put(HD_id_detalle,historialVentaDetalles.getIdDetalle());
        initialValues.put(HD_id_comprob,historialVentaDetalles.getIdComprobante());
        initialValues.put(HD_id_establec,historialVentaDetalles.getIdEstablecimiento());
        initialValues.put(HD_id_producto,historialVentaDetalles.getIdProducto());
        initialValues.put(HD_id_tipoper,historialVentaDetalles.getidTipoOperacion());
        initialValues.put(HD_orden,historialVentaDetalles.getOrden());
        initialValues.put(HD_comprobante,historialVentaDetalles.getComprobante());
        initialValues.put(HD_nom_estab,historialVentaDetalles.getNombreEstablecimiento());
        initialValues.put(HD_nom_producto,historialVentaDetalles.getNombreProducto());
        initialValues.put(HD_cantidad,historialVentaDetalles.getCantidad());
        initialValues.put(HD_importe,historialVentaDetalles.getImporte());
        initialValues.put(HD_fecha,historialVentaDetalles.getFecha());
        initialValues.put(HD_hora,historialVentaDetalles.getHora());
        initialValues.put(HD_valor_unidad,historialVentaDetalles.getValorUnidad());
        initialValues.put(HD_categoria_ope,historialVentaDetalles.getCategoriaOperacion());
        initialValues.put(HD_forma_ope,historialVentaDetalles.getFormaOperacion());
        initialValues.put(HD_cantidad_ope,historialVentaDetalles.getCantidadOperacion());
        initialValues.put(HD_importe_ope,historialVentaDetalles.getImporteOperacion());
        initialValues.put(HD_fecha_ope,historialVentaDetalles.getFechaOperacion());
        initialValues.put(HD_hora_ope,historialVentaDetalles.getHoraOperacion());
        initialValues.put(HD_lote, historialVentaDetalles.getLote());
        initialValues.put(HD_Guia,historialVentaDetalles.getLugarRegistro());
        initialValues.put(HD_estado, historialVentaDetalles.getEstado());
        initialValues.put(HD_id_agente,historialVentaDetalles.getIdAgente());
        initialValues.put(HD_cantidad_ope_dev,historialVentaDetalles.getCantidadOperacionesDevuelto());
        initialValues.put(HD_categoria_ope_dev,historialVentaDetalles.getCategoriaOperacionesDevuelto());
        initialValues.put(HD_importe_ope_dev,historialVentaDetalles.getImporteOperacionesDevuelto());
        initialValues.put(HD_fecha_ope_dev,historialVentaDetalles.getFechaOperacionesDevuelto());
        initialValues.put(HD_hora_ope_dev,historialVentaDetalles.getHoraOperacionesDevuelto());
        initialValues.put(Constants._SINCRONIZAR,historialVentaDetalles.getEstadoSincronizacion());

        mDb.update(SQLITE_TABLE_Histo_Venta_Detalle, initialValues,
                HD_id_detalle+"=?",new String[]{""+historialVentaDetalles.getIdDetalle()});
    }

    public boolean existeHistoVentaDetalle(String idDetalle) {
        boolean exists = false;
        Cursor mCursor = mDb.query(SQLITE_TABLE_Histo_Venta_Detalle, new String[] {HD_id_hventadet,
                        HD_id_detalle, HD_id_comprob, HD_id_establec, HD_id_producto, HD_id_tipoper,
                        HD_orden, HD_comprobante, HD_nom_producto, HD_cantidad, HD_importe, HD_fecha,
                        HD_categoria_ope, HD_forma_ope, HD_cantidad_ope, HD_importe_ope, HD_fecha_ope, HD_estado},
                HD_id_detalle + " = " + idDetalle, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
            exists=true;
        }
        if (mCursor.getCount()==0){
            exists=false;
        }
        return exists;
    }
    public void updateHistoVentaDetalle1(String idorig, int id_tipoper, int categoria_ope, int forma_ope,
            int cantidad_ope, double importe_ope, String fecha_ope, String hora_ope, String lote,
            String lugar_registro, int estado){
        ContentValues initialValues = new ContentValues();
        initialValues.put(HD_id_tipoper,id_tipoper);
        initialValues.put(HD_categoria_ope,categoria_ope);
        initialValues.put(HD_forma_ope,forma_ope);
        initialValues.put(HD_cantidad_ope,cantidad_ope);
        initialValues.put(HD_importe_ope,importe_ope);
        initialValues.put(HD_fecha_ope,fecha_ope);
        initialValues.put(HD_hora_ope,hora_ope);
        initialValues.put(HD_lote,lote);
        initialValues.put(HD_Guia,lugar_registro);
        initialValues.put(HD_estado,estado);
        mDb.update(SQLITE_TABLE_Histo_Venta_Detalle, initialValues,
                HD_id_hventadet+"=?",new String[]{idorig});
    }

    public void updateHistoVentaDetalle2(String idorig, String iddest, String vals){
        ContentValues initialValues = new ContentValues();
        initialValues.put(HD_id_hventadet,iddest);

        mDb.update(SQLITE_TABLE_Histo_Venta_Detalle, initialValues,
                HD_id_hventadet+"=? AND "+HD_importe_ope+"=?",new String[]{idorig, vals});
    }

    public boolean deleteAllHistoVentaDetalle() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Histo_Venta_Detalle, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public boolean deleteAllHistoVentaDetalleById(String id) {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_Histo_Venta_Detalle, HD_id_hventadet+"=?", new String[]{id});
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchHistoVentaDetalleByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_Histo_Venta_Detalle, new String[] {HD_id_hventadet,
                            HD_id_detalle, HD_id_comprob, HD_id_establec, HD_id_producto, HD_id_tipoper,
                            HD_orden, HD_comprobante, HD_nom_producto, HD_cantidad, HD_importe, HD_fecha,
                            HD_categoria_ope, HD_forma_ope, HD_cantidad_ope, HD_importe_ope, HD_fecha_ope, HD_estado},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_Histo_Venta_Detalle, new String[] {HD_id_hventadet,
                            HD_id_detalle, HD_id_comprob, HD_id_establec, HD_id_producto, HD_id_tipoper,
                            HD_orden, HD_comprobante, HD_nom_producto, HD_cantidad, HD_importe, HD_fecha,
                            HD_categoria_ope, HD_forma_ope, HD_cantidad_ope, HD_importe_ope, HD_fecha_ope, HD_estado},
                    HD_comprobante + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor filterExport() {
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Histo_Venta_Detalle, new String[] {HD_id_hventadet,
                        HD_id_detalle, HD_id_comprob, HD_id_establec,HD_id_agente, HD_id_producto, HD_id_tipoper,
                        HD_orden, HD_comprobante, HD_nom_producto, HD_cantidad, HD_importe, HD_fecha,
                        HD_categoria_ope, HD_categoria_ope_dev, HD_forma_ope, HD_cantidad_ope, HD_cantidad_ope_dev, HD_importe_ope,HD_importe_ope_dev, HD_fecha_ope_dev, HD_hora_ope_dev , HD_fecha_ope, HD_estado, HD_lote},
                Constants._SINCRONIZAR + " = " + Constants._CREADO + " OR " + Constants._SINCRONIZAR + " = " + Constants._ACTUALIZADO, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor filterExportUpdated() {
        Cursor mCursor = null;
        mCursor = mDb.query(true, SQLITE_TABLE_Histo_Venta_Detalle, new String[] {HD_id_hventadet,
                        HD_id_detalle, HD_id_comprob, HD_id_establec,HD_id_agente, HD_id_producto, HD_id_tipoper,
                        HD_orden, HD_comprobante, HD_nom_producto, HD_cantidad, HD_importe, HD_fecha,
                        HD_categoria_ope, HD_forma_ope, HD_cantidad_ope, HD_importe_ope, HD_fecha_ope, HD_estado, HD_lote},
                Constants._SINCRONIZAR + " = " + Constants._ACTUALIZADO, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public Cursor fetchAllHistoVentaDetalle() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Histo_Venta_Detalle, new String[] {HD_id_hventadet,
                        HD_id_detalle, HD_id_comprob, HD_id_establec, HD_id_producto, HD_id_tipoper,
                        HD_orden, HD_comprobante, HD_nom_producto, HD_cantidad, HD_importe, HD_fecha,
                        HD_categoria_ope, HD_forma_ope, HD_cantidad_ope, HD_importe_ope, HD_fecha_ope, HD_estado},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void changeEstadoToExport(String[] idsInformeGasto, int estadoSincronizacion){
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants._SINCRONIZAR,estadoSincronizacion);

        String signosInterrogacion = "";
        for (int i=0; i<idsInformeGasto.length; i++){
            if (i==idsInformeGasto.length-1)
            {
                signosInterrogacion+= "?";
            }else {
                signosInterrogacion+= "? OR ";
            }

        }

        Log.d("SIGNOS INTERROGACIÓN", signosInterrogacion);
        int cantidadRegistros = mDb.update(SQLITE_TABLE_Histo_Venta_Detalle, initialValues,
                HD_id_hventadet+"= "+ signosInterrogacion,idsInformeGasto);


        Log.d("REGISTROS ACTUALIZADO ", ""+cantidadRegistros);
    }

    public Cursor fetchAllHistoVentaDetalle0() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Histo_Venta_Detalle, new String[] {HD_id_hventadet,
                        HD_id_detalle, HD_id_comprob, HD_id_establec, HD_id_producto, HD_id_tipoper,
                        HD_orden, HD_comprobante, HD_nom_producto, HD_cantidad, HD_importe, HD_fecha,
                        HD_categoria_ope, HD_forma_ope, HD_cantidad_ope, HD_importe_ope, HD_fecha_ope, HD_estado},
                HD_id_comprob + " = 0", null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllHistoVentaDetalleByIdEst(String id) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Histo_Venta_Detalle, new String[] {HD_id_hventadet,
                        HD_id_detalle, HD_id_comprob, HD_id_establec, HD_id_producto, HD_id_tipoper,
                        HD_orden, HD_comprobante, HD_nom_producto, HD_cantidad, HD_importe, HD_fecha,
                        HD_categoria_ope, HD_forma_ope, HD_cantidad_ope, HD_importe_ope, HD_fecha_ope, HD_estado},
                HD_id_establec + " = " + id, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllHistoVentaDetalleByIdEst1(String id, String est) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Histo_Venta_Detalle, new String[] {HD_id_hventadet,
                        HD_id_detalle, HD_id_comprob, HD_id_establec, HD_id_producto, HD_id_tipoper,
                        HD_orden, HD_comprobante, HD_nom_producto, HD_cantidad, HD_importe, HD_fecha,
                        HD_categoria_ope, HD_forma_ope, HD_cantidad_ope, HD_importe_ope, HD_fecha_ope, HD_estado},
                HD_id_establec + " = " + id + " AND " + HD_estado + " = " + est, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllHistoVentaDetalleByIdEst2(String id, String ord) {

        Cursor mCursor = mDb.query(SQLITE_TABLE_Histo_Venta_Detalle, new String[] {HD_id_hventadet,
                        HD_id_detalle, HD_id_comprob, HD_id_establec, HD_id_producto, HD_id_tipoper,
                        HD_orden, HD_comprobante, HD_nom_producto, HD_cantidad, HD_importe, HD_fecha,
                        HD_categoria_ope, HD_forma_ope, HD_cantidad_ope, HD_importe_ope, HD_fecha_ope, HD_estado},
                HD_id_establec + " = " + id + " AND " + HD_orden + " = " + ord, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchHistoVentaDetalleByNameEst(String id, String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_Histo_Venta_Detalle, new String[] {HD_id_hventadet,
                            HD_id_detalle, HD_id_comprob, HD_id_establec, HD_id_producto, HD_id_tipoper,
                            HD_orden, HD_comprobante, HD_nom_producto, HD_cantidad, HD_importe, HD_fecha,
                            HD_categoria_ope, HD_forma_ope, HD_cantidad_ope, HD_importe_ope, HD_fecha_ope, HD_estado},
                    HD_id_establec + " = " + id, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_Histo_Venta_Detalle, new String[] {HD_id_hventadet,
                            HD_id_detalle, HD_id_comprob, HD_id_establec, HD_id_producto, HD_id_tipoper,
                            HD_orden, HD_comprobante, HD_nom_producto, HD_cantidad, HD_importe, HD_fecha,
                            HD_categoria_ope, HD_forma_ope, HD_cantidad_ope, HD_importe_ope, HD_fecha_ope, HD_estado},
                    HD_id_establec + " = " + id + " AND " + HD_nom_producto + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchHistoVentaDetalleByNameEst1(String id, String inputText, String est) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_Histo_Venta_Detalle, new String[] {HD_id_hventadet,
                            HD_id_detalle, HD_id_comprob, HD_id_establec, HD_id_producto, HD_id_tipoper,
                            HD_orden, HD_comprobante, HD_nom_producto, HD_cantidad, HD_importe, HD_fecha,
                            HD_categoria_ope, HD_forma_ope, HD_cantidad_ope, HD_importe_ope, HD_fecha_ope, HD_estado},
                    HD_id_establec + " = " + id + " AND " + HD_estado + " = " + est, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_Histo_Venta_Detalle, new String[] {HD_id_hventadet,
                            HD_id_detalle, HD_id_comprob, HD_id_establec, HD_id_producto, HD_id_tipoper,
                            HD_orden, HD_comprobante, HD_nom_producto, HD_cantidad, HD_importe, HD_fecha,
                            HD_categoria_ope, HD_forma_ope, HD_cantidad_ope, HD_importe_ope, HD_fecha_ope, HD_estado},
                    HD_id_establec + " = " + id + " AND " + HD_nom_producto + " like '%" + inputText + "%'" + " AND " +HD_estado + " = " + est, null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }



}