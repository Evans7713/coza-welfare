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

public class MembersRVAdapter extends RecyclerView.Adapter<MembersRVAdapter.ViewHolder>implements Filterable{
    // creating variables for our ArrayList and context
    private ArrayList<Members> membersArrayList;
    private ArrayList<Members> membersArrayListFull;
    private Context context;
    // creating constructor for our adapter class
    public MembersRVAdapter(ArrayList<Members> membersArrayList, Context context) {
        this.membersArrayList = membersArrayList;
        this.context = context;
        this.membersArrayListFull = new ArrayList<>(membersArrayList);
    }

    @NonNull
    @Override
    public MembersRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.member_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MembersRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Members model = membersArrayList.get(position);
        holder.dateTv.setText(model.getDate());
        holder.nameTv.setText(model.getName());
        holder.surnameTv.setText(model.getSurname());
        holder.genderTv.setText(model.getGender());
        holder.birthdayTv.setText(model.getBirthday());
        holder.maritalStatusTv.setText(model.getMarital_status());
        holder.emailTv.setText(model.getEmail());
        holder.phoneTv.setText(model.getPhone_no());
        /*Glide.with(holder.img.getContext()).load(model.getImageUrl()).placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop().error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);*/
        //Picasso.get().load(model.getImageUrl()).into(holder.img);
        Glide.with(holder.img.getContext()).load(model.getImageUrl()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return membersArrayList.size();
    }
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public Filter getFilter() {
        return memberfilter;
    }
    private Filter memberfilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Members> filteredlist = new ArrayList<>();
            if(constraint==null|| constraint.length()==0){
                filteredlist.addAll(membersArrayListFull);
            }
            else{
                String pattrn = constraint.toString().toLowerCase().trim();
                for(Members members : membersArrayListFull){
                    if(members.getName().toLowerCase().contains(pattrn)){
                        filteredlist.add(members);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredlist;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            membersArrayList.clear();
            membersArrayList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img;
        private final TextView dateTv;
        private final TextView nameTv;
        private final TextView surnameTv;
        private final TextView genderTv;
        private final TextView birthdayTv;
        private final TextView maritalStatusTv;
        private final TextView emailTv;
        private final TextView phoneTv;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.photo);
            dateTv = (TextView) itemView.findViewById(R.id.dateTv);
            nameTv = (TextView)itemView.findViewById(R.id.nameTv);
            surnameTv = (TextView)itemView.findViewById(R.id.surnameTv);
            genderTv = (TextView)itemView.findViewById(R.id.genderTv);
            birthdayTv = (TextView)itemView.findViewById(R.id.birthdayTv);
            maritalStatusTv = (TextView)itemView.findViewById(R.id.maritalStatusTv);
            emailTv = (TextView)itemView.findViewById(R.id.emailTv);
            phoneTv = (TextView)itemView.findViewById(R.id.phoneTv);

            // here we are adding on click listener
            // for our item of recycler view.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // after clicking of the item of recycler view.
                    // we are passing our course object to the new activity.
                    Members members = membersArrayList.get(getAdapterPosition());

                    // below line is creating a new intent.
                    Intent i = new Intent(context, UpdateMember.class);

                    // below line is for putting our course object to our next activity.
                    i.putExtra("member", members);

                    // after passing the data we are starting our activity.
                    context.startActivity(i);
                }
            });

        }
    }

}