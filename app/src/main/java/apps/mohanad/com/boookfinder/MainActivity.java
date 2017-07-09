package apps.mohanad.com.boookfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    /*the search text to get book title from user*/
    private EditText searchView;
    //string to store book title
    private String book_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * create new intent with a list of
     * related books to the book title
     *
     * @param view
     */
    public void search(View view) {

        //get the book title
        searchView = (EditText) findViewById(R.id.search_viewr);
        book_title = searchView.getText().toString();

        //create new intent
        Intent intent = new Intent(this, BookActivity.class);
        intent.putExtra("book_title", book_title);
        startActivity(intent);
    }

}
