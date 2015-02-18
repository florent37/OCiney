package com.bdc.ociney.modele.Person;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.ModelObject;

import java.util.ArrayList;
import java.util.List;

public class PersonFull extends Person {

    @Expose
    private Name name;

    @Expose
    private List<ModelObject> activity = new ArrayList<ModelObject>();

    public static PersonFull transformFull(PersonSmall person) {
        PersonFull p = new PersonFull();
        p.getActivity().addAll(person.getActivity());

        Log.d("PERSON1", person.toString());

        p.setPicture(person.getPicture());
        p.setCode(person.getCode());
        p.activity.clear();
        p.activity.addAll(person.getActivity());

        p.setStatistics(person.getStatistics());
        Name name = new Name();
        name.setFamily(person.getName());
        p.setName(name);

        Log.d("PERSON2", p.toString());

        return p;
    }

    public static List<PersonFull> transformListFull(List<PersonSmall> persons) {
        List<PersonFull> listFull = new ArrayList<PersonFull>();
        for (PersonSmall p : persons)
            listFull.add(transformFull(p));

        return listFull;
    }

    public List<ModelObject> getActivity() {
        return activity;
    }

    public void setActivity(List<ModelObject> activity) {
        this.activity = activity;
    }

    public Name getFullName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PersonFull{" +
                "name=" + name +
                super.toString() + "}";
    }

    public String getActivities() {
        String activities = "";
        int i = 0;

        if (this.activity.size() > 0) {

            for (ModelObject act : this.activity) {
                Log.e("Activities", act.get$());
                if (i != 0)
                    activities += " / ";

                activities += act.get$();
                i++;
            }
        } else {
            if (this.getActivityShort() != null) {
                String s = getActivityShort();
                return s.replaceAll(",", "/");
            }
        }

        return activities;
    }
}
