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

public class FirstTimerRVAdapter extends RecyclerView.Adapter<FirstTimerRVAdapter.ViewHolder> implements Filterable {
    // creating variables for our ArrayList and context
    private ArrayList<FirstTimer> firstTimerArrayList;
    private ArrayList<FirstTimer> firstTimerArrayListFull;
    private Context context;
    // creating constructor for our adapter class
    public FirstTimerRVAdapter(ArrayList<FirstTimer> firstTimerArrayList, Context context) {
        this.firstTimerArrayList = firstTimerArrayList;
        this.context = context;
        this.firstTimerArrayListFull = new ArrayList<>(firstTimerArrayList);
    }

    @NonNull
    @Override
    public FirstTimerRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.firsttimer_item, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull FirstTimerRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        FirstTimer firstTimer = firstTimerArrayList.get(position);
        holder.dateTV.setText(firstTimer.getDate());
        holder.attendantTV.setText(firstTimer.getAttendant());
        holder.campusTV.setText(firstTimer.getCampus());
        holder.nameTV.setText(firstTimer.getName());
        holder.ageTV.setText(firstTimer.getAge());
        holder.genderTV.setText(firstTimer.getGender());
        holder.phoneTV.setText(firstTimer.getPhone_no());
        holder.addressTV.setText(firstTimer.getAddress());
        holder.membershipTV.setText(firstTimer.getMembership());
        holder.remarksTV.setText(firstTimer.getRemarks());
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return firstTimerArrayList.size();
    }
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public Filter getFilter() {
        return firstfilter;
    }
    private Filter firstfilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<FirstTimer> filteredlist = new ArrayList<>();
            if(constraint==null|| constraint.length()==0){
                filteredlist.addAll(firstTimerArrayListFull);
            }
            else{
                String pattrn = constraint.toString().toLowerCase().trim();
                for(FirstTimer firstTimer : firstTimerArrayListFull){
                    if(firstTimer.getName().toLowerCase().contains(pattrn) || firstTimer.getCampus().toLowerCase().contains(pattrn) || firstTimer.getDate().toLowerCase().contains(pattrn)){
                        filteredlist.add(firstTimer);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredlist;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            firstTimerArrayList.clear();
            firstTimerArrayList.addAll((List)results.values);
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
        private final TextView membershipTV;
        private final TextView addressTV;
        private final TextView remarksTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            dateTV = itemView.findViewById(R.id.dateTV);
            attendantTV = itemView.findViewById(R.id.attendantTV);
            campusTV = itemView.findViewById(R.id.campusTV);
            nameTV = itemView.findViewById(R.id.nameTv);
            ageTV = itemView.findViewById(R.id.ageTv);
            genderTV = itemView.findViewById(R.id.genderTv);
            phoneTV = itemView.findViewById(R.id.phoneTv);
            addressTV = itemView.findViewById(R.id.addressTv);
            membershipTV = itemView.findViewById(R.id.membershipTv);
            remarksTV = itemView.findViewById(R.id.remarksTv);
            // here we are adding on click listener
// for our item of recycler view.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // after clicking of the item of recycler view.
                    // we are passing our course object to the new activity.
                    FirstTimer firstTimer = firstTimerArrayList.get(getAdapterPosition());

                    // below line is creating a new intent.
                    Intent intent = new Intent(context, UpdateFirstTimer.class);

                    // below line is for putting our course object to our next activity.
                    intent.putExtra("firstTimer", firstTimer);

                    // after passing the data we are starting our activity.
                    context.startActivity(intent);
                }
            });


        }
    }
}
