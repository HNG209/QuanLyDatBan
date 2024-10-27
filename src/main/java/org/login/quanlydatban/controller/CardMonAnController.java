package org.login.quanlydatban.controller;

import javafx.fxml.FXML;
import org.login.quanlydatban.entity.MonAn;

public class CardMonAnController {

    private int index;
    @FXML
    public void them(){
        System.out.println("calling from card " + index);
    }

    public void setIndex(int index){
        this.index = index;
    }
}
