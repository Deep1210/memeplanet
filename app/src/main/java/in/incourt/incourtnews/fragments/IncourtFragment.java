package in.incourt.incourtnews.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import in.incourt.incourtnews.helpers.ThemeHelper;

import butterknife.ButterKnife;

/**
 * Created by bhavan on 12/28/16.
 */

public class IncourtFragment extends Fragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        this.getActivity().setTheme(ThemeHelper.active(getContext()));
        super.onViewCreated(view, savedInstanceState);
    }

}
