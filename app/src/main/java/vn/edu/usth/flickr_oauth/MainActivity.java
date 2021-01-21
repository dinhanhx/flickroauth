package vn.edu.usth.flickr_oauth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.github.scribejava.core.model.OAuth1RequestToken;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private String apiKey = "b73450b170505cfc1020ef349a0a8cb4";
    private String sharedSecret = "679e34e410b67118";
    private Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flickrTask ft = new flickrTask();
        ft.execute();
    }

    public void sendMessage(View view) {
        EditText tokenBox = (EditText) view.findViewById(R.id.token_box);
        String token = tokenBox.getText().toString();

        TextView userInfo = (TextView) view.findViewById(R.id.user_info);
        userInfo.setText(token);
    }

    private class flickrTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            AuthInterface auth = flickr.getAuthInterface();
            OAuth1RequestToken requestToken = auth.getRequestToken();
            return auth.getAuthorizationUrl(requestToken, Permission.DELETE);
        }

        @Override
        protected void onPostExecute(String s) {
            WebView wb  = (WebView) findViewById(R.id.web_view);
            wb.loadUrl(s);
        }
    }
}