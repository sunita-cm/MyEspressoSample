package com.example.sunita.mysqliteroompersistencelibrary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.example.sunita.mysqliteroompersistencelibrary.activities.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.util.Checks.checkArgument;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private String newNoteDescription = "UI testing for Android";

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    /**
     * A custom {@link Matcher} which matches an item in a {@link RecyclerView} by its text.
     *
     * <p>
     * View constraints:
     * <ul>
     * <li>View must be a child of a {@link RecyclerView}
     * <ul>
     *
     * @param itemText the text to match
     * @return Matcher that matches text in the given view
     */
    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA RV with text " + itemText);
            }
        };
    }

    @Test
    public void clickAddNoteButton_opensAddNoteUi() {

        // Click on the add note button
        onView(withId(R.id.fab)).perform(click());

        // Check if the add note dialog is displayed
        onView(withId(R.id.tv_dialog_title)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void addNoteToNotesList_whenSaveClicked() {

        // Click on the add note button
        onView(withId(R.id.fab)).perform(click());

        // Add new note
        onView(withId(R.id.et_note)).perform(typeText(newNoteDescription), closeSoftKeyboard());

        // click on save
        onView(withId(R.id.tv_save)).perform(click());

        // Scroll notes list to added note, by finding its text
        onView(withId(R.id.rv_notes)).perform(scrollTo(hasDescendant(withText(newNoteDescription))));

        // Verify note is displayed on screen
        onView(withItemText(newNoteDescription)).check(matches(isDisplayed()));
    }

    @Test
    public void doNotAddNoteToNotesList_whenCancelClicked() {

        // Click on the add note button
        onView(withId(R.id.fab)).perform(click());

        // click on cancel button
        onView(withId(R.id.tv_cancel)).perform(click());

        // Verify dialog closed and notes list is displayed on screen
        onView(withId(R.id.rv_notes)).check(matches(isDisplayed()));
    }

    @Test
    public void deleteNoteFromNoteList_whenSwipeLeft() {

        // Click on the add note button
        onView(withId(R.id.fab)).perform(click());

        // Add new note
        onView(withId(R.id.et_note)).perform(typeText(newNoteDescription), closeSoftKeyboard());

        // Save the note
        onView(withId(R.id.tv_save)).perform(click());

        // swipe left to delete note
        onView(withId(R.id.rv_notes)).perform(swipeLeft());

        //verify that note does not exist after deletion
        onView(withId(R.id.rv_notes)).check(matches(hasDescendant(withText(newNoteDescription))));
    }

    @Test
    public void longPressOnNote_openUpdateNoteUI() {

        // long press on the note
        onView(withId(R.id.rv_notes)).perform(longClick());

        // Check if the update note dialog is displayed
        onView(withId(R.id.tv_editnote)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void updateNoteToNotesList_whenUpdateClicked() {

        String updatedNote = "Updated Note";

        // long press on the note
        onView(withId(R.id.rv_notes)).perform(longClick());

        // update note
        onView(withId(R.id.et_updatenote)).perform(clearText(), typeText(updatedNote), closeSoftKeyboard());

        // click on update
        onView(withId(R.id.tv_update)).perform(click());

        // Scroll notes list to updated note, by finding its description
        onView(withId(R.id.rv_notes)).perform(scrollTo(hasDescendant(withText(updatedNote))));

        // Verify updated note is displayed on screen
        onView(withItemText(updatedNote)).check(matches(isDisplayed()));
    }

    @Test
    public void doNotUpdateNoteToNotesList_whenCancelClicked() {

        // long press on the note
        onView(withId(R.id.rv_notes)).perform(longClick());

        // click on cancel button
        onView(withId(R.id.tv_cancel)).perform(click());

        // Verify dialog closed and notes list is displayed on screen
        onView(withId(R.id.rv_notes)).check(matches(isDisplayed()));
    }

    @Test
    public void errorShownOnEmptyNote_forAddingNewNote() {

        onView(withId(R.id.fab)).perform(click());

        // Save the note
        onView(withId(R.id.tv_save)).perform(click());

        // Verify empty notes toast is shown
        onView(withText(R.string.enternote)).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void errorShownOnEmptyNote_forUpdatingNote() {

        onView(withId(R.id.rv_notes)).perform(longClick());

        // Save the note
        onView(withId(R.id.tv_update)).perform(click());

        // Verify empty notes toast is shown
        onView(withText(R.string.enternote)).inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }
}
