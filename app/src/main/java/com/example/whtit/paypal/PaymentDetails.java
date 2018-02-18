package com.example.whtit.paypal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends AppCompatActivity {
    TextView txtid, txtamt, txtstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details2);

        txtid = (TextView) findViewById (R.id.txtid);
        txtamt = (TextView) findViewById(R.id.txtamt);
        txtstatus = (TextView) findViewById(R.id.txtstatus);

        Intent intent = getIntent();
        try {

            JSONObject jsonObject = new JSONObject( intent.getStringExtra("PaymentDetails") );
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showDetails(JSONObject response ,String paymentAmount){

        try{
            txtid.setText( response.getString("id") );
            txtamt.setText(response.getString("state"));
            txtamt.setText(response.getString(String.format("$%s",paymentAmount)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
