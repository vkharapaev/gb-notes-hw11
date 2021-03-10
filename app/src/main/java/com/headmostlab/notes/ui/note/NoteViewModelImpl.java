package com.headmostlab.notes.ui.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.headmostlab.notes.Event;
import com.headmostlab.notes.Utils;
import com.headmostlab.notes.model.Note;
import com.headmostlab.notes.repositories.NotesRepository;

public class NoteViewModelImpl extends androidx.lifecycle.ViewModel implements NoteViewModel {

    private static final String NOTE_KEY = "NOTE";

    private final SavedStateHandle dataStorage;
    private final MutableLiveData<Note> note = new MutableLiveData<>();
    private final MutableLiveData<Note> noteToShare = new MutableLiveData<>();
    private final NotesRepository repository;

    public NoteViewModelImpl(SavedStateHandle savedState, NotesRepository _repository) {
        dataStorage = savedState;
        repository = _repository;
        Note noteTmp = dataStorage.get(NOTE_KEY);
        if (noteTmp != null) {
            note.setValue(noteTmp);
        }
    }

    @Override
    public LiveData<Note> getSelectedNote() {
        return note;
    }

    @Override
    public LiveData<Note> getNoteToShare() {
        return noteToShare;
    }

    @Override
    public void setNote(Note note) {
        dataStorage.set(NOTE_KEY, note);
        this.note.setValue(note);
    }

    @Override
    public void share() {
        noteToShare.setValue(note.getValue());
    }


    @Override
    public LiveData<Event<Integer>> save(String title, String description, String date) {
        Note note = this.note.getValue();
        if (note == null) {
            note = new Note();
        }
        note.setTitle(title);
        note.setDescription(description);
        note.setCreationDate(Utils.parseData(date));
        return repository.update(note);
    }

    @Override
    public LiveData<Event<Integer>> deleteNote() {
        return repository.delete(note.getValue().getId());
    }
}
