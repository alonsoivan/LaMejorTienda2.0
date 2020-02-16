package com.ivn.lamejortienda.activities.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.ivn.lamejortienda.R;
import com.ivn.lamejortienda.activities.CestaActivity;

import static com.ivn.lamejortienda.clases.Constantes.URL_REALIZAR_PEDIDO;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements View.OnClickListener {

    Button btRealizarPedido;

    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_domicilio, container, false);

        btRealizarPedido = root.findViewById(R.id.btPedido);
        btRealizarPedido.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        Snackbar.make(v, "PEDIDO  REALIZADO.    GRACIAS  POR  SU  COMPRA.", Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.RED)
                .show();
    }
}