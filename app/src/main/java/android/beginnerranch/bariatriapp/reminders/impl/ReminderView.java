package android.beginnerranch.bariatriapp.reminders.impl;

/**
 * Class exposing authenticated user details to the UI.
 */
class ReminderView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    ReminderView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
