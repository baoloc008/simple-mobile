package com.simple.simplejobfinder.services

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class JobFinderFirebaseInstanceIdService : FirebaseInstanceIdService()
{

    private val TAG = JobFinderFirebaseInstanceIdService::class.java.simpleName

    override fun onTokenRefresh()
    {
        val refreshedToken = FirebaseInstanceId.getInstance().token

        Log.d(TAG, "Refreshed token: $refreshedToken")

        refreshedToken?.let { sendRegistrationToServer(refreshedToken) }

    }

    private fun sendRegistrationToServer(token: String)
    {

    }
}
