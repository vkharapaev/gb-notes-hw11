package com.headmostlab.notes;

import com.headmostlab.notes.repositories.NotesRepository;
import com.headmostlab.notes.repositories.NotesRepositoryImpl;

public class Application extends android.app.Application implements LocatorHost {

    private ServiceLocator serviceLocator;

    @Override
    public void onCreate() {
        super.onCreate();
        serviceLocator = new ServiceLocator();
        serviceLocator.register(NotesRepository.class, new NotesRepositoryImpl());
    }

    @Override
    public ServiceLocator getLocator() {
        return serviceLocator;
    }
}
