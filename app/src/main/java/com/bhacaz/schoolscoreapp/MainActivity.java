package com.bhacaz.schoolscoreapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseBooleanArray;
import android.view.*;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.bhacaz.vieetudiante.Categorie;
import com.bhacaz.vieetudiante.Cours;
import com.bhacaz.vieetudiante.Examen;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;


public class MainActivity extends ListActivity {

    ArrayList<Examen> examenArrayList = new ArrayList<Examen>();
    ListView listView;
    ListViewAdapter adapter;
    FloatingActionButton fab;
    Cours c;
    private Categorie cat;

    private ActionMode mActionMode;

//    ArrayList<String> stringArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cat = ((Categorie)getApplicationContext());

        c = (Cours) getIntent().getSerializableExtra("clickedCours");
        examenArrayList = c.getListElement();

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primaryBlue)));
        bar.setTitle(c.getNomCours());
        bar.setDisplayHomeAsUpEnabled(true);


        listView = (ListView) findViewById(android.R.id.list);

        adapter = new ListViewAdapter(this, examenArrayList);
        listView.setAdapter(adapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = listView.getCheckedItemCount();

                Menu menu = mode.getMenu();
                if(checkedCount > 1)
                    menu.findItem(R.id.action_edit).setVisible(false);
                else
                    menu.findItem(R.id.action_edit).setVisible(true);
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Sélectionné");
                // Calls toggleSelection method from ListViewAdapter Class
                adapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Create the menu from the xml file
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_selected, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                fab.hide();
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_discard:
                        SparseBooleanArray selected = adapter.getSelectedIds();
                        for (int i = (selected.size() - 1); i >= 0; i--)
                        {
                            if(selected.valueAt(i))
                            {
                                adapter.remove(selected.keyAt(i));
                            }
                        }
                        mode.finish();
                        return true;
                    case R.id.action_edit:
                        //Toast.makeText(getApplicationContext(), "Edit selected", Toast.LENGTH_SHORT).show();

                        showCustomDialog(adapter.getSelectedIds().keyAt(0)).show();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {
                adapter.removeSelection();
                fab.show();
            }
        });


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(listView);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog().show();
            }
        });



    }

    @Override
    protected void onStop()
    {
        super.onStop();
        c.setExamenArrayList(examenArrayList);



        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sh.edit();
        Gson gson = new Gson();

        String json = gson.toJson(c);
        editor.putString(c.getNomCours(), json);
        editor.commit();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            case R.id.action_settings:
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    private AlertDialog.Builder showCustomDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        alertDialog.setTitle("Ajouter un examen");
        final View customView = inflater.inflate(R.layout.new_examen_layout, null);


        final Spinner spi = (Spinner)customView.findViewById(R.id.spinner_categorie);
        ArrayAdapter<String> spiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cat.getCategorie());
        spi.setAdapter(spiAdapter);



        alertDialog.setView(customView);
        alertDialog.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText nom = (EditText)customView.findViewById(R.id.editText_newCours_nom);
                EditText pond = (EditText)customView.findViewById(R.id.editText_newExamen_pond);
                EditText res = (EditText)customView.findViewById(R.id.editText_newExamen_resultat);
                Examen e;

                if(res.getText().toString().length() == 0)
                {
                    e = new Examen(nom.getText().toString(), Double.parseDouble(pond.getText().toString()), spi.getSelectedItem().toString());
                }
                else
                {
                    e = new Examen(nom.getText().toString(), Double.parseDouble(pond.getText().toString()), Double.parseDouble(res.getText().toString()), spi.getSelectedItem().toString());
                }


                                examenArrayList.add(e);

                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        alertDialog.setNegativeButton("Annuler",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                // if this button is clicked, just close
                // the dialog box and do nothing
                dialog.cancel();
            }
        });



//        final Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.new_examen_layout);
//        dialog.setTitle("Ajouter un examen");
//
//
//
//        Button annuler_button = (Button) dialog.findViewById(R.id.button_newExamen_annuler);
//        annuler_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        return dialog;
        return alertDialog;
    }

    public AlertDialog.Builder showCustomDialog(final int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        alertDialog.setTitle("Modifier un examen");

        final View customView = inflater.inflate(R.layout.new_examen_layout, null);
        final EditText nom = (EditText) customView.findViewById(R.id.editText_newCours_nom);
        final EditText pond = (EditText) customView.findViewById(R.id.editText_newExamen_pond);
        final EditText res = (EditText) customView.findViewById(R.id.editText_newExamen_resultat);

        final Spinner spi = (Spinner)customView.findViewById(R.id.spinner_categorie);
        ArrayAdapter<String> spiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cat.getCategorie());
        spi.setAdapter(spiAdapter);

        Examen ex = adapter.getExamenList().get(position);

        nom.setText(ex.getNomCours());
        pond.setText(String.valueOf(ex.getPonderation()));
        res.setText(String.valueOf(ex.getResultat()));
        spi.setSelection(cat.getCategorie().indexOf(ex.getCategorie()));

        alertDialog.setView(customView);
        alertDialog.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Examen e;

                if (res.getText().toString().length() == 0) {
                    e = new Examen(nom.getText().toString(), Double.parseDouble(pond.getText().toString()), spi.getSelectedItem().toString());
                } else {
                    e = new Examen(nom.getText().toString(), Double.parseDouble(pond.getText().toString()), Double.parseDouble(res.getText().toString()), spi.getSelectedItem().toString());
                }


                adapter.getExamenList().set(position, e);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("Annuler",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                // if this button is clicked, just close
                // the dialog box and do nothing
                dialog.cancel();
            }
        });



//        final Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.new_examen_layout);
//        dialog.setTitle("Ajouter un examen");
//
//
//
//        Button annuler_button = (Button) dialog.findViewById(R.id.button_newExamen_annuler);
//        annuler_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        return dialog;
        return alertDialog;
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent();
        i.putExtra("CoursModif", c);
        setResult(RESULT_OK, i);
        finish();
    }

    private ArrayList<Examen> generateListView()
    {
        examenArrayList.add(new Examen("Examen 1", 35.0, 87.4));
        examenArrayList.add(new Examen("TP1", 10.0, 64.5));
        examenArrayList.add(new Examen("Examen 2", 40.0 ,46.2));

        return examenArrayList;
    }






}
