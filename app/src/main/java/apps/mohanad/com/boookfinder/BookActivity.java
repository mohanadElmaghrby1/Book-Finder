package apps.mohanad.com.boookfinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

    /*the book title to search for */
    private String book_title;

    /*the book list view to display alist of books */
    private ListView book_listview;

    /*the book api url*/
    private String BOOK_URL="https://www.googleapis.com/books/v1/volumes?maxResults=5&q=";

    /*the empty state textView to indicate no data and no internet */
    private TextView emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_listview);

        //get the book title from intent
        book_title=getIntent().getStringExtra("book_title");
        //get the listView
        book_listview=(ListView)findViewById(R.id.book_listview);
        //get the emptyState textView
        emptyState =(TextView)findViewById(R.id.empty_state);
        //set the listView setEmptyView to be TextView emptyState
        book_listview.setEmptyView(emptyState);

        /*check if the device is connected to network */
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected){
            /*create and execute asyncTasK*/
            BookAsyncTask bookTask = new BookAsyncTask();
            //passing the url and book title to search in the book api books title
            bookTask.execute(BOOK_URL+book_title+"+intitle");
        }
        else{
            //set the emptyState to indicate no connection
            emptyState.setText(R.string.no_internet_message);
        }

    }

    /**
     * update listview with the new data
     * @param booklist : a list to display in listView
     */
    private void updateUi(ArrayList<Book> booklist){
        BookAdapter adapter = new BookAdapter(this,booklist);
        book_listview.setAdapter(adapter);
    }

    private class BookAsyncTask extends AsyncTask<String , Void , ArrayList<Book> > {

        //progress dialgo
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //create and  Show progress dialog before sending http request
            pDialog = new ProgressDialog(BookActivity.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected ArrayList<Book>  doInBackground(String... params) {
            //create a list of books
            ArrayList<Book> result =QueryUtils.fetchListOfBookData(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> data) {
            super.onPostExecute(data);
            //set the emptyState to indicate no books found
            emptyState.setText(R.string.no_book_found_message);
            //update ui
            updateUi(data);
            //close the progress bar
            pDialog.dismiss();
        }
    }
}
