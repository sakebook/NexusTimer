package com.sakebook.android.nexustimer.presenter

import android.app.Fragment
import android.support.v17.leanback.widget.OnItemViewClickedListener
import android.support.v17.leanback.widget.Presenter
import android.support.v17.leanback.widget.Row
import android.support.v17.leanback.widget.RowPresenter
import android.support.v4.app.ActivityOptionsCompat
import android.widget.Toast
import com.sakebook.android.nexustimer.Utils
import com.sakebook.android.nexustimer.model.OffTimer
import com.sakebook.android.nexustimer.ui.SimpleDialogActivity
import com.sakebook.android.nexustimer.ui.TvFragment

/**
 * Created by sakemotoshinya on 16/06/04.
 */
class ItemViewClickedListener(val fragment: Fragment) : OnItemViewClickedListener {

    override fun onItemClicked(itemViewHolder: Presenter.ViewHolder, item: Any,
                               rowViewHolder: RowPresenter.ViewHolder, row: Row) {
        if (fragment !is TvFragment) {
            Toast.makeText(itemViewHolder.view.context, "non supported this fragment", Toast.LENGTH_LONG).show()
            return
        }

        if (item is OffTimer) {
            Toast.makeText(itemViewHolder.view.context, "OffTimer set ${item.minute} minute.", Toast.LENGTH_LONG).show()
            Utils.setAlarm(itemViewHolder.view.context, item.minute)
            return
        }

        Toast.makeText(itemViewHolder.view.context, "${item}, ${row.headerItem.id}, ${row.headerItem.name}", Toast.LENGTH_LONG).show()

        if (item as String == "DELETE") {
            val intent = SimpleDialogActivity.createIntent(fragment.activity, row.headerItem.id)
            val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(fragment.activity).toBundle()
            fragment.startActivityForResult(intent, fragment.REQUEST_CODE, bundle)
            return
        }

//        if (item is Video) {
//            val video = item as Video
//            val intent = Intent(getActivity(), VideoDetailsActivity::class.java)
//            intent.putExtra(VideoDetailsActivity.VIDEO, video)
//
//            val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                    getActivity(),
//                    (itemViewHolder.view as ImageCardView).mainImageView,
//                    VideoDetailsActivity.SHARED_ELEMENT_NAME).toBundle()
//            getActivity().startActivity(intent, bundle)
//        } else if (item is String) {
//            if (item.contains(getString(R.string.grid_view))) {
//                val intent = Intent(getActivity(), VerticalGridActivity::class.java)
//                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle()
//                startActivity(intent, bundle)
//            } else if (item.contains(getString(R.string.guidedstep_first_title))) {
//                val intent = Intent(getActivity(), GuidedStepActivity::class.java)
//                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle()
//                startActivity(intent, bundle)
//            } else if (item.contains(getString(R.string.error_fragment))) {
//                val errorFragment = BrowseErrorFragment()
//                getFragmentManager().beginTransaction().replace(R.id.main_frame, errorFragment).addToBackStack(null).commit()
//            } else {
//                Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show()
//            }
//        }
    }
}
