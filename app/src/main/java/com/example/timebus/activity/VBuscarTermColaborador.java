package com.example.timebus.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timebus.R;
import com.example.timebus.adaptador.RecyclerAdapterTermColaborador;
import com.example.timebus.model.ItemList4;
import com.example.timebus.retrofit_data.RetrofitClient4;
import com.example.timebus.retrofit_data.RetrofitTermColaborador;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VBuscarTermColaborador extends AppCompatActivity implements RecyclerAdapterTermColaborador.RecyclerItemClick, SearchView.OnQueryTextListener{

    private RecyclerView rvLista;
    private SearchView svSearch;
    private RecyclerAdapterTermColaborador adapter;
    private List<ItemList4> items;
    private RetrofitTermColaborador retrofitTermColaborador;

    Bundle datos;
    private TextView mostrarDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vbuscar_term_colaborador);

        datos = getIntent().getExtras();
        String datosobtenidos = datos.getString("pasardatos2");
        mostrarDatos = (TextView) findViewById(R.id.tvEmpresa);
        mostrarDatos.setText(datosobtenidos);

        initViews();
        initValues();
        initListener();
    }
    private void initViews(){
        rvLista = findViewById(R.id.rvLista);
        svSearch = findViewById(R.id.svSearch);
    }

    private void initValues() {
        retrofitTermColaborador = RetrofitClient4.getApiService4();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvLista.setLayoutManager(manager);

        getItemsSQL();
    }

    private void initListener() {
        svSearch.setOnQueryTextListener(this);
    }


    private void getItemsSQL() {
        String valor1_String = mostrarDatos.getText().toString();

        List<String> valores_dni_1 = new ArrayList<String>();
        valores_dni_1.add("73545825");
        valores_dni_1.add("75271531");
        valores_dni_1.add("70776127");

        if(valores_dni_1.contains(valor1_String)){
            retrofitTermColaborador.getItemsDB4(valor1_String).enqueue(new Callback<List<ItemList4>>(){

                @Override
                public void onResponse(Call<List<ItemList4>> call, Response<List<ItemList4>> response) {
                    items = response.body();
                    adapter = new RecyclerAdapterTermColaborador(items, VBuscarTermColaborador.this::itemClick);
                    rvLista.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<ItemList4>> call, Throwable t) {
                    Toast.makeText(VBuscarTermColaborador.this, "Error: "+t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }


    }
    @Override
    public void itemClick(ItemList4 item) {

        String valor_usuario = mostrarDatos.getText().toString();

        Intent siguiente =new Intent(this,VBuscarEmpColaborador.class) ;
        siguiente.putExtra("pasardatos3",valor_usuario);
        startActivity(siguiente);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }

    public void onClick(View view) {
        Intent miIntent = null;
        switch (view.getId())
        {
            case R.id.btnIRColab:
                miIntent = new Intent(VBuscarTermColaborador.this, VUbicacionTerminal.class);
                break;
        }
        if (miIntent != null)
        {
            startActivity(miIntent);
        }
    }
}
