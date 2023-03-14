package com.des.cozawelfare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CaseRVAdapter extends RecyclerView.Adapter<CaseRVAdapter.ViewHolder> implements Filterable {
    // creating variables for our ArrayList and context
    private ArrayList<Cases> casesArrayList;
    private ArrayList<Cases> casesArrayListFull;
    private Context context;
    // creating constructor for our adapter class
    public CaseRVAdapter(ArrayList<Cases> casesArrayList, Context context) {
        this.casesArrayList = casesArrayList;
        this.context = context;
        this.casesArrayListFull = new ArrayList<>(casesArrayList);
    }

    @NonNull
    @Override
    public CaseRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.case_item, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull CaseRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Cases cases = casesArrayList.get(position);
        holder.dateTV.setText(cases.getDate());
        holder.attendantTV.setText(cases.getAttendant());
        holder.campusTV.setText(cases.getCampus());
        holder.nameTV.setText(cases.getName());
        holder.surnameTV.setText(cases.getSurname());
        holder.ageTV.setText(cases.getAge());
        holder.genderTV.setText(cases.getGender());
        holder.phoneTV.setText(cases.getPhone_no());
        holder.childrenTV.setText(cases.getNo_of_Children());
        holder.maritalStatusTV.setText(cases.getMarital_Status());
        holder.durationTV.setText(cases.getYears_in_COZA());
        holder.bornAgainTV.setText(cases.getBorn_Again());
        holder.bfcTV.setText(cases.getBFC());
        holder.evangelismTV.setText(cases.getEvangelism());
        holder.addressTV.setText(cases.getAddress());
        holder.requestTV.setText(cases.getRequest());
        holder.requestStatusTV.setText(cases.getRequest_Status());
        holder.remarksTV.setText(cases.getRemarks());
        //Picasso.get().load(cases.getImageUrl()).into(holder.img);
        Glide.with(holder.img.getContext()).load(cases.getImageUrl()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return casesArrayList.size();
    }
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public Filter getFilter() {
        return casefilter;
    }
    private Filter casefilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Cases> filteredlist = new ArrayList<>();
            if(constraint==null|| constraint.length()==0){
                filteredlist.addAll(casesArrayListFull);
            }
            else{
                String pattrn = constraint.toString().toLowerCase().trim();
                for(Cases cases : casesArrayListFull){
                    if(cases.getName().toLowerCase().contains(pattrn) || cases.getCampus().toLowerCase().contains(pattrn) || cases.getDate().toLowerCase().contains(pattrn) || cases.getRequest_Status().contains(pattrn)){
                        filteredlist.add(cases);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredlist;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            casesArrayList.clear();
            casesArrayList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final ImageView img;
        private final TextView dateTV;
        private final TextView attendantTV;
        private final TextView campusTV;
        private final TextView nameTV;
        private final TextView surnameTV;
        private final TextView ageTV;
        private final TextView genderTV;
        private final TextView phoneTV;
        private final TextView childrenTV;
        private final TextView maritalStatusTV;
        private final TextView durationTV;
        private final TextView bornAgainTV;
        private final TextView bfcTV;
        private final TextView evangelismTV;
        private final TextView addressTV;
        private final TextView requestTV;
        private final TextView requestStatusTV;
        private final TextView remarksTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            img = (ImageView) itemView.findViewById(R.id.casePhotoIv);
            dateTV = itemView.findViewById(R.id.caseDateTV);
            attendantTV = itemView.findViewById(R.id.caseAttendantTV);
            campusTV = itemView.findViewById(R.id.caseCampusTV);
            nameTV = itemView.findViewById(R.id.caseNameTv);
            surnameTV = itemView.findViewById(R.id.caseSurnameTv);
            ageTV = itemView.findViewById(R.id.caseAgeTv);
            genderTV = itemView.findViewById(R.id.caseGenderTv);
            phoneTV = itemView.findViewById(R.id.casePhoneTv);
            childrenTV = itemView.findViewById(R.id.caseChildrenTv);
            maritalStatusTV = itemView.findViewById(R.id.caseMaritalStatusTv);
            durationTV = itemView.findViewById(R.id.caseDurationTv);
            bornAgainTV = itemView.findViewById(R.id.caseBornAgainTv);
            bfcTV = itemView.findViewById(R.id.caseBFCTv);
            evangelismTV = itemView.findViewById(R.id.caseEvangelismTv);
            addressTV = itemView.findViewById(R.id.caseAddressTv);
            requestTV = itemView.findViewById(R.id.caseRequestTv);
            requestStatusTV = itemView.findViewById(R.id.caseRequestStatusTv);
            remarksTV = itemView.findViewById(R.id.caseRemarksTv);
            // here we are adding on click listener
// for our item of recycler view.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // after clicking of the item of recycler view.
                    // we are passing our course object to the new activity.
                    Cases cases = casesArrayList.get(getAdapterPosition());

                    // below line is creating a new intent.
                    Intent iCase = new Intent(context, UpdateCase.class);

                    // below line is for putting our course object to our next activity.
                    iCase.putExtra("cases", cases);

                    // after passing the data we are starting our activity.
                    context.startActivity(iCase);
                }
            });


        }
    }
}
