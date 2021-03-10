package com.headmostlab.notes.ui.notelist;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.headmostlab.notes.R;
import com.headmostlab.notes.databinding.FragmentNoteListBinding;
import com.headmostlab.notes.databinding.NoteRowItemBinding;
import com.headmostlab.notes.model.Note;
import com.headmostlab.notes.ui.Constants;
import com.headmostlab.notes.ui.note.NoteFragment;

import java.util.Collections;
import java.util.List;

public class NoteListFragment extends Fragment {

    public static final String NOTE_TAG = "NOTE";
    private FragmentNoteListBinding binding;
    private NoteListViewModel viewModel;
    private NoteListAdapter adapter;
    private boolean isPortrait;

    public static NoteListFragment newNoteListFragment() {
        return new NoteListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isPortrait = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT;

        viewModel = new ViewModelProvider(this,
                new NoteListViewModelFactory(requireActivity(), this, null)).get(NoteListViewModelImpl.class);

        getParentFragmentManager().setFragmentResultListener(Constants.FRAGMENT_RESULT_DESELECT_NOTE, this,
                (requestKey, result) -> viewModel.deselect());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNoteListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initRecyclerView();
        viewModel.getNotes().observe(getViewLifecycleOwner(), notes -> adapter.setNotes(notes));
        viewModel.getSelectedNote().observe(getViewLifecycleOwner(), this::showNoteIfNotNull);

        if (isPortrait) {
            Fragment noteFragment = getParentFragmentManager().findFragmentByTag(NOTE_TAG);
            if (noteFragment != null) {
                getParentFragmentManager().beginTransaction()
                        .remove(noteFragment)
                        .commit();
            }
        }
        binding.addNoteButton.setOnClickListener(it -> addNote());
    }

    private void initRecyclerView() {
        adapter = new NoteListAdapter(Collections.emptyList());
        binding.noteList.setAdapter(adapter);
        binding.noteList.addItemDecoration(new MyItemDecoration(requireActivity()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
        binding = null;
    }

    private void showNoteIfNotNull(Note note) {
        if (note == null) {
            return;
        }

        showNote(note);
    }

    private void addNote() {

        showNote(null);
    }

    private void showNote(Note o) {
        if (isPortrait) {
            getParentFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.container, NoteFragment.newNoteFragment(o), NOTE_TAG)
                    .addToBackStack(null)
                    .commit();
        } else {
            getParentFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.childContainer, NoteFragment.newNoteFragment(o), NOTE_TAG)
                    .commit();
        }
    }

    private class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {

        private List<Note> notes;

        public NoteListAdapter(List<Note> notes) {
            this.notes = notes;
        }

        @NonNull
        @Override
        public NoteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {
            NoteRowItemBinding binding =
                    NoteRowItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        public void setNotes(List<Note> notes) {
            this.notes = notes;
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull NoteListAdapter.ViewHolder holder, int position) {
            holder.bind(notes.get(position));
        }

        @Override
        public int getItemCount() {
            return notes.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private final NoteRowItemBinding binding;

            public ViewHolder(NoteRowItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                binding.itemContainer.setOnClickListener(v -> viewModel.selectNote(getAdapterPosition()));
            }

            void bind(Note note) {
                binding.title.setText(note.getTitle());
                binding.description.setText(note.getDescription());
            }
        }
    }
}
