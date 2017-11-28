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
import android.text.method.DateTimeKeyListener;
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
    String idCliente="";
    String NombreLocalImprimir="";


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
        idCliente=bundle.getString("NCliente");
        NombreLocalImprimir=bundle.getString("Nombre");
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
                    Float descParaverificar=Float.valueOf(0);
                    if(etDescuento.getText().toString().equals("")){
                        descParaverificar= Float.valueOf(0);
                    }else{
                        descParaverificar=Float.parseFloat(etDescuento.getText().toString());

                    }
                    cantidadparacompararUsuario=Float.parseFloat(etCantidad.getText().toString());
                    if(ExiteProducto(producto[0].replace(" ","").toString(),row)==true||cantidadparacomparar<cantidadparacompararUsuario||descParaverificar>100){
                        AlertDialog.Builder errorExiteProducto= new AlertDialog.Builder(FacturaDeVentaActivity.this);
                        errorExiteProducto.setMessage("No puede ingresar un producto que ya este, o vender mas de lo que esta en inventario, o ingresar un descuento mayor a 100")
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
                        descuentoPorcentaje=Float.valueOf(0);
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
                        if(DevuelveExento(producto[0].toString())==false){
                            Float impuestoventa=Float.valueOf(13)/100;
                           Float precioConiv=PrecioFinalProducto*impuestoventa;
                            PrecioFinalProducto+=precioConiv;
                        }else{

                        }
                        Totaldescuentos+=(precioProducto*cantidad)*descuento;
                        TotalPagar+=PrecioFinalProducto;
                        TotalAPAgarPorElCliente="¢"+TotalPagar;
                        MontoTotalAPagar.setText(TotalAPAgarPorElCliente);


                        final String []cadena={producto[0].replace(" ",""),producto[1],cantidad+"",descuentoPorcentaje+"%",""+PrecioFinalProducto};
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
                                                    Eliminaaproducto(cadena[0],row);
                                                    Float montoRebajar=Float.parseFloat(cadena[4].toString());
                                                    if(cadena[3].equals("0.0%")){

                                                    }else{
                                                        Float preciorealproducto=DevuelvePRecioProductoEspecifico(producto[0].replace(" ",""));
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
                                                alerterrorMEnosdinero.setTitle("Error");
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
                                                                        "Gracias por preferirnos\n\n\n\n";
                                                                Calendar c = Calendar.getInstance();
                                                                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                                String formattedDate ="Fecha-Hora: "+ df.format(c.getTime())+"\n";
                                                                String fechabd=df.format(c.getTime());

                                                                IngresarFacturaVenta(0,1,"",fechabd.replaceAll("/","-"),0);
                                                                String NFact=DevuelveUtilmoRegistro()+"";
                                                                String abajoEncabezado="Tipo Venta: Contado\n" +
                                                                        "Tipo Pago: " +tipoPago+"\n" +
                                                                        "N.Cli: "+NombreLocalImprimir+"\n"+
                                                                        "Factura # "+NFact+"-"+IngresarActivity.Usuario+"\n"+
                                                                        ""+formattedDate+"\n--------------------------------";
                                                                String imprimir= IngresarVenta(NFact);
                                                                String Encabezado
                                                                        = "\n\t        Distribuidora MyF\n"
                                                                        + "\t  Aramed de Jesus Sequeira Vega\n"
                                                                        + "\t\t      Cedula: 5-256-190\n"
                                                                        + "\t   Tels: 83031359 / 88197499\n"
                                                                        + "\t  Email: aramedsequeira@yahoo.es\n"
                                                                        + "\t\t\t       Nicoya, Guanacaste\n"
                                                                        + "--------------------------------\n";
                                                                MandarImprimir(Encabezado,abajoEncabezado,imprimir,Descuento);

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
                                                    String[] pago=tipoPago.split("-");
                                                    int pagoImp=Integer.parseInt(pago[0]);
                                                    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                                                    String fechabd=df.format(c.getTime());
                                                    IngresarFacturaVenta(0,pagoImp,referencia,fechabd.replaceAll("/","-"),0);
                                                    String NFac=DevuelveUtilmoRegistro()+"";
                                                    String Descuento="--------------------------------\nTotal a pagar: "+TotalPagar+"\n" +
                                                            "Numero referencia transaccion: \n"+referencia+"\n" +
                                                            "Gracias por preferirnos\n\n\n\n" ;

                                                    String formattedDate ="Fecha-Hora: "+ df.format(c.getTime())+"\n";
                                                    String abajoEncabezado="Tipo Venta: Contado\n" +
                                                            "Tipo Pago: " +tipoPago+"\n"+
                                                            "N.Cli: "+NombreLocalImprimir+"\n"+
                                                            "Factura # "+NFac+"-"+IngresarActivity.Usuario+"\n"+
                                                            ""+formattedDate+"\n--------------------------------";
                                                    String imprimir= IngresarVenta(NFac);
                                                    String Encabezado
                                                            = "\n\t        Distribuidora MyF\n"
                                                            + "\t  Aramed de Jesus Sequeira Vega\n"
                                                            + "\t\t      Cedula: 5-256-190\n"
                                                            + "\t   Tels: 83031359 / 88197499\n"
                                                            + "\t  Email: aramedsequeira@yahoo.es\n"
                                                            + "\t\t\t       Nicoya, Guanacaste\n"
                                                            + "--------------------------------\n";
                                                    MandarImprimir(Encabezado,abajoEncabezado,imprimir,Descuento);

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
                                                    String[] numerodias=dias.split("-");
                                                    int plazodias=Integer.parseInt(numerodias[0]);
                                                    Calendar c = Calendar.getInstance();
                                                    Calendar calendar = Calendar.getInstance();
                                                    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                    String formattedDate ="Fecha-Hora: "+ df.format(c.getTime())+"\n";
                                                    String fechabd=df.format(c.getTime());

                                                    calendar.setTime(c.getTime()); // Configuramos la fecha que se recibe
                                                    calendar.add(Calendar.DAY_OF_YEAR, plazodias);  // numero de días a añadir, o restar en caso de días<0
                                                    calendar.getTime();
                                                    String Descuento="--------------------------------\nTotal a pagar: "+TotalPagar+"\n" +
                                                            "Plazo dias para cancelar: \n"+dias+"\n" +
                                                            "Dia de pago maximo\n" +df.format(calendar.getTime())+"\n"+
                                                            "Gracias por preferirnos\n\n\n\n" ;

                                                    IngresarFacturaVenta(1,5,"",fechabd.replaceAll("/","-"),plazodias);
                                                    String NFact=DevuelveUtilmoRegistro()+"";
                                                    String abajoEncabezado="Tipo Venta: Credito\n" +
                                                            "N.Cli: "+NombreLocalImprimir+"\n" +
                                                            "Factura # "+NFact+"-"+IngresarActivity.Usuario+"\n"+
                                                            ""+formattedDate+"\n--------------------------------";
                                                    String Encabezado
                                                            = "\n\t        Distribuidora MyF\n"
                                                            + "\t  Aramed de Jesus Sequeira Vega\n"
                                                            + "\t\t      Cedula: 5-256-190\n"
                                                            + "\t   Tels: 83031359 / 88197499\n"
                                                            + "\t  Email: aramedsequeira@yahoo.es\n"
                                                            + "\t\t\t       Nicoya, Guanacaste\n"
                                                            + "--------------------------------\n";
                                                    String imprimir= IngresarVenta(NFact);

                                                    MandarImprimir(Encabezado,abajoEncabezado,imprimir,Descuento);


                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    return;
                                                }
                                            });
                                    AlertDialog alconfirmacioncredito=confirmacioncredito.create();
                                    alconfirmacioncredito.setTitle("Confirmación");
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

    Boolean DevuelveExento(String codigo){
        boolean Execto=false;
        SQLiteDatabase db=conn.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT "+AyudanteCreacionBD.CAMPO_EXCENTO+" FROM "+ AyudanteCreacionBD.TABLA_PRODUCTO+" WHERE "+
                AyudanteCreacionBD.CAMPO_CODIGO_PRODUCTO+" = '"+codigo+"'", null);
        while (cursor.moveToNext()){
            if(cursor.getString(0).equalsIgnoreCase("Null")){
                Execto=false;
            }else{
                Execto=true;
            }

        }
        return Execto;
    }


    public void MandarImprimir(final String Encabezado, final  String abajoEncabezado, final String imprimir, final String Descuento){
        try{
            findBT();
            openBT();
                sendData(Encabezado);
                sendData(abajoEncabezado);
                sendData(imprimir);
                sendData(Descuento);
            AlertDialog.Builder confirmaimprimir= new AlertDialog.Builder(FacturaDeVentaActivity.this);
            confirmaimprimir.setMessage("Recoja la copia cliente.")
                    .setCancelable(false)
                    .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                findBT();
                                openBT();
                                sendData(Encabezado);
                                sendData(abajoEncabezado);
                                sendData(imprimir);
                                sendData(Descuento);
                                Intent ListSong = new Intent(getApplicationContext(), ListarClientesActivity.class);
                                startActivity(ListSong);
                                finish();
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),"No se puede ir a la pantalla principal", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            AlertDialog alconfirmaimprimir=confirmaimprimir.create();
            alconfirmaimprimir.setTitle("Volver a imprimir");
            alconfirmaimprimir.show();
        }catch (Exception e){

        }


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
            if(CodigoProductoABuscar.equals(productoEspecifico[0].replace(" ",""))){
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
            if(CodigoProductoABuscar.equals(productoEspecifico[0].replace(" ",""))){
                Precio=Float.parseFloat(productoEspecifico[3]);
                break;
            }
        }
        return Precio;
    }

    private void obtenerListaDias() {
        listadias=new ArrayList<String>();
        listadias.add("Seleccione");
        listadias.add("7-dias");
        listadias.add("15-dias");
        listadias.add("30-dias");
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



    public String IngresarVenta(String NFactura) {
        SQLiteDatabase db=conn.getReadableDatabase();
        String Mostrar="\nCod   Tlt      Cant   Descr \n";
        String codigo="";
        String nombre="";
        String prueba="";
        String cantidad="";
        String precio="";
        String Desc="";
        String codigoSql="";
        String codigoDetalle="";
        String rowConten="";
        for (int i = 0; i < DetalleFacturaVenta.getChildCount(); i++) {
            TableRow row = (TableRow) DetalleFacturaVenta.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView currentCell = (TextView) row.getChildAt(j);

                if(j==0){
                    codigoSql=currentCell.getText().toString();
                    codigoDetalle=codigoSql;
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
                }else if(j==3){
                    Desc=currentCell.getText().toString().replaceAll("%","");
                }else if(j==4){
                    precio=currentCell.getText().toString();
                }
            }
            Mostrar+=codigo+"  "+precio+"  "+cantidad+"  "+nombre+"\n";
            Float ca=Float.parseFloat(cantidad);
            Float descu=Float.parseFloat(Desc);
            Float preci=Float.parseFloat(precio);
            Float precioReal=DevuelvePRecioProductoEspecifico(codigo);
            Float descuentomoneda=Float.valueOf(0);
            if(descu==0.0){
                 descuentomoneda=Float.valueOf(0);
            }else{
                Float descuentoReal=descu/100;
                descuentomoneda=(precioReal*ca)-((precioReal*ca)*descuentoReal);
            }
            preci=preci/ca;
            IngresarDetalleFacturaVenta(NFactura,codigoDetalle,ca,descuentomoneda,preci);
            ContentValues cv = new ContentValues();
            Float total=DevuelveExistenciaProductoEspecifico(codigoSql);
            Float cantidadVendidad=Float.parseFloat(cantidad);
            Float nuevoCantidad=total-cantidadVendidad;
            cv.put(AyudanteCreacionBD.CAMPO_EXISTENCIA,nuevoCantidad);
            db.update(AyudanteCreacionBD.TABLA_PRODUCTO, cv, AyudanteCreacionBD.CAMPO_CODIGO_PRODUCTO+"= '"+codigoSql+"'", null);

            precio="";nombre="";cantidad="";codigo="";Desc="";codigoDetalle="";

        }

        return Mostrar+"\n";
    }

    public int DevuelveUtilmoRegistro(){
        int NFactura=0;
        SQLiteDatabase db=conn.getReadableDatabase();
        String consulta="SELECT MAX("+AyudanteCreacionBD.CAMPO_NUMERO_FACTURA+") FROM "+AyudanteCreacionBD.TABLA_FACTURA_VENTA;
        Cursor cursor=db.rawQuery(consulta,null);
        while (cursor.moveToNext()){
            NFactura=cursor.getInt(0);
        }

        return NFactura;
    }

    public void IngresarFacturaVenta(int TipoVenta,int TipoPago, String referencia, String fecha, int Cantidaddias){
        SQLiteDatabase db=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        int idClien=Integer.parseInt(idCliente.replaceAll(" ",""));
        values.put(AyudanteCreacionBD.CAMPO_TIPO_VENTA_VENTA,TipoVenta);
        values.put(AyudanteCreacionBD.CAMPO_ID_USUARIO_VENTA,IngresarActivity.IdUsuario);
        values.put(AyudanteCreacionBD.CAMPO_ID_CLIENTE_VENTA,idClien);
        values.put(AyudanteCreacionBD.CAMPO_TIPO_PAGO_VENTA,TipoPago);
        values.put(AyudanteCreacionBD.CAMPO_N_REFERENCIA,referencia);
        values.put(AyudanteCreacionBD.CAMPO_MONTO_TOTAL,TotalPagar);
        values.put(AyudanteCreacionBD.CAMPO_FECHA,fecha);
        values.put(AyudanteCreacionBD.CAMPO_CANTIDAD_DIAS,Cantidaddias);
        Long idResultante=db.insert(AyudanteCreacionBD.TABLA_FACTURA_VENTA,null,values);
        Toast.makeText(getApplicationContext(),"Se ingreso la factura con exito", Toast.LENGTH_LONG).show();
    }
    public void IngresarDetalleFacturaVenta(String NFatura,String producto,Float Cantidad,Float descuento,Float precio){
        SQLiteDatabase db=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        int nFactura=Integer.parseInt(NFatura);
        values.put(AyudanteCreacionBD.CAMPO_NUMERO_FACTURA_DETALLE,nFactura);
        values.put(AyudanteCreacionBD.CAMPO_ID_PRODUCTO_DETALLE,producto);
        values.put(AyudanteCreacionBD.CAMPO_CANTIDAD,Cantidad);
        values.put(AyudanteCreacionBD.CAMPO_DESCUENTO,descuento);
        values.put(AyudanteCreacionBD.CAMPO_PRECIO_VENTA,precio);
        Long idResultante=db.insert(AyudanteCreacionBD.TABLA_DETALLE_FACTURA_VENTA,null,values);
        //Toast.makeText(getApplicationContext(),"Se ingreso la factura con exito", Toast.LENGTH_LONG).show();
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
