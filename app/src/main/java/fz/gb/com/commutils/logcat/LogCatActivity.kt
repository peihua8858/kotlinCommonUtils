//package fz.gb.com.commutils.logcat
//
//import android.os.Bundle
//import android.view.View
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.chad.library.adapter.base.BaseQuickAdapter
//import com.chad.library.adapter.base.loadmore.BaseLoadMoreView
//import com.fz.common.app.BaseApplication
//import com.fz.common.file.read
//import fz.gb.com.commutils.model.AdapterBean
//import com.fz.common.rxjava.RxLifecycleUtil
//import com.fz.common.rxjava.XSubscriber
//import com.fz.common.text.copyTextToClipboard
//import com.fz.common.view.utils.LoadMoreHelper
//import com.fz.toast.showToast
//import com.fz.common.decoration.DividerItemDecoration
//import com.fz.common.rxjava.applySchedulers
//import com.fz.common.rxjava.bindLifecycle
//import com.fz.common.text.isNonEmpty
//import fz.gb.com.commutils.ZLoadMoreView
//import com.fz.commutil.R
//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
//import io.reactivex.rxjava3.core.Flowable
//import io.reactivex.rxjava3.schedulers.Schedulers
//import kotlinx.android.synthetic.main.activity_toolbar_recycler_view.*
//import java.io.File
//import java.util.*
//
///**
// * 记录奔溃日志列表,列表倒序显示日志文件
// * @author dingpeihua
// * @date 2020/11/19 16:40
// * @version 1.0
// */
//class LogCatActivity : AppCompatActivity(), LoadMoreHelper.OnLoadMoreHelperListener {
//    private val mAdapter: LogCatAdapter = LogCatAdapter()
//    private var loadMoreHelper: LoadMoreHelper<AdapterBean<File>, LogCatAdapter>? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_toolbar_recycler_view)
//        title = "奔溃日志查看器"
//        loadMoreHelper = LoadMoreHelper(recyclerView, mAdapter, this)
//        mAdapter.setOnItemClickListener(this::onItemClick)
//        loadMoreHelper?.onLoadingData()
//    }
//
//    private fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
//        val adapterBean: AdapterBean<File>? = mAdapter.getItem(position)
//        if (adapterBean != null) {
//            val file: File? = adapterBean.value
//            Flowable.fromCallable {
//                return@fromCallable file.read()
//            }.compose(applySchedulers())
//                    .to(bindLifecycle(this))
//                    .subscribe(object : XSubscriber<String>(this) {
//                        override fun onError(e: Throwable) {
//                            showToast("读取数据失败。")
//                        }
//
//                        override fun onSuccess(response: String) {
//                            showMessageDialog(response)
//                        }
//                    })
//        }
//    }
//
//    private fun showMessageDialog(response: String?) {
//        if (response.isNonEmpty()) {
//            AlertDialog.Builder(this)
//                    .setMessage(response)
//                    .setPositiveButton("复制") { _, _ ->
//                        if (response.copyTextToClipboard(this)) {
//                            showToast("复制成功！")
//                        }
//                    }
//                    .setNegativeButton("取消", null)
//                    .show()
//        } else {
//            showToast("没有找到数据")
//        }
//    }
//
//    override val loadMoreView: BaseLoadMoreView
//        get() = ZLoadMoreView()
//
//    override fun onRequest(curPage: Int, pageSize: Int): Boolean {
//        Flowable.fromCallable {
//            val data = arrayListOf<AdapterBean<File>>()
//            val logFolderName: String? = BaseApplication.getInstance()?.getLogFolder()
//            if (!logFolderName.isNullOrEmpty()) {
//                val parentFile = File(logFolderName)
//                val childFiles = parentFile.listFiles()
//                if (!childFiles.isNullOrEmpty()) {
//                    Arrays.sort(childFiles) { o1, o2 ->
//                        val o1time = o1?.lastModified() ?: 0L
//                        val o2time = o2?.lastModified() ?: 0L
//                        when {
//                            o1time > o2time -> -1
//                            o1time < o2time -> 1
//                            else -> 0
//                        }
//                    }
//                    for (file in childFiles) {
//                        data.add(AdapterBean(file))
//                    }
//                }
//            }
//            return@fromCallable data
//        }.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .to(RxLifecycleUtil.bindLifecycle(this))
//                .subscribe(object : XSubscriber<List<AdapterBean<File>>?>() {
//                    override fun onError(e: Throwable) {
//                        loadMoreHelper?.refreshComplete(true)
//                    }
//
//                    override fun onSuccess(response: List<AdapterBean<File>>?) {
//                        loadMoreHelper?.refreshData(response)
//                    }
//                })
//        return true
//    }
//
//    override fun showLoadingView() {
//        multiStateView?.showLoadingView()
//    }
//
//    override fun showContentView() {
//        multiStateView?.showContentView()
//    }
//
//    override fun showEmptyView() {
//        multiStateView?.showEmptyView()
//    }
//
//    override fun showErrorView() {
//        multiStateView?.showErrorView()
//    }
//
//    override val layoutManager: RecyclerView.LayoutManager
//        get() = LinearLayoutManager(this)
//    override val itemDecoration: RecyclerView.ItemDecoration?
//        get() = DividerItemDecoration(this)
//}