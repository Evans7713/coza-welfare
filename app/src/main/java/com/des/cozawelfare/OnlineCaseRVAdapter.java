package com.des.cozawelfare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OnlineCaseRVAdapter extends RecyclerView.Adapter<OnlineCaseRVAdapter.ViewHolder> implements Filterable {
    // creating variables for our ArrayList and context
    private ArrayList<Request> requestArrayList;
    private ArrayList<Request> requestArrayListFull;
    private Context context;

    public OnlineCaseRVAdapter(ArrayList<Request> requestArrayList,Context context){
            this.requestArrayList = requestArrayList;
            this.context=context;
            this.requestArrayListFull = new ArrayList<>(requestArrayList);
    }
    @NonNull
    @Override
    public OnlineCaseRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            // passing our layout file for displaying our card item
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.online_case_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineCaseRVAdapter.ViewHolder holder, int position){
            // setting data to our text views from our modal class.
            Request request=requestArrayList.get(position);

            holder.nameTv.setText(request.getName());
            holder.surnameTv.setText(request.getSurname());
            holder.dateTv.setText(request.getDate());
            holder.ageTv.setText(request.getAge());
            holder.genderTv.setText(request.getGender());
            holder.maritalStatusTv.setText(request.getMarital_status());
            holder.emailTv.setText(request.getEmail());
            holder.phone_noTv.setText(request.getPhone_no());
            holder.locationTv.setText(request.getLocation());
            holder.requestTv.setText(request.getHelp());
            holder.request_statusTv.setText(request.getRequest_status());
            //Picasso.get().load(request.getImageUrl()).into(holder.img);
            Glide.with(holder.img.getContext()).load(request.getImageUrl()).into(holder.img);

    }
    @Override
    public int getItemCount(){
            // returning the size of our array list.
            return requestArrayList.size();
    }
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public Filter getFilter() {
        return onlinecasefilter;
    }
    private Filter onlinecasefilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Request> filteredlist = new ArrayList<>();
            if(constraint==null|| constraint.length()==0){
                filteredlist.addAll(requestArrayListFull);
            }
            else{
                String pattrn = constraint.toString().toLowerCase().trim();
                for(Request requests : requestArrayListFull){
                    if(requests.getName().toLowerCase().contains(pattrn) || requests.getRequest_status().toLowerCase().contains(pattrn) || requests.getDate().toLowerCase().contains(pattrn)){
                        filteredlist.add(requests);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredlist;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            requestArrayList.clear();
            requestArrayList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final ImageView img;
        private final TextView nameTv;
        private final TextView surnameTv;
        private final TextView dateTv;
        private final TextView ageTv;
        private final TextView genderTv;
        private final TextView maritalStatusTv;
        private final TextView emailTv;
        private final TextView phone_noTv;
        private final TextView locationTv;
        private final TextView requestTv;
        private final TextView request_statusTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            img = itemView.findViewById(R.id.photoIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            surnameTv = itemView.findViewById(R.id.surnameTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            ageTv = itemView.findViewById(R.id.ageTv);
            genderTv = itemView.findViewById(R.id.genderTv);
            maritalStatusTv = itemView.findViewById(R.id.maritalStatusTv);
            emailTv = itemView.findViewById(R.id.emailTv);
            phone_noTv = itemView.findViewById(R.id.phoneTv);
            locationTv = itemView.findViewById(R.id.locationTv);
            requestTv = itemView.findViewById(R.id.requestTv);
            request_statusTv = itemView.findViewById(R.id.request_statusTv);

            // here we are adding on click listener
            // for our item of recycler view.
            // here we are adding on click listener
            // for our item of recycler view.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // after clicking of the item of recycler view.
                    // we are passing our course object to the new activity.
                    Request request = requestArrayList.get(getAdapterPosition());

                    // below line is creating a new intent.
                    Intent i = new Intent(context, UpdateERecords.class);

                    // below line is for putting our course object to our next activity.
                    i.putExtra("request", request);

                    // after passing the data we are starting our activity.
                    context.startActivity(i);

                }
            });

        }
    }

}

