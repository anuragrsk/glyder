package glyder.ind.retail.com.glyder;

import android.support.v4.app.Fragment;

/**
 * Created by anuraag on 6/27/16.
 */

public class BaseFragment extends Fragment {
    protected OnFragmentInteractionListener mListener;
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String uri);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
}
