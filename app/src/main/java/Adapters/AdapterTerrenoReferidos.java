package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novitierraapp.R;
import com.example.novitierraapp.entidades.TerrenoReferidos;

import java.util.ArrayList;

public class AdapterTerrenoReferidos extends RecyclerView.Adapter<AdapterTerrenoReferidos.ViewHolderTerrenoReferidos> {
    ArrayList<TerrenoReferidos> listTerrenoReferidos;
    Integer id=0;

    public AdapterTerrenoReferidos(ArrayList<TerrenoReferidos> listTerrenoReferidos) {
        this.listTerrenoReferidos = listTerrenoReferidos;
    }

    @NonNull
    @Override
    public ViewHolderTerrenoReferidos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_terrenosreferido,null,false);
        ViewHolderTerrenoReferidos viewHolderTerrenoReferidos = new ViewHolderTerrenoReferidos(view);
        return viewHolderTerrenoReferidos;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTerrenoReferidos.ViewHolderTerrenoReferidos holder, int position) {
        //donde interactua el adapter
        int i = position;
        TerrenoReferidos terrenoReferidos = listTerrenoReferidos.get(position);
        holder.numeracion.setText(String.valueOf(position+1)+".-");
        holder.urbanizacion.setText(terrenoReferidos.getUrbanizacion());
        holder.fecha.setText(terrenoReferidos.getFecha());

    }

    @Override
    public int getItemCount() {
        return listTerrenoReferidos.size();
    }

    public class ViewHolderTerrenoReferidos extends RecyclerView.ViewHolder {
        TextView urbanizacion,fecha,numeracion;
        public ViewHolderTerrenoReferidos(@NonNull View itemView) {
            super(itemView);
            //donde se declara los componentes
            numeracion = itemView.findViewById(R.id.numeracionTerrenos);
            urbanizacion = itemView.findViewById(R.id.recyclerVentaReferidoUrbanizacion);
            fecha = itemView.findViewById(R.id.recyclerVentaReferidoFecha);

        }
    }
}
