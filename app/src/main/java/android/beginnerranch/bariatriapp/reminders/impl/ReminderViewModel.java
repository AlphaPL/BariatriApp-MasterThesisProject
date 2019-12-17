package android.beginnerranch.bariatriapp.reminders.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import android.beginnerranch.bariatriapp.reminders.ReminderRepository;
import android.beginnerranch.bariatriapp.reminders.Result;
import android.beginnerranch.bariatriapp.reminders.impl.model.Reminder;
import android.beginnerranch.bariatriapp.R;

public class ReminderViewModel extends ViewModel {

    private MutableLiveData<ReminderFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<ReminderResult> loginResult = new MutableLiveData<>();
    private ReminderRepository loginRepository;

    ReminderViewModel(ReminderRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<ReminderFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<ReminderResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<Reminder> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            Reminder data = ((Result.Success<Reminder>) result).getData();
            loginResult.setValue(new ReminderResult(new ReminderView(data.getDisplayName())));
        } else {
            loginResult.setValue(new ReminderResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new ReminderFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new ReminderFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new ReminderFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
