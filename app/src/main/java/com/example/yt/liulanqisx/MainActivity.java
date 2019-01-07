package com.example.yt.liulanqisx;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends Activity {

    AutoCompleteTextView autotext_url;
    WebView web_show;
    private Button closeBtn=null;

    String[] booksArray = new String[]
            {
                    "http://maps.google.com",
                    "http://maps.baidu.com",
                    "http://qq.com",
                    "www.baidu.com",
                    "www.163.com"
            };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Activity activity = this;

        web_show=findViewById(R.id.web_show);
        web_show.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(WebView view, String strUrl)
            {
                view.loadUrl(strUrl);
                autotext_url.setText(strUrl);
                return false;
            }

            public void onPageStarted(WebView view, String strUrl, Bitmap favicon)
            {
                super.onPageStarted(view, strUrl, favicon);
                autotext_url.setText(strUrl);
            }

            public void onPageFinished(WebView view, String strUrl)
            {
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });


        autotext_url = (AutoCompleteTextView)findViewById(R.id.autotext_url);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, booksArray);
        autotext_url.setAdapter(aa);
        autotext_url.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent ev)
            {
                if (keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    String strUrl = autotext_url.getText().toString();

                    Pattern p = Pattern.compile("http://([\\w-]+\\.)+[\\w-]+(/[\\w-\\./?%=]*)?");
                    Matcher m = p.matcher(strUrl);
                    if (!m.find())
                    {
                        strUrl = "http://" + strUrl;
                    }

                    web_show.loadUrl(strUrl);

                    return true;
                }

                return false;
            }
        });

        // button
        final Button backBtn=findViewById(R.id.btn_back);
        final Button forwardBtn=findViewById(R.id.btn_forward);
        final Button closebtn=findViewById(R.id.btn_close);
        Button refreshBtn=findViewById(R.id.btn_refresh);
        Button homeBtn=findViewById(R.id.btn_home);
        backBtn.setEnabled(false);
        forwardBtn.setEnabled(false);


        backBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                web_show.goBack();
            }
        });

        forwardBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // TODO
                web_show.goForward();
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // TODO
                String strUrl = autotext_url.getText().toString();
                web_show.loadUrl(strUrl);
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // TODO
                web_show.loadUrl("http://www.baidu.com");
            }
        });
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn=(Button) v;
                finish();
                System.exit(0);

            }
        });




        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                if (msg.what == 0x1111)
                {
                    // whether can go back
                    if (web_show.canGoBack())
                    {
                        backBtn.setEnabled(true);
                    }
                    else
                    {
                        backBtn.setEnabled(false);
                    }

                    // whether can go forward
                    if (web_show.canGoForward())
                    {
                        forwardBtn.setEnabled(true);
                    }
                    else
                    {
                        forwardBtn.setEnabled(false);
                    }
                }

                super.handleMessage(msg);
            }
        };

        // create thread to change button states
        new Timer().schedule(new TimerTask()
        {
            public void run()
            {
                Message msg = new Message();
                msg.what = 0x1111;
                handler.sendMessage(msg);
            }
        }, 0, 100);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web_show.canGoBack())
        {
            web_show.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return super.onCreateOptionsMenu(menu);

    }

}
