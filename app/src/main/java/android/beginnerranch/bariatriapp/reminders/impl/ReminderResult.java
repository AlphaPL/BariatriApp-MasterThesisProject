package android.beginnerranch.bariatriapp.reminders.impl;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class ReminderResult {
    @Nullable
    private ReminderView success;
    @Nullable
    private Integer error;

    ReminderResult(@Nullable Integer error) {
        this.error = error;
    }

    ReminderResult(@Nullable ReminderView success) {
        this.success = success;
    }

    @Nullable
    ReminderView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
