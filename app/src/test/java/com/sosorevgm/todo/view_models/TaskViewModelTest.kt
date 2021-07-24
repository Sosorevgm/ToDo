package com.sosorevgm.todo.view_models

import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sosorevgm.todo.domain.account.AccountManager
import com.sosorevgm.todo.features.tasks.TasksUseCase
import com.sosorevgm.todo.features.tasks.TasksViewModel
import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.TaskPriority
import com.sosorevgm.todo.rules.CoroutinesDispatcherRule
import com.sosorevgm.todo.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.util.concurrent.TimeoutException

@VisibleForTesting
class TaskViewModelTest {

    companion object {
        private val notDoneTask = TaskModel(
            "1",
            "not done task",
            TaskPriority.DEFAULT,
            false,
            0,
            1200000,
            0
        )

        private val existingTask = TaskModel(
            "2",
            "existing task",
            TaskPriority.DEFAULT,
            true,
            0,
            1200000,
            0
        )

        private val cachedTasksList = listOf(notDoneTask)
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    @ExperimentalCoroutinesApi
    val coroutinesDispatcherRule = CoroutinesDispatcherRule()

    @Mock
    private lateinit var accountManager: AccountManager

    @Mock
    private lateinit var useCase: TasksUseCase

    private lateinit var viewModel: TasksViewModel

    @Before
    fun setUp() {
        accountManager = mock()
        Mockito.`when`(accountManager.tasksVisibility).thenReturn(false)

        useCase = mock {
            onGeneric {
                runBlocking {
                    getTasksToDo()
                }
            } doReturn (cachedTasksList.filter { !it.done })
        }
        Mockito.`when`(useCase.getTasksFromCache()).thenReturn(flowOf(cachedTasksList))

        viewModel = TasksViewModel(accountManager, useCase)
    }

    @Test
    fun `visibility live data value should be false after init`() {
        assertEquals(viewModel.tasksVisibility.getOrAwaitValue(), false)
    }

    @Test
    fun `visibility live data value should be true after tasks visibility click`() {
        viewModel.tasksVisibilityClick()
        assertEquals(viewModel.tasksVisibility.getOrAwaitValue(), true)
    }

    @Test
    fun `completed task live data value should be equals 0 after init`() {
        assertEquals(viewModel.completedTasks.getOrAwaitValue(), 0)
    }

    @Test
    fun `tasks view data should contains header, task and end items after init`() {
        assertEquals(viewModel.tasks.getOrAwaitValue().size == 3, true)
    }

    @Test(expected = TimeoutException::class)
    fun `navigation live data value should be null after init`() {
        viewModel.navigation.getOrAwaitValue()
    }

    @Test
    fun `navigation live data value should contain an event with a nullable task after a new task action happened`() {
        viewModel.onNewTaskClick()
        assertEquals(viewModel.navigation.getOrAwaitValue().task == null, true)
    }

    @Test
    fun `navigation live data value should contain an event with non-nullable task after the task click action happened`() {
        viewModel.onTaskClick(existingTask)
        assertEquals(viewModel.navigation.getOrAwaitValue().task != null, true)
    }
}