package com.example.novitierraapp;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.novitierraapp.entidades.Global;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class MapFragment extends Fragment {

    Button btUbicacion;

    private MapViewModel mViewModel;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    SearchView searchView;
    OutputStream outputStream;
    Integer id,lt;
    String nombre,apellidop,apellidom,apellidoc,prefijo,nrodocumento,extension,zona,barrio,avenida,calle,numero,telfmovil,referencia1,telefono1,referencia2,telefono2,uv,mz,cat,obs1,obs2;

    private String path = Environment.getExternalStorageDirectory().getPath() + "/Download/UbicacionCliente.jpg";
    private File file = new File(path);


    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        btUbicacion = view.findViewById(R.id.btCapturarUbicacion);
        searchView = view.findViewById(R.id.sv_buscar);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        String location = searchView.getQuery().toString();
                        List<Address> listaDir = null;
                        if(location != null || !location.equals("")){
                            Geocoder geocoder = new Geocoder(getContext());
                            try {
                                listaDir = geocoder.getFromLocationName(location,1);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if(listaDir.size()==0){
                                Toast.makeText(getContext(),"Intente otra direccion o Coordenadas", Toast.LENGTH_LONG).show();
                            }else {
                                Address address = listaDir.get(0);
                                LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                                Global.gLat=latLng.latitude;
                                Global.gLong=latLng.longitude;
                                mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                            }
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
            }
        });

        btUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getArguments()!=null){
                    id=getArguments().getInt("id");
                    nombre=getArguments().getString("nombre");
                    apellidop=getArguments().getString("apellidop");
                    apellidom=getArguments().getString("apellidom");
                    apellidoc=getArguments().getString("apellidoc");
                    prefijo = getArguments().getString("prefijo");
                    nrodocumento = getArguments().getString("nrodocumento");
                    extension = getArguments().getString("extension");
                    zona = getArguments().getString("zona");
                    barrio = getArguments().getString("barrio");
                    avenida = getArguments().getString("avenida");
                    calle = getArguments().getString("calle");
                    numero = getArguments().getString("numero");
                    telfmovil = getArguments().getString("telfmovil");
                    referencia1 = getArguments().getString("referencia1");
                    telefono1 = getArguments().getString("telefono1");
                    referencia2 = getArguments().getString("referencia2");
                    telefono2 = getArguments().getString("telefono2");
                    uv=getArguments().getString("uv");
                    mz=getArguments().getString("mz");
                    lt = getArguments().getInt("lt");
                    cat = getArguments().getString("cat");
                    obs1 = getArguments().getString("obs1");
                    obs2 = getArguments().getString("obs2");

                }

                mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(@Nullable Bitmap bitmap) {
                        Global.ubicacion=bitmap;
                        try {
                            outputStream = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
                        Toast.makeText(getContext(),"Ubicacion guardada",Toast.LENGTH_SHORT).show();
                        try {
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(getArguments()!=null){
                            Bundle bundle = new Bundle();
                            bundle.putInt("id",id);
                            bundle.putString("nombre",nombre);
                            bundle.putString("apellidop",apellidop);
                            bundle.putString("apellidom",apellidom);
                            bundle.putString("apellidoc",apellidoc);
                            bundle.putString("prefijo",prefijo);
                            bundle.putString("nrodocumento",nrodocumento);
                            bundle.putString("extension",extension);
                            bundle.putString("zona",zona);
                            bundle.putString("barrio",barrio);
                            bundle.putString("avenida",avenida);
                            bundle.putString("calle",calle);
                            bundle.putString("numero",numero);
                            bundle.putString("telfmovil",telfmovil);
                            bundle.putString("referencia1",referencia1);
                            bundle.putString("telefono1",telefono1);
                            bundle.putString("referencia2",referencia2);
                            bundle.putString("telefono2",telefono2);
                            bundle.putString("uv",uv);
                            bundle.putString("mz",mz);
                            bundle.putInt("lt",lt);
                            bundle.putString("cat",cat);
                            bundle.putString("obs1",obs1);
                            bundle.putString("obs2",obs2);
                            Navigation.findNavController(v).popBackStack();
                            Navigation.findNavController(v).navigate(R.id.formularioMapa,bundle);
                        }
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
//                // Add a marker in Santa Cruz and move the camera
                LatLng centro = new LatLng(-17.783438157398255, -63.18229459226132);
                mMap.addMarker(new MarkerOptions()
                        .position(centro)
                        .title("Santa Cruz - Centro"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(centro));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centro, 17));

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(latLng.latitude+" , "+latLng.longitude);
                        Global.gLat=latLng.latitude;
                        Global.gLong=latLng.longitude;
                        mMap.clear();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
                        mMap.addMarker(markerOptions);
                    }
                });

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        // TODO: Use the ViewModel
    }

}