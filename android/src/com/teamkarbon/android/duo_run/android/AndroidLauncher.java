package com.teamkarbon.android.duo_run.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.google.example.games.basegameutils.GameHelper;

import com.teamkarbon.android.duo_run.derptest;

public class AndroidLauncher extends AndroidApplication implements
        derptest.IGoogleServices,
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new derptest(this), config);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }

    //Basic android stuff
    @Override
    protected void onStart() {
        super.onStart();
        if (!mInSignInFlow && !mExplicitSignOut) {
            // auto sign in
            mGoogleApiClient.connect();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    //These are for Game Services
    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        //Reconnect
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }
        // if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign-in, please try again later."

            //TODO: Fix this (I hope this pops up the sign in thingy)
            //if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult, RC_SIGN_IN, R.string.signin_other_error)) {
            //    mResolvingConnectionFailure = false;
            //}
            Log.v(TAG, "Auto Sign in is enabled but something went wrong .-.");
        }
        Log.v(TAG, "Auto Sign in is disabled!");
        // Put code here to display the sign-in button
    }

    //These are for the Interface
    @Override
    public void signIn() {
        mGoogleApiClient.connect();
    }

    @Override
    public void signOut() {

    }

    @Override
    public void rateGame() {

    }

    @Override
    public void submitScore(String id, long score) {

    }

    @Override
    public void showScores(String id) {

    }

    @Override
    public void showAchievements() {

    }

    @Override
    public void submitNorAchievements(String id) {

    }

    @Override
    public void submitInAchievements(String id, int number) {

    }

    @Override
    public boolean isSignedIn() {
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

    }
}