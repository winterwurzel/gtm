package at.android.gm.guessthemovie;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by georg on 16-Nov-15.
 */
public class ButtonAdapter extends BaseAdapter {
    private Context mContext;
    private String rndmTitle;

    public ButtonAdapter(Context c, String rndmTitle) {
        mContext = c;
        this.rndmTitle = rndmTitle;
    }

    @Override
    public int getCount() {
        return rndmTitle.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Button myButton;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            myButton = new Button(mContext);
            myButton.setLayoutParams(new GridView.LayoutParams(150, 150));
            myButton.setPadding(8, 8, 8, 8);
        } else {
            myButton = (Button) convertView;
        }

        myButton.setText(rndmTitle.substring(position, position+1));
        myButton.setId(position);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),
                        "Button clicked index = " + myButton.getId(), Toast.LENGTH_SHORT)
                        .show();
            }
        });

        return myButton;
    }
}

