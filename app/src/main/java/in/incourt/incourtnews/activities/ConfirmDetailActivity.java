package in.incourt.incourtnews.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.core.http.RestAdapter;
import in.incourt.incourtnews.core.models.ContactSyncModel;
import in.incourt.incourtnews.core.models.UserLoginModel;
import in.incourt.incourtnews.helpers.ContactHelpers;
import in.incourt.incourtnews.helpers.IncourtLoader;
import in.incourt.incourtnews.helpers.UserHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmDetailActivity extends IncourtActivity {

    @BindView(R.id.backarrow)
    ImageView backarrow;

    @BindView(R.id.submit_button)
    TextView submit_button;

    @BindView(R.id.field_name)
    EditText field_name;

    @BindView(R.id.field_email)
    EditText field_email;

    @BindView(R.id.field_phone_number)
    EditText field_phone;

    @BindView(R.id.contact_sync_error)
    TextView contact_sync_error;

    IncourtLoader incourtLoader;

    ContactHelpers contactHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_detail);
    }



    @OnClick(R.id.backarrow)
    void setBackpressbtn() {
        onBackPressed();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        contactHelpers = new ContactHelpers(this);
    }

    // Check invalid email-->
    public boolean isEmailinValid(String emaill) {
        String regExpn =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        CharSequence inputStr = emaill;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
            return true;
        else
            return false;
    }

    @OnClick(R.id.submit_button)
    void formSubmit(){
        if(validate()){
            incourtLoader = IncourtLoader.init(this).start();
            RestAdapter.get().contactsignup(getRequestParameter()).enqueue(new Callback<UserLoginModel>() {
                @Override
                public void onResponse(Call<UserLoginModel> call, Response<UserLoginModel> response) {
                    UserLoginModel userLoginModel = response.body();
                    userLoginModel.getAppUser().contact_sync = true;
                    UserHelper.login(userLoginModel);
                    contactSync();
                }

                @Override
                public void onFailure(Call<UserLoginModel> call, Throwable t) {
                    incourtLoader.stop();
                }
            });

        }
    }

    HashMap<String, String> getRequestParameter(){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", field_phone.getText().toString());
        hashMap.put("name", field_name.getText().toString());
        hashMap.put("email", field_email.getText().toString());
        return hashMap;
    }

    boolean validate(){

        if(field_name.getText().toString().length() <= 0 ){
            field_name.setError("First Name is Required!");
            return false;
        }

        if(field_email.getText().toString().length() <= 0){
            field_email.setError("Email is Required");
            return false;
        }

        if(!isEmailinValid(field_email.getText().toString())){
            field_email.setError("Invalid Email!");
            return false;
        }

        if(field_phone.getText().toString().length() <= 0){
            field_phone.setError("Phone Number is Required!");
            return false;
        }

        if(!contactHelpers.getState()){
            contactHelpers.contactInit();
            contact_sync_error.setVisibility(View.VISIBLE);
            return false;
        }

        return true;
    }

    void contactSync(){
        RestAdapter.get().contactSync(contactHelpers.getContacts()).enqueue(new Callback<ContactSyncModel>() {
            @Override
            public void onResponse(Call<ContactSyncModel> call, Response<ContactSyncModel> response) {
                finish();
                incourtLoader.stop();
            }

            @Override
            public void onFailure(Call<ContactSyncModel> call, Throwable t) {
                incourtLoader.stop();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == contactHelpers.MY_PERMISSIONS_REQUEST_READ_CONTACTS){
            if(contactHelpers.getState()){
                contact_sync_error.setVisibility(View.INVISIBLE);
                formSubmit();
            }
            else contact_sync_error.setVisibility(View.VISIBLE);
        }
    }


}
