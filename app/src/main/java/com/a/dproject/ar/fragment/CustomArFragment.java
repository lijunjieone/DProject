package com.a.dproject.ar.fragment;


import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;

class CustomArFragment extends ArFragment {
    @Override
    protected Config getSessionConfiguration(Session session) {
        Config config = super.getSessionConfiguration(session);
        config.setPlaneFindingMode(Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL);
        return config;
    }
}