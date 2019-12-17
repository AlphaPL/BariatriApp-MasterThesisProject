package android.beginnerranch.bariatriapp.reminders.impl;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import android.beginnerranch.bariatriapp.reminders.ReminderDataSource;
import android.beginnerranch.bariatriapp.reminders.ReminderRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class ReminderViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ReminderViewModel.class)) {
            return (T) new ReminderViewModel(ReminderRepository.getInstance(new ReminderDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
