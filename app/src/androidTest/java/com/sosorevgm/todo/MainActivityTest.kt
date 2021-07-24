package com.sosorevgm.todo

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sosorevgm.todo.features.main.MainActivity
import com.sosorevgm.todo.utils.waitFor
import com.sosorevgm.todo.utils.withVectorDrawable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    companion object {
        private const val TASK_TEXT = "This is the text of the new task"
        private const val TASK_WITH_PRIORITY_TEXT = "!! This is the text of the new task"
        private const val UPDATED_TASK_TEXT = "Some new task description"
    }

    private lateinit var context: Context
    private lateinit var priorityHigh: String
    private lateinit var priorityDefault: String

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        priorityDefault = context.resources.getStringArray(R.array.priority_array)[0]
        priorityHigh = context.resources.getStringArray(R.array.priority_array)[2]
    }

    @Test
    fun tasksFragmentViewsDisplayedAtStart() {
        onView(withId(R.id.appbar)).check(matches(isDisplayed()))
        onView(withId(R.id.tasks_collapsing_toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_tasks_visibility)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_tasks_visibility)).check(matches(isDisplayed()))
        onView(withId(R.id.tasks_toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.tasks_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_add_new_task)).check(matches(isDisplayed()))
    }

    @Test
    fun createNewTaskAddedToScreen() {
        onView(withId(R.id.btn_add_new_task)).perform(click())
        onView(withId(R.id.et_task_description)).perform(
            typeTextIntoFocusedView(
                TASK_TEXT
            )
        )
        onView(withId(R.id.btn_chose_new_task_priority)).perform(click())
        onView(withText(priorityHigh)).perform(click())
        onView(withId(R.id.btn_save_new_task)).perform(click())
        onView(isRoot()).perform(waitFor(100))
        onView(withId(R.id.tv_task_description)).check(matches(withText(TASK_WITH_PRIORITY_TEXT)))
    }

    @Test
    fun updateTaskCheckBoxChanged() {
        onView(withId(R.id.task_holder_layout)).perform(
            swipeRight()
        )
        onView(withId(R.id.iv_task_checkbox)).check(matches(withVectorDrawable(R.drawable.icon_checkbox_green)))
    }

    @Test
    fun deleteTaskRemovedFromScreen() {
        onView(withId(R.id.task_holder_layout)).perform(
            swipeLeft()
        )
        onView(withId(R.id.task_holder_layout)).check(
            doesNotExist()
        )
    }

    @Test
    fun updateTaskPriorityAndText() {
        onView(withId(R.id.task_holder_layout)).perform(
            click()
        )
        onView(withId(R.id.et_task_description)).perform(
            clearText()
        )
        onView(withId(R.id.et_task_description)).perform(
            typeText(UPDATED_TASK_TEXT)
        )
        onView(withId(R.id.btn_chose_new_task_priority)).perform(click())
        onView(withText(priorityDefault)).perform(click())
        onView(withId(R.id.btn_save_new_task)).perform(click())
        onView(isRoot()).perform(waitFor(100))
        onView(withId(R.id.tv_task_description)).check(matches(withText(UPDATED_TASK_TEXT)))
    }
}