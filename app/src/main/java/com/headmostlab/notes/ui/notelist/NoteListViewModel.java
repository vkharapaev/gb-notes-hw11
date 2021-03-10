package com.headmostlab.notes.ui.notelist;

import androidx.lifecycle.LiveData;

import com.headmostlab.notes.model.Note;

import java.util.List;

public interface NoteListViewModel {
    LiveData<List<Note>> getNotes();

    LiveData<Note> getSelectedNote();

    void selectNote(int position);

    void deselect();
}
