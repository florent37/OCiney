package com.bdc.ociney.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.bdc.ociney.R;
import com.bdc.ociney.adapter.ObjectAdapter;
import com.bdc.ociney.fragment.core.BaseFragment;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Person.CastMember;
import com.bdc.ociney.view.CellMember;
import com.bdc.ociney.view.MovingImageView;

import java.util.List;


public class ListCastingFragment extends BaseFragment {

    public static List<CastMember> casting;
    static Movie movie;

    ObjectAdapter<CastMember> adapter;
    AbsListView list;

    MovingImageView imageFond;

    public static ListCastingFragment newInstance() {

        Bundle args = new Bundle();

        ListCastingFragment fragment = new ListCastingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_casting,
                container, false);

        setFragmentView(view);

        charger();
        remplir();
        ajouterListeners();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void charger() {
        list = (AbsListView) View.inflate(getActivity(), R.layout.list, null);
        ((ViewGroup) fragmentView).addView(list);

        this.imageFond = (MovingImageView) findViewById(R.id.imageFond);
    }

    @Override
    protected void remplir() {
        adapter = new ObjectAdapter<CastMember>(getActivity(), casting, R.layout.casting_member, CellMember.class);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (movie != null)
            imageFond.activerBlur(getActivity().getResources().getInteger(R.integer.fragment_movie_fond_blur))
                    .activerAnimations().loadFromUrl(getActivity(), movie.getUrlPoster(getActivity().getResources().getInteger(R.integer.fragment_movie_fond_blur_height)));

    }

    @Override
    protected void ajouterListeners() {
    }

    @Override
    public void onClick(View view) {

    }
}