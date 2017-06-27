package com.clickmedia.vasilis.vivapayments.details;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.clickmedia.vasilis.vivapayments.R;
import com.clickmedia.vasilis.vivapayments.model.Product;
import com.clickmedia.vasilis.vivapayments.model.User;
import com.clickmedia.vasilis.vivapayments.util.ActivityUtils;

/*
******************************
*  8         8    888888888  *
*   8       8     8          *
*    8     8      888888888  *
*     8   8       8          *
*      8 8        8          *
*       8     .   8         .*
******************************
* Vasilis Fouroulis 24/06/2017
 */
public class DetailsActivity extends AppCompatActivity implements DetailsFragment.OnloadProductListener {

	private DrawerLayout mDrawerLayout;
	private TextView mTextViewToolbar;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_act);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.ColorPrimaryDark);

		init();

		User user = null;
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			user = (User)bundle.getSerializable("user");
		}

		DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
		if (detailsFragment == null) {
			detailsFragment = DetailsFragment.newInstance();
			ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
					detailsFragment, R.id.contentFrame);
		}

		new DetailsPresenter(getProduct(), detailsFragment,user); //todo pass a repository , get user from shared Preferrences or repository
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// Open the navigation drawer when the home icon is selected from the toolbar.
				mDrawerLayout.openDrawer(GravityCompat.START);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void changeToolbarTitle(String title) {
		mTextViewToolbar.setText(title);
	}

	private void init() {

		// vf :Toolbar customization
		Toolbar mToolbar = (Toolbar)  findViewById(R.id.toolbar);
		mTextViewToolbar = (TextView) findViewById(R.id.toolbar_title);
		mTextViewToolbar.setVisibility(View.VISIBLE);
		mTextViewToolbar.setText(getString(R.string.toolbar_activity_details));

		setSupportActionBar(mToolbar);
		ActionBar ab = getSupportActionBar();
		ab.setHomeAsUpIndicator(R.drawable.ic_menu);
		ab.setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);//disable toolbar title
		setUpNavigationDrawer();
	}

	private void setUpNavigationDrawer(){
		mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		if (navigationView != null) {
			setupDrawerContent(navigationView);
		}
	}

	private void setupDrawerContent(NavigationView navigationView) {
		navigationView.setNavigationItemSelectedListener(
				new NavigationView.OnNavigationItemSelectedListener() {
					@Override
					public boolean onNavigationItemSelected(MenuItem menuItem) {
						switch (menuItem.getItemId()) {
							case R.id.list_navigation_menu_item:
								NavUtils.navigateUpFromSameTask(DetailsActivity.this);
								break;
							case R.id.statistics_navigation_menu_item:
								// Do nothing, we're already on that screen
								break;
							default:
								break;
						}
						// Close the navigation drawer when an item is selected.
						menuItem.setChecked(true);
						mDrawerLayout.closeDrawers();
						return true;
					}
				});
	}
	//todo Dynamically get product from server implement a Repository to load inside Presenter
	private Product getProduct(){

		String[] images    = getResources().getStringArray(R.array.product_images);
		String title       = getString(R.string.product_title);
		String description = getString(R.string.description);
		String buyLink     = getString(R.string.buylink);
		String price       = getString(R.string.price);
		int MaxInstallment = 12;

		return new Product(title,description,images,buyLink,price,MaxInstallment);
	}
}
