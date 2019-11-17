package in.incourt.incourtnews.helpers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import in.incourt.incourtnews.IncourtApplication;
import in.incourt.incourtnews.activities.IncourtActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by bhavan on 12/26/16.
 */

public class ContactHelpers {

    IncourtActivity activity;
    public static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 432;

    public ContactHelpers(IncourtActivity incourtActivity) {
        this.activity = incourtActivity;
        this.contactInit();
    }

    public void contactInit() {
        if (this.checkContactPermission() == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    public boolean getState(){
        return (this.checkContactPermission() == PackageManager.PERMISSION_GRANTED)? true: false;
    }

    public int checkContactPermission() {
        return ContextCompat.checkSelfPermission(this.activity, Manifest.permission.READ_CONTACTS);
    }


    public Map<String, List<HashMap<String, String>>> getContacts() {


        Map<String, List<HashMap<String, String>>> hashMaps = new HashMap<>();

        Map<String, List<HashMap<String, String>>> contacts = new HashMap<>();
        List<HashMap<String, String>> arrayList = new ArrayList<>();

        Cursor phones = IncourtApplication.getIncourtContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        int total = phones.getCount();
        while (phones.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            );
            arrayList.add(phones.getPosition(), hashMap);
        }
        contacts.put("contacts", arrayList);
        return contacts;

    }

    public String getUserPhone() {
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager) this.activity.getSystemService(Context.TELEPHONY_SERVICE);
        String phnNo = mTelephonyMgr.getLine1Number();
        return phnNo;
    }



}
