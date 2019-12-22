package com.example.parkeando.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parkeando.Config.Config;
import com.example.parkeando.R;
import com.google.android.material.textfield.TextInputLayout;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private static final int PAYPAL_REQUEST_CODE = 7777;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId( Config.PAYPAL_CLIENT_ID);




    private static final int SCAN_RESULT = 100;
    private TextView textViewTarjeta;
    private  TextView textViewFecha;
    private Button btnscanner, btnPaypal;
    private EditText etMonto;
    private TextInputLayout inputMonto;

    private String amount = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of ( this ).get ( HomeViewModel.class );
        View root = inflater.inflate ( R.layout.fragment_home, container, false );
       // final TextView textView = root.findViewById ( R.id.text_home );
        homeViewModel.getText ().observe ( this, new Observer<String> () {
            @Override
            public void onChanged(@Nullable String s) {
              //  textView.setText ( s );
            }
        } );


        textViewTarjeta = (TextView) root.findViewById(R.id.textViewTarjeta);
        textViewFecha = (TextView) root.findViewById(R.id.textViewFecha);

        inputMonto = (TextInputLayout) root.findViewById ( R.id.inputMonto );

        btnscanner = (Button) root.findViewById ( R.id.buttonScanner );
        btnPaypal = (Button) root.findViewById ( R.id.btnPaypal );
        etMonto = (EditText) root.findViewById ( R.id.etMonto);

//start paypal service
        Intent intent = new Intent(getContext (),PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        getContext ().startService(intent);
        /////////

        btnscanner.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                //scanearTarjeta ();
                validarMontoTarjeta ();
            }
        } );

        btnPaypal.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
              //  paypal();

                validarMontopaypal ();
            }
        } );





        return root;
    }

    private void validarMontopaypal(){
        String monto = etMonto.getText ().toString ();
        boolean a = esMontoValido(monto);

        if(a){
            paypal ();
        }

    }
    private void validarMontoTarjeta(){
        String monto = etMonto.getText ().toString ();
        boolean a = esMontoValido(monto);

        if(a){
            scanearTarjeta ();
        }

    }


    private boolean esMontoValido(String nombre) {
     if (nombre.isEmpty ()){

         inputMonto.setError("Monto Vacio");
         return false;
     }
     else{

         return true;
     }
    }

    private void paypal() {
        amount = etMonto.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal (String.valueOf(amount)),"MXN",
                "Pago ",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getContext (), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);

    }



    public void  scanearTarjeta(){
        Intent intent = new Intent(getContext (), CardIOActivity.class);
        intent.putExtra( CardIOActivity.EXTRA_REQUIRE_EXPIRY,true);
        intent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV,false);
        intent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE,false);
        startActivityForResult(intent,SCAN_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );

        if(requestCode == SCAN_RESULT){
            if(data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)){
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                textViewTarjeta.setText(scanResult.getRedactedCardNumber());

                if(scanResult.isExpiryValid()){
                    String mes = String.valueOf(scanResult.expiryMonth);
                    String an = String.valueOf(scanResult.expiryYear);
                    textViewFecha.setText(mes + "/" + an);
                }
            }
        }

        if (requestCode == PAYPAL_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null){
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                       /* startActivity(new Intent(this,PaymentDetails.class)
                                .putExtra("PaymentDetails",paymentDetails)
                                .putExtra("Amount",amount)); */
                       Toast.makeText ( getContext (), "Transaccion Exitosa...."+ paymentDetails, Toast.LENGTH_SHORT ).show ();

                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(getContext (), "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(getContext (), "Invalid", Toast.LENGTH_SHORT).show();
//fin de paypay :(
    }


    //para paypal






}