package edu.upenn.cis350.calorietracker;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jay Jung on 4/21/2016.
 */
public class HistoryListFragment extends ListFragment {
    private ArrayList<Food> listChoice;
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
        listChoice = (ArrayList<Food>) this.bundle.getSerializable(HistoryAdapter.CHOSEN_LIST);

        //true for exercise, false for food
        pageNumber = this.bundle.getInt(HistoryAdapter.PAGE_NUMBER);

        //build the data for the list to use
        ArrayList<Map<String, String>> listStrings = buildData();
        String[] from = { "name", "values"};
        int[] to = { R.id.trackableName, R.id.values};
        SimpleAdapter listAdapter = new SimpleAdapter
                (getActivity(),listStrings,R.layout.row_layout, from, to);
        setListAdapter(listAdapter);
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

    public ArrayList<Food> getListChoice() {
        return listChoice;
    }
}
