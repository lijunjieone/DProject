package com.a.helper.window.compact.compact;

import android.view.View;
import android.view.ViewRootImpl;

public interface IWindowChangeListener {
  void onAddWindow(ViewRootImpl viewRootImpl, View rootView);

  void onRemoveWindow(ViewRootImpl viewRootImpl, View rootView);
}
