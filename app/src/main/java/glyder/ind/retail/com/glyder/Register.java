package glyder.ind.retail.com.glyder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Register extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener{
    private TextView mUserName;
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mPassword;
    private TextView mPin;
    private TextView mPhone;
    private Button registrationButton;
    View focusView = null;
    private View mProgressView;
    private View mLoginFormView;
    private DataBaseHelper db;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setLogo(R.drawable.gder);
            // actionBar.show();
        }
        mUserName  = (TextView)findViewById(R.id.personEmail);
        mFirstName = (TextView)findViewById(R.id.firstName);
        mLastName  = (TextView)findViewById(R.id.lastName);
        mPassword  = (TextView)findViewById(R.id.password);
        mPin       = (TextView)findViewById(R.id.pin);
        mPhone     = (TextView)findViewById(R.id.phone);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        registrationButton    =  (Button)findViewById(R.id.registrationButton);
        db= new DataBaseHelper(getApplicationContext());
        context=this;
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName.setError(null);
                mFirstName.setError(null);
                mLastName.setError(null);
                mPassword.setError(null);
                mPin.setError(null);
                mPhone.setError(null);

                if(isInPutValid()){
                    User user=new User();
                    user.setEmail(mUserName.getText().toString());
                    user.setFirstName(mFirstName.getText().toString());
                    user.setLastName(mLastName.getText().toString());
                    user.setPassword(mPassword.getText().toString());
                    user.setPhone(mPhone.getText().toString());
                    user.setPin(mPin.getText().toString());
                    UserRegistrationTask userRegistrationTask = new UserRegistrationTask(user);
                    userRegistrationTask.execute((Void) null);
                }
            }
        });

//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }



    @Override
    public void onFragmentInteraction(String uri) {
        getSupportFragmentManager().popBackStack();
    }


    private class UserRegistrationTask extends AsyncTask<Void, Void, Boolean>{
        private User user;
        public UserRegistrationTask(User uuserInc){
            this.user=uuserInc;
        }
        @Override
        protected Boolean doInBackground(Void... params) {



            return  db.isUserExist(user);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            showProgress(false);
            if(!aBoolean.booleanValue()){
                AddAccount addAccount=new AddAccount(user);
                getSupportFragmentManager().beginTransaction().replace(R.id.parentContainer,addAccount)
                        .addToBackStack("AddAccount")
                        .commit();
            }else{
                mUserName.setError("User already exist, please user another email, phone to register.");
            }

        }
    }

    private boolean isInPutValid() {
        boolean isValid=true;
        if(isNull(mUserName) || mUserName.getText().toString().length() < 6
                || !mUserName.getText().toString().contains("@")){
            mUserName.setError("Email/User name is not valid. Please enter email/user name");
            focusView = mUserName;
            focusView.requestFocus();
            isValid=false;
        }

        if(isNull(mFirstName) ){
            mFirstName.setError("First Name is blank. Please enter first name.");
            if(isValid){
                focusView = mFirstName;
                focusView.requestFocus();
            }
            isValid=false;
        }
        if(isNull(mLastName)){
            mLastName.setError("Last Name is blank. Please enter last name.");
            if(isValid){
                focusView = mLastName;
                focusView.requestFocus();
            }
            isValid=false;
        }
        if(isNull(mPhone) || mPhone.getText().toString().length()<10){
            mPhone.setError("Phone is too short or blank. Please enter phone.");
            if(isValid){
                focusView = mPhone;
                focusView.requestFocus();
            }

            isValid=false;
        }
        if(isNull(mPassword) || mPassword.getText().toString().length()<5){
            mPassword.setError("Password is too short or blank. Please enter password.");
            if(isValid){
                focusView = mPassword;
                focusView.requestFocus();
            }

            isValid=false;
        }
        if(isNull(mPin) || mPin.getText().toString().length()!=4){
            mPin.setError("Pin is not of 4 digit. Please enter pin.");
            if(isValid){
                focusView = mPin;
                focusView.requestFocus();
            }
            isValid=false;
        }
        return isValid;
    }

    private boolean isNull(TextView textView) {
        return !(textView != null && textView.getText() != null
                &&  textView.getText().toString()!=null && textView.getText().toString().length()>0);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
