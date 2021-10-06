package com.sosorevgm.todo.features.tasks

import androidx.annotation.VisibleForTesting
import app.cash.turbine.test
import com.sosorevgm.todo.domain.account.AccountManager
import com.sosorevgm.todo.domain.navigation.Navigation
import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.TaskPriority
import com.sosorevgm.todo.rules.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@VisibleForTesting
class TaskViewModelTest {

    companion object {
        private val newTask = TaskModel("id", "text", TaskPriority.DEFAULT, false, 0L, 10000L, 0L)
    }

    @get:Rule
    @ExperimentalCoroutinesApi
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var accountManager: AccountManager

    @Mock
    private lateinit var useCase: TasksUseCase

    private lateinit var viewModel: TasksViewModel

    @Before
    @ExperimentalCoroutinesApi
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(useCase.getTasksFromCache()).thenReturn(flowOf(emptyList()))
        viewModel = TasksViewModel(accountManager, useCase)
    }

    @Test
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    fun `visibility state flow value should be false after init`() {
        mainCoroutineRule.testDispatcher.runBlockingTest {
            viewModel.tasksVisibility.test {
                assertEquals(false, awaitItem())
            }
        }
    }

    @Test
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    fun `visibility state flow value should be true after tasks visibility click`() {
        viewModel.tasksVisibilityClick()
        mainCoroutineRule.testDispatcher.runBlockingTest {
            viewModel.tasksVisibility.test {
                assertEquals(true, awaitItem())
            }
        }
    }

    @Test
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    fun `completed task state flow value should be equals 0 after init`() {
        mainCoroutineRule.testDispatcher.runBlockingTest {
            viewModel.completedTasks.test {
                assertEquals(0, awaitItem())
            }
        }
    }

    @Test
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    fun `tasks view data state flow should contains header and ending items after init`() {
        mainCoroutineRule.testDispatcher.runBlockingTest {
            viewModel.tasks.test {
                assertEquals(true, awaitItem().size == 2)
            }
        }
    }

    @Test(expected = TimeoutCancellationException::class)
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    fun `navigation shared flow should not emit value after init`() {
        mainCoroutineRule.testDispatcher.runBlockingTest {
            viewModel.navigation.test(Duration.Companion.microseconds(300)) {
                assertEquals(null, awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    fun `navigation shared flow should emit an event with a nullable task after a new task action happened`() {
        mainCoroutineRule.testDispatcher.runBlockingTest {
            viewModel.navigation.test {
                viewModel.onNewTaskClick()
                assertEquals(Navigation.Event(Navigation.Screen.NEW_TASK, null), awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    fun `navigation shared flow should emit an event with a not nullable task after the task click action happened`() {
        mainCoroutineRule.testDispatcher.runBlockingTest {
            viewModel.navigation.test {
                viewModel.onTaskClick(newTask)
                assertEquals(
                    Navigation.Event(Navigation.Screen.NEW_TASK, newTask),
                    awaitItem()
                )
                cancelAndConsumeRemainingEvents()
            }
        }
    }
}