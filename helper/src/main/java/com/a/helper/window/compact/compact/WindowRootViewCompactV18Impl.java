package com.a.helper.window.compact.compact;//package com.wanjian.sak.system.window.compact;
//
//
//import android.support.annotation.NonNull;
//import android.view.View;
//import android.view.ViewRootImpl;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.List;
//
//class WindowRootViewCompactV18Impl extends WindowRootViewCompactV16Impl {
//
//    private Field mViewsField;
//    private Object mWindowManagerGlobal;
//    private List<IWindowChangeListener> changeListeners = new ArrayList<>();
//    private ViewRootImpl[] lastViews;
//
//    WindowRootViewCompactV18Impl() {
//        super(null);
//    }
//
//    @Override
//    void init() {
//        try {
//            Class wmClz = Class.forName("android.view.WindowManagerGlobal");
//            Method getInstanceMethod = wmClz.getDeclaredMethod("getInstance");
//            mWindowManagerGlobal = getInstanceMethod.invoke(wmClz);
//            mViewsField = wmClz.getDeclaredField("mRoots");
//            mViewsField.setAccessible(true);
//            lastViews = (ViewRootImpl[]) mViewsField.get(mWindowManagerGlobal);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        } catch (InvocationTargetException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    void check() {
//        if (changeListeners == null || changeListeners.isEmpty()) {
//            return;
//        }
//        try {
//            ViewRootImpl[] curViews = (ViewRootImpl[]) mViewsField.get(mWindowManagerGlobal);
//            if (curViews != lastViews) {
//                notifyWindowChange(lastViews, curViews, changeListeners);
//                lastViews = curViews;
//            }
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    void onAddWindowChangeListener(@NonNull IWindowChangeListener changeListener) {
//        changeListeners.add(changeListener);
//        if (lastViews == null) {
//            return;
//        }
//        for (ViewRootImpl view : lastViews) {
//            changeListener.onAddWindow(view);
//        }
//    }
//
//    @Override
//    void onRemoveWindowChangeListener(@NonNull IWindowChangeListener changeListener) {
//        changeListeners.remove(changeListener);
//    }
//
//}