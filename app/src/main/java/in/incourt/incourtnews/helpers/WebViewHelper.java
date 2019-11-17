package in.incourt.incourtnews.helpers;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

import in.incourt.incourtnews.R;
import in.incourt.incourtnews.activities.IncourtActivity;
import in.incourt.incourtnews.helpers.interfaces.NoNetworkHelperInterface;
import in.incourt.incourtnews.others.AdBlocker;

/**
 * Created by bhavan on 3/9/17.
 */

public class WebViewHelper implements NoNetworkHelperInterface {


    View view;
    WebView webView;
    WebView mWebView;
    RelativeLayout web_view_container_main;
    String lastUrl;



    ProgressBar mProgressBar;


    public static WebViewHelper getWebViewHelperInstance(View view){
        WebViewHelper webViewHelper = new WebViewHelper();
        webViewHelper.view = view;
        webViewHelper.setupBackPressButton();
        return webViewHelper;
    }

    public void loadUrl(String url, boolean flag_for_over_ride){

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setMax(100);

        if(NetworkHelper.state()) {
            removeNoNetworkState();
            loadWebView(url);
            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            networkDisable();
        }

    }

    class MyJavaScriptInterface
    {

        public MyJavaScriptInterface(WebView aContentView)
        {
            mWebView = aContentView;
        }

        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(final String html)
        {
            // process the html as needed by the app

            Log.e("Html : ",html);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

            Log.e("Load Data","");
        }
    }

    public void loadBlankPage() {
        if((WebView) view.findViewById(R.id.web_view_container) != null);
            ((WebView) view.findViewById(R.id.web_view_container)).loadUrl("about:blank");
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public void loadWebView(String url){
        if (url != null && url.endsWith(".pdf")) {
            try {
                String urlEncoded = URLEncoder.encode(url, "UTF-8");
                url = "http://docs.google.com/gview?embedded=true&url=" + urlEncoded;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        mWebView = (WebView) view.findViewById(R.id.web_view_container2);
        mWebView.setVisibility(View.GONE);

        webView = (WebView) view.findViewById(R.id.web_view_container);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(mWebView), "HTMLOUT");
        webView.setWebViewClient(new WebViewClient(){
            private Map<String, Boolean> loadedUrls = new HashMap<>();


            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                boolean ad;
                if (!loadedUrls.containsKey(url)) {
                    ad = AdBlocker.isAd(url);
                    loadedUrls.put(url, ad);
                } else {
                    ad = loadedUrls.get(url);
                }
                return ad ? AdBlocker.createEmptyResource() :
                        super.shouldInterceptRequest(view, url);
            }

          /*  @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }*/

            /*@Override
            public void onLoadResource(WebView webView, String url) {
                ((TextView) view.findViewById(in.incourt.incourtnews.R.id.address_bar)).setText(getUrlHost(url));

                super.onLoadResource(webView, url);
            }*/

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                mProgressBar.setVisibility(View.INVISIBLE);
                webView.loadUrl("javascript:window.HTMLOUT.processHTML('<html><body>'+document.getElementsByTagName('article')[0].innerHTML+'</body></html>');");

            }
        });


        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.d("STATUS_INT", String.valueOf(newProgress));
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.loadUrl(url);
    }



    public void networkDisable(){
        web_view_container_main = (RelativeLayout) view.findViewById(in.incourt.incourtnews.R.id.web_view_container_main);
        NetworkHelper.renderNoNetwork(web_view_container_main.getContext(), web_view_container_main, this);
    }

    void setupBackPressButton(){
        view.findViewById(in.incourt.incourtnews.R.id.backarrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            ((IncourtActivity) view.getContext()).onBackPressed();
            }
        });
    }
    String getUrlHost(String urlString){
        try {
            URL makeUrl = new URL(urlString);
            urlString = makeUrl.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return urlString;
    }

    @Override
    public void onClickTryAgain() {
        if(NetworkHelper.state()){
            removeNoNetworkState();
            loadUrl(lastUrl, true);
        }else {
            IncourtToastHelprer.showNoNetwork();
        }
    }

    @Override
    public void onClickSetting() {
        NoNetWorkStateHelper.openSettingActivity();
    }


    void removeNoNetworkState(){
        if(web_view_container_main != null) {
            web_view_container_main.removeView(web_view_container_main.findViewById(in.incourt.incourtnews.R.id.no_network_main_container));
        }
    }

    public IncourtActivity getIncourtActivity(){
        return (IncourtActivity) view.getContext();
    }



}

