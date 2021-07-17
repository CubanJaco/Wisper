package cu.jaco.wisper.ui.main

import android.animation.ValueAnimator
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import cu.jaco.wisper.R
import cu.jaco.wisper.databinding.FragmentMainBinding
import cu.jaco.wisper.services.CountDownService
import cu.jaco.wisper.services.WipeDataReceiver
import cu.jaco.wisper.ui.base.BaseFragment
import cu.jaco.wisper.utils.isMyServiceRunning
import cu.jaco.wisper.utils.wipeData
import java.util.*

class MainFragment : BaseFragment(), View.OnClickListener, MotionLayout.TransitionListener {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var deviceAdminResultLauncher: ActivityResultLauncher<Intent>

    private var panicCount = 0
    private var startService = true

    override fun getToolbar() = binding.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this
        ).get(MainViewModel::class.java)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        deviceAdminResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    Log.d("LOL", "Se ha activado el administrador del dispositivo")
                }
                Activity.RESULT_CANCELED -> {
                    Log.d("LOL", "No se ha activado el administrador del dispositivo")
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@MainFragment
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestAdminPermission()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.panicButton.backgroundTintList = ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    resources,
                    android.R.color.holo_red_dark,
                    requireContext().theme
                )
            )
        } else {
            binding.panicButton.setCardBackgroundColor(
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.legacy_panic_button
                )
            )
        }

        if (requireContext().isMyServiceRunning(CountDownService::class.java))
            showSwipeLayout()
        else
            binding.countDown.visibility = View.INVISIBLE

        binding.panicButton.setOnClickListener(this)
        binding.swipeMotionLayout.addTransitionListener(this)

    }

    override fun onBackPressed(): Boolean {
        onBackExit()
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.panic_button -> {
                if (panicCount == 0)
                    Handler(Looper.getMainLooper()).postDelayed({ panicCount = 0 }, 3000)

                if (!requireContext().isMyServiceRunning(CountDownService::class.java) && panicCount == 0) {
                    binding.swipeMotionLayout.setTransitionDuration(0)
                    binding.swipeMotionLayout.transitionToStart()
                    binding.swipeMotionLayout.setTransitionDuration(1000)
                    showSwipeLayout()
                    requireActivity().startService(CountDownService.startIntent(requireContext()))
                } else if (panicCount >= 2) {
                    panicCount = 0
                    requireActivity().startService(CountDownService.cancelIntent(requireContext()))
                    requireContext().wipeData()
                    requireActivity().finish()
                }

                panicCount++

            }
        }
    }

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {

    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {

        when (p1) {
            R.id.end -> {
                requireContext().startService(CountDownService.cancelIntent(requireContext()))
                p0?.transitionToStart()
                hideSwipeLayout()
            }
        }

    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

    }

    fun onTick(leftTime: Long) {
        if (binding.swipeMotionLayout.visibility != View.VISIBLE && leftTime >= 0)
            showSwipeLayout()

        if (leftTime < 0)
            binding.countDown.visibility = View.INVISIBLE

        val seconds = (leftTime / 1000).toInt()
        binding.countDown.text = seconds.toString()

        if (binding.countDown.visibility != View.VISIBLE && leftTime >= 0)
            binding.countDown.visibility = View.VISIBLE

    }

    private fun requestAdminPermission() {

        val mDPM =
            requireContext().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val mDeviceAdmin = ComponentName(requireContext(), WipeDataReceiver::class.java)

        if (!mDPM.isAdminActive(mDeviceAdmin)) {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin)
            intent.putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                getString(R.string.admin_large_description)
            )
            deviceAdminResultLauncher.launch(intent)
        }
    }

    private fun showSwipeLayout() {

        binding.swipeMotionLayout.visibility = View.VISIBLE

        val animator = ValueAnimator.ofFloat(binding.swipeMotionLayout.alpha, 1f)
        animator.duration = requireContext().resources.getInteger(R.integer.alpha).toLong()
        animator.addUpdateListener { animation ->
            binding.swipeMotionLayout.alpha = animation.animatedValue as Float
        }
        animator.start()

    }

    private fun hideSwipeLayout() {

        binding.swipeMotionLayout.alpha = 1f

        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = requireContext().resources.getInteger(R.integer.alpha).toLong()
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            binding.swipeMotionLayout.alpha = value
            if (value <= 0.05f)
                binding.swipeMotionLayout.visibility = View.INVISIBLE
        }
        animator.start()

    }

}