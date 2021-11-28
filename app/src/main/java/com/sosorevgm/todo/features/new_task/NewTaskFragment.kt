package com.sosorevgm.todo.features.new_task

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sosorevgm.todo.R
import com.sosorevgm.todo.application.TodoApp
import com.sosorevgm.todo.databinding.FragmentNewTaskBinding
import com.sosorevgm.todo.extensions.getTaskDate
import com.sosorevgm.todo.extensions.launchWhenStarted
import com.sosorevgm.todo.models.TASK_BUNDLE
import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.TaskPriority
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject

class NewTaskFragment : Fragment(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy(mode = LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory)
            .get(NewTaskViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as TodoApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        binding.appbar.btnBack.setOnClickListener(this)
        binding.btnChoseNewTaskPriority.setOnClickListener(this)
        binding.appbar.btnSaveNewTask.setOnClickListener(this)
        binding.taskTimingSwitch.setOnClickListener(this)
        binding.newTaskSpinner.onItemSelectedListener = this

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.priority_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.newTaskSpinner.adapter = adapter
        }

        val task: TaskModel? = arguments?.getParcelable(TASK_BUNDLE)
        if (task != null) {
            setOldTask(task)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.oldTask == null) setDescriptionFocus()

        launchWhenStarted {
            viewModel.switchEvent.collect(::handleSwitchEvent)
        }

        launchWhenStarted {
            viewModel.dateFlow.collect(::handleDate)
        }

        launchWhenStarted {
            viewModel.navigationBack.collect {
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> {
                (view as TextView?)?.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gray
                    )
                )
                viewModel.setPriority(TaskPriority.DEFAULT)
            }
            1 -> {
                (view as TextView?)?.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.text_color
                    )
                )
                viewModel.setPriority(TaskPriority.LOW)
            }
            2 -> {
                (view as TextView?)?.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                viewModel.setPriority(TaskPriority.HIGH)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // do nothing
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> findNavController().navigateUp()
            R.id.btn_chose_new_task_priority -> binding.newTaskSpinner.performClick()
            R.id.btn_save_new_task -> {
                val description = binding.etTaskDescription.text.toString()
                if (description.isNotEmpty()) {
                    viewModel.btnSaveClicked(description)
                }
            }
            R.id.task_timing_switch -> {
                viewModel.switchClicked()
            }
        }
    }

    private fun handleSwitchEvent(needToSetDate: Boolean) {
        if (needToSetDate) {
            showDatePickerDialog()
        } else {
            binding.taskTimingSwitch.isChecked = false
        }
    }

    private fun handleDate(date: Long) {
        if (date == 0L) {
            binding.tvNewTaskDate.text = ""
        } else {
            binding.tvNewTaskDate.text = getTaskDate(date)
        }
    }

    private fun setOldTask(task: TaskModel) {
        viewModel.oldTask = task
        binding.etTaskDescription.setText(task.text, TextView.BufferType.EDITABLE)
        val selection = task.priority.getSpinnerSelection()
        binding.newTaskSpinner.setSelection(selection)
        viewModel.setPriority(task.priority)
        if (task.deadline != 0L) {
            binding.tvNewTaskDate.text = getTaskDate(task.deadline)
            binding.taskTimingSwitch.isChecked = true
            viewModel.setDate(task.deadline)
        }
        binding.ivDeleteTask.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.icon_delete_red
            )
        )
        binding.tvNewTaskDelete.setTextColor(Color.RED)
        binding.btnDeleteTask.setOnClickListener {
            viewModel.deleteOldTask()
        }
    }

    private fun setDescriptionFocus() {
        binding.etTaskDescription.requestFocus()
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etTaskDescription, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun showDatePickerDialog() {
        val cal: Calendar = Calendar.getInstance()
        val mYear = cal.get(Calendar.YEAR)
        val mMonth = cal.get(Calendar.MONTH)
        val mDay = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, y, m, d ->
                viewModel.setDate(y, m, d)
                binding.taskTimingSwitch.isChecked = true
            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.setOnCancelListener {
            binding.taskTimingSwitch.isChecked = false
        }
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).text =
            requireContext().getString(R.string.cancel)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).text =
            requireContext().getString(R.string.ready)
    }
}