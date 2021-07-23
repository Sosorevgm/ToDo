package com.sosorevgm.todo.view_models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sosorevgm.todo.features.new_task.NewTaskViewModel
import com.sosorevgm.todo.features.tasks.TasksUseCase
import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.TaskPriority
import com.sosorevgm.todo.rules.CoroutinesDispatcherRule
import com.sosorevgm.todo.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeoutException

class NewTaskViewModelTest {

    companion object {
        val oldTask = TaskModel(
            "1",
            "old task",
            TaskPriority.DEFAULT,
            false,
            0,
            1200000,
            0
        )
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    @ExperimentalCoroutinesApi
    val coroutinesDispatcherRule = CoroutinesDispatcherRule()

    @Mock
    private lateinit var tasksUseCase: TasksUseCase

    private lateinit var viewModel: NewTaskViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = NewTaskViewModel(tasksUseCase)
    }

    @Test
    fun `old task must be null after init`() {
        assertEquals(viewModel.oldTask == null, true)
    }

    @Test
    fun `old task should be updated after setting the task`() {
        viewModel.oldTask = oldTask
        assertEquals(viewModel.oldTask != null, true)
    }

    @Test
    fun `switch live data value should be equals true after switch click event`() {
        viewModel.switchClicked()
        assertEquals(viewModel.switchEvent.getOrAwaitValue() == true, true)
    }

    @Test
    fun `switch live data value should be equals false after set date and switch click event`() {
        viewModel.setDate(11000000)
        viewModel.switchClicked()
        assertEquals(viewModel.switchEvent.getOrAwaitValue() == false, true)
    }

    @Test(expected = TimeoutException::class)
    fun `navigation live data value should null after init`() {
        viewModel.navigationBack.getOrAwaitValue()
    }

    @Test
    fun `navigation live data value should be not null after save task action`() {
        viewModel.btnSaveClicked("new task description")
        viewModel.navigationBack.getOrAwaitValue()
    }

    @Test
    fun `navigation live data value should be not null after delete task action`() {
        viewModel.deleteOldTask()
        viewModel.navigationBack.getOrAwaitValue()
    }

    @Test(expected = TimeoutException::class)
    fun `date live data should be null after init`() {
        viewModel.dateLiveData.getOrAwaitValue()
    }

    @Test
    fun `date live data should be not null after set date`() {
        viewModel.setDate(2021, 7, 23)
        assertEquals(viewModel.dateLiveData.getOrAwaitValue() != null, true)
    }
}