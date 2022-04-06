package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novitierraapp.R;
import com.example.novitierraapp.entidades.Prospectos;

import java.util.ArrayList;

public class AdapterProspectos extends RecyclerView.Adapter<AdapterProspectos.ViewHolderProspectos> {
    ArrayList<Prospectos> listProspectos;

    public AdapterProspectos(ArrayList<Prospectos> listProspectos) {
        this.listProspectos = listProspectos;
    }

    @NonNull
    @Override
    public ViewHolderProspectos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_prospectoshoy,null,false);
        return new ViewHolderProspectos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProspectos holder, int position) {
        Prospectos prospecto = listProspectos.get(position);
        holder.numero.setText(String.valueOf(position+1)+".-");
        holder.nombre.setText(prospecto.getNombre_completo());
        holder.telefono.setText(String.valueOf(prospecto.getTelefono()));
        holder.urbanizacion.setText(prospecto.getUrbanizacion());
        holder.observacion.setText(prospecto.getObservacion());
    }

    @Override
    public int getItemCount() {
        return listProspectos.size();
    }

    public class ViewHolderProspectos extends RecyclerView.ViewHolder {
        TextView nombre,telefono,numero,urbanizacion,observacion;
        public ViewHolderProspectos(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreProspecto);
            telefono = itemView.findViewById(R.id.telefonoProspecto);
            numero = itemView.findViewById(R.id.numeracionProspectos);
            urbanizacion = itemView.findViewById(R.id.urbanizacionProspectos);
            observacion = itemView.findViewById(R.id.observacionProspectos);
        }

    }
}
