package com.fz.common.view

import android.content.Context
import androidx.annotation.IntDef
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.loadmore.BaseLoadMoreView
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.module.LoadMoreModuleConfig.defLoadMoreView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fz.common.network.NetworkUtil

/**
 * 加载更多数据
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/3/4 11:53
 */
open class LoadMoreHelper<T : MultiItemEntity, ADAPTER : BaseQuickAdapter<T, out BaseViewHolder>>(
        private val mRecyclerView: RecyclerView,
        private val mAdapter: ADAPTER,
        private val helperListener: OnLoadMoreHelperListener) {
    @IntDef(REQUEST_TYPE_LOADING,
            REQUEST_TYPE_NORMAL,
            REQUEST_TYPE_LOAD_MORE)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    internal annotation class RequestType

    /**
     * 列表数据请求类型
     */
    @RequestType
    private var mRequestType = REQUEST_TYPE_NORMAL

    /**
     * 当前页码
     */
    private var currentPage = 1

    /**
     * 每一页的数据量
     */
    private val pageSize = 20

    /**
     * 用于判断当前是否正在拉取数据
     */
    private var isLoadingData = false
    private val context: Context

    init {
        setLoadMoreView(mAdapter, helperListener)
        context = mRecyclerView.context
        mRecyclerView.layoutManager = helperListener.layoutManager
        val itemAnimator = helperListener.itemAnimator
        if (itemAnimator != null) {
            mRecyclerView.itemAnimator = itemAnimator
        }
        val itemDecoration = helperListener.itemDecoration
        if (itemDecoration != null) {
            mRecyclerView.addItemDecoration(itemDecoration)
        }
        mRecyclerView.adapter = mAdapter
    }

    private fun setLoadMoreView(adapter: ADAPTER, helperListener: OnLoadMoreHelperListener) {
        if (helperListener.isEnableLoadMore && adapter is LoadMoreModule) {
            defLoadMoreView = helperListener.loadMoreView
            val moreModule = adapter.loadMoreModule
            moreModule.loadMoreView = helperListener.loadMoreView
            moreModule.isEnableLoadMore = helperListener.isEnableLoadMore
            moreModule.setOnLoadMoreListener { onLoadMore() }
        }
    }

    interface OnLoadMoreHelperListener {
        /**
         * 加载更多视图
         *
         * @return
         * @author dingpeihua
         * @date 2019/3/4 13:46
         * @version 1.0
         */
        val loadMoreView: BaseLoadMoreView

        /**
         * 请求接口数据
         *
         * @param curPage
         * @param pageSize
         * @return
         * @author dingpeihua
         * @date 2019/3/4 11:40
         * @version 1.0
         */
        fun onRequest(curPage: Int, pageSize: Int): Boolean

        /**
         * 加载数据
         *
         * @author dingpeihua
         * @date 2019/3/4 11:48
         * @version 1.0
         */
        fun showLoadingView() {}

        /**
         * 显示内容
         *
         * @author dingpeihua
         * @date 2019/3/4 11:48
         * @version 1.0
         */
        fun showContentView() {}

        /**
         * 显示没有网络视图
         *
         * @author dingpeihua
         * @date 2019/9/22 12:23
         * @version 1.0
         */
        fun showNoNetworkView() {}

        /**
         * 显示空数据视图
         *
         * @author dingpeihua
         * @date 2019/3/4 11:48
         * @version 1.0
         */
        fun showEmptyView() {}

        /**
         * 显示错误视图
         *
         * @author dingpeihua
         * @date 2019/3/4 11:48
         * @version 1.0
         */
        fun showErrorView() {}

        /**
         * 是否启动加载更多
         *
         * @return
         */
        val isEnableLoadMore: Boolean
            get() = true

        /**
         * 是否初始化加载时检查网络状态
         *
         * @return
         * @author dingpeihua
         * @date 2019/3/12 18:34
         * @version 1.0
         */
        val isInitCheckNetwork: Boolean
            get() = true

        /**
         * 获取布局管理器,如果为null 或者不实现，则需要自行设置
         *
         * @return {@link RecyclerView#setLayoutManager(RecyclerView.LayoutManager)}
         * @author dingpeihua
         * @date 2018/11/20 18:39
         * @version 1.0
         */
        val layoutManager: RecyclerView.LayoutManager

        /**
         * 获取item 装饰 如果为null 或者不实现，则需要自行设置
         *
         * @return {@link RecyclerView#addItemDecoration(RecyclerView.ItemDecoration)}
         * @author dingpeihua
         * @date 2018/11/20 18:40
         * @version 1.0
         */
        val itemDecoration: RecyclerView.ItemDecoration?
            get() = null

        /**
         * 获取item 动画
         *
         * @author dingpeihua
         * @date 2018/11/20 18:41
         * @version 1.0
         */
        val itemAnimator: RecyclerView.ItemAnimator?
            get() = DefaultItemAnimator()

        /**
         * 处理完成
         *
         * @author dingpeihua
         * @date 2019/9/22 13:19
         * @version 1.0
         */
        fun onComplete() {

        }
    }

    /**
     * 加载更多数据
     *
     * @author dingpeihua
     * @date 2018/11/21 15:53
     * @version 1.0
     */
    fun onLoadMore() {
        if (!NetworkUtil.isConnected(context)) {
            return
        }
        if (isLoadingData) {
            return
        }
        ++currentPage
        onRequest(REQUEST_TYPE_LOAD_MORE)
    }

    fun onLoadingData() {
        currentPage = 1
        onRequest(REQUEST_TYPE_LOADING)
    }

    /**
     * 刷新数据，即请求第一页数据
     *
     * @author dingpeihua
     * @date 2019/3/4 14:07
     * @version 1.0
     */
    fun onRefresh() {
        currentPage = 1
        onRequest(REQUEST_TYPE_NORMAL)
    }

    /**
     * 重新尝试请求网络
     *
     * @author dingpeihua
     * @date 2018/11/20 19:23
     * @version 1.0
     */
    fun onRetry() {
        onLoadingData()
    }

    /**
     * 数据请求处理
     *
     * @author dingpeihua
     * @date 2018/11/20 19:23
     * @version 1.0
     */
    fun onRequest(@RequestType requestType: Int) {
        if (helperListener.isInitCheckNetwork
                && !NetworkUtil.isConnected(context, true)) {
            helperListener.onComplete()
            helperListener.showErrorView()
            return
        }
        if (isLoadingData) {
            return
        }
        mRequestType = requestType
        isLoadingData = helperListener.onRequest(currentPage, pageSize)
        if (isLoadingData) {

            when (requestType) {
                REQUEST_TYPE_LOADING -> helperListener.showLoadingView()
                else -> {
                }
            }
        }
    }

    /**
     * 计算总页数
     *
     * @param totalSize
     * @return
     */
    fun calTotalPage(totalSize: Int): Int {
        val pageNum = totalSize / pageSize
        val leftPage = if (totalSize % pageSize > 0) 1 else 0
        return pageNum + leftPage
    }

    /**
     * 刷新数据
     *
     * @param totalSize 数据总量
     * @param data      当前数据集合
     * @author dingpeihua
     * @date 2019/3/4 11:49
     * @version 1.0
     */
    fun refreshData(data: List<T>?) {
        refreshData(data, false)
    }

    /**
     * 刷新数据
     *
     * @param totalSize 数据总量
     * @param data      当前数据集合
     * @author dingpeihua
     * @date 2019/3/4 11:49
     * @version 1.0
     */
    fun refreshData(totalSize: Int, data: List<T>?) {
        refreshData(data, currentPage < calTotalPage(totalSize))
    }

    /**
     * 刷新数据
     *
     * @param data      当前数据集合
     * @param totalPage 数据总页数
     * @author dingpeihua
     * @date 2019/3/4 11:49
     * @version 1.0
     */
    fun refreshData(data: List<T>?, totalPage: Int) {
        refreshData(data, currentPage < totalPage)
    }

    /**
     * 添加或者更新数据源,并刷新列表
     *
     * @param data        数据列表
     * @param isClearData 是否清除数据
     * @param totalPage   总页数
     * @author dingpeihua
     * @date 2019/2/16 10:22
     * @version 1.0
     */
    fun refreshData(data: List<T>?, isClearData: Boolean, totalPage: Int) {
        refreshData(data, totalPage, isClearData, true)
    }

    /**
     * 添加或者更新数据源,并刷新列表
     *
     * @param data        数据列表
     * @param totalPage   总页数
     * @param isClearData 是否先清除数据
     * @param gone        是否隐藏加载更多
     * @author dingpeihua
     * @date 2019/2/16 10:20
     * @version 1.0
     */
    fun refreshData(data: List<T>?, totalPage: Int, isClearData: Boolean, gone: Boolean) {
        if (isClearData) {
            clearListData()
        }
        refreshData(data, totalPage, gone)
    }

    private fun clearListData() {
        val data = mAdapter.data
        val dataSize = data.size
        data.clear()
        //避免异常Inconsistency detected. Invalid view holder adapter positionViewHolder
        mAdapter.notifyItemRangeRemoved(mAdapter.headerLayoutCount, dataSize)
    }

    /**
     * @param data      数据列表
     * @param totalPage 总页数
     * @param gone      是否隐藏加载更多
     * @author dingpeihua
     * @date 2019/2/16 10:23
     * @version 1.0
     */
    fun refreshData(data: List<T>?, totalPage: Int, gone: Boolean) {
        refreshData(data, currentPage < totalPage, gone)
    }
    /**
     * 刷新数据
     *
     * @param data       数据列表
     * @param isMoreData 是否有更多数据
     * @param gone       是否显示提示文案 true为隐藏提示，反之显示文案
     * @author dingpeihua
     * @date 2019/3/4 11:50
     * @version 1.0
     */
    /**
     * 添加或者更新数据源,并刷新列表
     *
     * @param data       数据列表
     * @param isMoreData 是否有更多数据
     * @author dingpeihua
     * @date 2018/11/20 18:42
     * @version 1.0
     */
    @JvmOverloads
    fun refreshData(data: List<T>?, isMoreData: Boolean, gone: Boolean = true) {
        mRecyclerView.stopScroll()
        if (data != null && data.isNotEmpty()) {
            when (mRequestType) {
                REQUEST_TYPE_LOAD_MORE -> mAdapter.addData(data)
                else -> mAdapter.setNewInstance(ArrayList(data))
            }
        }
        refreshComplete(false, isMoreData, gone)
    }

    /**
     * 请求完成
     *
     * @author dingpeihua
     * @date 2018/11/21 15:38
     * @version 1.0
     */
    @JvmOverloads
    fun refreshComplete(isError: Boolean = false, isMoreData: Boolean = false, gone: Boolean = true) {
        val itemCount = itemCount
        if (itemCount > 0) {
            helperListener.showContentView()
        } else {
            if (isError) {
                helperListener.showErrorView()
            } else {
                helperListener.showEmptyView()
            }
        }
        if (isMoreData) {
            mAdapter.loadMoreModule.loadMoreComplete()
        } else {
            mAdapter.loadMoreModule.loadMoreEnd(gone)
        }
        isLoadingData = false
        helperListener.onComplete()
    }

    /**
     * 获取列表项目数量
     *
     * @author dingpeihua
     * @date 2018/11/21 15:51
     * @version 1.0
     */
    val itemCount: Int
        get() {
            val adapter: BaseQuickAdapter<*, *> = mAdapter
            val datas: List<*> = adapter.data
            return datas.size
        }

    /**
     * 获取当前请求类型
     *
     * @author dingpeihua
     * @date 2018/11/20 19:35
     * @version 1.0
     */
    fun getRequestType(): Int {
        return mRequestType
    }

    /**
     * 获取当前是否正在加载中
     *
     * @author dingpeihua
     * @date 2018/11/20 19:35
     * @version 1.0
     */
    fun isLoadingData(): Boolean {
        return isLoadingData
    }

    /**
     * 获取当前是否是刷新事件
     *
     * @author dingpeihua
     * @date 2018/11/30 12:56
     * @version 1.0
     */
    fun isRefresh(): Boolean {
        return mRequestType != REQUEST_TYPE_LOAD_MORE
    }

    /**
     * 获取当前是否是加载更多事件
     *
     * @author dingpeihua
     * @date 2018/11/30 12:56
     * @version 1.0
     */
    fun isLoadMore(): Boolean {
        return mRequestType == REQUEST_TYPE_LOAD_MORE
    }

    /**
     * 用于判断是否是第一页
     *
     * @author dingpeihua
     * @date 2019/2/16 10:25
     * @version 1.0
     */
    open fun isFistPage(): Boolean {
        return currentPage == 1
    }

    companion object {

        /**
         * 正常加载,不显示loading
         */
        const val REQUEST_TYPE_NORMAL = 0x011

        /**
         * 正常加载,显示loading
         */
        const val REQUEST_TYPE_LOADING = 0x013

        /**
         * 上拉加载更多数据
         */
        const val REQUEST_TYPE_LOAD_MORE = 0x014
    }
}