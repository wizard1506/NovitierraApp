package com.example.novitierraapp;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.novitierraapp.entidades.Global;

public class prospectosLista extends Fragment {
    WebView web;

    private ProspectosListaViewModel mViewModel;

    public static prospectosLista newInstance() {
        return new prospectosLista();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.prospectos_lista_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProspectosListaViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        web=view.findViewById(R.id.wvProspecto);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl("http://wizardapps.xyz/novitierra/consulta/misProspectos.php?codigo="+ Global.codigo);
    }
}