package com.siuli.andr.whitebird;

import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.siuli.andr.whitebird.addNote.AddNoteView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by william on 1/14/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivityInstrumentTestCase {

    public static final String STRING_TO_BE_TYPED = "Espresso Test";

    @Rule
    public ActivityTestRule<AddNoteView> addNoteViewRule = new ActivityTestRule<>(AddNoteView.class);

    @Test
    public void writeNoteTitle(){
        onView(withId(R.id.et_note_title)).perform(new ViewAction[]{typeText(STRING_TO_BE_TYPED)});
        onView(withId(R.id.et_note_title)).check(matches(withText(STRING_TO_BE_TYPED)));
    }


}
