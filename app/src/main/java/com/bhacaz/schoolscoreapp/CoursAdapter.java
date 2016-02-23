package com.bhacaz.schoolscoreapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bhacaz.vieetudiante.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhacaz on 15-04-27.
 */
public class CoursAdapter extends RecyclerView.Adapter<CoursAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView tvnomCours;
        TextView tvnumCours;
        TextView tvcreditCours;
        TextView tvnotePassCours;
        TextView tvmoyenneCours;
        TextView tvresulMinCours;
        TextView tvpointEvalCours;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            tvnomCours = (TextView)itemView.findViewById(R.id.person_name);
            tvnumCours = (TextView)itemView.findViewById(R.id.textView13);
            tvcreditCours = (TextView)itemView.findViewById(R.id.textView18);
            tvnotePassCours = (TextView)itemView.findViewById(R.id.textView20);
            tvmoyenneCours = (TextView)itemView.findViewById(R.id.textView_moyenneCours);
            tvresulMinCours = (TextView)itemView.findViewById(R.id.textView14);
            tvpointEvalCours = (TextView)itemView.findViewById(R.id.textView17);
            itemView.setClickable(true);

        }
    }

    private Context context;
    private List<Cours> mCours;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public CoursAdapter(List<Cours> cours, Context context)
    {
        mCours = cours;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return mCours.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_test, viewGroup, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }



    @Override
    public void onBindViewHolder(ViewHolder personViewHolder, int i) {
        DecimalFormat df = new DecimalFormat("#0.00");
        Double np = mCours.get(i).getNotePassage();
        Double mo = mCours.get(i).getMoyenne();
        Double rm = mCours.get(i).getmResultatMin();

        personViewHolder.tvnomCours.setText(mCours.get(i).getNomCours());
        personViewHolder.tvnumCours.setText(mCours.get(i).getNumCours());
        personViewHolder.tvcreditCours.setText(String.valueOf(mCours.get(i).getListElement().size()));
        personViewHolder.tvnotePassCours.setText(String.valueOf(mCours.get(i).getNotePassage()));


        personViewHolder.tvmoyenneCours.setText(String.valueOf(df.format(mCours.get(i).getMoyenne())));
        if(mo > np)
            personViewHolder.tvmoyenneCours.setTextColor(context.getResources().getColor(R.color.vertAuDessusDeLaNoteDePassage));
        else
            personViewHolder.tvmoyenneCours.setTextColor(context.getResources().getColor(R.color.rougeSousLaNoteDePassage));


        personViewHolder.tvresulMinCours.setText(String.valueOf(df.format(mCours.get(i).getmResultatMin())));
        if(rm > 100)
            personViewHolder.tvresulMinCours.setTextColor(context.getResources().getColor(R.color.rougeSousLaNoteDePassage));
        else
            personViewHolder.tvresulMinCours.setTextColor(context.getResources().getColor(R.color.vertAuDessusDeLaNoteDePassage));


        personViewHolder.tvpointEvalCours.setText(String.valueOf(mCours.get(i).getPointEvalue()) + "/100");

        if(selectedItems.get(i))
            personViewHolder.cv.setBackgroundColor(context.getResources().getColor(R.color.vertAuDessusDeLaNoteDePassage));
//        else
//            personViewHolder.cv.setBackgroundColor(Color.CYAN);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }




    public void toggleSelection(int position) {
        selectView(position, !selectedItems.get(position));
    }




    public void selectView(int position, boolean value) {
        if (value)
            selectedItems.put(position, value);
        else
            selectedItems.delete(position);

        System.out.println(selectedItems.toString());

        notifyDataSetChanged();
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

}
