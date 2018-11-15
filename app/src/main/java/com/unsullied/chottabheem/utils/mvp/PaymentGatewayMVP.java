package com.unsullied.chottabheem.utils.mvp;

import com.payumoney.core.PayUmoneySdkInitializer;

public interface PaymentGatewayMVP {
    public interface View{
        void getSuccessfulHash(PayUmoneySdkInitializer.PaymentParam mPaymentParam);
        void showError(String errorMsg);
    }

    public interface Presenter{
      //  void launchPayUMoneyFlow(String payableAmount,String mobileNumber,String emailId);
        void generateHashFromServer(String mobileNumber,String accountId,String name,String email,String payableAmount,String productInfo,String buttonText,
                                    String pageTitle);
    }
}
