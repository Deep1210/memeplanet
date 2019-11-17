package in.incourt.incourtnews.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import in.incourt.incourtnews.R;

import butterknife.BindView;
import butterknife.OnClick;

public class PartnershipActivity extends IncourtActivity {

    @BindView(R.id.sendbtn)
    TextView sendbtn;

    @BindView(R.id.backarrow)
    ImageView backpressbtn;

    EditText name_edit, email_edit, website_edit, phonee_edit, whatsapp_edit;
    boolean reuir_fields = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_partnership);

        name_edit = (EditText) findViewById(R.id.name_edit);
        email_edit = (EditText) findViewById(R.id.email_edit);
        website_edit = (EditText) findViewById(R.id.website_edit);
        phonee_edit = (EditText) findViewById(R.id.phonee_edit);
        whatsapp_edit = (EditText) findViewById(R.id.whatsapp_edit);


    }

    @OnClick(R.id.backarrow)
    void setBackpressbtn() {
        onBackPressed();
    }

    @OnClick(R.id.sendbtn)
    void sendbtnClick() {

        reuir_fields = true;

        String getstring_name, getstring_email, getstring_website, getstring_phone, getstring_whatsapp;

        getstring_name = name_edit.getText().toString();
        getstring_email = email_edit.getText().toString();
        getstring_website = website_edit.getText().toString();
        getstring_phone = phonee_edit.getText().toString();
        getstring_whatsapp = whatsapp_edit.getText().toString();

        if (getstring_name.length() == 0) {
            Toast.makeText(this, "Name is Required", Toast.LENGTH_SHORT).show();
            reuir_fields = false;
        } else if (getstring_email.length() == 0) {
            Toast.makeText(this, "Email is Required", Toast.LENGTH_SHORT).show();
            reuir_fields = false;
        } else if (getstring_website.length() == 0) {
            Toast.makeText(this, "Website is Required", Toast.LENGTH_SHORT).show();
            reuir_fields = false;
        } else {
            Log.d("GetStrings", "getstring_name" + " " + getstring_email + " " + getstring_website + " " + getstring_phone + " " + getstring_phone + " " + getstring_whatsapp);
            Toast.makeText(this, "Successfully", Toast.LENGTH_SHORT).show();
        }

    }

}
