package com.example.pinglist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pinglist.model.HostManager;
import com.example.pinglist.model.HostManager.Host;
import com.example.pinglist.model.HostManager.PingState;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details
 * (if present) is a {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ItemListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ItemListActivity extends FragmentActivity
        implements ItemListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
	private AsyncTask<Host, Integer, Long> task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
        }
        
        getActionBar().show();


        // TODO: If exposing deep links into your app, handle intents here.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.item_list_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	if (item.getItemId() == R.id.action_ping) {
    		ItemListFragment  itemListFragment = ((ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.item_list));
    		pingHosts(HostManager.HOSTS, ((HostListAdapter)itemListFragment.getListAdapter()));
    		return true;
    	}
    	return super.onMenuItemSelected(featureId, item);
    }

    private void pingHosts(List<Host> hosts, HostListAdapter hostListAdapter) {
    	if (task != null) {
    		task.cancel(true);
    	}
    	for (Host host: hosts) {
    		host.pingState = HostManager.PingState.PENDING;
    	}
		hostListAdapter.notifyDataSetChanged();
    	task = new PingTask().execute(hosts.toArray(new HostManager.Host[hosts.size()]));
	}
    
    private class PingTask extends AsyncTask<HostManager.Host, Integer, Long> {
        protected Long doInBackground(HostManager.Host... hosts) {
            int count = hosts.length;
            long totalSize = 0;
            for (int i = 0; i < count; i++) {
            	HostManager.Host host = hosts[i];
            	host.pingState = ping(host.ipNo) ? PingState.OK : PingState.ERROR;
                publishProgress((int) ((i / (float) count) * 100));
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }
            return totalSize;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
    		ItemListFragment  itemListFragment = ((ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.item_list));
        	((HostListAdapter)itemListFragment.getListAdapter()).notifyDataSetChanged();
        }

        protected void onPostExecute(Long result) {
//            showDialog("Downloaded " + result + " bytes");
        }
    }
    
    private boolean ping(String host) {
		Log.d("ping", "ping host " + host);
		Runtime runtime = Runtime.getRuntime();
		Process p = null;
		try {
			p = runtime.exec("/system/bin/ping -c 1 -W 1 " + host); 
		    BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		    while (true) {
		    	String line = in.readLine();
		    	if (line == null) {
		    		break;
		    	}
		    	Log.d("ping", "line=" + line);
		    }
			int mExitValue = p.waitFor();
			Log.d("ping", "ping mExitValue " + mExitValue);
			return mExitValue == 0;
		} catch (InterruptedException ignore) {
			Log.d("ping", "error ping host " + host, ignore);
		} catch (IOException e) {
			Log.d("ping", "error ping host " + host, e);
		} finally {
			p.destroy();
		}
		return false;
	}

	/**
     * Callback method from {@link ItemListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ItemDetailActivity.class);
            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
