package com.unsullied.chottabheem.utils.dataModel;

import java.util.List;

public class OperatorSelectionModel {
    private String operatorName,optionValue1,optionValue2,optionValue3,optionValue4,hint;
    private boolean clickable,open;
    private int operatorId;
    private List<CircleModel> circleList;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getOptionValue1() {
        return optionValue1;
    }

    public void setOptionValue1(String optionValue1) {
        this.optionValue1 = optionValue1;
    }

    public String getOptionValue2() {
        return optionValue2;
    }

    public void setOptionValue2(String optionValue2) {
        this.optionValue2 = optionValue2;
    }

    public String getOptionValue3() {
        return optionValue3;
    }

    public void setOptionValue3(String optionValue3) {
        this.optionValue3 = optionValue3;
    }

    public String getOptionValue4() {
        return optionValue4;
    }

    public void setOptionValue4(String optionValue4) {
        this.optionValue4 = optionValue4;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public List<CircleModel> getCircleList() {
        return circleList;
    }

    public void setCircleList(List<CircleModel> circleList) {
        this.circleList = circleList;
    }
}
