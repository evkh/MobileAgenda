package com.example.listapersonagens.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.listapersonagens.R;
import com.example.listapersonagens.dao.PersonagemDAO;
import com.example.listapersonagens.model.Personagem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.example.listapersonagens.ui.activities.ConstantesActivities.CHAVE_PERSONAGEM;

public class ListaPersonagemActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Lista de Personagens";
    private final PersonagemDAO dao = new PersonagemDAO();
    private  ArrayAdapter<Personagem> adapter;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_personagem);
        //Definindo o titulo
        setTitle(TITULO_APPBAR);
        configuraFabNovoPersonagem();
        configuraLista();

    }

    private void configuraFabNovoPersonagem() {
        //Puxando o FloatingActionButton
        FloatingActionButton botaoNovoPersonagem = findViewById(R.id.floatingActionButton2);
        botaoNovoPersonagem.setOnClickListener(new View.OnClickListener() {
            @Override
            //Ao clicar no botão executa o abreFormularioSalva()
            public void onClick(View view) {
                abreFormularioSalva();

            }

        });
    }

    private void abreFormularioSalva() {
        startActivity(new Intent(ListaPersonagemActivity.this, FormularioPersonagemActivity.class));
    }

    //protege os dados para não sumir ao dar back
    @Override
    protected void onResume() {
        super.onResume();
        atualizaPersonagem();

    }

    private void atualizaPersonagem() {
        adapter.clear();
        adapter.addAll(dao.todos());
    }

    private void  remove (Personagem personagem)
    {
        dao.remove(personagem);
        adapter.remove(personagem);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //menu.add("Remover");
        //menu.add("Teste");
        getMenuInflater().inflate(R.menu.activity_lista_personagens_menu, menu);
    }

    @Override
    //Pega a informação e deleta
    public boolean onContextItemSelected( MenuItem item) {
        configuraMenu(item);
        return super.onContextItemSelected(item);
    }

    private void configuraMenu(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.activity_lista_pesonagem_menu_remover) {

            new AlertDialog.Builder(this).setTitle("Removendo Personagem").setMessage("Tem certeza que deseja remover?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                    Personagem personagemEscolhido = adapter.getItem(menuInfo.position);
                    remove(personagemEscolhido);
                }
            })
                    .setNegativeButton("Não", null).show();




        }
    }

    //faz a configuração da lista
    private void configuraLista() {
        ListView listadePersonagens = findViewById(R.id.activity_main_lista_personagem);
        configuraAdapter(listadePersonagens);
        configuraItemPorClique(listadePersonagens);
        registerForContextMenu(listadePersonagens);
    }

    private void configuraItemPorClique(ListView listadePersonagens) {
        listadePersonagens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //Metodo que faz a selecao dos personagens
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long id) {
                Personagem personagemEscolhido = (Personagem) adapterView.getItemAtPosition(posicao);
                AbreFormularioModoEditar(personagemEscolhido);
            }
        });
    }

    //abre a janela editar
    private void AbreFormularioModoEditar(Personagem PersonagemEscolhido) {
        Intent VaiParaFormulario = new Intent(ListaPersonagemActivity.this  , FormularioPersonagemActivity.class);
        VaiParaFormulario.putExtra(CHAVE_PERSONAGEM, PersonagemEscolhido);
        startActivity(VaiParaFormulario);
    }

    private void configuraAdapter(ListView listadePersonagens) {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1 );
        listadePersonagens.setAdapter(adapter);
    }


}
