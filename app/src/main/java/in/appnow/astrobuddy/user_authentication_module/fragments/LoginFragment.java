package in.appnow.astrobuddy.user_authentication_module.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import org.json.JSONObject;

import javax.inject.Inject;

import in.appnow.astrobuddy.app.AstroAppConstants;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.user_authentication_module.activity.LoginActivity;
import in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_login.LoginComponent;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_login_fragment.LoginFragmentPresenter;
import in.appnow.astrobuddy.user_authentication_module.fragments.mvp_login_fragment.LoginFragmentView;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.ToastUtils;
import retrofit2.Call;

/**
 * Created by Abhishek Thanvi on 16/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class LoginFragment extends Fragment {

    @Inject
    LoginFragmentView loginFragmentView;
    @Inject
    LoginFragmentPresenter loginFragmentPresenter;
    private LoginComponent loginComponent;


    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loginComponent = ((LoginActivity) getActivity()).getLoginComponent();
        loginComponent.inject(this);
        loginFragmentPresenter.onCreate();
        signInFb();
        return loginFragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loginFragmentPresenter.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginFragmentView.GOOGLE_LOGIN_REQUEST) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
            if (loginFragmentView != null && loginFragmentView.client != null) {
                loginFragmentView.client.onActivityResult(requestCode, resultCode, data);
                getTwitterUserProfile();
            }
        } else {
            if (loginFragmentView != null && loginFragmentView.callbackManager != null) {
                loginFragmentView.callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void getTwitterUserProfile() {
        TwitterSession session = loginFragmentView.twitterSession();
        TwitterAuthClient authClient = new TwitterAuthClient();
        final String[] email = {""};
        if (session != null) {
            AccountService accountService = new TwitterApiClient(session).getAccountService();
            Call<User> callback = accountService.verifyCredentials(true, true, true);
            callback.clone().enqueue(new Callback<User>() {
                @Override
                public void success(Result<User> result) {

                    User user = result.data;
                    authClient.requestEmail(session, new Callback<String>() {
                        @Override
                        public void success(Result<String> result) {
                            email[0] = result.data;
                            Logger.DebugLog("Success email", email[0]);
                            String fullname = user.name;
                            long twitterID = user.getId();
                            String image = user.profileImageUrl;
                            String first_name = "", last_name = "";
                            if (fullname.contains(" ")) {
                                String splitString[] = fullname.split(" ");
                                first_name = splitString[0];
                                last_name = splitString[1];
                            } else {
                                first_name = fullname;
                            }

                            if (AstroApplication.getInstance().isInternetConnected(true))
                                loginFragmentPresenter.loginUserWithSocial(twitterID + "", first_name, last_name, "", email[0], "", image, AstroAppConstants.LOGIN_TWITTER);
                        }

                        @Override
                        public void failure(TwitterException exception) {
                            ToastUtils.shortToast("Twitter SignIn Failed. " + exception.getLocalizedMessage());
                            Logger.ErrorLog("Error EMail", exception.getLocalizedMessage());
                        }
                    });
                }

                @Override
                public void failure(TwitterException exception) {

                }
            });
        }
    }

    public void signInFb() {
        try {
            LoginManager.getInstance().registerCallback(loginFragmentView.callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            GraphRequest request = GraphRequest.newMeRequest(
                                    loginResult.getAccessToken(),
                                    (object, response) -> {
                                        JSONObject jobj;
                                        try {
                                            if (response != null) {
                                                jobj = new JSONObject(response.getRawResponse());
                                                String id = "";
                                                if (jobj.has("id")) {
                                                    id = jobj.getString("id");
                                                } else {
                                                    ToastUtils.longToast("Oops!! Failed to do facebook authentication. Please try again.");
//                                                    Crashlytics.log("Facebook Auth failed (NO ID RECEIVED) : " + jobj.toString());
                                                    return;
                                                }
                                                String email = "", first_name = "", last_name = "", gender = "", birthday = "";
                                                if (jobj.has("email")) {
                                                    email = jobj.getString("email");
                                                }
                                                if (jobj.has("first_name")) {
                                                    first_name = jobj.getString("first_name");
                                                }
                                                if (jobj.has("last_name")) {
                                                    last_name = jobj.getString("last_name");
                                                }
                                                if (jobj.has("gender")) {
                                                    gender = jobj.getString("gender");
                                                }
                                                if (jobj.has("birthday")) {
                                                    birthday = jobj.getString("birthday");
                                                }
                                                String image = "http://graph.facebook.com/" + id + "/picture?type=large";

                                                Logger.DebugLog("data : ", image);
                                                if (AstroApplication.getInstance().isInternetConnected(true))
                                                    loginFragmentPresenter.loginUserWithSocial(id, first_name, last_name, birthday, email, gender, image, AstroAppConstants.LOGIN_FACEBOOK);
                                            }
                                        } catch (Exception e) {
                                            // e.printStackTrace();
                                            e.printStackTrace();
                                            ToastUtils.shortToast("Facebook SignIn Failed. " + e.getLocalizedMessage());

                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,link,gender,birthday,first_name,last_name,email");
                            request.setParameters(parameters);
                            request.executeAsync();

                        }

                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            ToastUtils.shortToast("Facebook SignIn Failed. " + exception.getLocalizedMessage());
                            Logger.ErrorLog("FB LOGIN ERROR : ", exception.getLocalizedMessage());
                            exception.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            ToastUtils.shortToast("Facebook SignIn Failed.");
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (AstroApplication.getInstance().isInternetConnected(true))
                loginFragmentPresenter.loginUserWithSocial(account.getId(), account.getGivenName(), account.getFamilyName(), "", account.getEmail(), "", account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "", AstroAppConstants.LOGIN_GOOGLE);
        } catch (ApiException e) {
            ToastUtils.shortToast("Google SignIn Failed. " + e.getLocalizedMessage());
            Logger.ErrorLog("SignIn Failed ", e.getLocalizedMessage());
        }
    }


}
