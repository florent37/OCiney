package com.bdc.ociney.service;

import android.util.Base64;
import android.util.Log;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by florentchampigny on 18/04/2014.
 */
public class ServiceSecurity {

    public static final String ALLOCINE_SECRET_KEY = "29d185d98c984a359e6e6f26a0474269";

    static boolean AFFICHER_LOG = false;
    static String LOG_TAG = "ALLOCINE_SECRET";

    static String sha1(String input) {
        try {
            MessageDigest md = null;
            md = MessageDigest.getInstance("SHA1");
            md.update((input).getBytes("UTF-8"));
            byte raw[] = md.digest();
            String hash = Base64.encodeToString(raw, Base64.DEFAULT);

            hash = hash.replace("\n", "");

            return hash;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    static String getSED() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String sed = df.format(new Date());

        return sed;
    }

    static String getSIG(String params, String sed) {
        if (AFFICHER_LOG)
            Log.d("ALLOCINE_SECRET", "sed :" + sed);

        if (AFFICHER_LOG)
            Log.d("ALLOCINE_SECRET", "params :" + params);

        String input = ALLOCINE_SECRET_KEY + params + "&sed=" + sed;

        input = input.replace(":", "%3A");
        input = input.replace(",", "%2C");

        if (AFFICHER_LOG)
            Log.d("ALLOCINE_SECRET", "input :" + input);

        String sha1 = sha1(input);
        if (AFFICHER_LOG)
            Log.d("ALLOCINE_SECRET", "sha1 :" + sha1);


        String sig = "";
        try {
            sig = URLEncoder.encode(sha1, "UTF8");
            if (AFFICHER_LOG)
                Log.d("ALLOCINE_SECRET", "sig :" + sig);
        } catch (Exception e) {
            e.printStackTrace();
        } //Retrofit le fait tout seul, pas besoin ici d'encrypter eh HTML

        return sha1;
    }

    static String applatir(List<String> liste) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < liste.size(); ++j) {
            if (j != 0)
                sb.append(",");
            sb.append(liste.get(j));
        }
        return sb.toString();
    }

    static String construireParams(boolean ajouterCode, Object... params) {

        List<Object> ps = new ArrayList<Object>();

        ps.addAll(Arrays.asList(AllocineService.PARTNER, AllocineService.ALLOCINE_PARTNER_KEY));
        if (ajouterCode)
            ps.addAll(Arrays.asList(AllocineService.CODE, AllocineService.APP_ID));
        ps.addAll(Arrays.asList(AllocineService.FORMAT, AllocineService.FORMAT_JSON));

        ps.addAll(Arrays.asList(params));

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ps.size(); i += 2) {
            if (i + 1 < ps.size()) {
                if (i != 0)
                    sb.append("&");
                sb.append(ps.get(i)).append("=");

                Object value = ps.get(i + 1);
                if (value instanceof String)
                    sb.append((String) value);
                else if (value instanceof List) {
                    List<String> liste = (List<String>) value;
                    sb.append(applatir(liste));
                }
            }
        }
        return sb.toString();
    }

}
