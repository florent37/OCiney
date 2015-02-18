package com.bdc.ociney.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bdc.ociney.R;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class ImagesPagerAdapter extends FragmentStatePagerAdapter {

    Activity activity;
    List<String> imagesUrl;
    int position;

    public ImagesPagerAdapter(FragmentManager fm, Activity activity, List<String> imagesUrl) {
        super(fm);
        this.activity = activity;
        this.imagesUrl = imagesUrl;
    }

    @Override
    public Fragment getItem(final int position) {

        final int p = position;
        return new Fragment() {

            PhotoView image;
            String urlImage;

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {

                View view = View.inflate(getActivity(), R.layout.image_pager_movie, null);
                image = (PhotoView) view.findViewById(R.id.parallaxContent);
                urlImage = imagesUrl.get(p);
                Picasso.with(getActivity()).load(urlImage).into(image);

                image.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage(R.string.message_fond_ecran)
                                .setTitle(R.string.titre_fond_ecran)
                                .setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        new LoadWalpaper().execute();
                                    }
                                })
                                .setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();

                        return false;
                    }
                });

                try {
                    getItem(position + 1);
                } catch (Exception e) {
                }

                return view;
            }

            class LoadWalpaper extends AsyncTask<Object, Void, Void> {

                @Override
                protected Void doInBackground(Object... objects) {

                    DisplayMetrics metrics = new DisplayMetrics();
                    activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    int height = metrics.heightPixels;
                    int width = metrics.widthPixels;

                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
                    wallpaperManager.setWallpaperOffsetSteps(1, 1);
                    wallpaperManager.suggestDesiredDimensions(width, height);

                    try {
                        String urlWalpaper = urlImage.replace("/r_10000_" + activity.getResources().getInteger(R.integer.fragment_movie_fond_height), "/r_" + (width) + "_" + (height));
                        wallpaperManager.setStream(new URL(urlWalpaper).openStream());


                     /*   Bitmap bmap2 = BitmapFactory.decodeStream(new URL(urlWalpaper).openStream());

                        DisplayMetrics metrics = new DisplayMetrics();
                        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        int height = metrics.heightPixels;
                        int width = metrics.widthPixels;
                        Bitmap bitmap = Bitmap.createScaledBitmap(bmap2, width, height, true);
activity.setWallpaper(bitmap);
                           // wallpaperManager.setBitmap(bitmap);
*/

                    } catch (Exception e) {
                        if (e.getMessage() != null)
                            e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void v) {
                    super.onPostExecute(v);
                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(activity, R.string.fond_ecran_change, Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        };


    }

    @Override
    public int getCount() {
        return imagesUrl.size();
    }

}
