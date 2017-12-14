package in.hoptec.kotlin101;

import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by shivesh on 14/12/17.
 */

public class tmp {
    public tmp()
    {

        DatabaseReference db=null;
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dc:dataSnapshot.getChildren()
                     ) {

                    LinearLayout vv;
                    View z=null;
                    vv=(LinearLayout)z;

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
