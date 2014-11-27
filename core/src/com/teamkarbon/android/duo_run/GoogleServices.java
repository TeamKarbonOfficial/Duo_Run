package com.teamkarbon.android.duo_run;

/**
 * Created by Joe on 25/11/2014.
 */

public class GoogleServices implements derptest.IGoogleServices {
    private static GoogleServices ourInstance = new GoogleServices();

    public static GoogleServices getInstance() {
        return ourInstance;
    }

    private GoogleServices() {

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
    public void submitScore(long score) {

    }

    @Override
    public void showScores() {

    }

    @Override
    public boolean isSignedIn() {
        return false;
    }
}
