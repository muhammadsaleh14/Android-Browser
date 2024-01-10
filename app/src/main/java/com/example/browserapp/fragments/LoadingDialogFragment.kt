package com.example.browserapp.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieAnimationView
import com.example.browserapp.R

class LoadingDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            builder.setView(inflater.inflate(R.layout.loading_dialog, null))
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun startAnimation() {
        dialog?.findViewById<LottieAnimationView>(R.id.loading_animation)?.playAnimation()
    }

    fun stopAnimation() {
        dialog?.findViewById<LottieAnimationView>(R.id.loading_animation)?.cancelAnimation()
    }
}
