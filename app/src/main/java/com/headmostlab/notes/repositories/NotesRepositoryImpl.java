package com.headmostlab.notes.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.headmostlab.notes.Event;
import com.headmostlab.notes.R;
import com.headmostlab.notes.model.Note;
import com.headmostlab.notes.ui.Constants;

import java.util.ArrayList;
import java.util.List;

public class NotesRepositoryImpl implements NotesRepository {
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final LiveData<List<Note>> notes = new NotesMutableLiveData();

    public NotesRepositoryImpl() {
    }

    @Override
    public LiveData<List<Note>> requestNotes() {
        return notes;
    }

    @Override
    public LiveData<Event<Integer>> delete(@NonNull String noteId) {
        return new DeleteNoteMutableLiveData(noteId);
    }

    @Override
    public LiveData<Event<Integer>> update(@NonNull Note note) {
        return new UpdateNoteMutableLiveData(note);
    }

    private final class UpdateNoteMutableLiveData extends MutableLiveData<Event<Integer>> {
        public UpdateNoteMutableLiveData(Note note) {
            CollectionReference collection = firebaseFirestore.collection(Constants.COLLECTION_NOTES);
            if (note.getId() == null) {
                collection.add(note)
                        .addOnSuccessListener(documentReference -> setValue(new Event<>(R.string.msg_success)))
                        .addOnFailureListener(e -> setValue(new Event<>(R.string.msg_failure)));
            } else {
                collection.document(note.getId()).set(note)
                        .addOnSuccessListener(documentReference -> setValue(new Event<>(R.string.msg_success)))
                        .addOnFailureListener(e -> setValue(new Event<>(R.string.msg_failure)));
                ;
            }
        }
    }

    private final class DeleteNoteMutableLiveData extends MutableLiveData<Event<Integer>> {
        public DeleteNoteMutableLiveData(String noteId) {
            firebaseFirestore.collection(Constants.COLLECTION_NOTES).document(noteId).delete()
                    .addOnSuccessListener(aVoid -> setValue(new Event<>(R.string.msg_success)))
                    .addOnFailureListener(e -> setValue(new Event<>(R.string.msg_failure)));
        }
    }

    private final class NotesMutableLiveData extends MutableLiveData<List<Note>> {

        private ListenerRegistration listenerRegistration;

        @Override
        protected void onActive() {
            listenerRegistration = firebaseFirestore
                    .collection(Constants.COLLECTION_NOTES).addSnapshotListener((snapshot, error) -> {
                        if (snapshot != null) {
                            List<Note> notes = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : snapshot.getDocuments()) {
                                notes.add(documentSnapshot.toObject(Note.class));
                            }
                            setValue(notes);
                        }
                    });
        }

        @Override
        protected void onInactive() {
            listenerRegistration.remove();
        }
    }
}
