package com.mind.libs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MindScore implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private MindScoreView mvSafeScore;
    private Location mCurrentLocation;
    private int mSpeedLimit = 60;
    private int mSafeScore;
    private Context mContext;
    private Handler mHandler = new Handler();
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean isRideMode = false;

    public MindScore(MindScoreView mvSafeScore, Context mContext){
        this.mvSafeScore = mvSafeScore;
        this.mContext = mContext;

        buildGoogleApiClient();
        mGoogleApiClient.connect();


    }

    private synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void clearScore(){
        mvSafeScore.setScore(0);
        mSafeScore = 0;
    }

    public void startDrive(){
        isRideMode = true;
    }

    public void stopDrive(){
        isRideMode = false;
    }

    public int getScore(){
        return mSafeScore;
    }


    @Override
    public void onLocationChanged(Location location) {
        Location lastLocation = mCurrentLocation;
        mCurrentLocation = location;

        if (lastLocation != null && isRideMode) {
            float[] distance = new float[2];
            float actualDistance;
            Location.distanceBetween(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), lastLocation.getLatitude(), lastLocation.getLongitude(), distance);
            actualDistance = distance[0];

            float speed = actualDistance / (mCurrentLocation.getTime() - lastLocation.getTime());
            if ((int) (speed * 1800) != 0) {
                if ((int) (speed * 1800) > mSpeedLimit) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 10; i++) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mSafeScore--;

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mvSafeScore.tvSafeScore.setTextColor(Color.RED);
                                        mvSafeScore.tvSafeScore.setText(String.valueOf(mSafeScore));
                                    }
                                });
                            }
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mvSafeScore.tvSafeScore.setTextColor(Color.WHITE);

                                }
                            });

                        }
                    }).start();

                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 2; i++) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mSafeScore++;

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mvSafeScore.tvSafeScore.setText(String.valueOf(mSafeScore));
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this
        );
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
