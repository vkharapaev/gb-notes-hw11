package com.headmostlab.notes.ui.note;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.headmostlab.notes.Event;
import com.headmostlab.notes.R;
import com.headmostlab.notes.databinding.FragmentNoteBinding;
import com.headmostlab.notes.model.Note;
import com.headmostlab.notes.ui.Constants;

import java.text.DateFormat;
import java.util.Date;

public class NoteFragment extends Fragment {

    public static final String NOTE_KEY = "NOTE";
    private FragmentNoteBinding binding;
    private NoteViewModel viewModel;
    private boolean isPortrait;
    private OnBackPressedCallback onBackPressedCallback;
    private Note note;

    public static NoteFragment newNoteFragment(Note note) {
        NoteFragment fragment = new NoteFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTE_KEY, note);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this,
                new NoteViewModelFactory(requireActivity(), this, null)).get(NoteViewModelImpl.class);

        isPortrait = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT;

        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                deselectNote();
                getParentFragmentManager().popBackStack();
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (!isPortrait) {
            getParentFragmentManager().popBackStack();
        }
        binding = FragmentNoteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onBackPressedCallback.remove();
        binding = null;
    }

    private void deselectNote() {
        getParentFragmentManager().setFragmentResult(Constants.FRAGMENT_RESULT_DESELECT_NOTE, new Bundle());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            note = getArguments().getParcelable(NOTE_KEY);
            viewModel.setNote(note);
        }

        if (isPortrait) {
            requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), onBackPressedCallback);
        }

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker().build();
        picker.addOnPositiveButtonClickListener(selection ->
                binding.createDate.setText(DateFormat.getDateInstance().format(new Date(selection))));
        binding.createDate.setOnClickListener(v ->
                picker.show(getParentFragmentManager(), picker.toString()));

        if (note == null) {
            binding.deleteNoteButton.setVisibility(View.GONE);
        } else {
            setHasOptionsMenu(true);
            binding.deleteNoteButton.setOnClickListener(it -> deleteNoteWithConfirmation());
        }

        binding.saveNoteButton.setOnClickListener(it -> {
                    LiveData<Event<Integer>> resultLiveData = viewModel.save(
                            binding.title.getText().toString(),
                            binding.description.getText().toString(),
                            binding.createDate.getText().toString()
                    );
                    resultLiveData.observe(getViewLifecycleOwner(), event -> {
                                Integer content = event.getContentIfNotHandled();
                                if (content != null) {
                                    Toast.makeText(requireActivity(), getString(content), Toast.LENGTH_SHORT).show();
                                    deselectNote();
                                    if (isPortrait) {
                                        getParentFragmentManager().popBackStack();
                                    } else if (note == null) {
                                        getParentFragmentManager().beginTransaction().remove(this).commit();
                                    }
                                }
                            }
                    );
                }
        );

        viewModel.getSelectedNote().observe(getViewLifecycleOwner(), this::show);
        viewModel.getNoteToShare().observe(getViewLifecycleOwner(), this::share);
    }

    private void deleteNoteWithConfirmation() {
        new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.note_delete_warning_message)
                .setPositiveButton(R.string.btn_ok, (dialog, which) -> deleteNote())
                .setNegativeButton(R.string.btn_cancel, (dialog, which) -> {
                })
                .create().show();
    }

    private void deleteNote() {
        viewModel.deleteNote().observe(getViewLifecycleOwner(), event ->
                {
                    Integer content = event.getContentIfNotHandled();
                    if (content != null) {
                        Toast.makeText(requireActivity(), getString(content), Toast.LENGTH_SHORT).show();
                        deselectNote();
                        if (isPortrait) {
                            getParentFragmentManager().popBackStack();
                        } else {
                            getParentFragmentManager().beginTransaction().remove(this).commit();
                        }
                    }
                }
        );
    }

    public void show(Note note) {
        if (note != null) {
            binding.title.setText(note.getTitle());
            binding.description.setText(note.getDescription());
            binding.createDate.setText(DateFormat.getDateInstance().format(note.getCreationDate()));
        }
    }

    public void share(Note note) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, note.toHumanString());
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, getString(R.string.share));
        startActivity(shareIntent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.note_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            viewModel.share();
            return true;
        }
        return false;
    }
}
