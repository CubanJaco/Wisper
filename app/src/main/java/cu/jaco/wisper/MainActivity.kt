package cu.jaco.wisper

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import cu.jaco.wisper.databinding.ActivityMainBinding
import cu.jaco.wisper.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun fragmentContainer(): Int = R.id.nav_host_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

}