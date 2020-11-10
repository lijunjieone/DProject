package com.a.helper.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewRootImpl;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    public static ViewRootImpl[] diff(ViewRootImpl[] views1, ViewRootImpl[] views2) {
        List<ViewRootImpl> list1 = views1 == null ? new ArrayList<ViewRootImpl>() : Arrays.asList(views1);
        List<ViewRootImpl> list2 = views2 == null ? new ArrayList<ViewRootImpl>() : Arrays.asList(views2);
        List<ViewRootImpl> result = new ArrayList<>(32);

        for (ViewRootImpl view : list1) {
            if (list2.contains(view) == false) {
                result.add(view);
            }
        }
        ViewRootImpl[] array = new ViewRootImpl[result.size()];
        result.toArray(array);
        return array;
    }

    public static Activity findAct(View view) {
        List<WeakReference<Activity>> references = RunningActivityFetcher.fetch();
        if (references == null) {
            return null;
        }
        for (WeakReference<Activity> act : references) {
            Activity activity = act.get();
            if (activity == null) {
                continue;
            }
            if (view == activity.getWindow().getDecorView().getRootView()) {
                return activity;
            }
        }
        return null;
    }

    public static String getTopFragment(View view) {
        Activity activity = findAct(view);
        if (activity != null && activity instanceof FragmentActivity) {
            FragmentActivity a = (FragmentActivity) activity;
            return getTopFragment(a.getSupportFragmentManager());
        } else {
            return "";
        }
    }

    public static ViewRootImpl getRootViewImpl(ViewGroup view) {
        boolean isRoot = ((ViewParent) view instanceof ViewRootImpl);
        while (!isRoot && !(view.getParent() instanceof ViewRootImpl)) {
            view = (ViewGroup) view.getParent();
            isRoot = ((ViewParent) view instanceof ViewRootImpl);
        }
        return (ViewRootImpl) view.getParent();
    }

 
    public static String getTopFragment(FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return "";
        }
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments == null || fragments.isEmpty()) {
            return "";
        }

        for (Fragment fragment : fragments) {
            if (fragment.isVisible()) {
                View view = fragment.getView();
                String fragmentName = fragment.getClass().getName();
                return fragmentName;
            }
        }

        return "";

    }
}
