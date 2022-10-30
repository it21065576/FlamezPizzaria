package com.example.flamezpizzaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flamezpizzaria.Models.FeedbackDetails;
import com.example.flamezpizzaria.Models.ProductDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageFeedbacks extends AppCompatActivity {

    Button button;
    ListView listView;
    List<FeedbackDetails> user;
    DatabaseReference ref;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_feedbacks);

        listView = (ListView)findViewById(R.id.listview);

        user = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("FeedbackDetails");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()) {

                    FeedbackDetails feedbackDetails = studentDatasnap.getValue(FeedbackDetails.class);
                    user.add(feedbackDetails);
                }

                MyAdapter adapter = new MyAdapter(ManageFeedbacks.this, R.layout.custom_feedback_details, (ArrayList<FeedbackDetails>) user);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {

        TextView COL1;
        TextView COL2;
        TextView COL3;
        TextView COL4;
        ImageView imageView;
        Button button1;
        Button button2;
    }

    class MyAdapter extends ArrayAdapter<FeedbackDetails> {
        LayoutInflater inflater;
        Context myContext;
        List<FeedbackDetails> user;


        public MyAdapter(Context context, int resource, ArrayList<FeedbackDetails> objects) {
            super(context, resource, objects);
            myContext = context;
            user = objects;
            inflater = LayoutInflater.from(context);
            int y;
            String barcode;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.custom_feedback_details, null);

                holder.COL1 = (TextView) view.findViewById(R.id.Productid);
                holder.COL2 = (TextView) view.findViewById(R.id.ProductName);
                holder.COL3 = (TextView) view.findViewById(R.id.customername);
                holder.COL4 = (TextView) view.findViewById(R.id.customerDescription);
                holder.imageView = (ImageView) view.findViewById(R.id.imageView2);
                holder.button1 = (Button) view.findViewById(R.id.edit);
                holder.button2 = (Button) view.findViewById(R.id.delete);


                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText("Product ID:- "+user.get(position).getProductId());
            holder.COL2.setText("Product Name:- "+user.get(position).getProductName());
            holder.COL3.setText("Customer Name:- "+user.get(position).getCustomerName());
            holder.COL4.setText("Feedback:- "+user.get(position).getCustomerFeedback());
            Picasso.get().load(user.get(position).getImage()).into(holder.imageView);
            System.out.println(holder);

            holder.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("Do you want to delete this item?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    final String idd = user.get(position).getId();
                                    FirebaseDatabase.getInstance().getReference("FeedbackDetails").child(idd).removeValue();
                                    //remove function not written
                                    Toast.makeText(myContext, "Item deleted Successfully !!!", Toast.LENGTH_SHORT).show();

                                }
                            })

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();
                }
            });

            holder.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view1 = inflater.inflate(R.layout.custom_update_feedback_details, null);
                    dialogBuilder.setView(view1);

                    final TextView textView1 = (TextView) view1.findViewById(R.id.pid);
                    final TextView textView2 = (TextView) view1.findViewById(R.id.pname);
                    final EditText editText3 = (EditText) view1.findViewById(R.id.cname);
                    final EditText editText4 = (EditText) view1.findViewById(R.id.cdes);
                    final Button buttonupdate = (Button) view1.findViewById(R.id.submit);
                    final ImageView imageView = (ImageView) view1.findViewById(R.id.imageView);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final String idd = user.get(position).getId();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("FeedbackDetails").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = (String) snapshot.child("productId").getValue();
                            String name = (String) snapshot.child("productName").getValue();
                            String cname = (String) snapshot.child("customerName").getValue();
                            String feedback = (String) snapshot.child("customerFeedback").getValue();
                            String image = (String) snapshot.child("image").getValue();

                            textView1.setText(id);
                            textView2.setText(name);
                            editText3.setText(cname);
                            editText4.setText(feedback);
                            Picasso.get().load(image).into(imageView);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    buttonupdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String code = textView1.getText().toString();
                            String name = textView2.getText().toString();
                            String cname = editText3.getText().toString();
                            String description = editText4.getText().toString();

                            if (code.isEmpty()) {
                                textView1.setError("ID is required");
                            }else if (name.isEmpty()) {
                                textView2.setError("Product Name is required");
                            }else if (cname.isEmpty()) {
                                editText3.setError("customer Name is required");
                            }else if (description.isEmpty()) {
                                editText4.setError("Feedback is required");
                            }else {

                                HashMap map = new HashMap();
                                map.put("productId", code);
                                map.put("productName", name);
                                map.put("customerName", cname);
                                map.put("customerFeedback", description);
                                reference.updateChildren(map);

                                Toast.makeText(ManageFeedbacks.this, "Item Updated successfully !!!", Toast.LENGTH_SHORT).show();

                                alertDialog.dismiss();




                            }
                        }
                    });
                }
            });

            return view;

        }
    }
}