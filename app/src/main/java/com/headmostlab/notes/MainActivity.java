package com.headmostlab.notes;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.headmostlab.notes.databinding.ActivityMainBinding;
import com.headmostlab.notes.ui.about.AboutFragment;
import com.headmostlab.notes.ui.notelist.NoteListFragment;

public class MainActivity extends AppCompatActivity {

    public static final String FRAGMENT_MAIN = "main";
    public static final String FRAGMENT_ABOUT = "about";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        if (savedInstanceState != null) {
            return;
        }
        navigate(NoteListFragment.newNoteListFragment(), FRAGMENT_MAIN, false);
    }

    private void navigate(Fragment fragment, String tag, boolean addToBackStack) {
        if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
            return;
        }
        FragmentTransaction tran = getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true);
        tran.replace(R.id.container, fragment, tag);
        if (addToBackStack) {
            tran.addToBackStack(null);
        }
        tran.commit();
    }

    private void initView() {
        initToolBar();
        initDrawer();
    }

    private void initToolBar() {
        setSupportActionBar(binding.appBarMain.toolbar);
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                binding.drawer, binding.appBarMain.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(item -> {
            if (navigateFragment(item.getItemId())) {
                binding.drawer.closeDrawer(GravityCompat.START);
            }
            return false;
        });
    }

    private boolean navigateFragment(int menuId) {
        if (menuId == R.id.action_about) {
            navigate(new AboutFragment(), FRAGMENT_ABOUT, true);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
