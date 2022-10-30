package com.example.flamezpizzaria;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flamezpizzaria.Models.CardDetails;
import com.example.flamezpizzaria.Models.FeedbackDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AddFeedback extends AppCompatActivity {

    TextView textView1;
    TextView textView2;
    EditText editText3;
    EditText editText4;
    ImageView imageView;
    Button button;
    DatabaseReference ref;
    String id,name,image;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            id = bundle.getString("id");
            name = bundle.getString("name");
            image = bundle.getString("image");
        }

        ref = FirebaseDatabase.getInstance().getReference("FeedbackDetails");

        textView1 = (TextView)findViewById(R.id.pid);
        textView2 = (TextView)findViewById(R.id.pname);
        editText3 = (EditText)findViewById(R.id.cname);
        editText4 = (EditText)findViewById(R.id.cdes);
        button = (Button) findViewById(R.id.submit);
        imageView = (ImageView)findViewById(R.id.imageView);

        textView1.setText(id);
        textView2.setText(name);
        Picasso.get().load(image).into(imageView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String pid = textView1.getText().toString();
                final String pname = textView2.getText().toString();
                final String cname = editText3.getText().toString();
                final String cfeedback = editText4.getText().toString();

                if (pid.isEmpty()) {
                    textView1.setError("Product ID is required");
                }else if (pname.isEmpty()) {
                    textView2.setError("Product Name is required");
                }else if (cname.isEmpty()) {
                    editText3.setError("Customer Name is required");
                }else if (cfeedback.isEmpty()) {
                    editText4.setError("Feedback is required");
                }else {

                    String key = ref.push().getKey();

                    FeedbackDetails feedbackDetails = new FeedbackDetails(key, image, pid, pname, cname, cfeedback);
                    ref.child(key).setValue(feedbackDetails);



                    Toast.makeText(AddFeedback.this, "Successfully added", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddFeedback.this, CustomerHome.class);
                    startActivity(intent);
                }
            }
        });

    }
}