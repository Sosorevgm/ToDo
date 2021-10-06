package com.sosorevgm.todo.features.new_task

import androidx.annotation.VisibleForTesting
import app.cash.turbine.test
import com.sosorevgm.todo.features.tasks.TasksUseCase
import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.TaskPriority
import com.sosorevgm.todo.rules.MainCoroutineRule
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import kotlin.time.ExperimentalTime

@VisibleForTesting
class NewTaskViewModelTest {

    companion object {
        private const val NEW_TASK_DESC = "new task description"
        private val newTask = TaskModel("id", "text", TaskPriority.DEFAULT, false, 0L, 10000L, 0L)
    }

    @get:Rule
    @ExperimentalCoroutinesApi
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var tasksUseCase: TasksUseCase

    private lateinit var viewModel: NewTaskViewModel

    @Before
    fun setUp() {
        tasksUseCase = mock()
        viewModel = NewTaskViewModel(tasksUseCase)
    }

    @Test
    fun `oldTask must be null after init`() {
        assertEquals(true, viewModel.oldTask == null)
    }

    @Test
    fun `oldTask should be updated after setting the task`() {
        viewModel.oldTask = newTask
        assertEquals(true, viewModel.oldTask != null)
    }

    @Test
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    fun `switchEvent shared flow should emit true after switch click event`() {
        mainCoroutineRule.testDispatcher.runBlockingTest {
            viewModel.switchEvent.test {
                viewModel.switchClicked()
                assertEquals(true, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    fun `switchEvent shared flow should emit false after set date and switch click event`() {
        mainCoroutineRule.testDispatcher.runBlockingTest {
            viewModel.switchEvent.test {
                viewModel.setDate(11000000)
                viewModel.switchClicked()
                assertEquals(false, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test(expected = TimeoutCancellationException::class)
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    fun `navigationBack shared flow should not emit values after init`() {
        mainCoroutineRule.testDispatcher.runBlockingTest {
            viewModel.navigationBack.test {
                awaitComplete()
            }
        }
    }

    @Test
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    fun `navigationBack shared flow should be not null after save task action`() {
        mainCoroutineRule.testDispatcher.runBlockingTest {
            viewModel.navigationBack.test {
                viewModel.btnSaveClicked(NEW_TASK_DESC)
                assertEquals(Unit, awaitItem())
            }
        }
    }

    @Test
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    fun `navigationBack shared flow should be not null after delete task action`() {
        mainCoroutineRule.testDispatcher.runBlockingTest {
            viewModel.navigationBack.test {
                viewModel.deleteOldTask()
                assertEquals(Unit, awaitItem())
            }
        }
    }

    @Test
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    fun `dateFlow value should be equals 0 after init`() {
        mainCoroutineRule.testDispatcher.runBlockingTest {
            viewModel.dateFlow.test {
                assertEquals(0, awaitItem())
            }
        }
    }

    @Test
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    fun `dateFlow value should be not equals 0 after set date`() {
        mainCoroutineRule.testDispatcher.runBlockingTest {
            viewModel.dateFlow.test {
                viewModel.setDate(2021, 7, 23)
                assertEquals(true, expectMostRecentItem() != 0L)
            }
        }
    }
}