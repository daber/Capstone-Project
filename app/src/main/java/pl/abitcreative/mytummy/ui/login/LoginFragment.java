package pl.abitcreative.mytummy.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import pl.abitcreative.mytummy.databinding.FragmentLoginBinding;
import pl.abitcreative.mytummy.ui.eatslist.EatsListActivity;
import pl.abitcreative.mytummy.ui.widget.WidgetProvider;

import javax.inject.Inject;

/**
 * Created by daber on 13/03/17.
 */

public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, FirebaseAuth.AuthStateListener {
  private static final int    RC_SIGN_IN    = 1;
  private static final int    RC_PICK_PLACE = 2;
  private static final String TAG           = "LOGIN";
  @Inject
  GoogleApiClient mGoogleApiClient;
  private FragmentLoginBinding binding;
  private FirebaseAuth mAuth = FirebaseAuth.getInstance();

  @Override
  public void onAttach(Context context) {
    BaseActivity baseActivity = (BaseActivity) context;
    baseActivity.activityComponent.inject(this);
    super.onAttach(context);


  }

  private void checkLogin() {
    FirebaseUser user = mAuth.getCurrentUser();
    mAuth.addAuthStateListener(this);
    if (user != null) {
      onLoginSuccessfull();
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = FragmentLoginBinding.inflate(inflater);

    binding.signInButton.findViewById(R.id.sign_in_button);
    binding.signInButton.setOnClickListener(this);
    checkLogin();
    return binding.getRoot();
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

      } else {
        Log.d(TAG, "Login failed");
      }
    }
    Log.d(TAG, "Result code =" + resultCode);

  }

  private void onLoginSuccessfull() {
    getActivity().finish();
    Intent i = new Intent(getContext(), EatsListActivity.class);
    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(i);
    WidgetProvider.broadcastNewEntry(getContext());
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

  @Override
  public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
    if (firebaseAuth.getCurrentUser() != null) {
      if (getActivity() != null) {
        onLoginSuccessfull();
      }
    }
  }
}
