package glyder.ind.retail.com.glyder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;



public class AddAccount extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView cardNumber1;
    private TextView cardNumber2;
    private TextView cardNumber3;
    private TextView cardNumber4;
    private TextView month;
    private TextView year;
    private Button addCard;
    private String cardNumber;
    private OnFragmentInteractionListener mListener;
    private User user;
    private DataBaseHelper db;
    private FragmentManager context;
    public AddAccount(User userInc) {
        this.user=userInc;
        // Required empty public constructor
    }

    public AddAccount() {

        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardNumber1 = (TextView) view.findViewById(R.id.cardNumber1);
        cardNumber2 = (TextView) view.findViewById(R.id.cardNumber2);
        cardNumber3 = (TextView) view.findViewById(R.id.cardNumber3);
        cardNumber4 = (TextView) view.findViewById(R.id.cardNumber4);
        month = (TextView) view.findViewById(R.id.addCardMonth);
        year = (TextView) view.findViewById(R.id.addCardYear);
        addCard = (Button) view.findViewById(R.id.addCardBtn);

        cardNumber1.addTextChangedListener(new MyTextWatcher(cardNumber1, cardNumber2, true, 4));
        cardNumber2.addTextChangedListener(new MyTextWatcher(cardNumber2, cardNumber3, false, 4));
        cardNumber3.addTextChangedListener(new MyTextWatcher(cardNumber3, cardNumber4, false, 4));
        cardNumber4.addTextChangedListener(new MyTextWatcher(cardNumber4, month, false, 4));
        month.addTextChangedListener(new MyTextWatcher(month, year, false, 2));
        db= new DataBaseHelper(view.getContext());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Card");
        context=getActivity().getSupportFragmentManager();
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardNumber1.setError(null);
                cardNumber2.setError(null);
                cardNumber3.setError(null);
                cardNumber4.setError(null);
                month.setError(null);
                year.setError(null);
                if (isValidCard() && month!=null
                        && year!=null && year.getText()!=null && year.getText().toString()!=null && isYearValid(year.getText().toString())
                        && month.getText()!=null &&isMonthValid(month.getText().toString())) {
                    cardNumber = cardNumber1.getText().toString() + cardNumber2.getText().toString() + cardNumber3.getText().toString() + cardNumber4.getText().toString();
                    user.setCardNumber(cardNumber);
                    user.setExpireDate(month.getText().toString()+"/"+year.getText().toString());
                    UserAddCardTask userAddCardTask = new UserAddCardTask(user);
                    userAddCardTask.execute((Void) null);
                } else {
                    cardNumber1.setError("Please correct card information..");
                    cardNumber1.requestFocus();
                }


            }

            private boolean isValidCard() {
                int length = 0;
                if (cardNumber1 != null && cardNumber1.getText() != null && cardNumber1.getText().length() > 0)
                    length = length + cardNumber1.getText().length();
                if (cardNumber2 != null && cardNumber2.getText() != null && cardNumber2.getText().length() > 0)
                    length = length + cardNumber2.getText().length();
                if (cardNumber3 != null && cardNumber3.getText() != null && cardNumber3.getText().length() > 0)
                    length = length + cardNumber3.getText().length();
                if (cardNumber4 != null && cardNumber4.getText() != null && cardNumber4.getText().length() > 0)
                    length = length + cardNumber4.getText().length();
                return (length == 16);
            }
        });
    }

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        android.app.ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add Card");
            // actionBar.show();
        }        return inflater.inflate(R.layout.fragment_add_account, container, false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(String uri);
//    }

    public  boolean validateCardNumber(String paramString){
        int i = 0;
        int j = 0;
        int k = -1 + paramString.length();
        if (k >= 0)
        {
            int m = Integer.parseInt(paramString.substring(k, k + 1));
            if (j != 0)
            {
                m *= 2;
                if (m > 9)
                    m = 1 + m % 10;
            }
            i += m;
            if (j == 0);
            for (j = 1; ; j = 0)
            {
                k--;
                break;
            }
        }
        return i % 10 == 0;
    }

    private  boolean isYearValid(String year)
    {
        if(year!=null && year.trim().length()>0) {
            int i = Calendar.YEAR;
            int j = -2000 + Calendar.getInstance().get(i);
            if ((Integer.parseInt(year) < j))
                return false;
            return true;
        }else{
            return false;
        }


    }


    private boolean isMonthValid(String month){
        if(month!=null && month.trim().length()>0) {
            int j = Integer.parseInt(month);
            if ((j < 1) || (j > 12))
                return false;
            return true;
        }else{
            return false;
        }
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    class MyTextWatcher implements TextWatcher {
        private TextView first;
        private TextView next;
        private boolean changeIcon;
        private int noOfChar;

        MyTextWatcher(TextView first, TextView next, boolean changeIcon, int noOfChar) {
            this.first = first;
            this.next = next;
            this.changeIcon = changeIcon;
            this.noOfChar = noOfChar;
        }

        public void afterTextChanged(Editable paramAnonymousEditable) {
        }

        public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
        }

        public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
            if (paramAnonymousCharSequence.toString().length() == noOfChar)
                next.requestFocus();
            ImageView localImageView = (ImageView) getActivity().findViewById(R.id.imgVisaOrMaster);
            if (paramAnonymousCharSequence.toString().startsWith("4") && changeIcon) {
                localImageView.setImageResource(R.drawable.iconvisa);
                return;
            } else if (paramAnonymousCharSequence.toString().startsWith("5") && changeIcon) {
                localImageView.setImageResource(R.drawable.mastercard);
                return;
            } else if (changeIcon) {
                localImageView.setImageResource(R.drawable.iconvisamaster);
            }
        }
    }

        public class UserAddCardTask extends AsyncTask<Void, Void, Boolean> {


            ProgressDialog progress = new ProgressDialog(getActivity());
            private User uInc;

            UserAddCardTask(User user1) {
                this.uInc=user1;

                //ProgressDialog.show(getApplicationContext(), "Login", "Verifying...");
            }

            protected void onPreExecute() {
                // progress.setTitle("Add Card");
                //   progress.setMessage("Please wait...");
                progress.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                // TODO: attempt authentication against a network service.

                try {
                    return db.createAccount(user);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }

                // TODO: register the new account here.
               // return true;
            }

            @Override
            protected void onPostExecute(final Boolean success) {

                // showProgress(false);
                progress.dismiss();

                if (success) {

                    Intent intent=new Intent();
                    intent.setClass(getActivity(),ShopNow.class);
                    intent.putExtra("UserName",user.getFirstName()+" "+user.getLastName());
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    cardNumber1.setError( "Error: Please try after some time.");
                    cardNumber1.requestFocus();

                }
            }
        }

}

