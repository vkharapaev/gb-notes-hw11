package com.headmostlab.notes.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.headmostlab.notes.Event;
import com.headmostlab.notes.model.Note;

import java.util.List;

public interface NotesRepository {
    LiveData<List<Note>> requestNotes();

    LiveData<Event<Integer>> delete(@NonNull String noteId);

    LiveData<Event<Integer>> update(@NonNull Note note);
}
