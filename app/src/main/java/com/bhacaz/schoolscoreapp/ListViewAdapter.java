package com.bhacaz.schoolscoreapp;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bhacaz.vieetudiante.Categorie;
import com.bhacaz.vieetudiante.Examen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhacaz on 15-04-24.
 */
public class ListViewAdapter extends ArrayAdapter<Examen> {

    private final Context context;
    private final ArrayList<Examen> examenList;
    private SparseBooleanArray mSelectedItemsIds = new SparseBooleanArray();


    public ListViewAdapter(Context context, ArrayList<Examen> examenList)
    {
        super(context, R.layout.row, examenList);

        this.context = context;
        this.examenList = examenList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row, parent, false);

        TextView nomView = (TextView) rowView.findViewById(R.id.nomExamenRow);
        TextView pondView = (TextView) rowView.findViewById(R.id.pondExamenRow);
        TextView resultatView = (TextView) rowView.findViewById(R.id.resultatExamenRow);






        nomView.setText(examenList.get(position).getNomCours());
        pondView.setText(String.valueOf(examenList.get(position).getPonderation())+"%");
        resultatView.setText(String.valueOf(examenList.get(position).getResultat())+"%");
        double note = examenList.get(position).getResultat();
        if(note >= 50)
            resultatView.setTextColor(context.getResources().getColor(R.color.vertAuDessusDeLaNoteDePassage));
        else if(note < 50 && note > 0)
            resultatView.setTextColor(context.getResources().getColor(R.color.rougeSousLaNoteDePassage));

        return rowView;

    }

    public ArrayList<Examen> getExamenList(){
        return examenList;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public void remove(int i)
    {
        examenList.remove(i);
        notifyDataSetChanged();
    }


}
