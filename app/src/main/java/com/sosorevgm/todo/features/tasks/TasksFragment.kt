package com.sosorevgm.todo.features.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosorevgm.todo.R
import com.sosorevgm.todo.databinding.FragmentTasksBinding
import com.sosorevgm.todo.models.TASK_BUNDLE
import com.sosorevgm.todo.models.TaskModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class TasksFragment : DaggerFragment(), TaskRVAdapter.IListener, TaskTouchHelper.IListener,
    View.OnClickListener {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskAdapter: TaskRVAdapter
    private lateinit var taskTouchHelper: TaskTouchHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)
            .get(TasksViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        taskAdapter = TaskRVAdapter(this)
        binding.tasksRecyclerView.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }
        taskTouchHelper = TaskTouchHelper(this)
        ItemTouchHelper(taskTouchHelper).attachToRecyclerView(binding.tasksRecyclerView)
        binding.btnAddNewTask.setOnClickListener(this)
        binding.toolbar.btnTasksVisibility.setOnClickListener(this)
        binding.toolbar.collapsingToolbar.setOnClickListener(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.tasksVisibility.observe(viewLifecycleOwner) { isVisible ->
            if (isVisible) {
                binding.toolbar.ivTasksVisibility.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.icon_visibility
                    )
                )
            } else {
                binding.toolbar.ivTasksVisibility.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.icon_visibility_off
                    )
                )
            }
        }

        viewModel.completedTasks.observe(viewLifecycleOwner) {
            binding.toolbar.root.setTasks(it)
        }

        viewModel.allTasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onTaskCheckboxClicked(task: TaskModel) {
        viewModel.onTaskCheckboxClicked(task)
    }

    override fun onTaskClicked(task: TaskModel) {
        findNavController().navigate(
            R.id.new_task_screen,
            Bundle().apply { putParcelable(TASK_BUNDLE, task) })
    }

    override fun taskDoneSwipe(position: Int) {
        val task = taskAdapter.currentList[position] as TaskViewData.Task
        viewModel.taskDoneSwipe(task.toTaskModel())
    }

    override fun taskDeleteSwipe(position: Int) {
        val task = taskAdapter.currentList[position] as TaskViewData.Task
        viewModel.taskDeleteSwipe(task.toTaskModel())
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.collapsing_toolbar -> {
                binding.tasksRecyclerView.layoutManager?.smoothScrollToPosition(
                    binding.tasksRecyclerView,
                    null,
                    0
                )
            }
            R.id.btn_add_new_task -> findNavController().navigate(R.id.new_task_screen)
            R.id.btn_tasks_visibility -> viewModel.tasksVisibilityClicked()
        }
    }
}