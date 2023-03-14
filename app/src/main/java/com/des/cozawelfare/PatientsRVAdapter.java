package com.des.cozawelfare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PatientsRVAdapter extends RecyclerView.Adapter<PatientsRVAdapter.ViewHolder> implements Filterable {
    // creating variables for our ArrayList and context
    private ArrayList<Patient> patientArrayList;
    private ArrayList<Patient> patientArrayListFull;
    private Context context;
    // creating constructor for our adapter class
    public PatientsRVAdapter(ArrayList<Patient> patientArrayList, Context context) {
        this.patientArrayList = patientArrayList;
        this.context = context;
        this.patientArrayListFull = new ArrayList<>(patientArrayList);
    }

    @NonNull
    @Override
    public PatientsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.patient_item, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull PatientsRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Patient patient = patientArrayList.get(position);
        holder.dateTV.setText(patient.getDate());
        holder.attendantTV.setText(patient.getAttendant());
        holder.campusTV.setText(patient.getCampus());
        holder.nameTV.setText(patient.getName());
        holder.ageTV.setText(patient.getAge());
        holder.genderTV.setText(patient.getGender());
        holder.phoneTV.setText(patient.getPhone_no());
        holder.complainTV.setText(patient.getComplain());
        holder.diagnosisTV.setText(patient.getDiagnosis());
        holder.treatmentTV.setText(patient.getTreatment());
        holder.remarksTV.setText(patient.getRemarks());
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return patientArrayList.size();
    }
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public Filter getFilter() {
        return patientfilter;
    }
    private Filter patientfilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Patient> filteredlist = new ArrayList<>();
            if(constraint==null|| constraint.length()==0){
                filteredlist.addAll(patientArrayListFull);
            }
            else{
                String pattrn = constraint.toString().toLowerCase().trim();
                for(Patient patient : patientArrayListFull){
                    if(patient.getName().toLowerCase().contains(pattrn) || patient.getCampus().toLowerCase().contains(pattrn) || patient.getDate().toLowerCase().contains(pattrn)){
                        filteredlist.add(patient);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredlist;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            patientArrayList.clear();
            patientArrayList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView dateTV;
        private final TextView attendantTV;
        private final TextView campusTV;
        private final TextView nameTV;
        private final TextView ageTV;
        private final TextView genderTV;
        private final TextView phoneTV;
        private final TextView complainTV;
        private final TextView diagnosisTV;
        private final TextView treatmentTV;
        private final TextView remarksTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            dateTV = itemView.findViewById(R.id.dateTv);
            attendantTV = itemView.findViewById(R.id.attendantTV);
            campusTV = itemView.findViewById(R.id.campusTV);
            nameTV = itemView.findViewById(R.id.nameTv);
            ageTV = itemView.findViewById(R.id.ageTv);
            genderTV = itemView.findViewById(R.id.genderTv);
            phoneTV = itemView.findViewById(R.id.phoneTv);
            complainTV = itemView.findViewById(R.id.complainTv);
            diagnosisTV = itemView.findViewById(R.id.diagnosisTv);
            treatmentTV = itemView.findViewById(R.id.treatmentTv);
            remarksTV = itemView.findViewById(R.id.remarksTv);
            // here we are adding on click listener
// for our item of recycler view.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // after clicking of the item of recycler view.
                    // we are passing our course object to the new activity.
                    Patient patient = patientArrayList.get(getAdapterPosition());

                    // below line is creating a new intent.
                    Intent i = new Intent(context, UpdatePatient.class);

                    // below line is for putting our course object to our next activity.
                    i.putExtra("patient", patient);

                    // after passing the data we are starting our activity.
                    context.startActivity(i);
                }
            });


        }
    }
}
