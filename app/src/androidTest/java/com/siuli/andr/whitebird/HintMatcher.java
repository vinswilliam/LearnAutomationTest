package com.siuli.andr.whitebird;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.is;

/**
 * Created by william on 1/14/2016.
 */
public class HintMatcher {

    static Matcher<View> withHint(final String substring){
        return withHint(is(substring));
    }

    static Matcher<View> withHint(final Matcher<String> stringMatcher){
        checkNotNull(stringMatcher);

        return new BoundedMatcher<View, EditText>(EditText.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with hint: ");
                stringMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(EditText item) {
                final CharSequence hint = item.getHint();
                return hint != null && stringMatcher.matches(hint.toString());
            }
        };
    }
}
