package com.bigdata101.bigdata101;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigdata101.bigdata101.model.Article;
import com.bigdata101.bigdata101.service.DateFormatter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArticleViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArticleViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Article article;
    private String mParam2;

    private MyFragmentInteraction mListener;

    public ArticleViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArticleViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArticleViewFragment newInstance(Parcelable param1, String param2) {
        ArticleViewFragment fragment = new ArticleViewFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            article = getArguments().getParcelable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_article_view, container, false);

        TextView articleText = rootview.findViewById(R.id.article_view);
        TextView authorText = rootview.findViewById(R.id.article_view_author);
        TextView dateText = rootview.findViewById(R.id.article_view_date);
        articleText.setText(article.getArticle());
        authorText.append(article.getAuthor());
        dateText.append(DateFormatter.getFormattedDate(article.getRequestTime()));


        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.fragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MyFragmentInteraction) {
            mListener = (MyFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
