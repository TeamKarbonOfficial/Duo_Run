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

public class AndroidLauncher extends AndroidApplication implements derptest.IGoogleServices {

    //GameHelper
    private GameHelper _gameHelper;

    private final static int REQUEST_SCORE = 9002;
    private final static int REQUEST_ACHIEVEMENTS = 9002;
    private final String ERROR = "ERROR: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new derptest(this), config);
    }

    @Override
    public void signIn() {

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
        return false;
    }

    @Override
    public void onSignInSucceeded() {

    }
}