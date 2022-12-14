package com.example.flamezpizzaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.flamezpizzaria.Models.OrderDetails;
import com.example.flamezpizzaria.Models.OrderedItems;
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

// add customer details
public class OrderList extends AppCompatActivity {

    Button button;
    ListView listView;
    List<OrderDetails> user;
    DatabaseReference ref;
    String upprice;
    String pricee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        listView = (ListView) findViewById(R.id.listviewdel);
        button = (Button) findViewById(R.id.gotodelItms);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderList.this, OrderedFoodList.class);
                startActivity(intent);
            }
        });

        user = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("ProductDetails");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()) {

                    OrderDetails orderDetails = studentDatasnap.getValue(OrderDetails.class);
                    user.add(orderDetails);
                }

                MyAdapter adapter = new MyAdapter(OrderList.this, R.layout.custom_order_items, (ArrayList<OrderDetails>) user);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
// view holders
    static class ViewHolder {

        ImageView imageView;
        TextView COL1;
        TextView COL2;
        Button button;
    }

    class MyAdapter extends ArrayAdapter<OrderDetails> {
        LayoutInflater inflater;
        Context myContext;
        List<OrderDetails> user;


        public MyAdapter(Context context, int resource, ArrayList<OrderDetails> objects) {
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
                view = inflater.inflate(R.layout.custom_order_items, null);

                holder.COL1 = (TextView) view.findViewById(R.id.item_name);
                holder.COL2 = (TextView) view.findViewById(R.id.item_price);
                holder.imageView = (ImageView) view.findViewById(R.id.item_image);
                holder.button = (Button) view.findViewById(R.id.ordernow);


                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText(user.get(position).getName());
            holder.COL2.setText(user.get(position).getPrice());
            Picasso.get().load(user.get(position).getImage()).into(holder.imageView);
            System.out.println(holder);

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view = inflater.inflate(R.layout.custom_order_item_user, null);
                    dialogBuilder.setView(view);

                    final TextView textView1 = (TextView) view.findViewById(R.id.item_id);
                    final TextView textView2 = (TextView) view.findViewById(R.id.item_namee);
                    final TextView textView3 = (TextView) view.findViewById(R.id.item_pricee);
                    final ImageView imageView1 = (ImageView) view.findViewById(R.id.item_imagee);
                    final EditText editText1 = (EditText) view.findViewById(R.id.itemcuname);
                    final EditText editText2 = (EditText) view.findViewById(R.id.itemcunic);
                    final EditText editText3 = (EditText) view.findViewById(R.id.itemcucontat);
                    final EditText editText4 = (EditText) view.findViewById(R.id.itemcuaddress);
                    final EditText editText5 = (EditText) view.findViewById(R.id.itemcuqty);
                    final Button buttonAdd = (Button) view.findViewById(R.id.itemuordernow);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final String idd = user.get(position).getId();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ProductDetails").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = (String) snapshot.child("id").getValue();
                            String name = (String) snapshot.child("name").getValue();
                            pricee = (String) snapshot.child("price").getValue();
                            upprice = (String) snapshot.child("updatePrice").getValue();
                            String image = (String) snapshot.child("image").getValue();

                            textView1.setText(id);
                            textView2.setText(name);
                            textView3.setText(upprice);
                            Picasso.get().load(image).into(imageView1);

                            buttonAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders");

                                    final String username = editText1.getText().toString();
                                    final String nic = editText2.getText().toString();
                                    final String contact = editText3.getText().toString();
                                    final String address = editText4.getText().toString();
                                    final String qty = editText5.getText().toString();

                                    String image = snapshot.child("image").getValue().toString();

                                    if (username.isEmpty()) {
                                        editText1.setError("Name is required");
                                    }else if (nic.isEmpty()) {
                                        editText2.setError("NIC is required");
                                    }else if (contact.isEmpty()) {
                                        editText3.setError("Contact Number is required");
                                    }else if (contact.length() > 10) {
                                        editText3.setError("Contact Number must have 10 numbers");
                                    }else if (address.isEmpty()) {
                                        editText4.setError("Address is required");
                                    }
                                    else if (qty.isEmpty()) {
                                        editText5.setError("Quantity is required");
                                    }else {

                                        Integer qtyval = Integer.valueOf(editText5.getText().toString());
                                        String id = textView1.getText().toString();
                                        String name = textView2.getText().toString();
                                        Integer price = Integer.valueOf(upprice);

                                        Integer tax = (price*2) / 100 ;
                                        Integer total = price+tax;
                                        String finalval = String.valueOf(total * qtyval);

                                        String key = ref.push().getKey();

                                        OrderedItems orderedItems = new OrderedItems(key,name, pricee ,finalval,qty,image,username,nic,contact,address);
                                        reference.child(key).setValue(orderedItems);

                                        Toast.makeText(OrderList.this, "Successfully added", Toast.LENGTH_SHORT).show();

                                        alertDialog.dismiss();
                                    }

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });

                }

            });

            return view;

        }
    }
}