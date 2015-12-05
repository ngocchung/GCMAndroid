# GCM Client
Simple Google Cloud Messaging Client Android Application

Activity:
```
	private void getGCMToken() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InstanceID instanceID = InstanceID.getInstance(mContext);
                    String token = instanceID.getToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    if (token != null && !token.isEmpty()) {
                        SharedPreferences appPrefs = mContext.getSharedPreferences(SHARD_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = appPrefs.edit();
                        prefsEditor.putString(GCM_TOKEN, token);
                        prefsEditor.apply();
                    }
                    Log.i(LOG_TAG, token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }		
```

GcmService:
```
	@Override
    public void onMessageReceived(String from, Bundle data) {
        JSONObject jsonObject = new JSONObject();
        Set<String> keys = data.keySet();
        for (String key : keys) {
            try {
                jsonObject.put(key, data.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            sendNotification("Received: " + jsonObject.toString(5));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
```

[GCM Server App](https://github.com/ngocchung/gcmserver)
