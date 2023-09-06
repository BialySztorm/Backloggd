package com.example.backloggd;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.view.KeyEvent;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Włącz obsługę JavaScript
        webView.loadUrl("https://backloggd.com"); // Załaduj stronę backloggd.com

        // Ukryj ActionBar w tej aktywności
        getSupportActionBar().hide();

        // Konfiguracja obsługi nawigacji wewnątrz WebView
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Obsłuż nawigację wewnątrz WebView
                view.loadUrl(url);
                return true;
            }
        });
    }
    // Obsługa przycisku cofnij w WebView
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}