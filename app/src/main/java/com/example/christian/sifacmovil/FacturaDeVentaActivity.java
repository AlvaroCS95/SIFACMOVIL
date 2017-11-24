package com.example.christian.sifacmovil;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christian.sifacmovil.Modelos.Producto;

import java.util.ArrayList;

import com.example.christian.sifacmovil.AyudanteCrecionBD.AyudanteCreacionBD;

public class FacturaDeVentaActivity extends AppCompatActivity {
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
    TextView text,view,MontoTotalAPagar;
    Button IngresarProducto,Vender;
    Spinner listaDeProductos,ListadeDias,TipoVenta,TipoPago;
    ArrayList<String> listaProductos;
    ArrayList<Producto> productosList;
    ArrayList<String> listadias;
    ArrayList<String> listatipopago;
    ArrayList<String> listatipoVenta;
    TableLayout DetalleFacturaVenta;
    TableRow row;
    EditText etCantidad, etDescuento,etMontoPago,TotalPagaCliente;
    String TotalAPAgarPorElCliente="";
    Float precioProducto= Float.valueOf(0);
    Float totalQuePagaCliente= Float.valueOf(0);
    Float descuento= Float.valueOf(0);
    Float cantidad= Float.valueOf(0);
    Float descuentoPorcentaje= Float.valueOf(0);
    Float TotalPagar= Float.valueOf(0);
    Float PrecioFinalProducto= Float.valueOf(0);


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
        etMontoPago=(EditText)findViewById(R.id.etMontodePagoTotal);
        TotalPagaCliente=(EditText)findViewById(R.id.etMontodePagoTotalCliente);
        DetalleFacturaVenta=(TableLayout)findViewById(R.id.DetalleFacturaVenta);
        DetalleFacturaVenta.setClickable(true);
        DialogoConfirmacion mensaje;
        final String CodigoProducto = "";

        Bundle bundle=getIntent().getExtras();
        text.setText(bundle.getString("NCliente")+" - Nombre Cliente: "+bundle.getString("Nombre"));

        consultarListaProductos();
        obtenerListaDias();
        obtenerListaTipoPago();
        obtenerListaTipoVenta();
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
                   if(ExiteProducto(producto[0].toString(),row)==true){
                       AlertDialog.Builder errorExiteProducto= new AlertDialog.Builder(FacturaDeVentaActivity.this);
                       errorExiteProducto.setMessage("Ya Existe este Producto por favor ingrese otro, o eliminelo y vuelvalo a agregar")
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
                       final String []cadena={producto[0],producto[1],cantidad+"",descuentoPorcentaje+"%",""+PrecioFinalProducto};
                       row=new TableRow(getBaseContext());

                       for(int posicion=0;posicion<5;posicion++){

                           view=new TextView(getBaseContext());
                           view.setGravity(Gravity.CENTER_VERTICAL);
                           view.setPadding(15,15,15,15);
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
                if(DetalleFacturaVenta.getChildCount()==0||TotalPagaCliente.getText().toString().equals("")){
                    AlertDialog.Builder errorNollenotodo= new AlertDialog.Builder(FacturaDeVentaActivity.this);
                    errorNollenotodo.setMessage("Por favor ingrese productos y una suma con la cual el cliente va a cancelar.")
                            .setCancelable(false)
                            .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            });
                    AlertDialog alerterrorNollenotodo=errorNollenotodo.create();
                    alerterrorNollenotodo.setTitle("Error");
                    alerterrorNollenotodo.show();
                    return;
                }else{//inicio if si los datos estan ingresados
                    totalQuePagaCliente=Float.parseFloat(TotalPagaCliente.getText().toString());
                    if(totalQuePagaCliente<TotalPagar){//inicio if si lo que el cliente dio es menor a lo que tiene que pagar
                        AlertDialog.Builder errorPAgoIncompleto= new AlertDialog.Builder(FacturaDeVentaActivity.this);
                        errorPAgoIncompleto.setMessage("Pago incompleto, infreso una suma menor a la adeudada")
                                .setCancelable(false)
                                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                });
                        AlertDialog alerterrorPAgoIncompletoo=errorPAgoIncompleto.create();
                        alerterrorPAgoIncompletoo.setTitle("Error");
                        alerterrorPAgoIncompletoo.show();
                        return;
                    }else{//inicia el recorrido para ingresar la factura


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
            listaProductos.add(productosList.get(i).getCodigo()+" - "+productosList.get(i).getNombre()+" - "+productosList.get(i).getExistencias());
        }

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

    public void printTable(View v) {

        //This method is to get the contents of the table once it is filled

        String rowContent = "";
        for (int i = 0; i < DetalleFacturaVenta.getChildCount(); i++) {
            TableRow row = (TableRow) DetalleFacturaVenta.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView currentCell = (TextView) row.getChildAt(j);
                rowContent += currentCell.getText() + ", ";
            }
            Toast.makeText(getApplicationContext(),rowContent, Toast.LENGTH_LONG).show();
        }
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
                    Toast.makeText(getApplicationContext(),"Se elimino con exito: ", Toast.LENGTH_LONG).show();
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

}
