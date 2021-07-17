package cu.jaco.wisper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import cu.jaco.wisper.databinding.ActivityMainBinding
import cu.jaco.wisper.services.CountDownService
import cu.jaco.wisper.ui.base.BaseActivity
import cu.jaco.wisper.ui.main.MainFragment

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private var serviceReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val f = visibleFragment()
            if (f is MainFragment) {
                f.onTick(intent?.getLongExtra(CountDownService.ARG_LEFT_TIME, -1L) ?: -1L)
            }
        }
    }

    override fun fragmentContainer(): Int = R.id.nav_host_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        registerReceiver(serviceReceiver, IntentFilter(CountDownService.TICK_ACTION))
    }

    override fun onDestroy() {
        unregisterReceiver(serviceReceiver)
        super.onDestroy()
    }

}