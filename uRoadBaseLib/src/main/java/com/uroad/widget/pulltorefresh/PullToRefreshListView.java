/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.uroad.widget.pulltorefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.uroad.uroad_ctllib.R;

public class PullToRefreshListView extends PullToRefreshAdapterViewBase<ListView> {

	private LoadingLayout mHeaderLoadingView;
	// private LoadingLayout mFooterLoadingView;
	private FootLoadingLayout mNewFootLoadingView;
	private FrameLayout mLvFooterLoadingFrame;
	private ListView listView;

	public PullToRefreshListView(Context context) {
		super(context);
		setDisableScrollingWhileRefreshing(false);
		getFooterLayout().setVisibility(View.GONE);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDisableScrollingWhileRefreshing(false);
		getFooterLayout().setVisibility(View.GONE);
	}

	public PullToRefreshListView(Context context, Mode mode) {
		super(context, mode);
		setDisableScrollingWhileRefreshing(false);
		getFooterLayout().setVisibility(View.GONE);
	}

	@Override
	public ContextMenuInfo getContextMenuInfo() {
		return ((InternalListView) getRefreshableView()).getContextMenuInfo();
	}

	@Override
	public void setPageIndex(int _pageIndex) {
		// TODO Auto-generated method stub
		super.setPageIndex(_pageIndex);
	}

	@Override
	public int getPageIndex() {
		// TODO Auto-generated method stub
		return super.getPageIndex();
	}

	public void setPullLabel(String pullLabel, Mode mode) {
		super.setPullLabel(pullLabel, mode);

		if (null != mHeaderLoadingView && mode.canPullDown()) {
			mHeaderLoadingView.setPullLabel(pullLabel);
		}
		if (null != mNewFootLoadingView && mode.canPullUp()) {
			mNewFootLoadingView.setPullLabel(pullLabel);
		}
	}

	public void setRefreshingLabel(String refreshingLabel, Mode mode) {
		super.setRefreshingLabel(refreshingLabel, mode);

		if (null != mHeaderLoadingView && mode.canPullDown()) {
			mHeaderLoadingView.setRefreshingLabel(refreshingLabel);
		}
		if (null != mNewFootLoadingView && mode.canPullUp()) {
			mNewFootLoadingView.setRefreshingLabel(refreshingLabel);
		}
	}

	public void setReleaseLabel(String releaseLabel, Mode mode) {
		super.setReleaseLabel(releaseLabel, mode);

		if (null != mHeaderLoadingView && mode.canPullDown()) {
			mHeaderLoadingView.setReleaseLabel(releaseLabel);
		}
		if (null != mNewFootLoadingView && mode.canPullUp()) {
			mNewFootLoadingView.setReleaseLabel(releaseLabel);
		}
	}

