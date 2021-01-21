package vn.edu.usth.flickr_oauth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;

public class MainActivity extends AppCompatActivity {

    private String apiKey = "b73450b170505cfc1020ef349a0a8cb4";
    private String sharedSecret = "679e34e410b67118";
    private Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());
    AuthInterface authInter = flickr.getAuthInterface();
    OAuth1RequestToken requestToken;
    Auth auth;

    Button submit;
    EditText tokenBox;
    TextView tokenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit = (Button) findViewById(R.id.token_submit);
        tokenBox = (EditText) findViewById(R.id.token_box);
        tokenView = (TextView) findViewById(R.id.token_view);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = tokenBox.getText().toString();
                // Do your magic here
                AsyncTask<Void, Void, Void> authTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        OAuth1AccessToken accessToken = (OAuth1AccessToken) authInter.getAccessToken(requestToken, token);
                        try {
                            auth = authInter.checkToken(accessToken);
                        } catch (FlickrException e) {
                            e.printStackTrace();
                        }
                        Log.i("succeed", auth.getUser().getUsername());
                        return null;
                    }
                };
                authTask.execute();
                tokenView.setText(token);
            }
        });

        flickrTask ft = new flickrTask();
        ft.execute();
    }


    private class flickrTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            requestToken = authInter.getRequestToken();
            return authInter.getAuthorizationUrl(requestToken, Permission.DELETE);
        }

        @Override
        protected void onPostExecute(String s) {
            WebView wb  = (WebView) findViewById(R.id.web_view);
            wb.loadUrl(s);
        }
    }
}