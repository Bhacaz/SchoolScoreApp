package com.bhacaz.schoolscoreapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bhacaz.vieetudiante.Cours;
import com.bhacaz.vieetudiante.Examen;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melnykov.fab.FloatingActionButton;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivityCours extends Activity {

    private ArrayList<Cours> listCours = new ArrayList<Cours>();
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;
    private CoursAdapter adapter;
    private int coursSelected;
    SharedPreferences sh;
    GestureDetectorCompat gestureDetector;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_cours);
        //generateListExamen();



        sh = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        Map<String, ?> mapAll = sh.getAll();

        for(Map.Entry<String, ?> entry : mapAll.entrySet())
        {
            Type type = new TypeToken<Cours>() {}.getType();
            //System.out.println(entry.getKey()+ " -- " + entry.getValue().toString());
            Cours c = gson.fromJson(entry.getValue().toString(), type);
            listCours.add(c);
        }



//        String json = sh.getString("saveSh", null);
//        Type type = new TypeToken<ArrayList<Cours>>() {}.getType();
//        listCours = gson.fromJson(json, type);

        if(listCours == null)
            listCours = new ArrayList<Cours>();

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primaryBlue)));

        adapter = new CoursAdapter(listCours, this);
        rv = (RecyclerView) findViewById(R.id.rv);





        mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, rv, new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                // ...
                coursSelected = position;
                Intent i = new Intent(getApplication().getApplicationContext(), MainActivity.class);
                i.putExtra("clickedCours", listCours.get(position));
                MainActivityCours.this.startActivityForResult(i, 1);
            }

            @Override
            public void onItemLongClick(View view, int position)
            {
                // ...
                //Toast.makeText(getApplicationContext(),position + " -- this is my Toast message!!! =)", Toast.LENGTH_LONG).show();
               // adapter.toggleSelection(position);
                showCustomDialog(position).show();
                fab.show();
            }
        }));




        fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.attachToRecyclerView(rv);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog().show();
            }
        });

        recalculeCours();
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onStop()
    {
        super.onStop();



        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sh.edit();
        Gson gson = new Gson();

        for(Cours c : listCours)
        {
            String json = gson.toJson(c);
            editor.putString(c.getNomCours(), json);
        }
        editor.commit();

//        String json = gson.toJson(listCours);
//        editor.putString("saveSh", json);
//        editor.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            listCours.set(coursSelected, (Cours)data.getSerializableExtra("CoursModif"));
            recalculeCours();
            adapter.notifyDataSetChanged();
            fab.show();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_cours, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void recalculeCours()
    {
        for(Cours c : listCours)
            c.recalcule();
    }

    public AlertDialog.Builder showCustomDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        alertDialog.setTitle("Ajouter un cours");
        final View customView = inflater.inflate(R.layout.new_cours_layout, null);
        alertDialog.setView(customView);
        alertDialog.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText nom = (EditText)customView.findViewById(R.id.editText_newCours_nom);
                EditText num = (EditText)customView.findViewById(R.id.editText_NewCoursDialog_num);
                EditText nPass = (EditText)customView.findViewById(R.id.editText_NewCoursDialog_notePass);

                Cours e;

                //public Cours(String nom, String num, Double n, int c)

                e = new Cours(nom.getText().toString(), num.getText().toString(), Double.parseDouble(nPass.getText().toString()));


                listCours.add(e);
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

    public AlertDialog.Builder showCustomDialog(final int position)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        alertDialog.setTitle("Modifier un cours");
        final View customView = inflater.inflate(R.layout.new_cours_layout, null);
        final EditText nom = (EditText)customView.findViewById(R.id.editText_newCours_nom);
        final EditText num = (EditText)customView.findViewById(R.id.editText_NewCoursDialog_num);
        final EditText nPass = (EditText)customView.findViewById(R.id.editText_NewCoursDialog_notePass);

        nom.setText(listCours.get(position).getNomCours());
        num.setText(listCours.get(position).getNumCours());
        nPass.setText(String.valueOf(listCours.get(position).getNotePassage()));


        alertDialog.setView(customView);
        alertDialog.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listCours.get(position).setNomCours(nom.getText().toString());
                listCours.get(position).setNumCours(num.getText().toString());
                listCours.get(position).setNotePassage(Double.valueOf(nPass.getText().toString()));

                recalculeCours();

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


        alertDialog.setNeutralButton("Supprimer", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.dismiss();
                showConf(position).show();


            }
        });





        return alertDialog;
    }

    public AlertDialog.Builder showConf(final int position)
    {
        final AlertDialog.Builder conf = new AlertDialog.Builder(this)
                .setTitle("Supprimer le cours " + listCours.get(position).getNomCours())
                .setMessage("Voulez-vous vraiment supprimer le cours " + listCours.get(position).getNomCours() + " de la liste de cours?")
                .setPositiveButton("Confirmer", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which){

                        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sh.edit();
                        editor.remove(listCours.get(position).getNomCours()).commit();

                        listCours.remove(position);
                        recalculeCours();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Annuler" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showCustomDialog(position).show();
                    }
                });
        return conf;
    }












    private void generateListExamen()
    {
        //Cours(String nom, String num, Double n, int c)
        listCours.add(new Cours("Design", "GEL-3001", 50.0));
        listCours.add(new Cours("Ethique", "PHY-1001", 60.0));
        listCours.add(new Cours("Electromagnetisme", "GEL-4001", 50.0));
    }
}
