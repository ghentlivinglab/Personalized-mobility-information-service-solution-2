package vop.groep06.mobiligent.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import vop.groep06.mobiligent.MainActivity;
import vop.groep06.mobiligent.R;


public class NoNetworkFragment extends Fragment {

    View view;

    public NoNetworkFragment () {
        super();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.no_network_fragment, container, false);
        Button button = (Button) view.findViewById(R.id.reloadButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.reload();
            }
        });
        return view;
    }
}
