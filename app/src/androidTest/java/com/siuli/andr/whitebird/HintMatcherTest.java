package com.siuli.andr.whitebird;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.siuli.andr.whitebird.addNote.AddNoteView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by william on 1/14/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HintMatcherTest {
    private static final String INVALID_STRING_TO_BE_TYPED = "Earl Grey";

    private static final String COFFEE_ENDING = "coffee?";

    private static final String COFFEE_INVALID_ENDING = "tea?";

    @Rule
    public ActivityTestRule<AddNoteView> addNoteViewActivityTestRule = new ActivityTestRule<>(AddNoteView.class);

    private String mStringWithValidEnding;

    private String mStringToBeTyped;

    @Before
    public void initValidStrings(){
        mStringWithValidEnding = "Random " + "coffee";

        mStringToBeTyped = "Espresso";
    }

    @Test
    public void hint_isDisplayedInEditText(){
        String hintText = addNoteViewActivityTestRule.getActivity().getResources().getString(R.string.note_title);

        onView(withId(R.id.et_note_title)).check(matches(HintMatcher.withHint(hintText)));
    }
}
