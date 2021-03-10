package com.headmostlab.notes.ui.note;

import androidx.lifecycle.LiveData;

import com.headmostlab.notes.Event;
import com.headmostlab.notes.model.Note;

public interface NoteViewModel {

    LiveData<Note> getSelectedNote();

    LiveData<Note> getNoteToShare();

    void setNote(Note note);

    void share();

    LiveData<Event<Integer>> save(String title, String description, String date);

    LiveData<Event<Integer>> deleteNote();
}
