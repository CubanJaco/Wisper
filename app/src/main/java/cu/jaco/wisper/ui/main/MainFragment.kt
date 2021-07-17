package cu.jaco.wisper.ui.main

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.ViewModelProvider
import cu.jaco.wisper.R
import cu.jaco.wisper.databinding.FragmentMainBinding
import cu.jaco.wisper.services.WipeDataReceiver
import cu.jaco.wisper.ui.base.BaseFragment
import cu.jaco.wisper.utils.wipeData
import java.util.*

class MainFragment : BaseFragment(), View.OnClickListener, MotionLayout.TransitionListener {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var deviceAdminResultLauncher: ActivityResultLauncher<Intent>

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

        binding.swipeMotionLayout.addTransitionListener(this)
    }

    override fun onBackPressed(): Boolean {
        onBackExit()
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {

    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {

        when (p1) {
            R.id.end -> {
                requireContext().wipeData()
            }
        }

    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

    }

    private fun requestAdminPermission() {

        val mDPM = requireContext().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
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

}