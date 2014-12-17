package com.teamkarbon.android.duo_run.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;
import com.teamkarbon.android.duo_run.derptest;

public class AndroidLauncher extends AndroidApplication implements
        derptest.AndroidMethods,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;
    private boolean mExplicitSignOut = false;
    private boolean mInSignInFlow = false;

    private final String TAG = "Game Services";
    private static int RC_SIGN_IN = 9001;
    private final static int REQUEST_SCORE = 9002;
    private final static int REQUEST_ACHIEVEMENTS = 9002;
    private final String ERROR = "ERROR: ";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new derptest(this), config);

        AdBuddiz.setPublisherKey("4227bd68-dfa3-4c9f-a5fc-df45c2a2b372");
        AdBuddiz.cacheAds(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
    }

    //Basic android stuff
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    //These are for Game Services
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected() called. Sign in successful!");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() called. Trying to reconnect.");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() called, result: " + connectionResult);
        if (mResolvingConnectionFailure) {
            // already resolving
            Log.d(TAG, "onConnectionFailed() ignoring connection failure; already resolving.");
            return;
        }
        // if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            if (mSignInClicked || mAutoStartSignInFlow) {
                mAutoStartSignInFlow = false;
                mSignInClicked = false;
                mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient,
                        connectionResult, RC_SIGN_IN, getString(R.string.signin_other_error));
            }

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign-in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }

            Log.v(TAG, "Auto Sign in is enabled but something went wrong .-.");
        }
        Log.v(TAG, "Auto Sign in is disabled!");
        // Put code here to display the sign-in button
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                    + responseCode + ", intent=" + intent);
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (responseCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                BaseGameUtils.showActivityResultError(this,requestCode,responseCode, R.string.signin_other_error);
            }
        }
    }

    //These are for the Interface
    //Game Services
    @Override
    public void startsignIn() {
        Log.d(TAG, "(Interface) signIn() have been called!");
        if (!mInSignInFlow && !mExplicitSignOut) {
            // auto sign in
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void signIn() {
        Log.d(TAG, "(Interface) signIn() have been called!");
        mGoogleApiClient.connect();
    }

    @Override
    public void signOut() {
        Log.d(TAG, "(Interface) signOut() have been called!");
        Games.signOut(mGoogleApiClient);
    }

    @Override
    public void submitScore(String id, long score) {
        Log.d(TAG, "(Interface) submitScore() have been called!");
        if (isSignedIn()) {
            Games.Leaderboards.submitScore(mGoogleApiClient, id, score);
            debug("erm", "Score Submitted");
        } else {
        }
    }

    @Override
    public void showScores(String id) {
        Log.d(TAG, "(Interface) showScores() have been called!");
        if (isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, id), REQUEST_SCORE);
        } else {
            Toast.makeText(getApplicationContext(), ERROR + "You are not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showAchievements() {
        Log.d(TAG, "(Interface) showAchievements() have been called!");
        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_ACHIEVEMENTS);
        } else {
            Toast.makeText(getApplicationContext(), ERROR + "You are not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void submitNorAchievements(String id) {
        Log.d(TAG, "(Interface) submitNorAchievements() have been called!");
        if (isSignedIn()) {
            Games.Achievements.unlock(mGoogleApiClient, id);
        } else {
            Toast.makeText(getApplicationContext(), ERROR + "You are not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void submitInAchievements(String id, int number) {
        Log.d(TAG, "(Interface) submitInAchievements() have been called!");
        if (isSignedIn()) {
            Games.Achievements.increment(mGoogleApiClient, id, number);
        } else {
            Toast.makeText(getApplicationContext(), ERROR + "You are not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean isSignedIn() {
        Log.d(TAG, "(Interface) isSignedIn() have been called!");
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            // signed in
            return true;
        } else {
            //Not signed in
            return false;
        }
    }

    @Override
    public void onSignInSucceeded() {
        Log.d(TAG, "(Interface) onSignInSucceeded() have been called!");
    }

    // Ads
    @Override
    public void showflAds() {
        AdBuddiz.showAd(this);
    }

    //Toast notifs
    @Override
    public void showToastMessage(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Shared Preferences
    @Override
    public void prefputBoolean(String KeyName, boolean value) {
        editor.putBoolean(KeyName, value);
    }

    @Override
    public void prefputString(String KeyName, String value) {
        editor.putString(KeyName, value);
    }

    @Override
    public void prefputInt(String KeyName, int value) {
        editor.putInt(KeyName, value);
    }

    @Override
    public void prefputFloat(String KeyName, float value) {
        editor.putFloat(KeyName, value);
    }

    @Override
    public void prefputLong(String KeyName, long value) {
        editor.putLong(KeyName, value);
    }

    @Override
    public boolean prefgetBoolean(String KeyName) {
        return pref.getBoolean(KeyName, Boolean.parseBoolean(null));
    }

    @Override
    public String prefgetString(String KeyName) {
        return pref.getString(KeyName, null);
    }

    @Override
    public int prefgetInt(String KeyName) {
        return pref.getInt(KeyName, Integer.parseInt(null));
    }

    @Override
    public float prefgetFloat(String KeyName) {
        return pref.getFloat(KeyName, Float.parseFloat(null));
    }

    @Override
    public long prefgetLong(String KeyName) {
        return pref.getLong(KeyName, Long.parseLong(null));
    }

    @Override
    public void prefClear() {
        editor.clear();
    }

    @Override
    public void prefCommit() {
        editor.clear();
    }
}