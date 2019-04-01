package com.eulerhermes.research.app;

import java.io.IOException;
import java.util.Calendar;

import com.eulerhermes.research.BuildConfig;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.eulerhermes.research.R;

public class LinkedInLoginActivity extends Activity
{

    /*CONSTANT FOR THE AUTHORIZATION PROCESS*/

    /****YOUR LINKEDIN APP INFO HERE*********/
    private static final String	API_KEY				= BuildConfig.LINKED_IN_API_KEY;
    private static final String	SECRET_KEY			= BuildConfig.LINKED_IN_SECRET_KEY;
    // This is any string we want to use. This will be used for avoid CSRF attacks. You can generate one here: http://strongpasswordgenerator.com/
    private static final String	STATE				= BuildConfig.LINKED_IN_STATE;
    private static final String	REDIRECT_URI		= BuildConfig.LINKED_IN_REDIRECT;

    private static final String	SCOPES				= "r_basicprofile%20r_emailaddress";
    /*********************************************/

    // These are constants used for build the urls
    private static final String	AUTHORIZATION_URL	= "https://www.linkedin.com/uas/oauth2/authorization";
    private static final String	ACCESS_TOKEN_URL	= "https://www.linkedin.com/uas/oauth2/accessToken";
    private static final String	SECRET_KEY_PARAM	= "client_secret";
    private static final String	RESPONSE_TYPE_PARAM	= "response_type";
    private static final String	GRANT_TYPE_PARAM	= "grant_type";
    private static final String	GRANT_TYPE			= "authorization_code";
    private static final String	RESPONSE_TYPE_VALUE	= "code";
    private static final String	CLIENT_ID_PARAM		= "client_id";
    private static final String	SCOPE_PARAM			= "scope";
    private static final String	STATE_PARAM			= "state";
    private static final String	REDIRECT_URI_PARAM	= "redirect_uri";
    /*---------------------------------------*/
    private static final String	QUESTION_MARK		= "?";
    private static final String	AMPERSAND			= "&";
    private static final String	EQUALS				= "=";

    private static final String PROFILE_URL = "https://api.linkedin.com/v1/people/~:(first-name,last-name,email-address)";
    private static final String OAUTH_ACCESS_TOKEN_PARAM ="oauth2_access_token";

