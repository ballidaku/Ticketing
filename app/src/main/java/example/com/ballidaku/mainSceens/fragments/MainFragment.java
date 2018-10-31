package example.com.ballidaku.mainSceens.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import example.com.ballidaku.R;
import example.com.ballidaku.adapters.MainFragmentAdapter;
import example.com.ballidaku.commonClasses.CommonMethods;
import example.com.ballidaku.commonClasses.GridSpacingItemDecoration;
import example.com.ballidaku.databinding.FragmentMainBinding;
import example.com.ballidaku.mainSceens.MainActivity;

public class MainFragment<D>  extends Fragment
{
    FragmentMainBinding fragmentMainBinding;
    View view;
    Context context;

    MainFragmentAdapter mainFragmentAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        fragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        view = fragmentMainBinding.getRoot();
        context = getContext();
        fragmentMainBinding.setHandler(this);


        setUpViews();

        return view;
    }

    void setUpViews()
    {
        ((MainActivity) context).updateToolbarTitle(getString(R.string.zones));

//        ((MainActivity)context).setSupportActionBar(((MainActivity)context).activityMainBinding.toolbar);
//        ((MainActivity)context).getSupportActionBar().setDisplayShowHomeEnabled(false);
//        ((MainActivity)context).getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        ArrayList<D> mainList = new ArrayList<>();
        mainList.add((D) "VAN VIHAR\nDHUNGRI");
        mainList.add((D) "VAN VIHAR\nMANALI");
        mainList.add((D) "NATURE PARK\nNEAR WL INTERPRETATION CENTRE\nMANALI");
        mainList.add((D) "MINI ZOO\nREWALSAR");


        mainFragmentAdapter = new MainFragmentAdapter<>(context, mainList);
        fragmentMainBinding.recycleView.addItemDecoration(new GridSpacingItemDecoration(2, CommonMethods.getInstance().dpToPx(context, 5), true));
        fragmentMainBinding.recycleView.setItemAnimator(new DefaultItemAnimator());

        fragmentMainBinding.recycleView.setAdapter(mainFragmentAdapter);
    }

    public void setAdapter(ArrayList data)
    {
        mainFragmentAdapter.addData(data);
    }





}