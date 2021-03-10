package com.headmostlab.notes.ui.notelist;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AbstractSavedStateViewModelFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.savedstate.SavedStateRegistryOwner;

import com.headmostlab.notes.ServiceLocator;
import com.headmostlab.notes.repositories.NotesRepository;

public class NoteListViewModelFactory extends AbstractSavedStateViewModelFactory
        implements ViewModelProvider.Factory {

    private final ServiceLocator serviceLocator;

    public NoteListViewModelFactory(Context context, @NonNull SavedStateRegistryOwner owner, @Nullable Bundle defaultArgs) {
        super(owner, defaultArgs);
        serviceLocator = ServiceLocator.from(context);
    }

    @NonNull
    @Override
    protected <T extends ViewModel> T create(@NonNull String key, @NonNull Class<T> modelClass, @NonNull SavedStateHandle handle) {
        return (T) new NoteListViewModelImpl(handle, serviceLocator.locate(NotesRepository.class));
    }
}
