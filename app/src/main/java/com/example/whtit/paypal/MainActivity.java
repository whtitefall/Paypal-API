package com.example.whtit.paypal;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;



public class MainActivity extends AppCompatActivity {

    private static final int PAYPAL_REQUEST_CODE  = 7171;


    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    Button btpay;
    EditText edamount;
    String amount ;

    protected void onDestroy(){

        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);


        btpay= (Button) findViewById(R.id.paybt);
        edamount= (EditText) findViewById(R.id.payamt);




        btpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processpayment();
            }
        });



    }

    private void processpayment() {
        amount= edamount.getText().toString();
        PayPalPayment palPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD", "Donate" ,PayPalPayment.PAYMENT_INTENT_SALE );
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,palPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }
    protected void onActivityResult (int requestcode, int resultcode, Intent data){

                if (requestcode == PAYPAL_REQUEST_CODE){

                    if (resultcode == RESULT_OK){

                        PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                        if (confirmation != null){

                            try{
                                String paymentDetails = confirmation.toJSONObject().toString(4);
                                startActivity(new Intent (this, PaymentDetails.class)

                                        .putExtra("Payment Details",paymentDetails)
                                        .putExtra("Payment Amount",amount)  ) ;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (resultcode == Activity.RESULT_CANCELED ){
                            Toast.makeText(this,"cancel ", Toast.LENGTH_SHORT).show();
                        }else  if (resultcode == PaymentActivity.RESULT_EXTRAS_INVALID){
                            Toast.makeText(this,"invalild",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
    }

}
