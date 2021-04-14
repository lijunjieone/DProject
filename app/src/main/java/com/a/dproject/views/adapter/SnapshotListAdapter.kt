
package  com.a.dproject.views.adapter

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.a.dproject.BR
import com.a.dproject.R
import com.a.dproject.mvvm.viewmodel.SnapshotListViewModel
import com.a.dproject.views.CommonMultiItem
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class SnapshotListAdapter(data: List<CommonMultiItem<SnapshotListViewModel.SnapshotListDataModel>>) : BaseMultiItemQuickAdapter<CommonMultiItem<SnapshotListViewModel.SnapshotListDataModel>, SnapshotListAdapter.DataBindingViewHolder>(data) {


    init {
        addItemType(CommonMultiItem.ITEM_HEADER, R.layout.fragment_header_snapshot_list)
        addItemType(CommonMultiItem.ITEM_ONE, R.layout.fragment_item_snapshot_list)
    }

    override fun convert(helper: DataBindingViewHolder, item: CommonMultiItem<SnapshotListViewModel.SnapshotListDataModel>) {
        val binding = helper.binding
        binding.setVariable(BR.item, item.content)
        binding.executePendingBindings()


        when (helper.itemViewType) {
            CommonMultiItem.ITEM_HEADER -> helper.setText(R.id.iv_header, item.content?.letter)
            CommonMultiItem.ITEM_ONE -> when (helper.layoutPosition % 2) {
                0 -> helper.setText(R.id.tv_letter, "one ${item.content?.letter}}")
                1 -> helper.setText(R.id.tv_letter, "two ${item.content?.letter}}")
            }
        }
    }


    
    override fun getItemView(layoutResId: Int, parent: ViewGroup): View {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, layoutResId, parent, false) ?: return super.getItemView(layoutResId, parent)
        val view = binding.root
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding)
        return view
    }

    inner class DataBindingViewHolder(view: View) : BaseViewHolder(view) {

        val binding: ViewDataBinding
            get() = itemView.getTag(R.id.BaseQuickAdapter_databinding_support) as ViewDataBinding
    }


}

