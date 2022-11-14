package Adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.novitierraapp.entidades.Referidos;
import com.example.novitierraapp.misReferidos;
import com.example.novitierraapp.terrenosReferido;
import com.example.novitierraapp.updateReferido;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterReferidos extends RecyclerView.Adapter<AdapterReferidos.ViewHolderReferidos>{
    ArrayList<Referidos> listReferidos;
    Integer id=0;
    String nombre= "Nombre";
    String apellido = "Apellido";
    private static  String URL_deleteReferido="http://wizardapps.xyz/novitierra/api/deleteReferido.php";


    public AdapterReferidos(ArrayList<Referidos> listReferidos) {
        this.listReferidos = listReferidos;
    }

    @NonNull
    @Override
    public ViewHolderReferidos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_referidos,null,false);
        ViewHolderReferidos viewHolderReferidos = new ViewHolderReferidos(view);
        return viewHolderReferidos;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterReferidos.ViewHolderReferidos holder, int position) {
            int i = position;
            Referidos referidos = listReferidos.get(position);
            holder.numeracion.setText(String.valueOf(position+1)+".-");
            holder.nombre.setText(referidos.getNombres());
            holder.apellido.setText(referidos.getApellidos());
            holder.telefono.setText(String.valueOf(referidos.getTelf()));

            holder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(view.getContext());
                    alerta.setMessage("Desea eliminar el Referido?").setCancelable(false)
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    id=listReferidos.get(i).getId_referido();
                                    eliminarReferido(view);
                                    Intent intent = new Intent(view.getContext(),misReferidos.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    view.getContext().startActivity(intent);
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
            holder.btUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    id=listReferidos.get(i).getId_referido();
                    nombre = listReferidos.get(i).getNombres();
                    apellido = listReferidos.get(i).getApellidos();
                    Intent intent = new Intent(view.getContext(), updateReferido.class);
                    intent.putExtra("id", id);
                    intent.putExtra("nombre", nombre);
                    intent.putExtra("apellido", apellido);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                }
            });
            holder.btverTerrenos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id=listReferidos.get(i).getId_referido();
                    nombre = listReferidos.get(i).getNombres();
                    apellido = listReferidos.get(i).getApellidos();
                    Intent intent = new Intent(v.getContext(), terrenosReferido.class);
                    intent.putExtra("id", id);
                    intent.putExtra("nombre", nombre);
                    intent.putExtra("apellido", apellido);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });

    }
    public void eliminarReferido(View v){
        StringRequest request = new StringRequest(Request.Method.POST, URL_deleteReferido, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    if(response.contains("algo salio mal")){
                        Toast.makeText(v.getContext(),"No se pudo modificar el registro debido a un error",Toast.LENGTH_LONG).show();
                    }
                    else{Toast.makeText(v.getContext(),"Referido eliminado",Toast.LENGTH_LONG).show();}

                }else{
                    Toast.makeText(v.getContext(), "No se ha podido eliminar el referido", Toast.LENGTH_LONG).show();
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
//        Navigation.findNavController(v).navigate(R.id.prospectosHoy);

    }

    @Override
    public int getItemCount() {
        return listReferidos.size();
    }

    public class ViewHolderReferidos extends RecyclerView.ViewHolder {
        TextView numeracion,nombre,apellido,telefono;
        Button btUpdate,btDelete,btverTerrenos;
        public ViewHolderReferidos(@NonNull View itemView) {
            super(itemView);

            numeracion = itemView.findViewById(R.id.numeracionReferidos);
            nombre = itemView.findViewById(R.id.recyclerReferidoNombre);
            apellido = itemView.findViewById(R.id.recyclerReferidoApellido);
            telefono = itemView.findViewById(R.id.recyclerReferidoTelefono);
            btUpdate = itemView.findViewById(R.id.updateReferido);
            btDelete = itemView.findViewById(R.id.deleteReferido);
            btverTerrenos = itemView.findViewById(R.id.verTerrenosReferido);

            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
