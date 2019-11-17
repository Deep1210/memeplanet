package in.incourt.incourtnews.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.OnClick;
import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.helpers.FacebookHelper;
import in.incourt.incourtnews.helpers.ImageHelper;
import in.incourt.incourtnews.helpers.UserHelper;

public class UserAccountActivity extends IncourtActivity {

    @BindView(R.id.backarrow)
    ImageView backpressbtn;

    @BindView(R.id.logoutbtn)
    TextView logoutbtn;

    @BindView(R.id.facebook_connect)
    LinearLayout facebook_connect;

    @BindView(R.id.sync_contact)
    LinearLayout sync_contact;

    TextView login_user_name_text;
    ImageView user_profile_image;

    @BindView(R.id.user_account_sync_text)
    TextView user_account_sync_text;

    GoogleSignInClient mGoogleSIgnInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_account);
        setUpUserProfile();
    }

    void setUpUserProfile(){

        if (UserHelper.getFacebookState()) {
                Log.e("Gmail Detail","Error");
                ImageHelper.loadImage(FacebookHelper.profilePictureSrc(UserHelper.getAppUserRegisterId()), (ImageView) findViewById(in.incourt.incourtnews.R.id.user_profile_image));
                login_user_name_text = (TextView) findViewById(R.id.login_user_name_text);
                login_user_name_text.setText(UserHelper.getName());
                findViewById(R.id.user_account_facebook_connect).setVisibility(View.GONE);
        } else {
                findViewById(R.id.user_account_facebook_login_row).setVisibility(View.GONE);
        }

        /*if(UserHelper.getGmailState()) {
            Log.e("Gmail Detail","Error");
            user_profile_image = findViewById(R.id.user_profile_image);
            login_user_name_text = findViewById(R.id.login_user_name_text);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleSIgnInClient = GoogleSignIn.getClient(this, gso);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(UserAccountActivity.this);
            if(acct != null){
                Log.e("Gmail Details",acct.getDisplayName());
                String personName = acct.getDisplayName();
                Uri personPhoto = acct.getPhotoUrl();

                login_user_name_text.setText("Name:"+personName);
                Glide.with(this).load(personPhoto).into(user_profile_image);
            }else{
                Log.e("Gmail Detail","Error");
            }
            logoutbtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    logoutbtnClick1();
                }
            });
        }*/

        if(UserHelper.getContactSyncState()){
            user_account_sync_text = (TextView) findViewById(R.id.user_account_sync_text);
            user_account_sync_text.setText("Synced with ("+UserHelper.getUserPhone()+")");
        }
    }

    @OnClick(R.id.backarrow)
    void setBackpressbtn() {
        onBackPressed();
    }

    @OnClick(R.id.logoutbtn)
    void logoutbtnClick() {
        if(UserHelper.getState()){
            LoginManager.getInstance().logOut();
            UserHelper.logout();
            recreate();
        }
    }

    /*@OnClick(R.id.logoutbtn)
    void logoutbtnClick1(){
        mGoogleSIgnInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UserAccountActivity.this, "Successfully Signed Out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserAccountActivity.this, FacebookLoginActivity.class));
                        finish();
                    }
                });
    }*/

    @OnClick(R.id.facebook_connect)
    void facebook_connectClick() {
        Intent intent = new Intent(getApplicationContext(), FacebookLoginActivity.class);
        intent.putExtra(IncourtApplication.FACEBOOK_LOGIN_FROM_SETTINGS, true);
        startActivityForResult(intent, IncourtApplication.FACEBOOK_LOGIN_ACITVITY_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IncourtApplication.FACEBOOK_LOGIN_ACITVITY_CODE){
           recreate();
        }
    }

    @OnClick(R.id.sync_contact)
    void sync_contactClick() {
        if(!UserHelper.getContactSyncState()){
            Intent intent = new Intent(getApplicationContext(), ConfirmDetailActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        setUpUserProfile();
        super.onResume();
    }
}
