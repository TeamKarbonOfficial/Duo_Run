package com.teamkarbon.android.duo_run.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.GameHelper;
import com.teamkarbon.android.duo_run.derptest;

public class AndroidLauncher extends AndroidApplication implements derptest.IGoogleServices, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //GameHelper
    private GameHelper _gameHelper;

    private final static int REQUEST_SCORE = 9002;
    private final static int REQUEST_ACHIEVEMENTS = 9002;
    private final String ERROR = "ERROR: ";

    // Client used to interact with Google APIs
    private GoogleApiClient mGoogleApiClient;

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Automatically start the sign-in flow when the Activity starts
    private boolean mAutoStartSignInFlow = true;

    // request codes we use when invoking an external activity
    private static final int RC_RESOLVE = 5000;
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new derptest(this), config);

        // Create the GameHelper.
        _gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        _gameHelper.enableDebugLog(false);

        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
        {
            @Override
            public void onSignInSucceeded()
            {
            }

            @Override
            public void onSignInFailed()
            {
            }
        };

        _gameHelper.setup(gameHelperListener);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .addApi(Drive.API).addScope(Drive.SCOPE_APPFOLDER) //Saves
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;
            //if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult,
            //        RC_SIGN_IN, getString(R.string.signin_other_error))) {
            //    mResolvingConnectionFailure = false;
            //}
        }

        //TODO: When Sign in fails, do something
    }

    //TODO: Make this actually link to a button
    public void onSignInButtonClicked() {
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }

    //TODO: Make this actually link to a button
    public void onSignOutButtonClicked() {
        mSignInClicked = false;
        Games.signOut(mGoogleApiClient);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    //TODO: You can make this work if you want
    //This basically records the user's score if he/she's offline, and push it to the cloud when the user comes online again
    class AccomplishmentsOutbox {
        boolean mPrimeAchievement = false;
        boolean mHumbleAchievement = false;
        boolean mLeetAchievement = false;
        boolean mArrogantAchievement = false;
        int mBoredSteps = 0;
        int mEasyModeScore = -1;
        int mHardModeScore = -1;

        boolean isEmpty() {
            return !mPrimeAchievement && !mHumbleAchievement && !mLeetAchievement &&
                    !mArrogantAchievement && mBoredSteps == 0 && mEasyModeScore < 0 &&
                    mHardModeScore < 0;
        }

        public void saveLocal(Context ctx) {
            /* TODO: This is left as an exercise. To make it more difficult to cheat,
             * this data should be stored in an encrypted file! And remember not to
             * expose your encryption key (obfuscate it by building it from bits and
             * pieces and/or XORing with another string, for instance). */
        }

        public void loadLocal(Context ctx) {
            /* TODO: This is left as an exercise. Write code here that loads data
             * from the file you wrote in saveLocal(). */
        }
    }

    //Arghhh, I don't think we need the code above, going to keep it for now
    @Override
    public void signIn() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _gameHelper.beginUserInitiatedSignIn();
                }
            });
        }
        catch (Exception e) {
            Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void signOut() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    _gameHelper.signOut();
                }
            });
        }
        catch (Exception e) {
            Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void rateGame() {
        //Just for fun lol
        String str ="https://play.google.com/store/apps/details?id=come.teamkarbon.android.duo_run.android";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    }

    @Override
    public void submitScore(String id, long score) {
        if (isSignedIn()) {
            Games.Leaderboards.submitScore(_gameHelper.getApiClient(), id, score);
            Toast.makeText(getApplicationContext(), "Score of " + String.valueOf(score) + " have been submitted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), ERROR + "You are not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showScores(String id) {
        if (isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(_gameHelper.getApiClient(), id), REQUEST_SCORE);
        } else {
            Toast.makeText(getApplicationContext(), ERROR + "You are not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    //NOTE! This is for NORMAL Achievements
    @Override
    public void submitNorAchievements(String id) {
        if (isSignedIn()) {
            Games.Achievements.unlock(mGoogleApiClient, id);
        } else {
            Toast.makeText(getApplicationContext(), ERROR + "You are not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    //NOTE! This is for Incremental Achievements
    @Override
    public void submitInAchievements(String id, int number) {
        if (isSignedIn()) {
            Games.Achievements.increment(mGoogleApiClient, id, number);
        } else {
            Toast.makeText(getApplicationContext(), ERROR + "You are not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showAchievements() {
        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_ACHIEVEMENTS);
        } else {
            Toast.makeText(getApplicationContext(), ERROR + "You are not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean isSignedIn() {
        return _gameHelper.isSignedIn();
    }

    @Override
    public void onSignInSucceeded() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        _gameHelper.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        _gameHelper.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        _gameHelper.onActivityResult(requestCode, resultCode, data);
    }
}