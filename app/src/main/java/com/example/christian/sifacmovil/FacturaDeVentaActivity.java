package com.example.christian.sifacmovil;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christian.sifacmovil.Modelos.Producto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import com.example.christian.sifacmovil.AyudanteCrecionBD.AyudanteCreacionBD;

public class FacturaDeVentaActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
    //bluettoth
    static BluetoothAdapter mBluetoothAdapter;
    static BluetoothSocket mmSocket;
    static BluetoothDevice mmDevice;
    public static OutputStream mmOutputStream;
    public static InputStream mmInputStream;
    public static Thread workerThread;

    public static byte[] readBuffer;
    public  static int readBufferPosition;
    public static volatile boolean stopWorker;
    //
    TextView text,view,MontoTotalAPagar;
    Button IngresarProducto,Vender;
    Spinner listaDeProductos,ListadeDias,TipoVenta,TipoPago;
    ArrayList<String> listaProductos;
    ArrayList<Producto> productosList;
    ArrayList<AyudanteDetalleFacturaVenta> AyudanteDetaleFactura;
    ArrayList<String> listadias;
    ArrayList<String> listatipopago;
    ArrayList<String> listatipoVenta;
    TableLayout DetalleFacturaVenta;
    TableRow row;
    Float cantidadparacomparar=Float.valueOf(0);
    Float cantidadparacompararUsuario=Float.valueOf(0);
    EditText etCantidad, etDescuento,TotalPagaCliente;
    String TotalAPAgarPorElCliente="";
    Float precioProducto= Float.valueOf(0);
    Float totalQuePagaCliente= Float.valueOf(0);
    Float descuento= Float.valueOf(0);
    Float cantidad= Float.valueOf(0);
    Float descuentoPorcentaje= Float.valueOf(0);
    Float TotalPagar= Float.valueOf(0);
    Float PrecioFinalProducto= Float.valueOf(0);
    String rowContent = "";
    int Contador=0;
    String dato="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura_de_venta);
        listaDeProductos=(Spinner)findViewById(R.id.spinnerListaProductos);
        ListadeDias=(Spinner)findViewById(R.id.spinnerPlazoDías);
        TipoVenta=(Spinner)findViewById(R.id.spinnerITpoVenta);
        TipoPago=(Spinner)findViewById(R.id.spinnerTipoPago);
        text=(TextView)findViewById(R.id.CodigoNombrecliente);
        IngresarProducto=(Button)findViewById(R.id.btAgregarProducto);
        Vender=(Button)findViewById(R.id.btRealizarVenta);
        MontoTotalAPagar=(TextView)findViewById(R.id.MontoTotalAPagar);
        etCantidad=(EditText)findViewById(R.id.etCantidad);
        etDescuento=(EditText)findViewById(R.id.etDescuento);
        TotalPagaCliente=(EditText)findViewById(R.id.etMontodePagoTotalCliente);
        DetalleFacturaVenta=(TableLayout)findViewById(R.id.DetalleFacturaVenta);
        DetalleFacturaVenta.setClickable(true);
        Bundle bundle=getIntent().getExtras();
        dato="";
        text.setText(bundle.getString("NCliente")+" - Nombre Cliente: "+bundle.getString("Nombre"));



        consultarListaProductos();
        obtenerListaDias();
        obtenerListaTipoPago();
        obtenerListaTipoVenta();
        AyudanteDetaleFactura= new ArrayList<AyudanteDetalleFacturaVenta>();
        ArrayAdapter<CharSequence> adaptadorProductos=new ArrayAdapter(this,android.R.layout.simple_spinner_item,listaProductos);
        listaDeProductos.setAdapter(adaptadorProductos);
        ArrayAdapter<CharSequence> adaptadorDias=new ArrayAdapter(this,android.R.layout.simple_spinner_item,listadias);
        ListadeDias.setAdapter(adaptadorDias);
        ArrayAdapter<CharSequence> adaptadorTipoPago=new ArrayAdapter(this,android.R.layout.simple_spinner_item,listatipopago);
        TipoPago.setAdapter(adaptadorTipoPago);
        ArrayAdapter<CharSequence> adaptadorTipoVEnta=new ArrayAdapter(this,android.R.layout.simple_spinner_item,listatipoVenta);
        TipoVenta.setAdapter(adaptadorTipoVEnta);


        int Cantidad=0;

        IngresarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etCantidad.getText().toString().equals("")||listaDeProductos.getItemAtPosition(listaDeProductos.getSelectedItemPosition()).toString().equals("Seleccione")){
                    AlertDialog.Builder error= new AlertDialog.Builder(FacturaDeVentaActivity.this);
                    error.setMessage("Por favor primero seleccione un producto y una canidad")
                            .setCancelable(false)
                            .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            });
                    AlertDialog alert9=error.create();
                    alert9.setTitle("Error");
                    alert9.show();

                }else{
                    final String[] producto=listaDeProductos.getItemAtPosition(listaDeProductos.getSelectedItemPosition()).toString().split("-");
                    cantidadparacomparar=Float.parseFloat(producto[2]);
                    cantidadparacompararUsuario=Float.parseFloat(etCantidad.getText().toString());
                    if(ExiteProducto(producto[0].toString(),row)==true||cantidadparacomparar<cantidadparacompararUsuario){
                        AlertDialog.Builder errorExiteProducto= new AlertDialog.Builder(FacturaDeVentaActivity.this);
                        errorExiteProducto.setMessage("No puede ingresar un producto que ya este, o vender mas de lo que esta en inventario")
                                .setCancelable(false)
                                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                });
                        AlertDialog alert2=errorExiteProducto.create();
                        alert2.setTitle("Error");
                        alert2.show();
                    }else{
                        String precio=producto[2].toString();
                        precioProducto =Float.parseFloat(precio);
                        if(etDescuento.getText().toString().equals("")){
                            descuento= Float.valueOf(0);
                        }else{
                            descuentoPorcentaje=Float.parseFloat(etDescuento.getText().toString());
                            descuento=descuentoPorcentaje/100;
                        }
                        cantidad = Float.parseFloat(etCantidad.getText().toString());
                        PrecioFinalProducto=(precioProducto*cantidad)-(precioProducto*descuento);
                        TotalPagar+=PrecioFinalProducto;
                        TotalAPAgarPorElCliente="¢"+TotalPagar;
                        MontoTotalAPagar.setText(TotalAPAgarPorElCliente);
                        //AyudanteDetalleFacturaVenta ayudanteObjeto= new AyudanteDetalleFacturaVenta(producto[0],producto[1],cantidad,descuentoPorcentaje,PrecioFinalProducto);
                        //AyudanteDetaleFactura.add(ayudanteObjeto);
                       // dato+=producto[0]+"-"+producto[1]+cantidad+"-"+descuentoPorcentaje+"-"+PrecioFinalProducto+"-";
                        //Contador++;
                        final String []cadena={producto[0],producto[1],cantidad+"",descuentoPorcentaje+"%",""+PrecioFinalProducto};
                        row=new TableRow(getBaseContext());

                        for(int posicion=0;posicion<5;posicion++){

                            view=new TextView(getBaseContext());
                            view.setGravity(Gravity.CENTER_VERTICAL);
                            view.setPadding(10,10,10,10);
                            view.setText(cadena[posicion]);

                            view.setBackgroundResource(R.color.colorTabla);
                            view.setTextColor(Color.WHITE);
                            row.addView(view);

                            row.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {

                                    AlertDialog.Builder confirmacion= new AlertDialog.Builder(FacturaDeVentaActivity.this);
                                    confirmacion.setMessage("Realmente quiere eliminar este producto: "+producto[1])
                                            .setCancelable(false)
                                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Eliminaaproducto(producto[0],row);

                                                    if(SeEliminoArray(producto[0])==true){
                                                        Toast.makeText(getApplicationContext(),"Se elimino con exito: ", Toast.LENGTH_LONG).show();
                                                    }
                                                    Float montoRebajar=Float.parseFloat(cadena[4].toString());
                                                    TotalPagar=TotalPagar-montoRebajar;
                                                    TotalAPAgarPorElCliente="¢"+TotalPagar;
                                                    MontoTotalAPagar.setText(TotalAPAgarPorElCliente);
                                                }
                                            }).setNegativeButton("No",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    });


                                    AlertDialog alert=confirmacion.create();
                                    alert.setTitle("Elimiar producto");
                                    alert.show();
                                }
                            });

                        }
                        DetalleFacturaVenta.addView(row);
                        etCantidad.setText("");
                        etDescuento.setText("");
                    }
                }
            }
        });

        Vender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DetalleFacturaVenta.getChildCount()==0||TotalPagaCliente.getText().toString().equals("")||TipoPago.getItemAtPosition(TipoPago.getSelectedItemPosition()).toString().equals("Seleccione")){

                    return;
                }else{//inicio if si los datos estan ingresados
                    totalQuePagaCliente=Float.parseFloat(TotalPagaCliente.getText().toString());
                    if(totalQuePagaCliente<TotalPagar){//inicio if si lo que el cliente dio es menor a lo que tiene que pagar

                        return;
                    }else{//inicia el recorrido para ingresar la factura

                        printTable(view);

                    }
                }


            }

        });
    }

    private void consultarListaProductos() {
        SQLiteDatabase db=conn.getReadableDatabase();

        Producto producto=null;
        productosList =new ArrayList<Producto>();
        ///select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM "+ AyudanteCreacionBD.TABLA_PRODUCTO,null);

        while (cursor.moveToNext()){
            producto=new Producto();
            //Producto.set(cursor.getInt(0));
            producto.setCodigo(cursor.getString(0));
            producto.setNombre(cursor.getString(1));
            producto.setExistencias(cursor.getFloat(2));
            producto.setPrecio(cursor.getFloat(3));
            producto.setExento(cursor.getString(4));

            Log.i("id",producto.getCodigo().toString());
            Log.i("Nombre",producto.getNombre().toString());
            Log.i("Existencias", String.valueOf(producto.getExistencias()));
            Log.i("Precio",String.valueOf(producto.getPrecio()));
            Log.i("Exento",producto.getExento().toString());


            productosList.add(producto);

        }
        obtenerListaProductos();
    }

    public void MandarImprimir(String Encabezado,String Texto){




    }

    private void obtenerListaProductos() {
        listaProductos=new ArrayList<String>();
        listaProductos.add("Seleccione");

        for(int i=0;i<productosList.size();i++){
            listaProductos.add(productosList.get(i).getCodigo()+" - "+productosList.get(i).getNombre()+" - "+productosList.get(i).getExistencias());
        }

    }
    public float DevuelveExistenciaProductoEspecifico(String CodigoProductoABuscar){
        Float Existencias=Float.valueOf(0);
        for(int ContadorRecorreListaproductos=0;ContadorRecorreListaproductos<listaProductos.size();ContadorRecorreListaproductos++){
            String[] productoEspecifico=listaProductos.get(ContadorRecorreListaproductos).split("-");
            if(CodigoProductoABuscar.equals(productoEspecifico[0])){
                Existencias=Float.parseFloat(productoEspecifico[2]);
                break;
            }
        }
        return Existencias;
    }

    private void obtenerListaDias() {
        listadias=new ArrayList<String>();
        listadias.add("Seleccione");
        listadias.add("5-días");
        listadias.add("10-días");
        listadias.add("15-días");
    }

    private void obtenerListaTipoPago() {
        listatipopago=new ArrayList<String>();
        listatipopago.add("Seleccione");
        listatipopago.add("0-Contado");
        listatipopago.add("1-Credito");

    }

    private void obtenerListaTipoVenta() {
        listatipoVenta=new ArrayList<String>();
        listatipoVenta.add("Seleccione");
        listatipoVenta.add("1-Efectivo");
        listatipoVenta.add("2-Cheque");
        listatipoVenta.add("3-Transaccion");
        listatipoVenta.add("4-TarjetaCredito");
    }

    public boolean SeEliminoArray(String Codigo){
        Boolean Exito=false;

        for (int i=0; i<AyudanteDetaleFactura.size();i++){
            if(AyudanteDetaleFactura.get(i).getCodigo().equals(Codigo)){
                Toast.makeText(getApplicationContext(),AyudanteDetaleFactura.get(i).getCodigo(), Toast.LENGTH_LONG).show();
                AyudanteDetaleFactura.remove(i);
                Exito=true;
            }
        }

        return Exito;
    }

    public void MostrarArray(){
        String datos="";
        for (int i=0; i<AyudanteDetaleFactura.size();i++){
            datos+=AyudanteDetaleFactura.get(i).getCodigo()+AyudanteDetaleFactura.get(i).getTotalPagar();
        }
        Toast.makeText(getApplicationContext(),datos, Toast.LENGTH_LONG).show();
    }
    public String printTable(View v) {
        //String[] datosaImprimir = new String[DetalleFacturaVenta.getChildCount()];
        //ArrayList
        String Mostrar="";
        String rowConten="";
        for (int i = 0; i < DetalleFacturaVenta.getChildCount(); i++) {
            TableRow row = (TableRow) DetalleFacturaVenta.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView currentCell = (TextView) row.getChildAt(j);
                rowConten += currentCell.getText() + "-";
            }
            Mostrar+=rowConten+"\n";
            rowConten="";
        }
        Toast.makeText(getApplicationContext(),Mostrar, Toast.LENGTH_LONG).show();
        return rowConten;
    }

    public void Eliminaaproducto(String CodigoProdcutoEliminar,TableRow row) {

        //This method is to get the contents of the table once it is filled

        String codigoProducto = "";
        for (int i = 0; i < DetalleFacturaVenta.getChildCount(); i++) {
            row = (TableRow) DetalleFacturaVenta.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView currentCell = (TextView) row.getChildAt(j);
                codigoProducto = currentCell.getText().toString();
                if(CodigoProdcutoEliminar.equals(codigoProducto)){
                    DetalleFacturaVenta.removeView(row);

                    break;
                }
            }

        }
    }

    public boolean ExiteProducto(String CodigoProdcutoEliminar,TableRow row) {

        //This method is to get the contents of the table once it is filled
        boolean Existe=false;
        String codigoProducto = "";
        for (int i = 0; i < DetalleFacturaVenta.getChildCount(); i++) {
            row = (TableRow) DetalleFacturaVenta.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView currentCell = (TextView) row.getChildAt(j);
                codigoProducto = currentCell.getText().toString();
                if(CodigoProdcutoEliminar.equals(codigoProducto)){
                    Existe=true;
                    break;
                }
            }

        }
        return Existe;
    }

    public boolean IngresarVenta(TableRow row) {

        //This method is to get the contents of the table once it is filled
        boolean Exito=false;
        String productoALaVenta = "";
        for (int i = 0; i < DetalleFacturaVenta.getChildCount(); i++) {
            row = (TableRow) DetalleFacturaVenta.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView currentCell = (TextView) row.getChildAt(j);
                productoALaVenta += currentCell.getText().toString()+"-";

            }
            String[] productoParaVender=productoALaVenta.split("-");
            productoALaVenta="";


        }


        return Exito;
    }


    void findBT() {
        // Toast.makeText(null,"se metio fiinbt", Toast.LENGTH_LONG).show();
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                // myLabel.setText("No bluetooth adapter available");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("BlueTooth Printer")) {
                        mmDevice = device;
                        break;
                    }
                }
            }



        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void openBT() throws IOException {
        try {
            //Toast.makeText(null,"se metio openbt", Toast.LENGTH_LONG).show();
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 100;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[102400];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void sendData(String data) throws IOException {
        try {

            // the text typed by the user
            String msg = data;
            msg += "\n";

            mmOutputStream.write(msg.getBytes());

            // tell the user data were sent
            //myLabel.setText("Data sent.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            //  myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
