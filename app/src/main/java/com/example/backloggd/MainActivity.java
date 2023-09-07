package com.example.backloggd;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.view.KeyEvent;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Sprawdź, czy jest pierwsze uruchomienie
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            showExternalLinkDialog();
        }

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
                boolean externalLinksEnabled = sharedPreferences.getBoolean("externalLinksEnabled", false);

                if (!externalLinksEnabled) {
                    // Obsłuż nawigację wewnątrz WebView
                    view.loadUrl(url);
                } else {
                    // Otwórz zewnętrzną przeglądarkę
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
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

    // Pokaż dialog z pytaniem o otwieranie zewnętrznych linków
    private void showExternalLinkDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Opening external links")
                .setMessage("Do you want to open external links outside of the application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ustaw odpowiedź na "Tak" w pamięci podręcznej
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("externalLinksEnabled", true);
                        editor.putBoolean("isFirstRun", false);
                        editor.apply();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ustaw odpowiedź na "Nie" w pamięci podręcznej
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("externalLinksEnabled", false);
                        editor.putBoolean("isFirstRun", false);
                        editor.apply();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // Domyślnie ustaw odpowiedź na "Nie" w pamięci podręcznej
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("externalLinksEnabled", false);
                        editor.putBoolean("isFirstRun", false);
                        editor.apply();
                    }
                })
                .show();
    }
}