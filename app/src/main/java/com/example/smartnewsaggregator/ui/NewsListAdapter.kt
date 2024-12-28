package com.example.smartnewsaggregator.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.smartnewsaggregator.databinding.ItemNewsBinding
import com.example.smartnewsaggregator.domain.model.Article

class NewsListAdapter : RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {

    private val newsList = mutableListOf<Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    fun setNewsList(newsList: List<Article>) {
        this.newsList.clear()
        this.newsList.addAll(newsList)
        notifyDataSetChanged()
    }

    class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.apply {
                this.textNewsTitle.text = article.title
                textNewsDescription.text = article.description
                imageNewsThumbnail.load(
                    article.imageUrl
                )

            }
        }
    }
}