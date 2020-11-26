package com.andariadar.newsapplication.utils

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.getColorRes(@ColorRes id: Int) = ContextCompat.getColor(requireContext(), id)
