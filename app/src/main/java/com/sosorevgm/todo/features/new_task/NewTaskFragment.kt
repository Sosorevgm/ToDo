package com.sosorevgm.todo.features.new_task

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sosorevgm.todo.R
import com.sosorevgm.todo.databinding.FragmentNewTaskBinding
import com.sosorevgm.todo.extensions.getTaskDate
import com.sosorevgm.todo.models.TASK_BUNDLE
import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.TaskPriority
import com.sosorevgm.todo.models.getSpinnerSelection
import dagger.android.support.DaggerFragment
import java.util.*
import javax.inject.Inject


class NewTaskFragment : DaggerFragment(), View.OnClickListener {

    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(NewTaskViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        binding.btnBack.setOnClickListener(this)
        binding.btnChoseNewTaskPriority.setOnClickListener(this)
        binding.btnSaveNewTask.setOnClickListener(this)
        binding.taskTimingSwitch.setOnClickListener(this)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.priority_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.newTaskSpinner.adapter = adapter
        }
        binding.newTaskSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> viewModel.setPriority(TaskPriority.DEFAULT)
                        1 -> viewModel.setPriority(TaskPriority.LOW)
                        2 -> viewModel.setPriority(TaskPriority.HIGH)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        val task: TaskModel? = arguments?.getParcelable(TASK_BUNDLE)
        if (task != null) {
            viewModel.setOldTask(task)
            // setting task description
            binding.etTaskDescription.setText(task.description, TextView.BufferType.EDITABLE)
            // setting task priority
            val selection = task.priority.getSpinnerSelection()
            binding.newTaskSpinner.setSelection(selection)
            viewModel.setPriority(task.priority)
            //setting task date
            if (task.date != 0L) {
                binding.tvNewTaskDate.text = getTaskDate(task.date)
                binding.taskTimingSwitch.isChecked = true
                viewModel.setDate(task.date)
            }
            // setting delete button
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.switchEvent.observe(viewLifecycleOwner) { needToSetDate ->
            if (needToSetDate) {
                val cal: Calendar = Calendar.getInstance()
                val mYear = cal.get(Calendar.YEAR)
                val mMonth = cal.get(Calendar.MONTH)
                val mDay = cal.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    R.style.DatePickerStyle,
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
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).text =
                    requireContext().getString(R.string.ready)
            } else {
                binding.taskTimingSwitch.isChecked = false
            }
        }

        viewModel.dateLiveData.observe(viewLifecycleOwner) {
            if (it == 0L) {
                binding.tvNewTaskDate.text = ""
            } else {
                binding.tvNewTaskDate.text = getTaskDate(it)
            }
        }

        viewModel.navigationBack.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
}