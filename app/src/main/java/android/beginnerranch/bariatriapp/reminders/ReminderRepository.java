package android.beginnerranch.bariatriapp.reminders;

import android.beginnerranch.bariatriapp.reminders.impl.model.Reminder;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class ReminderRepository {

    private static volatile ReminderRepository instance;

    private ReminderDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private Reminder user = null;

    // private constructor : singleton access
    private ReminderRepository(ReminderDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ReminderRepository getInstance(ReminderDataSource dataSource) {
        if (instance == null) {
            instance = new ReminderRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(Reminder user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<Reminder> login(String username, String password) {
        // handle login
        Result<Reminder> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<Reminder>) result).getData());
        }
        return result;
    }
}
