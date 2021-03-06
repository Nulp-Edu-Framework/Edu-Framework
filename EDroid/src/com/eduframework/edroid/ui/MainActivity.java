package com.eduframework.edroid.ui;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.atmosphere.wasync.Function;

import com.eduframework.edroid.R;
import com.eduframework.edroid.adapter.NavDrawerListAdapter;
import com.eduframework.edroid.dto.PresentationDTO;
import com.eduframework.edroid.model.NavDrawerItem;
import com.eduframework.edroid.service.EduFrameworkAPIService;
import com.eduframework.edroid.service.EduFrameworkAPIServiceImpl;
import com.eduframework.edroid.service.EduFrameworkChatService;
import com.eduframework.edroid.service.EduFrameworkChatServiceImpl;
import com.eduframework.edroid.service.EduFrameworkPresentationService;
import com.eduframework.edroid.service.EduFrameworkPresentationServiceImpl;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	
	private EduFrameworkPresentationService eduPresentationService;
	private EduFrameworkChatService eduChatService;
	
	private FragmentManager fragmentManager;
	
	private Boolean isFirstRun = true;
	
	private Integer lectionId;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		Intent intent = getIntent();
		lectionId = intent.getIntExtra("LECTURE_ID", -1);
		
		eduPresentationService = new EduFrameworkPresentationServiceImpl(lectionId);
		eduChatService = new EduFrameworkChatServiceImpl(lectionId);
		fragmentManager = getFragmentManager();

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
		// Pages
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// What's hot, We  will add a counter here
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
		

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return false;
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {		
		switch (position) {
	       case 0:
	            if(fragmentManager.findFragmentByTag("one") != null) {
	                //if the fragment exists, show it.
	                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("one")).commit();
	            } else {
	                //if the fragment does not exist, add it to fragment manager.
	                fragmentManager.beginTransaction().add(R.id.frame_container, new PresentationFragment(eduPresentationService), "one").commit();
	            }
	            if(fragmentManager.findFragmentByTag("two") != null){
	                //if the other fragment is visible, hide it.
	                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("two")).commit();
	            }
	            
	            if(fragmentManager.findFragmentByTag("three") != null){
	                //if the other fragment is visible, hide it.
	                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("three")).commit();
	            }
	            break;
	        case 1:
	            if(fragmentManager.findFragmentByTag("two") != null) {
	                //if the fragment exists, show it.
	                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("two")).commit();
	            } else {
	                //if the fragment does not exist, add it to fragment manager.
	                fragmentManager.beginTransaction().add(R.id.frame_container, new ChatFragment(eduChatService), "two").commit();
	            }
	            if(fragmentManager.findFragmentByTag("one") != null){
	                //if the other fragment is visible, hide it.
	                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("one")).commit();
	            }
	            if(fragmentManager.findFragmentByTag("three") != null){
	                //if the other fragment is visible, hide it.
	                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("three")).commit();
	            }
	            break;
	        case 2:
	            if(fragmentManager.findFragmentByTag("three") != null) {
	                //if the fragment exists, show it.
	                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("three")).commit();
	            } else {
	                //if the fragment does not exist, add it to fragment manager.
	                fragmentManager.beginTransaction().add(R.id.frame_container, new AudioFragment(lectionId), "three").commit();
	            }
	            
	            if(fragmentManager.findFragmentByTag("one") != null){
	                //if the other fragment is visible, hide it.
	                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("one")).commit();
	            }
	            
	            if(fragmentManager.findFragmentByTag("two") != null){
	                //if the other fragment is visible, hide it.
	                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("two")).commit();
	            }
	            break;
		default:
            if(fragmentManager.findFragmentByTag("one") != null) {
                //if the fragment exists, show it.
                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("one")).commit();
            } else {
                //if the fragment does not exist, add it to fragment manager.
                fragmentManager.beginTransaction().add(R.id.frame_container, new ChatFragment(eduChatService), "one").commit();
            }
            if(fragmentManager.findFragmentByTag("one") != null){
                //if the other fragment is visible, hide it.
                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("one")).commit();
            }
            
            if(fragmentManager.findFragmentByTag("two") != null){
                //if the other fragment is visible, hide it.
                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("two")).commit();
            }
            
            if(fragmentManager.findFragmentByTag("three") != null){
                //if the other fragment is visible, hide it.
                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("three")).commit();
            }
            break;
		}
		
		fragmentManager.executePendingTransactions();
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);

	}


	@Override
	protected void onStart() {
		super.onStart();
		if(isFirstRun){
			displayView(0);
			displayView(1);
			displayView(0);
			isFirstRun = false;
		}
	}
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		eduChatService.leaveChat();
		eduPresentationService.leavePresentation();
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
