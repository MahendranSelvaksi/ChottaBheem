package com.unsullied.chottabheem.utils.mvp;

import com.unsullied.chottabheem.utils.RedeemModel;

import java.util.List;

public interface RedeemMVP {

    interface View {
        public void updateAdapter(List<RedeemModel> mData);
    }

    interface Presenter {
        void getRedeemList();

        void addRedeem(String redeemName, String phone,String operator, String redeemValue);
    }
}
