

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Dialog;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.mapquest.navigation.sampleapp.R;
import com.mapquest.navigation.sampleapp.activity.RouteSelectionActivity;
import com.mapquest.navigation.sampleapp.searchahead.SearchBarView;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withInputType;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.startsWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class iTest {

    private String mStringToBetyped;

    @Rule
    public ActivityTestRule<RouteSelectionActivity> mActivityRule = new ActivityTestRule<>(
            RouteSelectionActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        mStringToBetyped = "Espresso";
    }

    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.
//        onView(withId(R.id.editTextUserInput))
////                .perform(typeText(mStringToBetyped), closeSoftKeyboard());
////        onView(withId(R.id.changeTextBt)).perform(click());
////
////        // Check that the text was changed.
////        onView(withId(R.id.textToBeChanged))
////                .check(matches(withText(mStringToBetyped)));

        onView(withText("OK")).perform(click());
        onView(withId(R.id.origin))
                .perform(click());
        onView(withId(R.id.fake_search_bar_view))
                .perform(click());
        onView(first(withId(R.id.text_entry))).perform(typeText("orlando"));
        //onView( first(withText( "Love Song")) ).perform( click() );
        onView(withId(R.id.searchAheadListView)).check(matches(isDisplayed()));
        onData(startsWith("or"))
                .inAdapterView(withId(R.id.searchAheadListView))
                .atPosition(0)
                .perform(click());

    }

    public static Matcher<View> first(final Matcher<View> expected ){

        return new TypeSafeMatcher<View>() {
            private boolean first = false;

            @Override
            protected boolean matchesSafely(View item) {

                if( expected.matches(item) && !first ){
                    return first = true;
                }

                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Matcher.first( " + expected.toString() + " )" );
            }
        };
    }

};

