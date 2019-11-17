package in.incourt.incourtnews.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import in.incourt.incourtnews.R;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;

public class Media_BloggersActivity extends IncourtActivity {

    @BindView(R.id.backarrow)
    ImageView backpressbtn;

    @BindView(R.id.partnership_btn)
    TextView partnership_btn;

    @BindView(R.id.copyrightissues_btn)
    TextView copyrightissues_btn;
    Context context = Media_BloggersActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_media__bloggers_);
    }

    @OnClick(R.id.backarrow)
    void setBackpressbtn() {
        onBackPressed();
    }

    @OnClick(R.id.partnership_btn)
    void partnership_btnClick() {
        Intent intent = new Intent(context,PartnershipActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.copyrightissues_btn)
    void copyrightissues_btnClick() {
    }

}
