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
import com.sosorevgm.todo.domain.navigation.Navigation
import com.sosorevgm.todo.features.tasks.recycler.TaskRVAdapter
import com.sosorevgm.todo.features.tasks.recycler.TaskTouchHelper
import com.sosorevgm.todo.features.tasks.recycler.TaskViewData
import com.sosorevgm.todo.features.tasks.recycler.toTaskModel
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

    private val viewModel by lazy(mode = LazyThreadSafetyMode.NONE) {
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
        binding.appbar.tasksToolbar.setOnClickListener(this)
        binding.appbar.btnTasksVisibility.setOnClickListener(this)
        binding.appbar.tasksCollapsingToolbar.apply {
            setExpandedTitleTextAppearance(R.style.ExpandedToolbarTitle)
            setCollapsedTitleTextAppearance(R.style.CollapsedToolbarTitle)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.tasksVisibility.observe(viewLifecycleOwner) { isVisible ->
            if (isVisible) {
                binding.appbar.ivTasksVisibility.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.icon_visibility
                    )
                )
            } else {
                binding.appbar.ivTasksVisibility.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.icon_visibility_off
                    )
                )
            }
        }

        viewModel.completedTasks.observe(viewLifecycleOwner) {
            binding.appbar.tvToolbarTasksDone.text =
                requireContext().getString(R.string.tasks_done, it)
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

        viewModel.navigation.observe(viewLifecycleOwner) { event ->
            when (event.screen) {
                Navigation.Screen.NEW_TASK -> {
                    val bundle = Bundle()
                    if (event.task != null) bundle.putParcelable(TASK_BUNDLE, event.task)
                    findNavController().navigate(R.id.new_task_screen, bundle)
                }
            }
        }
    }

    override fun onTaskCheckboxClick(task: TaskModel) {
        viewModel.onTaskCheckboxClick(task)
    }

    override fun onTaskClick(task: TaskModel) {
        viewModel.onTaskClick(task)
    }

    override fun onNewTaskClick() {
        viewModel.onNewTaskClick()
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
            R.id.tasks_toolbar -> {
                binding.tasksRecyclerView.layoutManager?.smoothScrollToPosition(
                    binding.tasksRecyclerView,
                    null,
                    0
                )
                binding.appbar.root.setExpanded(true)
            }
            R.id.btn_add_new_task -> viewModel.onNewTaskClick()
            R.id.btn_tasks_visibility -> viewModel.tasksVisibilityClick()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}