package com.fahad.hishabeetodo.ui

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fahad.hishabeetodo.R
import com.fahad.hishabeetodo.databinding.DataInputPopupBinding
import com.fahad.hishabeetodo.databinding.FragmentMainBinding
import com.fahad.hishabeetodo.db.TodoEntity
import com.fahad.hishabeetodo.viewmodels.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding: FragmentMainBinding
    private lateinit var dataAdapter: NoteItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentMainBinding.inflate(layoutInflater).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClickListener()
    }

    private fun initClickListener() {
        dataAdapter.mClickListener = { response ->
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.updateIsChecked(!response.is_checked, response.id).invokeOnCompletion {
                    showDataToUI()
                }
            }
        }
        binding.popupBtn.setOnClickListener {
            showInsertDataPopup()
        }
    }

    private fun getDeviceMetrics(context: Context): DisplayMetrics {
        val metrics = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = wm.defaultDisplay
        display.getMetrics(metrics)
        return metrics
    }

    private fun showInsertDataPopup() {
        var popUpBinding: DataInputPopupBinding
        val view: ConstraintLayout =
            DataInputPopupBinding.inflate(layoutInflater).also { popUpBinding = it }.root
        val context: Context = ContextThemeWrapper(requireContext(), R.style.PopUpTheme)
        val builder = MaterialAlertDialogBuilder(context)
        builder.setView(view)
        val dialog = builder.create()
        dialog.window?.attributes?.width =
            (getDeviceMetrics(requireContext())?.widthPixels?.times(0.90))?.toInt()
        //  dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        popUpBinding.crossBtn.setOnClickListener {
            dialog.dismiss()
        }

        popUpBinding.submitButton.setOnClickListener {
            val inputText = popUpBinding.inputInner.text.toString().trim()
            if (inputText.isNotEmpty()) {
                val currentTimestamp = System.currentTimeMillis()
                viewModel.saveData(
                    TodoEntity(id = currentTimestamp, note_txt = inputText, is_checked = false)
                ).invokeOnCompletion {
                    dialog.dismiss()
                    showDataToUI()
                }
            } else {
                Toast.makeText(
                    requireContext(), "Cannot input empty task into list", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initView() {
        dataAdapter = NoteItemAdapter()
        with(binding.recyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dataAdapter
        }
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition
                dataAdapter.updateData(position)
                viewModel.deleteData(viewModel.dataList.value?.get(position)?.id ?: 0)
                showDataToUI()
            }
        }
        showDataToUI()

        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(binding.recyclerView)

    }

    private fun showDataToUI() {
        viewModel.getAllData().invokeOnCompletion {
            CoroutineScope(Dispatchers.Main).launch {
                dataAdapter.initData(viewModel.dataList.value ?: mutableListOf())
            }
        }
    }
}