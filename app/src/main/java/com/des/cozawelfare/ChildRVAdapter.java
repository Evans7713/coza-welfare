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

public class ChildRVAdapter  extends RecyclerView.Adapter<ChildRVAdapter.ViewHolder> implements Filterable{
    // creating variables for our ArrayList and context
    private ArrayList<Children> childrenArrayList;
    private ArrayList<Children> childrenArrayListFull;
    Context context;


    // creating constructor for our adapter class
    public ChildRVAdapter(ArrayList<Children> childrenArrayList, Context context) {
        this.childrenArrayList = childrenArrayList;
        this.context = context;
        this.childrenArrayListFull = new ArrayList<>(childrenArrayList);
    }

    @NonNull
    @Override
    public ChildRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.child_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ChildRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Children children = childrenArrayList.get(position);
        holder.childNameTV.setText(children.getName());
        holder.familyNameTV.setText(children.getSurname());
        holder.ageTV.setText(children.getAge());
        holder.genderTV.setText(children.getGender());
        holder.phoneTV.setText(children.getPhone_no());
        holder.cardTV.setText(children.getCard_no());
        holder.clockInStateTV.setText(children.getClockInState());
        holder.clockOutStateTV.setText(children.getClockOutState());
        holder.dateTV.setText(children.getDate());
        holder.attendantTV.setText(children.getAttendant());
        holder.campusTV.setText(children.getCampus());
        //Picasso.get().load(children.getImageUrl()).into(holder.img);
        Glide.with(holder.img.getContext()).load(children.getImageUrl()).into(holder.img);

    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return childrenArrayList.size();
    }
    public long getItemId(int position)
    {
        return position;
    }
    @Override
    public Filter getFilter() {
        return childfilter;
    }
    private Filter childfilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Children> filteredlist = new ArrayList<>();
            if(constraint==null|| constraint.length()==0){
                filteredlist.addAll(childrenArrayListFull);
            }
            else{
                String pattrn = constraint.toString().toLowerCase().trim();
                for(Children children : childrenArrayListFull){
                    if(children.getName().toLowerCase().contains(pattrn) || children.getCard_no().toLowerCase().contains(pattrn)|| children.getCampus().toLowerCase().contains(pattrn) || children.getDate().toLowerCase().contains(pattrn)){
                        filteredlist.add(children);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredlist;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            childrenArrayList.clear();
            childrenArrayList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };



    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final ImageView img;
        private final TextView childNameTV;
        private final TextView familyNameTV;
        private final TextView ageTV;
        private final TextView genderTV;
        private final TextView phoneTV;
        private final TextView cardTV;
        private final TextView clockInStateTV;
        private final TextView clockOutStateTV;
        private final TextView dateTV;
        private final TextView attendantTV;
        private final TextView campusTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            img = (ImageView) itemView.findViewById(R.id.click_image);
            childNameTV = itemView.findViewById(R.id.name);
            familyNameTV = itemView.findViewById(R.id.familyName);
            ageTV = itemView.findViewById(R.id.age);
            genderTV = itemView.findViewById(R.id.gender);
            phoneTV = itemView.findViewById(R.id.phone);
            cardTV = itemView.findViewById(R.id.card);
            clockInStateTV = itemView.findViewById(R.id.clockIn);
            clockOutStateTV = itemView.findViewById(R.id.clockOut);
            dateTV = itemView.findViewById(R.id.dateTV);
            attendantTV = itemView.findViewById(R.id.attendantTV);
            campusTV = itemView.findViewById(R.id.campusTV);
            // here we are adding on click listener
// for our item of recycler view.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // after clicking of the item of recycler view.
                    // we are passing our course object to the new activity.
                    Children children = childrenArrayList.get(getAdapterPosition());

                    // below line is creating a new intent.
                    Intent i = new Intent(context, UpdateChild.class);

                    // below line is for putting our course object to our next activity.
                    i.putExtra("child", children);

                    // after passing the data we are starting our activity.
                    context.startActivity(i);
                }
            });

        }
    }

}
