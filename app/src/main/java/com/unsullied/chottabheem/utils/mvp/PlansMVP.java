package com.unsullied.chottabheem.utils.mvp;

import com.unsullied.chottabheem.utils.dataModel.BrowsePlansChildModel;

import java.util.List;

public interface PlansMVP {
    public interface PlansView {
        void loadPlansData(List<BrowsePlansChildModel> mData);

        void errorShow(String errorMsg);
    }

    public interface PlansPresenter {
        void getPlans(String url,String hint);
    }
}
