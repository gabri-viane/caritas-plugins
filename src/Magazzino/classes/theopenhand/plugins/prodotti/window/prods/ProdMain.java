/*
 * Copyright 2021 gabri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package theopenhand.plugins.prodotti.window.prods;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import theopenhand.plugins.prodotti.window.confs.add.ConfRegister;
import theopenhand.plugins.prodotti.window.confs.shower.ConfShower;
import theopenhand.plugins.prodotti.window.prods.add.ProdRegister;
import theopenhand.plugins.prodotti.window.prods.shower.ProdShower;

/**
 *
 * @author gabri
 */
public class ProdMain extends AnchorPane {

    @FXML
    private BorderPane containerBP;

    @FXML
    private Button newProdBTN;

    @FXML
    private Button selectProdBTN;

    @FXML
    private Button newConfBTN;

    @FXML
    private Button selectConfBTN;

    public ProdMain() {
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL resource = getClass().getResource("/theopenhand/plugins/prodotti/window/prods/ProdMain.fxml");
            loader.setLocation(resource);
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ProdMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        newProdBTN.setOnAction(a -> addProd());
        selectProdBTN.setOnAction(a -> showProd());
        newConfBTN.setOnAction(a -> addConf());
        selectConfBTN.setOnAction(a -> showConf());
    }

    private void addProd(){
        containerBP.setCenter(new ProdRegister());
    }
    
    public void showProd(){
        containerBP.setCenter(new ProdShower());
    }
    
    private void addConf(){
        containerBP.setCenter(new ConfRegister());
    }
    
    public void showConf(){
        containerBP.setCenter(new ConfShower());
    }

}
