package com.sakshi.youtubecommentfetcherapp;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final static String expression = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
    EditText videiId;
    Button searchBtn;
    LinearLayout linearLayout;
    TextView textView;
    ListView superListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);


        videiId = findViewById( R.id.ytid );
        linearLayout = findViewById( R.id.idLLsearch );
        superListView = findViewById( R.id.superListView);
        textView = findViewById( R.id.view_text );

        searchBtn = findViewById( R.id.search );
        searchBtn.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                // checking if our editText field is empty or not.
                if (videiId.getText().toString().isEmpty()) {
                    videiId.setError( "Please enter search query" );
                    return;
                }

                // if listView will be shown then, below code will be used to hide actionbar
                if (getSupportActionBar() != null) {
                    getSupportActionBar().hide();
                }
                // to make linearlayout used for searching invisible so that listView can be shown on the screen
                linearLayout.setVisibility( View.INVISIBLE );

                // To make " List of Books [Book Name entered in editText]" visible
                textView.setVisibility( View.VISIBLE );

                // To show " List of Books [Book Name entered in editText]" text on the top of listView
                textView.setText( "List of Comments" );

                // To make it Visible before showing list of books in ListView
                superListView.setVisibility( View.VISIBLE );

                String id = getVideoId( videiId.getText().toString() );
                // if the search query is not empty then we are
                // calling getBookInfo method to load all
                // the books from the API
                getSuperHeroes( id );


            }
        });
    }


    public static String getVideoId(String videoUrl) {
        if (videoUrl == null || videoUrl.trim().length() <= 0){
            return null;
        }
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(videoUrl);
        try {
            if (matcher.find())
                return matcher.group();
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return null;
    }



    private void getSuperHeroes(String id) {
        Call<Results> call = RetrofitClient.getInstance().getMyApi().getsuperHeroes("AIpaSyAbfxyKBnnSKsBho6k8C2OSFdJNk8NAy9w", "plainText", "snippet", 50, id);
        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                List<ItemsArray> myitemsList = response.body().getUserArray();
                String[] commentssss = new String[myitemsList.size()];
                for (int i = 0; i < myitemsList.size(); i++) {
                    Snippet obj = myitemsList.get( i ).getSnippet();
                    TopComments comments = obj.getComment();
                    Snippetii snippetii = comments.getSnippetii();
                    String res = snippetii.getTextDisplay();
                    commentssss[i] = res;
                }
                superListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, commentssss));
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Toast.makeText( getApplicationContext(), t.toString(), Toast.LENGTH_LONG ).show();
            }

        });


    }


}