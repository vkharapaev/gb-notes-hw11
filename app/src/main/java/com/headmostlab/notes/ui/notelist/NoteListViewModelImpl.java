package com.headmostlab.notes.ui.notelist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.headmostlab.notes.model.Note;
import com.headmostlab.notes.repositories.NotesRepository;

import java.util.List;

public class NoteListViewModelImpl extends androidx.lifecycle.ViewModel implements NoteListViewModel {

    public static final String NOTE_KEY = "NOTE";
    private final SavedStateHandle dataStorage;
    private final MutableLiveData<Note> selectedNote = new MutableLiveData<>();
    private final NotesRepository notesRepository;

    public NoteListViewModelImpl(SavedStateHandle savedState, NotesRepository repository) {
        this.notesRepository = repository;
        loadNotes();
        dataStorage = savedState;
        Note note = savedState.get(NOTE_KEY);
        if (note != null) {
            selectedNote.setValue(note);
        }
    }

    public LiveData<List<Note>> getNotes() {
        return notesRepository.requestNotes();
    }

    @Override
    public LiveData<Note> getSelectedNote() {
        return selectedNote;
    }

    @Override
    public void selectNote(int position) {
        Note note = notesRepository.requestNotes().getValue().get(position);
        selectedNote.setValue(note);
        dataStorage.set(NOTE_KEY, note);
    }

    @Override
    public void deselect() {
        selectedNote.setValue(null);
    }

    private void loadNotes() {
        notesRepository.requestNotes();
    }
}
