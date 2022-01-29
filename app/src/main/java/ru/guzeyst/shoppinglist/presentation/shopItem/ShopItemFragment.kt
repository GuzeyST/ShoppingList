package ru.guzeyst.shoppinglist.presentation.shopItem

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.guzeyst.shoppinglist.*
import ru.guzeyst.shoppinglist.databinding.ShopItemFragmentBinding

class ShopItemFragment : Fragment() {

    private lateinit var binding: ShopItemFragmentBinding
    private lateinit var viewModel: ShopItemViewModel
    private var screenMode: String = DEFAULT_MODE
    private var shopItemId: Int = UNDEFINED_ID
    private lateinit var onEditFinishListener: OnEditFinishListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ShopItemFragmentBinding.inflate(inflater, container, false)
        parseParams()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditFinishListener){
            onEditFinishListener = context
        }else{
            throw java.lang.RuntimeException("Activity is not impl OnEditFinishListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
        getObservOnError()
        setObservInputText()
        setObservReadyCloseScreen()
    }

    private fun launchAddMode() = with(binding) {
        btSave.setOnClickListener {
            val name = tvName.text.toString()
            val count = tvCount.text.toString()
            viewModel.addItem(name, count)
        }
    }

    private fun launchEditMode() = with(binding) {
        viewModel.getItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner, {
            tvName.setText(it.name)
            tvCount.setText(it.count.toString())
        })

        btSave.setOnClickListener {
            viewModel.editItem(tvName.text.toString(), tvCount.text.toString())
        }
    }

    private fun getObservOnError() {
        viewModel.errorInputName.observe(viewLifecycleOwner, {
            binding.tilTitleName.error = if (it) ERROR_INPUT_NAME
            else null
        })
        viewModel.errorInputCount.observe(viewLifecycleOwner, {
            binding.tilTitleCount.error = if (it) ERROR_INPUT_COUNT
            else null
        })
    }

    private fun parseParams() {
        val args = requireArguments()
        if(!args.containsKey(SCREEN_MODE)){
            throw RuntimeException(ERROR_ABSENT_EXTRA_MODE)
        }
        val mode = args.getString(SCREEN_MODE)

        if(mode != MODE_ADD && mode != MODE_EDIT){
            throw RuntimeException(ERROR_UNKNOWN_EXTRA_MODE + "$mode")
        }

        screenMode = mode

        if(screenMode == MODE_EDIT){
            if(!args.containsKey(SHOP_ITEM_ID)){
                throw RuntimeException(ERROR_ABSENT_ITEM_ID)
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, UNDEFINED_ID)
        }
    }

    private fun setObservInputText() {
        binding.tvName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onEditFinishListener.onFinish()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }
        })

        binding.tvCount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }
        })
    }

    private fun setObservReadyCloseScreen() {
        viewModel.readyToCloseScreen.observe(viewLifecycleOwner, {
            onEditFinishListener.onFinish()
        })
    }

    interface OnEditFinishListener{
        fun onFinish()
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}