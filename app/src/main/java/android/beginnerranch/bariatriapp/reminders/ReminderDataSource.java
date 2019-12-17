package android.beginnerranch.bariatriapp.reminders;

import android.beginnerranch.bariatriapp.reminders.impl.model.Reminder;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class ReminderDataSource {

    public Result<Reminder> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            Reminder fakeUser =
                    new Reminder(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