	protected ListView createListView(Context context, AttributeSet attrs) {
		final ListView lv;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			lv = new InternalListViewSDK9(context, attrs);
		} else {
			lv = new InternalListView(context, attrs);
		}
		return lv;
	}

	@Override
	protected final ListView createRefreshableView(Context context, AttributeSet attrs) {
		ListView lv = createListView(context, attrs);

		// Get Styles from attrs
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullToRefresh);

		// Create Loading Views ready for use later
		FrameLayout frame = new FrameLayout(context);
		mHeaderLoadingView = createLoadingLayout(context, Mode.PULL_DOWN_TO_REFRESH, a);
		frame.addView(mHeaderLoadingView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		mHeaderLoadingView.setVisibility(View.GONE);
		lv.addHeaderView(frame, null, false);

		mLvFooterLoadingFrame = new FrameLayout(context);
		mNewFootLoadingView = new FootLoadingLayout(context);
		// mFooterLoadingView = createLoadingLayout(context,
		// Mode.PULL_UP_TO_REFRESH, a);
		mLvFooterLoadingFrame.addView(mNewFootLoadingView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		if (getMode() == Mode.BOTH || getMode() == Mode.PULL_UP_TO_REFRESH)
			mNewFootLoadingView.setVisibility(View.VISIBLE);
		else
			mNewFootLoadingView.setVisibility(View.GONE);
		mNewFootLoadingView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setFootViewClickEvent();
			}
		});
		// mNewFootLoadingView =new FootLoadingLayout(context);
		// lv.addFooterView(mNewFootLoadingView);

		a.recycle();

		// Set it to this so it can be used in ListActivity/ListFragment
		lv.setId(android.R.id.list);
		lv.setCacheColorHint(0x00000000);
		listView = lv;

		return lv;
	}

	public void setDivider(Drawable divider) {
		if (listView != null)
			listView.setDivider(divider);
	}

	public ListView getlistView() {
		if (listView != null)
			return listView;
		return null;
	}

	public void setDividerHeight(int height) {
		if (listView != null)
			listView.setDividerHeight(height);
	}

	@Override
	protected void resetHeader() {

		// If we're not showing the Refreshing view, or the list is empty, then
		// the header/footer views won't show so we use the
		// normal method

		ListAdapter adapter = mRefreshableView.getAdapter();
		if (!getShowViewWhileRefreshing() || null == adapter || adapter.isEmpty()) {
			super.resetHeader();
			return;
		}

		View originalLoadingLayout;
		LoadingLayout listViewLoadingLayout = null;
		FootLoadingLayout footLoadingLayout = null;
		int scrollToHeight = getHeaderHeight();
		int selection;
		boolean scroll;

		switch (getCurrentMode()) {
		case PULL_UP_TO_REFRESH:
			originalLoadingLayout = getFooterLayout();
			showFootView();
			footLoadingLayout = mNewFootLoadingView;
			footLoadingLayout.reset();
			selection = mRefreshableView.getCount() - 1;
			scroll = mRefreshableView.getLastVisiblePosition() == selection;
			break;
		case PULL_DOWN_TO_REFRESH:
			showFootView();
			mNewFootLoadingView.reset();
		default:
			originalLoadingLayout = getHeaderLayout();
			listViewLoadingLayout = mHeaderLoadingView;
			scrollToHeight *= -1;
			selection = 0;
			scroll = mRefreshableView.getFirstVisiblePosition() == selection;
			break;
		}

		// Set our Original View to Visible
		originalLoadingLayout.setVisibility(View.VISIBLE);
		if (originalLoadingLayout instanceof FootLoadingLayout)
			originalLoadingLayout.setVisibility(View.GONE);

		/**
		 * Scroll so the View is at the same Y as the ListView header/footer,
		 * but only scroll if we've pulled to refresh and it's positioned
		 * correctly
		 */
		if (scroll && getState() != MANUAL_REFRESHING) {
			mRefreshableView.setSelection(selection);
			if (getCurrentMode() != Mode.PULL_UP_TO_REFRESH)
				setHeaderScroll(scrollToHeight);
		}

		// Hide the ListView Header/Footer
		switch (getCurrentMode()) {
		case PULL_UP_TO_REFRESH:
			footLoadingLayout.setVisibility(View.VISIBLE);
			break;
		case PULL_DOWN_TO_REFRESH:
		default:
			listViewLoadingLayout.setVisibility(View.GONE);
			break;
		}

		super.resetHeader();
	}

	@Override
	protected void setRefreshingInternal(boolean doScroll) {

		// If we're not showing the Refreshing view, or the list is empty, then
		// the header/footer views won't show so we use the
		// normal method
		ListAdapter adapter = mRefreshableView.getAdapter();
		if (!getShowViewWhileRefreshing() || null == adapter || adapter.isEmpty()) {
			super.setRefreshingInternal(doScroll);
			return;
		}

		super.setRefreshingInternal(false);

		final View originalLoadingLayout;
		final LoadingLayout listViewLoadingLayout;
		final int selection, scrollToY;
		final FootLoadingLayout footLoadingLayout;
		switch (getCurrentMode()) {
		case PULL_UP_TO_REFRESH:
			originalLoadingLayout = getFooterLayout();
			footLoadingLayout = mNewFootLoadingView;
			originalLoadingLayout.setVisibility(View.GONE);
			footLoadingLayout.setVisibility(View.VISIBLE);
			footLoadingLayout.refreshing();
			selection = mRefreshableView.getCount() - 1;
			scrollToY = getScrollY() - getHeaderHeight();

			break;
		case PULL_DOWN_TO_REFRESH:
			hideFootView();
		default:
			originalLoadingLayout = getHeaderLayout();
			listViewLoadingLayout = mHeaderLoadingView;

			listViewLoadingLayout.setVisibility(View.VISIBLE);
			originalLoadingLayout.setVisibility(View.INVISIBLE);
			listViewLoadingLayout.refreshing();
			selection = 0;
			scrollToY = getScrollY() + getHeaderHeight();
			break;
		}

		if (doScroll && getCurrentMode() != Mode.PULL_UP_TO_REFRESH) {
			// We scroll slightly so that the ListView's header/footer is at the
			// same Y position as our normal header/footer
			setHeaderScroll(scrollToY);
		}

		// Hide our original Loading View

		// Show the ListView Loading View and set it to refresh

		if (doScroll) {
			// Make sure the ListView is scrolled to show the loading
			// header/footer
			mRefreshableView.setSelection(selection);

			// Smooth scroll as normal
			smoothScrollTo(0);
		}
	}

	public void hideFootView() {
		mNewFootLoadingView.setVisibility(View.GONE);
	}

	public void showFootView() {
		if (getMode() == Mode.BOTH || getMode() == Mode.PULL_UP_TO_REFRESH)
			mNewFootLoadingView.setVisibility(View.VISIBLE);
	}

	protected class InternalListView extends ListView implements EmptyViewMethodAccessor {

		private boolean mAddedLvFooter = false;

		public InternalListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void draw(Canvas canvas) {
			/**
			 * This is a bit hacky, but ListView has got a bug in it when using
			 * Header/Footer Views and the list is empty. This masks the issue
			 * so that it doesn't cause an FC. See Issue #66.
			 */
			try {
				super.draw(canvas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public ContextMenuInfo getContextMenuInfo() {
			return super.getContextMenuInfo();
		}

		@Override
		public void setAdapter(ListAdapter adapter) {
			// Add the Footer View at the last possible moment
			if (!mAddedLvFooter) {
				addFooterView(mLvFooterLoadingFrame, null, true);
				mAddedLvFooter = true;
			}

			super.setAdapter(adapter);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshListView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}

	}

	@TargetApi(9)
	final class InternalListViewSDK9 extends InternalListView {

		public InternalListViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(PullToRefreshListView.this, deltaY, scrollY, isTouchEvent);

			return returnValue;
		}
	}

}
