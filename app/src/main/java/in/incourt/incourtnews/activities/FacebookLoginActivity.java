package in.incourt.incourtnews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import in.incourt.incourtnews.R;
import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.helpers.FacebookHelper;
import in.incourt.incourtnews.helpers.IncourtToastHelprer;
import in.incourt.incourtnews.helpers.NetworkHelper;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class FacebookLoginActivity extends IncourtActivity {


    @BindView(R.id.facebookconnectbtn)
    ImageView facebookconnectbtn;
    LinearLayout facebookview, wireview;

    @BindView(R.id.facebook_login_cancel)
    TextView facebook_login_cancel;

    @BindView(R.id.gmailconnectbtn)
    ImageView gmailconnectbtn;

    //int RC_SIGN_IN = 0;

    //GoogleSignInClient mGoogleSignInClient;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_facebook_login);

        facebookview = (LinearLayout) findViewById(R.id.facebookview);
        wireview = (LinearLayout) findViewById(R.id.wireview);

        callbackManager = CallbackManager.Factory.create();
        FacebookHelper.setupFaceBookInit(FacebookLoginActivity.this, callbackManager);


        callbackManager = CallbackManager.Factory.create();
        FacebookHelper.setupFaceBookInit(this, callbackManager);

        gmailconnectbtn = findViewById(R.id.gmailconnectbtn);

        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);*/

        gmailconnectbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                IncourtToastHelprer.showToast("Coming Soon...");
                //gmailLogin();

            }
        });
    }

    /*private void gmailLogin(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IncourtApplication.FACEBOOK_LOGIN_CONFIRM_CODE) finish();
        callbackManager.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }*/
    }

    /*private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            startActivity(new Intent(FacebookLoginActivity.this, UserAccountActivity.class));
        } catch (ApiException e){
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(FacebookLoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }*/

    /*@Override
    protected void onStart(){
        //LoginManager.getInstance().logInWithReadPermissions(FacebookLoginActivity.this, Arrays.asList("public_profile", "user_friends"));
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            startActivity(new Intent(FacebookLoginActivity.this, UserAccountActivity.class));
        }
        super.onStart();
    }*/


    @OnClick(R.id.facebookconnectbtn)
    void facebookconnectbtnClick() {

        if(!NetworkHelper.state()){
            Toast.makeText(this, "No Internet, please check your connection!", Toast.LENGTH_SHORT).show();
        }else {
            LoginManager.getInstance().logInWithReadPermissions(FacebookLoginActivity.this, Arrays.asList("public_profile", "user_friends"));
        }

    }

    @OnClick(R.id.facebook_login_cancel)
    void facebookLoginCancel(){
        finish();
    }

}