    private WebView				webView;
    private ProgressDialog		pd;
    private ProgressDialog		pdt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkedin_login);

        getActionBar().setLogo(R.drawable.ic_arrow_left);


        webView = (WebView) findViewById(R.id.webview);
        webView.requestFocus(View.FOCUS_DOWN);
        pd = ProgressDialog.show(this, "", "Loading..", true);

        // Set a custom web view client
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                // This method will be executed each time a page finished loading.
                // The only we do is dismiss the progressDialog, in case we are showing any.
                if (pd != null && pd.isShowing())
                {
                    Log.d("LinkedInLoginActivity.onCreate(...).new WebViewClient() {...}",
                          "onPageFinished: ");
                    pd.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl)
            {
                Log.d("LinkedInLoginActivity.onCreate(...).new WebViewClient() {...}",
                      "shouldOverrideUrlLoading: " + authorizationUrl);
                // This method will be called when the Auth proccess redirect to our RedirectUri.
                // We will check the url looking for our RedirectUri.
                if (authorizationUrl.startsWith(REDIRECT_URI))
                {
                    Log.i("Authorize", "");
                    Uri uri = Uri.parse(authorizationUrl);
                    // We take from the url the authorizationToken and the state token. We have to check that the state token returned by the Service
                    // is the same we sent.
                    // If not, that means the request may be a result of CSRF and must be rejected.
                    String stateToken = uri.getQueryParameter(STATE_PARAM);
                    if (stateToken == null || !stateToken.equals(STATE))
                    {
                        Log.e("Authorize", "State token doesn't match");
                        return true;
                    }

                    // If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                    String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                    if (authorizationToken == null)
                    {
                        Log.i("Authorize", "The user doesn't allow authorization.");
                        return true;
                    }
                    Log.i("Authorize", "Auth token received: " + authorizationToken);

                    // Generate URL for requesting Access Token
                    String accessTokenUrl = getAccessTokenUrl(authorizationToken);
                    // We make the request in a AsyncTask
                    new PostRequestAsyncTask().execute(accessTokenUrl);

                }
                else
                {
                    // Default behaviour
                    Log.i("Authorize", "Redirecting to: " + authorizationUrl);
                    webView.loadUrl(authorizationUrl);
                }
                return true;
            }
        });

        // Get the authorization Url
        String authUrl = getAuthorizationUrl();
        Log.i("Authorize", "Loading Auth Url: " + authUrl);
        // Load the authorization URL into the webView
        webView.loadUrl(authUrl);
    }

    /**
     * Method that generates the url for get the access token from the Service
     * @return Url
     */
    private static String getAccessTokenUrl(String authorizationToken)
    {
        String URL = ACCESS_TOKEN_URL
                +QUESTION_MARK
                +GRANT_TYPE_PARAM+EQUALS+GRANT_TYPE
                +AMPERSAND
                +RESPONSE_TYPE_VALUE+EQUALS+authorizationToken
                +AMPERSAND
                +CLIENT_ID_PARAM+EQUALS+API_KEY
                +AMPERSAND
                +REDIRECT_URI_PARAM+EQUALS+REDIRECT_URI
                +AMPERSAND
                +SECRET_KEY_PARAM+EQUALS+SECRET_KEY;
        Log.i("accessToken URL",""+URL);
        return URL;
    }
    /**
     * Method that generates the url for get the authorization token from the Service
     * @return Url
     */
    private static String getAuthorizationUrl()
    {
        String URL = AUTHORIZATION_URL
                +QUESTION_MARK+RESPONSE_TYPE_PARAM+EQUALS+RESPONSE_TYPE_VALUE
                +AMPERSAND  +CLIENT_ID_PARAM    +EQUALS +API_KEY
                +AMPERSAND  +SCOPE_PARAM        +EQUALS +SCOPES
                +AMPERSAND  +STATE_PARAM        +EQUALS +STATE
                +AMPERSAND  +REDIRECT_URI_PARAM +EQUALS +REDIRECT_URI;
        Log.i("authorization URL",""+URL);
        return URL;
    }

    private static final String getProfileUrl(String accessToken)
    {
        return PROFILE_URL
                +QUESTION_MARK
                +OAUTH_ACCESS_TOKEN_PARAM+EQUALS+accessToken;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    private class PostRequestAsyncTask extends AsyncTask<String, Void, Bundle>
    {

        @Override
        protected void onPreExecute()
        {
            Log.d("LinkedInLoginActivity.PostRequestAsyncTask", "onPreExecute: ");
            pdt = ProgressDialog.show(LinkedInLoginActivity.this, "", "Signing in..", true);
        }

        @Override
        protected Bundle doInBackground(String... urls)
        {
            if (urls.length > 0)
            {
                String url = urls[0];
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpost = new HttpPost(url);
                try
                {
                    HttpResponse response = httpClient.execute(httpost);
                    Log.d("LinkedInLoginActivity.PostRequestAsyncTask",
                          "doInBackground: " + response);
                    if (response != null)
                    {
                        Log.d("LinkedInLoginActivity.PostRequestAsyncTask",
                              "doInBackground: " + response.getStatusLine().getStatusCode());
                        // If status is OK 200
                        if (response.getStatusLine().getStatusCode() == 200)
                        {
                            String result = EntityUtils.toString(response.getEntity());

                            Log.d("LinkedInLoginActivity.PostRequestAsyncTask",
                                  "doInBackground: " + result);
                            // Convert the string result to a JSON Object
                            JSONObject resultJson = new JSONObject(result);
                            // Extract data from JSON Response
                            int expiresIn = resultJson.has("expires_in") ? resultJson
                                    .getInt("expires_in") : 0;

                            String accessToken = resultJson.has("access_token") ? resultJson
                                    .getString("access_token") : null;
                            Log.e("Tokenm", "" + accessToken);
                            if (expiresIn > 0 && accessToken != null)
                            {
                                Log.i("Authorize", "This is the access Token: " + accessToken
                                        + ". It will expires in " + expiresIn + " secs");

                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.SECOND, expiresIn);
                                long expireDate = calendar.getTimeInMillis();

                                HttpGet httpget = new HttpGet(getProfileUrl(accessToken));
                                httpget.setHeader("x-li-format", "json");

                                httpClient = new DefaultHttpClient();
                                response = httpClient.execute(httpget);
                                if(response != null)
                                {
                                    Log.d("LinkedInLoginActivity.PostRequestAsyncTask",
                                          "doInBackground: " + response.getStatusLine().getStatusCode());
                                    //If status is OK 200
                                    if(response.getStatusLine().getStatusCode()==200)
                                    {
                                        result = EntityUtils.toString(response.getEntity());

                                        //Convert the string result to a JSON Object
                                        JSONObject data = new JSONObject(result);

                                        Bundle bundle = new Bundle();
                                        bundle.putString("firstName", data.optString("firstName"));
                                        bundle.putString("lastName", data.optString("lastName"));
                                        bundle.putString("email", data.optString("emailAddress"));
                                        bundle.putString("accessToken", accessToken);
                                        bundle.putLong("expiresDate", expireDate);

                                        return bundle;
                                    }

                                }
                                Log.d("LinkedInLoginActivity.PostRequestAsyncTask",
                                      "doInBackground: response null");
                                return null;
                            }
                        }
                    }
                }
                catch (IOException e)
                {
                    Log.e("Authorize", "Error Http response " + e.getLocalizedMessage());
                }
                catch (ParseException e)
                {
                    Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
                }
                catch (JSONException e)
                {
                    Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bundle data)
        {
            if (pdt != null && pdt.isShowing())
            {
                pdt.dismiss();
            }

            if (data != null)
                onSignInComplete(data);
        }
    };

    private void onSignInComplete(Bundle data)
    {
        Intent intent = new Intent();
        intent.putExtra("firstName", data.getString("firstName"));
        intent.putExtra("lastName", data.getString("lastName"));
        intent.putExtra("email", data.getString("email"));
        intent.putExtra("accessToken", data.getString("accessToken"));
        intent.putExtra("expiresDate", data.getLong("expiresDate"));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
