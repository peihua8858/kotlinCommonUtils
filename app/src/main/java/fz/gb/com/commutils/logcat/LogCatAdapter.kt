package fz.gb.com.commutils.logcat

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fz.common.model.AdapterBean
import java.io.File

class LogCatAdapter : BaseMultiItemQuickAdapter<AdapterBean<File>, BaseViewHolder>(
        arrayListOf()
), LoadMoreModule {
    init {
        addItemType(0, android.R.layout.simple_list_item_1)
    }

    override fun convert(holder: BaseViewHolder, item: AdapterBean<File>) {
        holder.setText(android.R.id.text1, item.value.name)
    }
}