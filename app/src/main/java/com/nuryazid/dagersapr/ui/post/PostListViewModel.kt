package com.nuryazid.dagersapr.ui.post

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.nuryazid.dagersapr.R
import com.nuryazid.dagersapr.base.BaseViewModel
import com.nuryazid.dagersapr.model.Post
import com.nuryazid.dagersapr.network.PostApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import javax.inject.Inject

class PostListViewModel : BaseViewModel() {
    @Inject
    lateinit var postApi: PostApi

    private var subscription: Disposable? = null

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadPosts() }

    val postListAdapter: PostListAdapter = PostListAdapter()

    init {
        loadPosts()
    }

    private fun loadPosts() {

        Realm.getDefaultInstance().use { realm ->
            realm.beginTransaction()

            if (realm.where(Post::class.java).count() > 0) {
                val posts = realm.copyFromRealm(realm.where(Post::class.java).findAll())
                onRetrievePostListSuccess(posts, false)
            } else {
                subscription = postApi.getPosts()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { onRetrievePostListStart() }
                    .doOnTerminate { onRetrievePostListFinish() }
                    .subscribe(
                        { result -> onRetrievePostListSuccess(result, true) },
                        { onRetrievePostListError() }
                    )
            }

            realm.commitTransaction()
        }


    }

    private fun onRetrievePostListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrievePostListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess(postList: List<Post>, update: Boolean) {
        if (update) {
            Realm.getDefaultInstance().use { realm ->
                realm.beginTransaction()
                realm.copyToRealmOrUpdate(postList)
                realm.commitTransaction()
            }
        }
        postListAdapter.updatePostList(postList)
    }

    private fun onRetrievePostListError() {
        errorMessage.value = R.string.post_error
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }
}