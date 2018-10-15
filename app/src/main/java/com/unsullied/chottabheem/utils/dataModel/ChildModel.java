package com.unsullied.chottabheem.utils.dataModel;

public class ChildModel {
    private int childId;
    private String childName,childReferralCode,childEmailId;

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildReferralCode() {
        return childReferralCode;
    }

    public void setChildReferralCode(String childReferralCode) {
        this.childReferralCode = childReferralCode;
    }

    public String getChildEmailId() {
        return childEmailId;
    }

    public void setChildEmailId(String childEmailId) {
        this.childEmailId = childEmailId;
    }
}
