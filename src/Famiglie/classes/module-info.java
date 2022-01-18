/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2SEModule/module-info.java to edit this template
 */

open module Famiglie {
    requires Commons;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.base;
    
    exports theopenhand.plugins.famiglie.data;
    exports theopenhand.plugins.famiglie.data.holders;
    exports theopenhand.plugins.famiglie.window.pickers;
}
