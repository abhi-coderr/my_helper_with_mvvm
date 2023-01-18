package com.example.asyncapicalling2.ui.activities


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.asyncapicalling2.R
import com.example.asyncapicalling2.databinding.ActivityMainBinding
import com.example.asyncapicalling2.databinding.CustomDialogLayoutBinding
import com.example.asyncapicalling2.network.model.IMDB
import com.example.asyncapicalling2.ui.adapters.RecyclerAdapter
import com.example.asyncapicalling2.ui.viewmodels.MainViewModel
import com.example.asyncapicalling2.utils.PreferenceDataStoreHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var myAdapter: RecyclerAdapter
    private lateinit var customDialogLayoutBinding: CustomDialogLayoutBinding

    private val Context.dataStore by preferencesDataStore(name = "movie_datastore")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val preferenceDataStoreHelper = PreferenceDataStoreHelper(dataStore = dataStore)

        setViewModel()
        setUpRecyclerView()

        mainViewModel.responseData.observe(this, Observer { it ->
            if (it != null) {
                lifecycleScope.launch {
//                    val arrayListOfIMDB = arrayListOf<IMDB>(it)
                    preferenceDataStoreHelper.genericWrite("movie_data", it)
                    val getFromPreference = preferenceDataStoreHelper.genericRead<IMDB>("movie_data")
                    Log.d("krishna", "$getFromPreference")
                    myAdapter.submitList(getFromPreference.results.map { it.copy() })
                }
                activityMainBinding.errorTv.visibility = View.GONE
                activityMainBinding.progressBar.visibility = View.GONE
            } else {
                activityMainBinding.errorTv.visibility = View.VISIBLE
                activityMainBinding.progressBar.visibility = View.VISIBLE
                Toast.makeText(this, "Please try again!", Toast.LENGTH_SHORT).show()
            }
        })

        mainViewModel.showProgress.observe(this, Observer {
            if (it) {
                activityMainBinding.progressBar.visibility = View.VISIBLE
            } else {
                activityMainBinding.progressBar.visibility = View.GONE
            }
        })

        mainViewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        activityMainBinding.searchBtn.setOnClickListener {
            if (activityMainBinding.searchBar.text.isEmpty()) {
                Toast.makeText(this, "Kindly enter movie name", Toast.LENGTH_SHORT).show()
            } else {
                val searchExpressionValue = activityMainBinding.searchBar.text
                mainViewModel.getImdbData(searchExpressionValue.toString())
            }
        }

    }


    private fun showDialog(title: String) {

        val settingsDialog = Dialog(this)
        customDialogLayoutBinding = CustomDialogLayoutBinding.inflate(layoutInflater)
        settingsDialog.setContentView(customDialogLayoutBinding.root)
        settingsDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        settingsDialog.show()

//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
////        dialog.setContentView(com.example.asyncapicalling2.R.layout.custom_dialog_layout)
//        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        Glide.with(this)
            .load(title)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(customDialogLayoutBinding.dialogImageView)

        settingsDialog.show()

    }

    private fun setViewModel() {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    private fun setUpRecyclerView() {
        activityMainBinding.apply {
            myAdapter = RecyclerAdapter(onEvent = {
                when (it) {
                    is RecyclerAdapter.MyEventSealedClass.OnImageClick ->
                        Toast.makeText(this@MainActivity,
                            it.results.title,
                            Toast.LENGTH_SHORT).show()
                    is RecyclerAdapter.MyEventSealedClass.OnDialogShow -> {
                        showDialog(it.results.image.toString())
                    }
                }
            })

            mainRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

            mainRecyclerView.adapter = myAdapter
        }

    }

}