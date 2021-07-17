package cu.jaco.wisper.ui.main

import androidx.lifecycle.ViewModel
import cu.jaco.wisper.repositories.database.AppDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private var database: AppDatabase
) : ViewModel() {

}