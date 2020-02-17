package com.ivn.lamejortienda.activities.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.ivn.lamejortienda.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements View.OnClickListener {

    Button btRealizarPedido;
    EditText etDir1;
    EditText etDir2;
    EditText etCP;
    EditText etCiudad;

    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;

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


        etDir1 = root.findViewById(R.id.etDir1);
        etDir2 = root.findViewById(R.id.etDir2);
        etCP = root.findViewById(R.id.etCP);
        etCiudad = root.findViewById(R.id.etCiudad);

        return root;
    }

    @Override
    public void onClick(View v) {
        String dir1 = etDir1.getText().toString();
        String dir2 = etDir2.getText().toString();
        String ciudad = etCiudad.getText().toString();
        String cp = etCP.getText().toString();

        if(dir1.isEmpty() || dir2.isEmpty() || ciudad.isEmpty() || cp.isEmpty())
            Snackbar.make(v, getString(R.string.todos_campos), Snackbar.LENGTH_SHORT)
                    .setActionTextColor(Color.RED)
                    .show();
        else
            Snackbar.make(v, getString(R.string.pedido_realizado), Snackbar.LENGTH_SHORT)
                    .setActionTextColor(Color.RED)
                    .show();
    }
}