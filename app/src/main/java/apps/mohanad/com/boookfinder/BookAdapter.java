package apps.mohanad.com.boookfinder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mohanad on 05/07/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(@NonNull Context context, @NonNull List<Book> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        final Book book = getItem(position);

        //setting book titile
        TextView bookTitle=(TextView)convertView.findViewById(R.id.book_title);
        bookTitle.setText(book.getTitle());

        //setting book titile
        TextView bookAuthors=(TextView)convertView.findViewById(R.id.book_authors);
        bookAuthors.setText(book.getAuthor());

        RatingBar ratingBar = (RatingBar)convertView.findViewById(R.id.ratting_bar);
        ratingBar.setRating(book.getRate());

        /*get the recipe image and set display to the ui*/
        ImageView img=(ImageView)convertView.findViewById(R.id.book_image);
        String imageUrl=book.getImageUrl();
        //load image from url and dsiplay to ui
        Picasso.with(getContext()).load(imageUrl).into(img);


        return  convertView;
    }
}
