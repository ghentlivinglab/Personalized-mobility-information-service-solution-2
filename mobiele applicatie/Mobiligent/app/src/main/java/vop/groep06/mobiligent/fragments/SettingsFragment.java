package vop.groep06.mobiligent.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import vop.groep06.mobiligent.MainActivity;
import vop.groep06.mobiligent.R;
import vop.groep06.mobiligent.RefreshAccessToken;
import vop.groep06.mobiligent.models.User;

public class SettingsFragment extends Fragment {

    //view elements
    private EditText emailField;
    private EditText firstNameField;
    private EditText lastNameField;


    View view;
    User user;

    enum ChangeData {
        NAMES,
        EMAIL,
        PASSWORD
    }

    public SettingsFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.change_settings_fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.user = (User) bundle.getSerializable("user");
        }

        emailField = (EditText) view.findViewById(R.id.emailField);
        firstNameField = (EditText) view.findViewById(R.id.firstNameField);
        lastNameField = (EditText) view.findViewById(R.id.lastNameField);


        emailField.setText(user.getEmail());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());

        TextWatcher textWatcherName = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Button saveNameButton = (Button) view.findViewById(R.id.saveNameButton);
                if (firstNameField.getText().toString().equals(user.getFirstName()) &&
                        lastNameField.getText().toString().equals(user.getLastName())) {
                    saveNameButton.setEnabled(false);
                } else {
                    saveNameButton.setEnabled(true);
                }
            }
        };

        TextWatcher textWatcherEmail = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Button saveEmailButton = (Button) view.findViewById(R.id.saveEmailButton);
                if (emailField.getText().toString().equals(user.getEmail())) {
                    saveEmailButton.setEnabled(false);
                } else {
                    saveEmailButton.setEnabled(true);
                }
            }
        };



        emailField.addTextChangedListener(textWatcherEmail);
        firstNameField.addTextChangedListener(textWatcherName);
        lastNameField.addTextChangedListener(textWatcherName);

        Button changePassButton = (Button) view.findViewById(R.id.changePasswordButton);
        changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangePasswordClick();
            }
        });

        (view.findViewById(R.id.saveNameButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UserDataChangeTask(user.getUserURL(),ChangeData.NAMES, new ArrayList<>(Arrays.asList(firstNameField.getText().toString(), lastNameField.getText().toString()))).execute();
            }
        });

        (view.findViewById(R.id.saveEmailButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UserDataChangeTask(user.getChangeEmailUrl(),ChangeData.EMAIL, new ArrayList<>(Arrays.asList(user.getEmail(), emailField.getText().toString()))).execute();
            }
        });

        return view;
    }

    public void onChangePasswordClick () {
        final View dialogView = View.inflate(getActivity(), R.layout.change_password_modal, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        final EditText old = (EditText) dialogView.findViewById(R.id.oldPassEditText);
        final EditText new1 = (EditText) dialogView.findViewById(R.id.newPassEditText);
        final EditText new2 = (EditText) dialogView.findViewById(R.id.newPassRepeatEditText);

        dialogView.findViewById(R.id.passwordCancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.passwordSaveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!new1.getText().toString().equals(new2.getText().toString())) {
                    new2.setError("Paswoorden komen niet overeen!");
                    new2.requestFocus();
                } else if (new1.getText().toString().length()<8) {
                    new2.setError("Nieuw paswoord is niet lang genoeg (min. 8 characters).");
                    new2.requestFocus();
                } else {
                    new UserDataChangeTask(user.getChangePassUrl(),
                            ChangeData.PASSWORD,
                            new ArrayList<>(Arrays.asList(old.getText().toString(), new2.getText().toString())),
                            alertDialog,
                            dialogView).execute();
                }
            }
        });

        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    private void checkFieldsChanged() {
        Button saveNameButton = (Button) view.findViewById(R.id.saveNameButton);
        if (firstNameField.getText().toString().equals(user.getFirstName()) &&
                lastNameField.getText().toString().equals(user.getLastName())) {
            saveNameButton.setEnabled(false);
        } else {
            saveNameButton.setEnabled(true);
        }

        Button saveEmailButton = (Button) view.findViewById(R.id.saveEmailButton);
        if (emailField.getText().toString().equals(user.getEmail())) {
            saveEmailButton.setEnabled(false);
        } else {
            saveEmailButton.setEnabled(true);
        }
    }

    private class UserDataChangeTask extends AsyncTask<Void, Void, Boolean> {

        private String postUrl;
        private JSONObject response;
        private ChangeData changeData;
        private ArrayList<String> args;
        private int statusCode;
        AlertDialog alertDialog;
        View dialogView;


        public UserDataChangeTask (String postUrl, ChangeData changeData, ArrayList<String> args, Object... alertObjects) {
            this.postUrl = postUrl;
            this.changeData = changeData;
            this.args = args;
            if (changeData == ChangeData.PASSWORD) {
                this.alertDialog = (AlertDialog) alertObjects[0];
                this.dialogView = (View) alertObjects[1];
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;

            try {

                URL url = new URL(postUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                if (changeData == ChangeData.NAMES) {
                    urlConnection.setRequestMethod("PUT");
                } else {
                    urlConnection.setRequestMethod("POST");
                }
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Authorization", user.getAccessToken());
                urlConnection.connect();

                JSONObject request = new JSONObject();

                if (changeData == ChangeData.NAMES) {
                    request.put("first_name", args.get(0));
                    request.put("last_name", args.get(1));
                    request.put("email", user.getEmail());
                } else if (changeData == ChangeData.EMAIL) {
                    request.put("old_email", args.get(0));
                    request.put("new_email", args.get(1));
                } else {
                    request.put("old_password", args.get(0));
                    request.put("new_password", args.get(1));
                }

                String httpBody = request.toString();

                try (BufferedWriter out =
                             new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()))
                ) {
                    out.write(httpBody);
                }

                statusCode = urlConnection.getResponseCode();
                if (statusCode != 200) {
                    return false;
                }
                if (changeData != ChangeData.NAMES) {
                    try (BufferedReader in =
                                 new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                        String responseBody = convertInputStream(in);
                        response = new JSONObject(responseBody);
                    }
                }
            } catch (IOException | JSONException ex) {
                throw new RuntimeException(ex);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                try {
                    Toast.makeText(getActivity(), "Updating succesful!", Toast.LENGTH_SHORT).show();
                    if (changeData != ChangeData.NAMES) {
                        if (changeData == ChangeData.PASSWORD) {
                            alertDialog.dismiss();
                        }
                        user.setEmail(emailField.getText().toString());
                        user.setRefreshToken(response.getString("token"));
                        user.setId(response.getString("user_id"));
                        ((MainActivity) getActivity()).updateToken();

                        //new accesstoken is needed after changing email
                        if(changeData == ChangeData.EMAIL) {
                            new RefreshAccessToken(user, getActivity()).execute();
                        }
                    } else {
                        user.setFirstName(firstNameField.getText().toString());
                        user.setLastName(lastNameField.getText().toString());
                    }
                    checkFieldsChanged();
                    ((MainActivity)getActivity()).noKeyBoadOnScreen();
                } catch (JSONException ex) {

                }
            } else {
                if (changeData == ChangeData.PASSWORD && statusCode == 403) {
                    ((EditText) dialogView.findViewById(R.id.oldPassEditText)).setError("Old password incorrect.");
                    (dialogView.findViewById(R.id.oldPassEditText)).requestFocus();
//                    Toast.makeText(getActivity(), "Old password incorrect.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Updating failed. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        private String convertInputStream(BufferedReader in) throws IOException {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }
}
