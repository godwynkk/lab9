package edu.temple.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

public class BookSearchActivity extends AppCompatActivity {

    EditText urlEditText;
    Button searchButton;
    TextView outputTextView;
    ImageView imageView;
    RequestQueue requestQueue;
    Intent sendIntent;
    JSONArray jsonArray;

    private final String QUERY = "query";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        urlEditText = findViewById(R.id.urlEditText);
        searchButton = findViewById(R.id.searchButton);
        outputTextView = findViewById(R.id.outputTextView);
        imageView = findViewById(R.id.imageView);

        requestQueue = Volley.newRequestQueue(this);

        sendIntent = new Intent(this, MainActivity.class);

        searchButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String urlString = "https://kamorris.com/lab/cis3515/search.php?term=" + urlEditText.getText().toString();

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONArray>(){

                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length() > 0){
                            try {
                                Random random = new Random();
                                jsonArray = response;
                                JSONObject bookObject = response.getJSONObject(random.nextInt(response.length()));
                                outputTextView.setText(bookObject.getString("title"));
                                Picasso.get().load(Uri.parse(bookObject.getString("cover_url"))).into(imageView);

                                Bundle bundle = new Bundle();
                                bundle.putString("jsonArray", response.toString());
                                sendIntent.putExtras(bundle);

                                startActivity(sendIntent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            outputTextView.setText("No search results found");
                        }

                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BookSearchActivity.this, "Woops", Toast.LENGTH_SHORT).show();
                    }
                });

                requestQueue.add(jsonArrayRequest);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

}