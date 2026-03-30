package ru.netology.nmedia.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val text = binding.content.text?.toString().orEmpty()
            val isCreatingNew = (viewModel.edited.value?.id ?: 0L) == 0L
            if (isCreatingNew) {
                viewModel.setDraftPost(text)
            }
            findNavController().navigateUp()
        }

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id != 0L) {
                binding.content.setText(post.content)
                binding.content.setSelection(binding.content.text?.length ?: 0)
            } else {
                val draftText = viewModel.draftPost.value.orEmpty()
                val initialText =
                    requireActivity().intent.getStringExtra(Intent.EXTRA_TEXT).orEmpty()
                val textToShow = when {
                    draftText.isNotBlank() -> draftText
                    initialText.isNotBlank() -> initialText
                    else -> ""
                }
                if (textToShow.isNotBlank()) {
                    binding.content.setText(textToShow)
                    binding.content.setSelection(binding.content.text?.length ?: 0)
                    requireActivity().intent.removeExtra(Intent.EXTRA_TEXT)
                } else {
                    binding.content.setText("")
                }
            }
        }

        binding.save.setOnClickListener {
            val text = binding.content.text.toString().trim()
            if (text.isBlank()) {

                Snackbar.make(
                    binding.root,
                    R.string.empty_notificaton,
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            viewModel.save(text)
            findNavController().navigateUp()
        }

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                viewModel.cancelEditing()
            }
        })
        return binding.root
    }
}