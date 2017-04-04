package pl.abitcreative.mytummy.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import pl.abitcreative.mytummy.BaseActivity;
import pl.abitcreative.mytummy.R;
import pl.abitcreative.mytummy.ui.eatslist.EatsListActivity;

import javax.inject.Inject;

/**
 * Created by daber on 13/03/17.
 */

public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
  private static final int    RC_SIGN_IN    = 1;
  private static final int    RC_PICK_PLACE = 2;
  private static final String TAG           = "LOGIN";
  @Inject
  GoogleApiClient mGoogleApiClient;
  private View mView;
  private View mLoginButton;
  private FirebaseAuth mAuth = FirebaseAuth.getInstance();

  @Override
  public void onAttach(Context context) {
    BaseActivity baseActivity = (BaseActivity) context;
    baseActivity.activityComponent.inject(this);
    super.onAttach(context);

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    if (user != null) {
      onLoginSuccessfull();
    }


  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mView = inflater.inflate(R.layout.fragment_login, container, false);
    mLoginButton = mView.findViewById(R.id.sign_in_button);
    mLoginButton.setOnClickListener(this);

    return mView;
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    if (id == R.id.sign_in_button) {
      signIn();
    }
  }


  private void signIn() {
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == RC_SIGN_IN) {

      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      if (result.isSuccess()) {
        Log.d(TAG, "Login OK");
        GoogleSignInAccount account = result.getSignInAccount();
        firebaseAuthWithGoogle(account);
        onLoginSuccessfull();
      } else {
        Log.d(TAG, "Login failed");
      }
    }
    Log.d(TAG, "Result code =" + resultCode);

  }

  private void onLoginSuccessfull() {
    getActivity().finish();
    Intent i = new Intent(getContext(), EatsListActivity.class);
    startActivity(i);
  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
    Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

    AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
    mAuth.signInWithCredential(credential)
        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
            if (!task.isSuccessful()) {
              Log.w(TAG, "signInWithCredential", task.getException());
            }
          }
        });
  }
}
