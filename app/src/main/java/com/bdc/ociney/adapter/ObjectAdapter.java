package com.bdc.ociney.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bdc.ociney.view.ViewCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 20/04/2014.
 */
public class ObjectAdapter<OBJECT> extends ArrayAdapter<OBJECT> {

    public ObjectAdapterLoadMore listener;
    Context context;
    int layoutId;
    Class cellClass;

    ArrayList<Integer> apparitions = new ArrayList<Integer>();

    public ObjectAdapter(Context context, List<OBJECT> objets, int layoutId, Class<? extends ViewCell> cellClass) {
        super(context, layoutId, objets);
        this.context = context;
        this.layoutId = layoutId;
        this.cellClass = cellClass;

        //this.cells = new ViewCell[this.objects.size()];
    }

    @Override
    public int getCount() {
        int count = 0;
        try {
            count = super.getCount();
        } catch (Exception e) {
        }
        return count;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = View.inflate(context, layoutId, null);

        ViewCell cell = null;
        if (view.getTag() != null && view.getTag() instanceof ViewCell)
            cell = (ViewCell) view.getTag();
        if (view.getTag() == null || !(view.getTag() instanceof ViewCell)) {
            try {
                cell = (ViewCell) Class.forName(cellClass.getName()).newInstance();
                cell.construire(context, view);
            } catch (Exception e) {
                e.printStackTrace();
            }
            view.setTag(cell);
        }
        cell.construire((OBJECT) getItem(i), i);


        int apparition = apparaitre(i);
        if (apparition == 1) {
            try {
                cell.animer();
            } catch (Exception e) {
            }
        }

        if (apparition >= 1 && listener != null && getCount() > 4 && i >= (getCount() - 3)) {
            listener.loadMore();
        }

        return view;
    }

    public int apparaitre(int positon) {
        int apparition = apparitions.size() <= positon ? 0 : apparitions.get(positon);
        apparition++;
        apparitions.add(positon, apparition);
        return apparition;
    }

    public interface ObjectAdapterLoadMore {
        public void loadMore();
    }
}
