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

public class misFormularios extends Fragment {
    WebView web;

    private MisFormulariosViewModel mViewModel;

    public static misFormularios newInstance() {
        return new misFormularios();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mis_formularios_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MisFormulariosViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        web = view.findViewById(R.id.webviewForms);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl("http://wizardapps.xyz/novitierra/consulta/misFormularios.php?codigo="+ Global.codigo);
    }
}