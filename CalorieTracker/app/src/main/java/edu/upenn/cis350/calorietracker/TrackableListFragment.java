package edu.upenn.cis350.calorietracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.*;
import android.widget.*;

import java.util.*;

/**
 * Created by Zachary on 2/28/16.
 * An activity/fragment class for showing the lists
 */
public class TrackableListFragment extends ListFragment {

    private ArrayList<Trackable> listChoice;
    public static boolean listType;
    public static int pageNumber;
    private Bundle bundle;

    /**
     * Method to create view
     */
    @Override
    public View onCreateView
            (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list, container, false);
        return view;
    }

    /**
     * Creating activity
     */
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.bundle = getArguments();
        listChoice = (ArrayList<Trackable>) this.bundle.getSerializable(PagerAdapter.CHOSEN_LIST);
        //true for exercise, false for food
        listType = this.bundle.getBoolean(PagerAdapter.IS_EXERCISE);
        pageNumber = this.bundle.getInt(PagerAdapter.PAGE_NUMBER);
        switch (pageNumber) {
             case 0:
                registerForContextMenu(getListView());
                break;
            case 1:
                break;
            case 2:
                registerForContextMenu(getListView());
                break;
            default:
                break;
        }
        ArrayList<Map<String, String>> listStrings = buildData();
        String[] from = { "name", "values"};
        int[] to = { R.id.trackableName, R.id.values};
        SimpleAdapter listAdapter = new SimpleAdapter
                (getActivity(),listStrings,R.layout.row_layout, from, to);
        setListAdapter(listAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==android.R.id.list) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.edit: {
                Trackable trackable = listChoice.get(info.position);
                Intent i = new Intent();
                i.putExtra("listCanceled", false);
                i.putExtra("trackableFromList", trackable);
                if (TrackableListFragment.pageNumber == 0) {
                    i.putExtra("isEditing", 1); //today list
                }
                else if (TrackableListFragment.pageNumber == 2) {
                    i.putExtra("isEditing", 2); // favorite list
                }
                getActivity().setResult(Activity.RESULT_OK,i);
                getActivity().finish();
                return true;
            }
            case R.id.delete: {
                if (!TrackableListFragment.listType) { // food is false, exercise is true
                    if (TrackableListFragment.pageNumber == 0) {
                        TrackableList.removeTodayFood(info.position);
                    }
                    else if (TrackableListFragment.pageNumber == 2) {
                        TrackableList.removeFavoriteFood(info.position);
                    }
                }
                else {
                    if (TrackableListFragment.pageNumber == 0) {
                        TrackableList.removeTodayExercise(info.position);
                    }
                    else if (TrackableListFragment.pageNumber == 2) {
                        TrackableList.removeFavoriteExercise(info.position);
                    }
                }
                getActivity().recreate();
                return true;
            }
            case R.id.cancel:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        Trackable item = listChoice.get(position);
        FoodDialog fd = new FoodDialog();
        fd.setNote(item.notes);
        fd.setTitle(item.getName());
        fd.setTrackable(item);
        fd.setlf(this);
        fd.show(getFragmentManager(), "dialog");
    }

    private ArrayList<Map<String, String>> buildData() {
        ArrayList<Map<String, String>> strings = new ArrayList<Map<String, String>>();
        for (Trackable t : listChoice) {
            strings.add(putData(t.getName(), t.toString()));
        }
        return strings;
    }

    private HashMap<String, String> putData(String name, String values) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("name", name);
        item.put("values", values);
        return item;
    }

}
