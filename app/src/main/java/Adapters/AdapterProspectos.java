package Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.novitierraapp.Encuesta;
import com.example.novitierraapp.R;
import com.example.novitierraapp.entidades.Prospectos;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterProspectos extends RecyclerView.Adapter<AdapterProspectos.ViewHolderProspectos> {
    ArrayList<Prospectos> listProspectos;
    Integer id=0;
    Context mcontext;
    private static  String URL_deleteprospecto="http://wizardapps.xyz/novitierra/api/deleteProspecto.php";
    public AdapterProspectos(ArrayList<Prospectos> listProspectos) {
        this.listProspectos = listProspectos;
    }

    @NonNull
    @Override
    public ViewHolderProspectos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_prospectoshoy,null,false);
        ViewHolderProspectos viewHolderProspectos = new ViewHolderProspectos(view);
        return viewHolderProspectos;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolderProspectos holder, int position) {
        int i = position;
        Prospectos prospecto = listProspectos.get(position);
        holder.numero.setText(String.valueOf(position+1)+".-");
        holder.nombre.setText(prospecto.getNombre_completo());
        holder.telefono.setText(String.valueOf(prospecto.getTelefono()));
        holder.zona.setText(prospecto.getZona());
        holder.lugar.setText(prospecto.getLugar());
        holder.urbanizacion.setText(prospecto.getUrbanizacion());
        holder.observacion.setText(prospecto.getObservacion());
        holder.llamada.setText(prospecto.getLlamada());
        holder.vigencia.setText(prospecto.getVigencia());

        holder.updateProspecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id",listProspectos.get(i).getId_prospectos());
                bundle.putString("nombre",listProspectos.get(i).getNombre_completo());
                bundle.putInt("telefono",listProspectos.get(i).getTelefono());
                bundle.putString("zona",listProspectos.get(i).getZona());
                bundle.putString("lugar",listProspectos.get(i).getLugar());
                bundle.putString("urbanizacion",listProspectos.get(i).getUrbanizacion());
                bundle.putString("observacion",listProspectos.get(i).getObservacion());
                bundle.putString("llamada",listProspectos.get(i).getLlamada());
                Navigation.findNavController(v).popBackStack();
                Navigation.findNavController(v).navigate(R.id.fragmentUpdateProspecto,bundle);
            }
        });

        //
        holder.deleteProspecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(v.getContext());
                alerta.setMessage("Desea eliminar el Prospecto?").setCancelable(false)
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                id=listProspectos.get(i).getId_prospectos();
                                Navigation.findNavController(v).popBackStack();
                                eliminarProspecto(v);
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
//                Navigation.findNavController(v).navigate(R.id.prospectosHoy);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listProspectos.size();
    }

    public class ViewHolderProspectos extends RecyclerView.ViewHolder {
        TextView nombre,telefono,numero,urbanizacion,observacion,llamada,zona,lugar,vigencia;
        Button updateProspecto, deleteProspecto;

        public ViewHolderProspectos(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreProspecto);
            telefono = itemView.findViewById(R.id.telefonoProspecto);
            numero = itemView.findViewById(R.id.numeracionProspectos);
            urbanizacion = itemView.findViewById(R.id.urbanizacionProspectos);
            observacion = itemView.findViewById(R.id.observacionProspectos);
            zona = itemView.findViewById(R.id.zonaProspectos);
            lugar = itemView.findViewById(R.id.lugarProspectos);
            llamada = itemView.findViewById(R.id.llamadaProspecto);
            vigencia = itemView.findViewById(R.id.vigenciaProspecto);
            updateProspecto = itemView.findViewById(R.id.updateProspecto);
            deleteProspecto = itemView.findViewById(R.id.deleteProspecto);

        }

    }

    public void eliminarProspecto(View v){
        StringRequest request = new StringRequest(Request.Method.POST, URL_deleteprospecto, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    if(response.contains("algo salio mal")){
                        Toast.makeText(v.getContext(),"No se pudo modificar el registro debido a un error",Toast.LENGTH_LONG).show();
                    }
                    else{Toast.makeText(v.getContext(),"Datos modificados",Toast.LENGTH_LONG).show();}

                }else{
                    Toast.makeText(v.getContext(), "No se ha podido modificar el prospecto", Toast.LENGTH_LONG).show();
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
        Navigation.findNavController(v).navigate(R.id.prospectosHoy);
    }


}
