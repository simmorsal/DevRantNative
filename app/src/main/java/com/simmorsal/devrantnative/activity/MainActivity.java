package com.simmorsal.devrantnative.activity;

import android.app.Activity;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.simmorsal.devrantnative.R;
import com.simmorsal.devrantnative.fragment.CollabsFragment;
import com.simmorsal.devrantnative.fragment.MoreFragment;
import com.simmorsal.devrantnative.fragment.NotifsFragment;
import com.simmorsal.devrantnative.fragment.RantsFragment;
import com.simmorsal.devrantnative.fragment.StoriesFragment;
import com.simmorsal.devrantnative.utils.Tools;
import com.simmorsal.recolor_project.ReColor;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Activity context;
    ViewGroup rootView;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    RelativeLayout relRants, relStories, relCollabs, relNotifs, relMore;
    View viewRants, viewStories, viewCollabs, viewNotifs, viewMore;
    FloatingActionButton btnFab;

    final String fragment1 = "Rants", fragment2 = "Stories", fragment3 = "Collabs",
            fragment4 = "Notifs", fragment5 = "More";

    String oldTag = fragment1;
    int animationsSpeed = 280;
    int enoughXTranslationForFab = 0;
    int enoughYTranslationForFab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializer();
        runFirstFragment();
        fragmentChanger();
        calculateEnoughTranslationForFab();
    }

    private void initializer() {
        context = MainActivity.this;
        rootView = findViewById(R.id.rootView);
        fragmentManager = getSupportFragmentManager();
        relRants = findViewById(R.id.relRants);
        relStories = findViewById(R.id.relStories);
        relCollabs = findViewById(R.id.relCollabs);
        relNotifs = findViewById(R.id.relNotifs);
        relMore = findViewById(R.id.relMore);
        viewRants = findViewById(R.id.viewRants);
        viewStories = findViewById(R.id.viewStories);
        viewCollabs = findViewById(R.id.viewCollabs);
        viewNotifs = findViewById(R.id.viewNotifs);
        viewMore = findViewById(R.id.viewMore);
        btnFab = findViewById(R.id.btnFab);
    }

    private void runFirstFragment() {
//        setFragment(new RantsFragment(), fragment1);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frlViewPager, new RantsFragment(), fragment1).addToBackStack(fragment1);
        fragmentTransaction.commit();
    }

    private void calculateEnoughTranslationForFab() {
        btnFab.post(new Runnable() {
            @Override
            public void run() {
                int screenWidth = Tools.getScreenWidth(context);
                int btnFabWidth = btnFab.getWidth();

                enoughXTranslationForFab = (int) ((screenWidth / 2) - ((btnFabWidth / 2) + (screenWidth * .05f)));

                enoughYTranslationForFab = Tools.dpToPx(90);
            }
        });
    }

    private void fragmentChanger() {
        relRants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new RantsFragment(), fragment1);
            }
        });
        relStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new StoriesFragment(), fragment2);
            }
        });
        relCollabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new CollabsFragment(), fragment3);
            }
        });
        relNotifs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new NotifsFragment(), fragment4);
            }
        });
        relMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new MoreFragment(), fragment5);
            }
        });
    }

    public void setFragment(Fragment fragment, String tag) {
        fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentDisplayingFragment = new Fragment();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment o : fragments) {
                if (o != null && o.isVisible())
                    currentDisplayingFragment = o;
            }
        }

        if (currentDisplayingFragment.getTag().equals(tag))
            return;

        // animating fragment transition
        getTransactionAnimation(currentDisplayingFragment, tag);

        // animating view under bottom navigation icons
        animateViewUnderButtons(tag);

        // repositioning fab
        repositionFab(tag);

        fragmentTransaction.replace(R.id.frlViewPager, fragment, tag).addToBackStack(tag);
        fragmentTransaction.commit();
    }

    private void getTransactionAnimation(Fragment currentDisplayingFragment, String tag) {


        if (currentDisplayingFragment.getTag().equals(fragment5))
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        else if (currentDisplayingFragment.getTag().equals(fragment4)) {
            if (tag.equals(fragment5)) {
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        } else if (currentDisplayingFragment.getTag().equals(fragment3)) {
            if (tag.equals(fragment5) || tag.equals(fragment4)) {
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        } else if (currentDisplayingFragment.getTag().equals(fragment2)) {
            if (tag.equals(fragment3) || tag.equals(fragment4) || tag.equals(fragment5)) {
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        } else if (currentDisplayingFragment.getTag().equals(fragment1)) {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        }
    }


    private void animateViewUnderButtons(String newTag) {

        View viewOld = null, viewNew = null;
        ImageView ivOld = null, ivNew = null;

        switch (oldTag){
            case fragment1:
                viewOld = viewRants;
                ivOld = (ImageView) relRants.getChildAt(1);
                break;

            case fragment2:
                viewOld = viewStories;
                ivOld = (ImageView) relStories.getChildAt(1);
                break;

            case fragment3:
                viewOld = viewCollabs;
                ivOld = (ImageView) relCollabs.getChildAt(1);
                break;

            case fragment4:
                viewOld = viewNotifs;
                ivOld = (ImageView) relNotifs.getChildAt(1);
                break;

            case fragment5:
                viewOld = viewMore;
                ivOld = (ImageView) relMore.getChildAt(1);
        }

        switch (newTag){
            case fragment1:
                viewNew = viewRants;
                ivNew = (ImageView) relRants.getChildAt(1);
                break;

            case fragment2:
                viewNew = viewStories;
                ivNew = (ImageView) relStories.getChildAt(1);
                break;

            case fragment3:
                viewNew = viewCollabs;
                ivNew = (ImageView) relCollabs.getChildAt(1);
                break;

            case fragment4:
                viewNew = viewNotifs;
                ivNew = (ImageView) relNotifs.getChildAt(1);
                break;

            case fragment5:
                viewNew = viewMore;
                ivNew = (ImageView) relMore.getChildAt(1);
        }

        oldTag = newTag;

        ViewGroup.LayoutParams layoutParamsOld, layoutParamsNew;
        layoutParamsOld = viewOld.getLayoutParams();
        layoutParamsNew = viewNew.getLayoutParams();

        layoutParamsOld.height = 0;
        layoutParamsNew.height = ViewGroup.LayoutParams.MATCH_PARENT;

        TransitionManager.beginDelayedTransition(rootView);
        viewOld.setLayoutParams(layoutParamsOld);
        viewNew.setLayoutParams(layoutParamsNew);

        new ReColor(context).setImageViewColorFilter(ivOld, "FF8A65", "eeeeee", animationsSpeed*2/3);
        new ReColor(context).setImageViewColorFilter(ivNew, "eeeeee", "FF8A65", animationsSpeed*2/3);
    }

    private void repositionFab(String tag) {

        switch (tag){
            case fragment1:
                setFabTranslation(0, 0);
                break;

            case fragment2:
                setFabTranslation(enoughXTranslationForFab, 0);
                break;

            case fragment3:
                if ((int) btnFab.getTranslationY() != enoughYTranslationForFab)
                    setFabTranslation((int) btnFab.getTranslationX(), enoughYTranslationForFab);
                break;

            case fragment4:
                if ((int) btnFab.getTranslationY() != enoughYTranslationForFab)
                    setFabTranslation((int) btnFab.getTranslationX(), enoughYTranslationForFab);
                break;

            case fragment5:
                if ((int) btnFab.getTranslationY() != enoughYTranslationForFab)
                    setFabTranslation((int) btnFab.getTranslationX(), enoughYTranslationForFab);
                break;
        }
    }

    private void setFabTranslation(int x, int y){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            btnFab.animate().translationX(x).translationY(y).setDuration(animationsSpeed).withLayer();
        else {
            btnFab.setTranslationX(x);
            btnFab.setTranslationY(y);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (oldTag.equals(fragment1))
            finish();
        else
            setFragment(new RantsFragment(), fragment1);
    }
}
