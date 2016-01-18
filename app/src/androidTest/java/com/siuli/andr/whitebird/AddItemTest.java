package com.siuli.andr.whitebird;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.siuli.andr.whitebird.listNotes.ListNoteView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.core.Is.is;

/**
 * Created by william on 1/18/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddItemTest {

    @Rule
    public ActivityTestRule<ListNoteView> mListNoteView = new ActivityTestRule<>(ListNoteView.class);

    private static DataInteraction onRow(String str){
        return onData(hasEntry(equalTo("ROW_TEXT"), is(str)));
    }

    @Test
    public void lastItem_notDisplayed(){
        onView(withText("tes1")).check(doesNotExist());
    }

    @Test
    public void checkItem_exist(){
    }

    private final String TITLE_TO_ADD = "Title 123 Abc yoyooo";
    private final String DESC_TO_ADD = "Desc to add";
    private final int ITEM_POSITION = 0;

    @Test
    public void add_item(){
        onView(withId(R.id.fab_add_note)).perform(click());

        onView(withId(R.id.et_note_title)).perform(typeText(TITLE_TO_ADD), closeSoftKeyboard());
        onView(withId(R.id.et_note_desc)).perform(typeText(DESC_TO_ADD), closeSoftKeyboard());
        onView(withId(R.id.action_save)).perform(click());

    }

    @Test
    public void item_performClick(){
        onView(withId(R.id.rv_note)).perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_POSITION, click()));

        onView(withId(R.id.tv_title_detail)).check(matches(withText(TITLE_TO_ADD)));
        onView(withId(R.id.tv_description_detail)).check(matches(withText(DESC_TO_ADD)));
    }




}
