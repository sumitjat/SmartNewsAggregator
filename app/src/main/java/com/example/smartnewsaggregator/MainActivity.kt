package com.example.smartnewsaggregator

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartnewsaggregator.data.remote.NewsApiService
import com.example.smartnewsaggregator.databinding.ActivityMainBinding
import com.example.smartnewsaggregator.domain.model.ApiResult
import com.example.smartnewsaggregator.domain.repository.NewsUpdateService
import com.example.smartnewsaggregator.domain.usecase.MainViewModel
import com.example.smartnewsaggregator.ui.NewsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val newsListAdapter = NewsListAdapter()

    @Inject
    lateinit var apiService: NewsApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.recyclerView.apply {
            val dividerItemDecoration = DividerItemDecoration(
                this@MainActivity,
                LinearLayoutManager.VERTICAL
            )
            adapter = newsListAdapter
            addItemDecoration(dividerItemDecoration)
        }



        lifecycleScope.launch {
            delay(10000)
            NewsUpdateService.startService(this@MainActivity)
        }

        lifecycleScope.launch {
            viewModel.newsState.collect { result ->
                Log.d("MainActivity", "Result: $result")
                when (result) {
                    is ApiResult.Error -> {

                    }

                    ApiResult.Loading -> {

                    }

                    is ApiResult.Success -> {
                        newsListAdapter.setNewsList(result.data)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        NewsUpdateService.stopService(this)
    }
}