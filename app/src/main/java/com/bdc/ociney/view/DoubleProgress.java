package com.bdc.ociney.view;

import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bdc.ociney.R;

/**
 * Created by florentchampigny on 29/04/2014.
 */
public class DoubleProgress extends LinearLayout {

    private static final String TAG = "DoubleProgress";

    View mView;

    String stringHaut = "Haut";
    String stringBas = "Bas";
    float max;
    int temps;
    int attente;
    int textFromAlpha = 40;
    int textToAlpha = 100;

    View barreHaut, barreBas;
    View progressBarHaut, progressBarBas;
    TextView valeurHaut, valeurBas;
    TextView texteHaut, texteBas;

    float vHaut = 0;
    float vBas = 0;

    public DoubleProgress(Context context) {
        this(context, null);
    }

    public DoubleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.DoubleProgress);
        try {
            stringHaut = styledAttrs.getString(R.styleable.DoubleProgress_texteHaut);
            stringBas = styledAttrs.getString(R.styleable.DoubleProgress_texteBas);
            max = styledAttrs.getFloat(R.styleable.DoubleProgress_max, 5);
            temps = styledAttrs.getInt(R.styleable.DoubleProgress_temps, 1000);
            attente = styledAttrs.getInt(R.styleable.DoubleProgress_attente, 0);
            textFromAlpha = styledAttrs.getInt(R.styleable.DoubleProgress_attente, 0);
            textToAlpha = styledAttrs.getInt(R.styleable.DoubleProgress_attente, 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            styledAttrs.recycle();
        }

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mView = inflater.inflate(R.layout.double_progress, this, true); //ajoute la view

        charger();
        remplir();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void charger() {
        barreHaut = mView.findViewById(R.id.barreHaut);
        barreBas = mView.findViewById(R.id.barreBas);

        progressBarHaut = barreHaut.findViewById(R.id.progressBar);
        progressBarBas = barreBas.findViewById(R.id.progressBar);

        valeurHaut = (TextView) barreHaut.findViewById(R.id.valeur);
        valeurBas = (TextView) barreBas.findViewById(R.id.valeur);

        texteHaut = (TextView) barreHaut.findViewById(R.id.text);
        texteBas = (TextView) barreBas.findViewById(R.id.text);
    }

    private void remplir() {
        texteHaut.setText(stringHaut);
        texteBas.setText(stringBas);
    }

    public void setValeurHaut(float f) {
        vHaut = f;
    }

    public void setValeurBas(float f) {
        vBas = f;
    }

    public void animer() {

        /**
         * Attente de la largeur de la vue barreHaut
         */
        barreHaut.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                //now we can retrieve the width and height
                int width = barreHaut.getWidth();

                animer(width);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                    barreHaut.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    barreHaut.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    private void animer(int width) {

        valeurHaut.setText("");
        valeurBas.setText("");

        ViewGroup.LayoutParams params = progressBarHaut.getLayoutParams();
        params.width = 0;
        progressBarHaut.setLayoutParams(params);

        ViewGroup.LayoutParams params2 = progressBarBas.getLayoutParams();
        params2.width = 0;
        progressBarBas.setLayoutParams(params2);

        float largeurMax = barreHaut.getWidth();
        float pourcentHaut = vHaut / max;
        float pourcentBas = vBas / max;

        int largeurHaut = (int) (largeurMax * pourcentHaut);
        int largeurtBas = (int) (largeurMax * pourcentBas);

        final AnimatorSet anim = new AnimatorSet();
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration((long) temps);
        anim.playTogether(
                ValueAnimator.ofObject(new WidthEvaluator(progressBarHaut, valeurHaut, vHaut), 0, largeurHaut),
                ValueAnimator.ofObject(new WidthEvaluator(progressBarBas, valeurBas, vBas), 0, largeurtBas)
        );

        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                anim.start();
            }
        }, attente);

    }

    private class WidthEvaluator extends IntEvaluator {

        private View v;
        private TextView tv;
        private float valeur;

        public WidthEvaluator(View v, TextView tv, float valeur) {
            this.v = v;
            this.tv = tv;
            this.valeur = valeur;
        }


        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            int num = super.evaluate(fraction, startValue, endValue);

            tv.setText(String.format("%.1f", fraction * valeur).replace(",", "."));

            ViewGroup.LayoutParams params = v.getLayoutParams();
            params.width = num;
            v.setLayoutParams(params);

            return num;
        }
    }

}
