package Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.novitierraapp.R;
import com.example.novitierraapp.entidades.Prospectos;
import com.example.novitierraapp.entidades.Titular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterTitulares extends RecyclerView.Adapter<AdapterTitulares.ViewHolderTitulares> {
    ArrayList<Titular> listTitulares;
    Integer id=0;
    Context mcontext;
    private static  String URL_deleteTitular="http://wizardapps.xyz/novitierra/api/deleteTitularapp.php";

    public AdapterTitulares(ArrayList<Titular> listTitulares) {
        this.listTitulares = listTitulares;
    }

    @NonNull
    @Override
    public ViewHolderTitulares onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_titular,null,false);
        ViewHolderTitulares viewHolderTitulares = new ViewHolderTitulares(view);
        return viewHolderTitulares;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTitulares.ViewHolderTitulares holder, int position) {
        int i = position;
        Titular titular = listTitulares.get(position);
        holder.numero.setText(String.valueOf(position+1)+".-");
        holder.codigot.setText(String.valueOf(titular.getId_titular()));
        holder.nombret.setText(titular.getNombres().toUpperCase()+" "+titular.getApellidoP().toUpperCase()+" "+ titular.getApellidoM().toUpperCase());
        holder.documentot.setText(String.valueOf(titular.getNro_documento()));
        holder.urbanizaciont.setText(titular.getUrbanizacion());
        holder.proyectot.setText(titular.getProyecto().toString());
        holder.uvt.setText(titular.getUv());
        holder.mzt.setText(titular.getMz());
        holder.ltt.setText(titular.getLt().toString());
        holder.metros2t.setText(titular.getMetros2());

        holder.updatet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 1;
                Bundle bundle = new Bundle();
                bundle.putInt("id",listTitulares.get(i).getId_titular());
                bundle.putInt("flag",flag);
                Navigation.findNavController(v).popBackStack();
                Navigation.findNavController(v).navigate(R.id.cargarFormulario,bundle);
            }
        });

        //
        holder.eliminart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(v.getContext());
                alerta.setMessage("Desea eliminar el Titular?").setCancelable(false)
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        id=listTitulares.get(i).getId_titular();
                        Navigation.findNavController(v).popBackStack();
                        EliminarTitular(v);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Eliminar");
                titulo.show();

            }
        });

        holder.ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id",listTitulares.get(i).getId_titular());
                bundle.putString("nombre",listTitulares.get(i).getNombres());
                bundle.putString("apellidop",listTitulares.get(i).getApellidoP());
                bundle.putString("apellidom",listTitulares.get(i).getApellidoM());
                bundle.putString("apellidoc",listTitulares.get(i).getApellidoC());
                bundle.putString("prefijo",listTitulares.get(i).getPrefijo());
                bundle.putString("nrodocumento",listTitulares.get(i).getNro_documento());
                bundle.putString("extension",listTitulares.get(i).getExtension());
                bundle.putString("zona",listTitulares.get(i).getZona());
                bundle.putString("barrio",listTitulares.get(i).getBarrio());
                bundle.putString("avenida",listTitulares.get(i).getAvenida());
                bundle.putString("calle",listTitulares.get(i).getCalle());
                bundle.putString("numero",listTitulares.get(i).getNumero());
                bundle.putString("telfmovil",listTitulares.get(i).getTelf_movil());
                bundle.putString("referencia1",listTitulares.get(i).getReferencia1());
                bundle.putString("telefono1",listTitulares.get(i).getTelf_referencia1());
                bundle.putString("referencia2",listTitulares.get(i).getReferencia2());
                bundle.putString("telefono2",listTitulares.get(i).getTelf_referencia2());
                bundle.putString("uv",listTitulares.get(i).getUv());
                bundle.putString("mz",listTitulares.get(i).getMz());
                bundle.putInt("lt",listTitulares.get(i).getLt());
                bundle.putString("cat",listTitulares.get(i).getCat());
                bundle.putString("obs1",listTitulares.get(i).getObservacion());
                bundle.putString("obs2",listTitulares.get(i).getObservacion2());
                Navigation.findNavController(v).popBackStack();
//                Navigation.findNavController(v).navigate(R.id.formularioMapa,bundle);
                Navigation.findNavController(v).navigate(R.id.mapFragment,bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listTitulares.size();
    }
    public void filterListTitular(ArrayList<Titular> filteredListTitular){
        listTitulares = filteredListTitular;
        notifyDataSetChanged();
    }

    public class ViewHolderTitulares extends RecyclerView.ViewHolder {
        TextView codigot,numero,nombret,documentot,urbanizaciont,proyectot,uvt,mzt,ltt,metros2t;
        Button updatet,eliminart,ubicacion;

        public ViewHolderTitulares(@NonNull View itemView) {
            super(itemView);
            numero = itemView.findViewById(R.id.numeracionTitular);
            codigot = itemView.findViewById(R.id.codigoTitular);
            nombret = itemView.findViewById(R.id.nombreTitular);
            documentot = itemView.findViewById(R.id.documentoTitular);
            urbanizaciont = itemView.findViewById(R.id.urbanizacionTitular);
            proyectot = itemView.findViewById(R.id.proyectoTitular);
            uvt = itemView.findViewById(R.id.uvTitular);
            mzt = itemView.findViewById(R.id.mzTitular);
            ltt = itemView.findViewById(R.id.loteTitular);
            metros2t = itemView.findViewById(R.id.metros2Titular);

            updatet = itemView.findViewById(R.id.updateTitular);
            eliminart = itemView.findViewById(R.id.eliminarTitular);
            ubicacion = itemView.findViewById(R.id.ubicacionTitular);
        }
    }

//    /adaptar metodo eliminar******************
    public void EliminarTitular(View v){
        StringRequest request = new StringRequest(Request.Method.POST, URL_deleteTitular, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    if(response.contains("algo salio mal")){
                        Toast.makeText(v.getContext(),"No se pudo modificar el registro debido a un error",Toast.LENGTH_LONG).show();
                    }
                    else{Toast.makeText(v.getContext(),"Datos Eliminados",Toast.LENGTH_LONG).show();}

                }else{
                    Toast.makeText(v.getContext(), "No se ha podido modificar el titular", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(v.getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("id",(id.toString()));
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(request);
        Navigation.findNavController(v).navigate(R.id.nav_home);
    }
}
