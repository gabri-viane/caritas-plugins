/*
 * Copyright 2022 gabri.
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
package theopenhand.plugins.borse.connector.runtimes;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import theopenhand.plugins.borse.window.borse.BorseMain;
import theopenhand.plugins.borse.window.borse.add.BorsaRegister;
import theopenhand.plugins.borse.window.borse.shower.BorsaShower;
import theopenhand.plugins.borse.window.elementi.editor.ElementiCreator;
import theopenhand.statics.StaticReferences;
import theopenhand.window.graphics.creators.DialogCreator;
import theopenhand.window.graphics.ribbon.RibbonFactory;
import theopenhand.window.graphics.ribbon.elements.RibbonGroup;

/**
 *
 * @author gabri
 */
public class Ribbon {

    private final BorseRR rr;
    private BorseMain bm;
    private BorsaShower bs;

    public Ribbon(BorseRR rr) {
        this.rr = rr;
        createRibbon();
    }

    private void createRibbon() {
        RibbonGroup reg_grp = RibbonFactory.createGroup(rr, "Crea", "Borse");
        RibbonGroup show_grp = RibbonFactory.createGroup(rr, "Visualizza", "Borse");
        RibbonGroup hand_grp = RibbonFactory.createGroup(rr, "Gestisci", "Borse");

        Button reg_bor = new Button("Nuova Borsa");
        reg_bor.setOnAction(a -> createBorsa());
        Button reg_els = new Button("Componi");
        reg_els.setOnAction(a -> createElems());
        VBox vb = new VBox(reg_bor, reg_els);
        vb.setSpacing(5);
        vb.setPrefHeight(Region.USE_COMPUTED_SIZE);
        vb.setMinHeight(Region.USE_COMPUTED_SIZE);
        reg_grp.addNode(vb);

        Button show_b = new Button("Mostra Borsa");
        show_b.setOnAction(a -> showBorsa());
        show_grp.addNode(show_b);

        Button hand_b = new Button("Elenco Borse");
        hand_b.setOnAction(a -> homeBorse());
        hand_grp.addNode(hand_b);

    }

    public void createBorsa() {
        BorsaRegister br = new BorsaRegister();
        Stage dialog = DialogCreator.createDialog(br);
        br.onExitPressed(() -> {
            dialog.close();
        });
        dialog.show();
    }

    public void createElems() {
        ElementiCreator ec = new ElementiCreator();
        StaticReferences.getMainWindowReference().setCenterNode(ec);
    }

    public void showBorsa() {
        if (bs == null) {
            bs = new BorsaShower();
        }
        StaticReferences.getMainWindowReference().setCenterNode(bs);
    }

    public void homeBorse() {
        if (bm == null) {
            bm = new BorseMain();
        }
        bm.onRefresh(true);
        StaticReferences.getMainWindowReference().setCenterNode(bm);
    }

}
