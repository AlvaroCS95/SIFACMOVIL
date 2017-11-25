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
import android.support.design.widget.FloatingActionButton;
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
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    TextView text,view,MontoTotalAPagar,CodigoNombrecliente;
    //EditText etCantidad, etDescuent,TotalPagaCliente,Referencia;
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
    EditText etCantidad, etDescuento,TotalPagaCliente,etreferencia;
    String TotalAPAgarPorElCliente="";
    Float precioProducto= Float.valueOf(0);
    Float totalQuePagaCliente= Float.valueOf(0);
    Float descuento= Float.valueOf(0);
    Float Totaldescuentos= Float.valueOf(0);
    Float cantidad= Float.valueOf(0);
    Float descuentoPorcentaje= Float.valueOf(0);
    Float TotalPagar= Float.valueOf(0);
    Float TotalPagarSinDescuento= Float.valueOf(0);
    Float PrecioFinalProducto= Float.valueOf(0);
    String rowContent = "";
    int Contador=0;
    String dato="";
    String NReferencia="";
    String DatosParaImprimir="";
    String TipoVentaParaimprimir="";


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
        CodigoNombrecliente=(TextView)findViewById(R.id.CodigoNombrecliente);
        etCantidad=(EditText)findViewById(R.id.etCantidad);
        etreferencia=(EditText)findViewById(R.id.etNReferencia);
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
                        String precio=producto[3].toString();
                        precioProducto =Float.parseFloat(precio);
                        if(etDescuento.getText().toString().equals("")){
                            descuento= Float.valueOf(0);
                        }else{
                            descuentoPorcentaje=Float.parseFloat(etDescuento.getText().toString());
                            descuento=descuentoPorcentaje/100;
                        }
                        cantidad = Float.parseFloat(etCantidad.getText().toString());
                        PrecioFinalProducto=(precioProducto*cantidad)-((precioProducto*cantidad)*descuento);
                        Totaldescuentos+=(precioProducto*cantidad)*descuento;
                        TotalPagar+=PrecioFinalProducto;
                        TotalAPAgarPorElCliente="¢"+TotalPagar;
                        MontoTotalAPagar.setText(TotalAPAgarPorElCliente);

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
                                                    Float montoRebajar=Float.parseFloat(cadena[4].toString());
                                                    if(cadena[3].equals("0.0%")){

                                                    }else{
                                                        Float preciorealproducto=DevuelvePRecioProductoEspecifico(producto[0]);
                                                        String[] de=cadena[3].split("%");
                                                        Float porc=Float.parseFloat(de[0]);
                                                        Float cant=Float.parseFloat(cadena[2]);
                                                        Float precioReal=preciorealproducto*cant;
                                                        Float desc=porc/100;
                                                        Totaldescuentos-=((precioReal*cant)*desc);
                                                    }

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
                if(DetalleFacturaVenta.getChildCount()==0||TipoVenta.getItemAtPosition(TipoVenta.getSelectedItemPosition()).toString().equals("Seleccione")){
                    AlertDialog.Builder errorNotienetodo= new AlertDialog.Builder(FacturaDeVentaActivity.this);
                    errorNotienetodo.setMessage("Debe ingresar tipo venta o un producto para vender")
                            .setCancelable(false)
                            .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            });
                    AlertDialog alert10=errorNotienetodo.create();
                    alert10.setTitle("Error");
                    alert10.show();
                    return;
                }else{
                        //tipoventa 0-Contado
                    if(TipoVenta.getItemAtPosition(TipoVenta.getSelectedItemPosition()).toString().equals("0-Contado")){
                        if(TipoPago.getItemAtPosition(TipoPago.getSelectedItemPosition()).toString().equals("Seleccione")){
                            AlertDialog.Builder errorNoTipoPago= new AlertDialog.Builder(FacturaDeVentaActivity.this);
                            errorNoTipoPago.setMessage("Primero debe selecionar un tipo de pago")
                                    .setCancelable(false)
                                    .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    });
                            AlertDialog alerterrorNoTipoPago=errorNoTipoPago.create();
                            alerterrorNoTipoPago.setTitle("Error");
                            alerterrorNoTipoPago.show();
                            return;
                        }else{
                            if(TipoPago.getItemAtPosition(TipoPago.getSelectedItemPosition()).toString().equals("1-Efectivo")){
                                        if(TotalPagaCliente.getText().toString().equals("")){//si no ingreso nada que el cliente paga
                                            AlertDialog.Builder errorNoMontoCliente= new AlertDialog.Builder(FacturaDeVentaActivity.this);
                                            errorNoMontoCliente.setMessage("ingrese un monto con el que el cliente va a pagar")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            return;
                                                        }
                                                    });
                                            AlertDialog alerterrorerrorNoMontoClienteo=errorNoMontoCliente.create();
                                            alerterrorerrorNoMontoClienteo.setTitle("Error");
                                            alerterrorerrorNoMontoClienteo.show();
                                            return;
                                        }else{// si ingreso algo
                                            totalQuePagaCliente=Float.parseFloat(TotalPagaCliente.getText().toString());
                                            if(totalQuePagaCliente<TotalPagar){//inicio if si lo que el cliente dio es menor a lo que tiene que pagar
                                                AlertDialog.Builder errorMEnosdinero= new AlertDialog.Builder(FacturaDeVentaActivity.this);
                                                errorMEnosdinero.setMessage("Esta ingresando una cantidaad menor a la que el clinete debe")
                                                        .setCancelable(false)
                                                        .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                return;
                                                            }
                                                        });
                                                AlertDialog alerterrorMEnosdinero=errorMEnosdinero.create();
                                                alerterrorMEnosdinero.setTitle("Confirmación de venta");
                                                alerterrorMEnosdinero.show();
                                                return;
                                            }else{//inicia el recorrido para ingresar la factura
                                                AlertDialog.Builder confirmacio= new AlertDialog.Builder(FacturaDeVentaActivity.this);
                                                confirmacio.setMessage("Esta Seguro que quiere ingresar esta venta")
                                                        .setCancelable(false)
                                                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Float TotalSinDescuento=TotalPagar+Totaldescuentos;
                                                                String tipoPago=TipoPago.getItemAtPosition(TipoPago.getSelectedItemPosition()).toString();
                                                                Float vuelto=totalQuePagaCliente-TotalPagar;
                                                                String Descuento="--------------------------------\nTotal a pagar: "+TotalPagar+"\n" +
                                                                        "Descuento aplicado: " +Totaldescuentos+"\n" +
                                                                        "Total sin descuento: "+TotalSinDescuento+"\n" +
                                                                        "Cliente paga con: "+totalQuePagaCliente+"\n" +
                                                                        "Su vuelto:"+vuelto+"\n" +
                                                                        "Gracias por preferirnos";
                                                                Calendar c = Calendar.getInstance();
                                                                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                                String formattedDate ="Fecha-Hora: "+ df.format(c.getTime())+"\n";
                                                                String imprimir= IngresarVenta();
                                                                String abajoEncabezado="Tipo Venta: Contado\n" +
                                                                        "Tipo Pago: " +tipoPago+"\n"+
                                                                        ""+formattedDate+"\n--------------------------------";

                                                                String Encabezado
                                                                        = "\n\t        Distribuidora MyF\n"
                                                                        + "\t  Aramed de Jesus Sequeira Vega\n"
                                                                        + "\t\t      Cedula: 5-256-190\n"
                                                                        + "\t   Tels: 83031359 / 88197499\n"
                                                                        + "\t  Email: aramedsequeira@yahoo.es\n"
                                                                        + "\t\t\t       Nicoya, Guanacaste\n"
                                                                        + "--------------------------------\n";
                                                                try{
                                                                    findBT();
                                                                    openBT();
                                                                    sendData(Encabezado);
                                                                    sendData(abajoEncabezado);
                                                                    sendData(imprimir);
                                                                    sendData(Descuento);
                                                                }catch (Exception e){

                                                                }
                                                                try {
                                                                    Intent ListSong = new Intent(getApplicationContext(), ListarClientesActivity.class);
                                                                    startActivity(ListSong);
                                                                    finish();
                                                                }catch (Exception e){
                                                                    Toast.makeText(getApplicationContext(),"No se puede ir a la pantalla principal", Toast.LENGTH_LONG).show();
                                                                }

                                                            }
                                                        })
                                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                return;
                                                            }
                                                        });
                                                AlertDialog alertconfirmacio=confirmacio.create();
                                                alertconfirmacio.setTitle("Confirmación");
                                                alertconfirmacio.show();

                                            }
                                        }
                            }else{//inicio otro tipo pago
                                if(etreferencia.getText().equals("")){
                                    AlertDialog.Builder errorNoReferencia= new AlertDialog.Builder(FacturaDeVentaActivity.this);
                                    errorNoReferencia.setMessage("Debe asignar un número de referencia")
                                            .setCancelable(false)
                                            .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    return;
                                                }
                                            });
                                    AlertDialog alerterrorNoReferencia=errorNoReferencia.create();
                                    alerterrorNoReferencia.setTitle("Error");
                                    alerterrorNoReferencia.show();
                                    return;
                                }else{// si tiene todos los datos para ingresar una venta contado con referencia
                                    AlertDialog.Builder confirmaciContadoTipoPAgoNReferencia= new AlertDialog.Builder(FacturaDeVentaActivity.this);
                                    confirmaciContadoTipoPAgoNReferencia.setMessage("Esta Seguro que quiere ingresar esta venta")
                                            .setCancelable(false)
                                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String tipoPago=TipoPago.getItemAtPosition(TipoPago.getSelectedItemPosition()).toString();
                                                    String referencia=etreferencia.getText().toString();
                                                    Calendar c = Calendar.getInstance();
                                                    String Descuento="--------------------------------\nTotal a pagar: "+TotalPagar+"\n" +
                                                            "Numero referencia transaccion: \n"+referencia+"\n" +
                                                            "Gracias por preferirnos" ;
                                                    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                    String formattedDate ="Fecha-Hora: "+ df.format(c.getTime())+"\n";
                                                    String abajoEncabezado="Tipo Venta: Contado\n" +
                                                            "Tipo Pago: " +tipoPago+"\n"+
                                                            ""+formattedDate+"\n--------------------------------";
                                                    String Encabezado
                                                            = "\n\t        Distribuidora MyF\n"
                                                            + "\t  Aramed de Jesus Sequeira Vega\n"
                                                            + "\t\t      Cedula: 5-256-190\n"
                                                            + "\t   Tels: 83031359 / 88197499\n"
                                                            + "\t  Email: aramedsequeira@yahoo.es\n"
                                                            + "\t\t\t       Nicoya, Guanacaste\n"
                                                            + "--------------------------------\n";
                                                    String imprimir= IngresarVenta();
                                                    try{
                                                        findBT();
                                                        openBT();
                                                        sendData(Encabezado);
                                                        sendData(abajoEncabezado);
                                                        sendData(imprimir);
                                                        sendData(Descuento);

                                                    }catch (Exception e){

                                                    }
                                                    try {
                                                        Intent ListSong = new Intent(getApplicationContext(), ListarClientesActivity.class);
                                                        startActivity(ListSong);
                                                        finish();
                                                    }catch (Exception e){
                                                        Toast.makeText(getApplicationContext(),"No se puede ir a la lista de clientes", Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    return;
                                                }
                                            });
                                    AlertDialog alconfirmaciContadoTipoPAgoNReferencia=confirmaciContadoTipoPAgoNReferencia.create();
                                    alconfirmaciContadoTipoPAgoNReferencia.setTitle("Confirmación");
                                    alconfirmaciContadoTipoPAgoNReferencia.show();
                                }//fin datos otro tipo de pago contado

                            }

                        }

                    } else if(TipoVenta.getItemAtPosition(TipoVenta.getSelectedItemPosition()).toString().equals("1-Credito")){
                                if(ListadeDias.getItemAtPosition(ListadeDias.getSelectedItemPosition()).toString().equals("Seleccione")){
                                    AlertDialog.Builder errorNoNumeroDias= new AlertDialog.Builder(FacturaDeVentaActivity.this);
                                    errorNoNumeroDias.setMessage("Primero debe asignar un plazo de días")
                                            .setCancelable(false)
                                            .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    return;
                                                }
                                            });
                                    AlertDialog alerterrorerrorNoNumeroDias=errorNoNumeroDias.create();
                                    alerterrorerrorNoNumeroDias.setTitle("Error");
                                    alerterrorerrorNoNumeroDias.show();
                                    return;
                                } else {// si tiene días asignados
                                    AlertDialog.Builder confirmacioncredito= new AlertDialog.Builder(FacturaDeVentaActivity.this);
                                    confirmacioncredito.setMessage("Esta Seguro que quiere ingresar esta venta")
                                            .setCancelable(false)
                                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    String dias=ListadeDias.getItemAtPosition(ListadeDias.getSelectedItemPosition()).toString();
                                                    Calendar c = Calendar.getInstance();
                                                    String Descuento="--------------------------------\nTotal a pagar: "+TotalPagar+"\n" +
                                                            "Plazo dias para cancelar: \n"+dias+"\n" +
                                                            "Gracias por preferirnos" ;
                                                    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                    String formattedDate ="Fecha-Hora: "+ df.format(c.getTime())+"\n";
                                                    String abajoEncabezado="Tipo Venta: Credito\n" +
                                                            ""+formattedDate+"\n--------------------------------";
                                                    String Encabezado
                                                            = "\n\t        Distribuidora MyF\n"
                                                            + "\t  Aramed de Jesus Sequeira Vega\n"
                                                            + "\t\t      Cedula: 5-256-190\n"
                                                            + "\t   Tels: 83031359 / 88197499\n"
                                                            + "\t  Email: aramedsequeira@yahoo.es\n"
                                                            + "\t\t\t       Nicoya, Guanacaste\n"
                                                            + "--------------------------------\n";
                                                    String imprimir= IngresarVenta();
                                                    try{
                                                        findBT();
                                                        openBT();
                                                        sendData(Encabezado);
                                                        sendData(abajoEncabezado);
                                                        sendData(imprimir);
                                                        sendData(Descuento);

                                                    }catch (Exception e){

                                                    }
                                                    try {
                                                        Intent ListSong = new Intent(getApplicationContext(), ListarClientesActivity.class);
                                                        startActivity(ListSong);
                                                        finish();
                                                    }catch (Exception e){
                                                        Toast.makeText(getApplicationContext(),"No se puede ir a la lista de clientes", Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    return;
                                                }
                                            });
                                    AlertDialog alconfirmacioncredito=confirmacioncredito.create();
                                    alconfirmacioncredito.setTitle("Confirmació");
                                    alconfirmacioncredito.show();

                                }
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


    private void obtenerListaProductos() {
        listaProductos=new ArrayList<String>();
        listaProductos.add("Seleccione");

        for(int i=0;i<productosList.size();i++){
            listaProductos.add(productosList.get(i).getCodigo()+" - "+productosList.get(i).getNombre()+" - "+productosList.get(i).getExistencias()+" - "+productosList.get(i).getPrecio());
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
    public float DevuelvePRecioProductoEspecifico(String CodigoProductoABuscar){
        Float Precio=Float.valueOf(0);
        for(int ContadorRecorreListaproductos=0;ContadorRecorreListaproductos<listaProductos.size();ContadorRecorreListaproductos++){
            String[] productoEspecifico=listaProductos.get(ContadorRecorreListaproductos).split("-");
            if(CodigoProductoABuscar.equals(productoEspecifico[0])){
                Precio=Float.parseFloat(productoEspecifico[3]);
                break;
            }
        }
        return Precio;
    }

    private void obtenerListaDias() {
        listadias=new ArrayList<String>();
        listadias.add("Seleccione");
        listadias.add("5-dias");
        listadias.add("10-dias");
        listadias.add("15-dias");
    }

    private void obtenerListaTipoPago() {
        listatipopago=new ArrayList<String>();
        listatipopago.add("Seleccione");
        listatipopago.add("1-Efectivo");
        listatipopago.add("2-Cheque");
        listatipopago.add("3-Transaccion");
        listatipopago.add("4-TarjetaCredito");

    }

    private void obtenerListaTipoVenta() {
        listatipoVenta=new ArrayList<String>();
        listatipoVenta.add("Seleccione");
        listatipoVenta.add("0-Contado");
        listatipoVenta.add("1-Credito");
    }



    public String IngresarVenta() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String Mostrar="\nCod   Tlt       Cant   Descr \n";
        String codigo="";
        String nombre="";
        String prueba="";
        String cantidad="";
        String precio="";
        String codigoSql="";
        String rowConten="";
        for (int i = 0; i < DetalleFacturaVenta.getChildCount(); i++) {
            TableRow row = (TableRow) DetalleFacturaVenta.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView currentCell = (TextView) row.getChildAt(j);

                if(j==0){
                    codigoSql=currentCell.getText().toString();
                    codigo=String.format("%1$-4s",codigoSql);
                }else if(j==1){
                   if(currentCell.getText().toString().length()>10){
                        prueba=currentCell.getText().toString().replaceAll("\\s","");
                        nombre=prueba.substring(0,10);
                    }else {
                            prueba=currentCell.getText().toString().replaceAll("\\s","");
                            nombre=String.format("%1$-10s",prueba);
                    }
                }else if(j==2){
                    cantidad=currentCell.getText().toString();
                }else if(j==4){
                    precio=currentCell.getText().toString();
                }
            }
            Mostrar+=codigo+"  "+precio+"   "+cantidad+"  "+nombre+"\n";
            ContentValues cv = new ContentValues();
            Float total=DevuelveExistenciaProductoEspecifico(codigoSql);
            Float cantidadVendidad=Float.parseFloat(cantidad);
            Float nuevoCantidad=total-cantidadVendidad;
            cv.put(AyudanteCreacionBD.CAMPO_EXISTENCIA,nuevoCantidad);
            db.update(AyudanteCreacionBD.TABLA_PRODUCTO, cv, AyudanteCreacionBD.CAMPO_CODIGO_PRODUCTO+"="+codigoSql, null);
            precio="";nombre="";cantidad="";codigo="";

        }

        return Mostrar+"\n";
    }

    public void IngresarFacturaVenta(){
        SQLiteDatabase db=conn.getWritableDatabase();

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
