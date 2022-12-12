package com.example.smspackagesender;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smspackagesender.senderPack.Sender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyView> {
    public static int unchecked = 3;

    public boolean isAllChecked = false;
    Context context;
    ArrayList<Sender> senders;
    ArrayList<Sender> copiedSenders = new ArrayList<>();
    List<Integer> positins=new ArrayList<>();

    public RecyclerAdapter(Context context, ArrayList<Sender> senders) {
        this.context = context;
        this.senders = senders;
    }

    public boolean isAllChecked() {
        return isAllChecked;
    }

    public void setAllChecked(boolean allChecked) {
        isAllChecked = allChecked;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.number_item,parent,false);

        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {

        holder.contactName.setText(senders.get(position).getName());
        holder.contactNumber.setText(senders.get(position).getNumber());



        if (!isAllChecked){
            holder.chekedContact.setChecked(false);
            positins.clear();
        }
        else {
            holder.chekedContact.setChecked(true);

            for (int i = 0; i < senders.size(); i++) {
                positins.add(i);
            }
        }



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (positins.size() == senders.size()){
                    unchecked = 0;
                }

                handler.postDelayed(this, 200);
            }
        },1);


    }

    @Override
    public int getItemCount() {
        return senders.size();
    }

    public List<Integer> getPositins(){
        return positins;
    }

    public  void filter(@NonNull String text){
        copiedSenders.addAll(senders);
        senders.clear();
        if (text.isEmpty()){
            senders.addAll(copiedSenders);

        }else {
            text = text.toLowerCase();
            for (Sender s:copiedSenders){
                if (s.getName().toLowerCase().contains(text)){
                    senders.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }



    public class MyView extends RecyclerView.ViewHolder {

        TextView contactName,contactNumber;
        CheckBox chekedContact;
        CardView cardView;



        public MyView(@NonNull View itemView) {
            super(itemView);

            contactName = itemView.findViewById(R.id.name_contact);
            contactNumber = itemView.findViewById(R.id.number_contact);
            chekedContact = itemView.findViewById(R.id.checkBox);
            cardView = itemView.findViewById(R.id.cardviewpage);

        chekedContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chekedContact.isChecked())
                    positins.add(getAdapterPosition());
                else {
                    positins.remove(positins.indexOf(getAdapterPosition()));
                    unchecked = 1;
                }
            }
        });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chekedContact.isChecked()){
                        chekedContact.setChecked(false);
                        positins.remove(positins.indexOf(getAdapterPosition()));
                        unchecked = 1;

                    }else{
                       chekedContact.setChecked(true);
                        positins.add(getAdapterPosition());

                    }
                }
            });
        }


    }
}
