package ui.note

import ui.main.extentions.getColorInt
import android.graphics.drawable.ColorDrawable
import android.service.autofill.Validators.not
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.common.base.CharMatcher.`is`
import com.google.common.base.Verify.verify
import data.model.Color
import data.model.Note
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext
import org.koin.standalone.StandAloneContext.loadKoinModules
import ru.geekbrains.kotlin.R

class NoteActivityTest : TestCase() {
    private val viewModel: NoteViewModel = spyk(NoteViewModel(MockK()))
    private val viewStateLiveData = MutableLiveData<NoteViewState>()
    private val testNote = Note("333", "title", "body")

    @get:Rule
    val activityTestRule = ActivityTestRule(NoteActivity::class.java, true, false)

    @Before
    fun setUp() {
        loadKoinModules(listOf(
                module {
                    viewModel { viewModel }
                }))

        every { viewModel.getViewState() } returns viewStateLiveData
        every { viewModel.loadNote(any()) } just runs
        every { viewModel.saveChanges(any()) } just runs
        every { viewModel.deleteNote() } just runs

        Intent().apply {
            putExtra(NoteActivity::class.java.name + "extra.NOTE_ID", testNote.id)
        }.let {
            activityTestRule.launchActivity(it)
        }
    }

    @After
    fun tearDown() {
        StandAloneContext.stopKoin()
    }

    @Test
    fun should_show_color_picker() {
        onView(withId(R.id.palette)).perform(click())
        onView(withId(R.id.colorPicker)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun should_hide_color_picker() {
        onView(withId(R.id.palette)).perform(click()).perform(click())
        onView(withId(R.id.colorPicker)).check(matches(not(isDisplayed())))
    }

    @Test
    fun should_set_toolbar_color() {
        onView(withId(R.id.palette)).perform(click())
        onView(withTagValue(`is`(Color.BLUE))).perform(click())

        val colorInt = Color.BLUE.getColorInt(activityTestRule.activity)

        onView(withId(R.id.toolbar)).check { view, _ ->
            assertTrue("toolbar background color does not match",
                    (view.background as? ColorDrawable)?.color == colorInt)
        }
    }

    @Test
    fun should_call_viewModel_loadNote() {
        verify(exactly = 1) { viewModel.loadNote(testNote.id) }
    }

    @Test
    fun should_show_note() {
        activityTestRule.launchActivity(null)
        viewStateLiveData.postValue(NoteViewState(NoteViewState.Data(note = testNote)))

        onView(withId(R.id.titleEt)).check(matches(withText(testNote.title)))
        onView(withId(R.id.bodyEt)).check(matches(withText(testNote.body)))
    }

    @Test
    fun should_call_saveNote() {
        onView(withId(R.id.titleEt)).perform(typeText(testNote.title))
        verify(timeout = 1000) { viewModel.saveChanges(any()) }
    }

    @Test
    fun should_call_deleteNote() {
        openActionBarOverflowOrOptionsMenu(activityTestRule.activity)
        onView(withText(R.string.delete_menu_title)).perform(click())
        onView(withText(R.string.ok_bth_title)).perform(click())
        verify { viewModel.deleteNote() }
    }


}