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
package theopenhand.plugins.prodotti.connector.runtimes;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import theopenhand.plugins.prodotti.window.confs.add.ConfRegister;
import theopenhand.plugins.prodotti.window.confs.shower.ConfShower;
import theopenhand.plugins.prodotti.window.dons.add.DonRegister;
import theopenhand.plugins.prodotti.window.dons.shower.DonShower;
import theopenhand.plugins.prodotti.window.mag.MagMain;
import theopenhand.plugins.prodotti.window.mag.add.entrate.EntRegister;
import theopenhand.plugins.prodotti.window.mag.shower.entrate.EntMain;
import theopenhand.plugins.prodotti.window.mots.add.MotRegister;
import theopenhand.plugins.prodotti.window.mots.shower.MotShower;
import theopenhand.plugins.prodotti.window.prods.ProdMain;
import theopenhand.plugins.prodotti.window.prods.add.ProdRegister;
import theopenhand.plugins.prodotti.window.prods.shower.ProdShower;
import theopenhand.statics.StaticReferences;
import theopenhand.window.graphics.creators.DialogCreator;
import theopenhand.window.graphics.ribbon.RibbonFactory;
import theopenhand.window.graphics.ribbon.elements.RibbonGroup;

/**
 *
 * @author gabri
 */
public class Ribbon {

    private final ProdottiRR rr;
    private EntMain em;
    private MagMain mm;
    private ProdMain pm;
    private DonShower ds;
    private ConfShower cs;
    private MotShower ms;
    private ProdShower ps;

    public Ribbon(ProdottiRR rr) {
        this.rr = rr;
        createRibbon();
    }

    private void createRibbon() {
        RibbonGroup reg_grp = RibbonFactory.createGroup(rr, "Crea", "Prodotti");
        RibbonGroup reg_grp_mag= RibbonFactory.createGroup(rr, "Crea", "Magazzino");
        RibbonGroup show_prod_grp = RibbonFactory.createGroup(rr, "Visualizza", "Prodotti");
        RibbonGroup show_mag_grp = RibbonFactory.createGroup(rr, "Visualizza", "Magazzino");
        RibbonGroup hand_grp = RibbonFactory.createGroup(rr, "Gestisci", "Prodotti");

        //Aggiungi
        Button new_prod = new Button("Nuovo Prodotto");
        new_prod.setOnAction(a -> regProd());

        Button new_conf = new Button("Nuova Confezione");
        new_conf.setOnAction(a -> regConf());

        VBox v1 = new VBox(new_prod, new_conf);
        v1.setSpacing(5);
        v1.setPrefHeight(Region.USE_COMPUTED_SIZE);
        v1.setMinHeight(Region.USE_COMPUTED_SIZE);

        reg_grp.addNode(v1);

        Button new_entr = new Button("Registra Entrata");
        new_entr.setOnAction(a -> regEntr());

        Button new_don = new Button("Nuovo Donatore");
        new_don.setOnAction(a -> regDon());

        VBox v2 = new VBox(new_entr, new_don);
        v2.setSpacing(5);
        v2.setPrefHeight(Region.USE_COMPUTED_SIZE);
        v2.setMinHeight(Region.USE_COMPUTED_SIZE);
        reg_grp_mag.addNode(v2);

        //Mostra
        Button show_prod = new Button("Mostra prodotto");
        show_prod.setOnAction(a -> showProd());

        Button show_conf = new Button("Mostra Confezione");
        show_conf.setOnAction(a -> showConf());

        VBox v3 = new VBox(show_prod, show_conf);
        v3.setSpacing(5);
        v3.setPrefHeight(Region.USE_COMPUTED_SIZE);
        v3.setMinHeight(Region.USE_COMPUTED_SIZE);
        show_prod_grp.addNode(v3);

        Button show_ent = new Button("Entrate");
        show_ent.setOnAction(a -> showEntr());

        Button show_don = new Button("Donatori");
        show_don.setOnAction(a -> showDon());

        VBox v4 = new VBox(show_ent, show_don);
        v4.setSpacing(5);
        v4.setPrefHeight(Region.USE_COMPUTED_SIZE);
        v4.setMinHeight(Region.USE_COMPUTED_SIZE);

        //Gestisci
        Button home_prod = new Button("Prodotti/Confezioni");
        home_prod.setOnAction(a -> homeProd());

        Button home_mag = new Button("Magazzino");
        home_mag.setOnAction(a -> homeMag());

        hand_grp.addNode(home_prod);
        show_mag_grp.addNode(home_mag).addNode(v4);

        Button new_mot = new Button("Motivo modifica");
        new_mot.setOnAction(a -> regMotivo());

        Button show_mot = new Button("Mostra Motivo");
        show_mot.setOnAction(a -> showMot());

        VBox v5 = new VBox(new_mot, show_mot);
        v5.setSpacing(5);
        v5.setPrefHeight(Region.USE_COMPUTED_SIZE);
        v5.setMinHeight(Region.USE_COMPUTED_SIZE);
        show_mag_grp.addNode(v5);

    }

    public void regProd() {
        ProdRegister pr = new ProdRegister();
        Stage dialog = DialogCreator.createDialog(pr);
        pr.onExitPressed(() -> {
            dialog.close();
        });
        dialog.show();
    }

    public void regConf() {
        ConfRegister cr = new ConfRegister();
        Stage dialog = DialogCreator.createDialog(cr);
        cr.onExitPressed(() -> {
            dialog.close();
        });
        dialog.show();
    }

    public void regEntr() {
        EntRegister er = new EntRegister();
        Stage dialog = DialogCreator.createDialog(er);
        er.onExitPressed(() -> {
            dialog.close();
        });
        dialog.show();
    }

    public void regDon() {
        DonRegister dr = new DonRegister();
        Stage dialog = DialogCreator.createDialog(dr);
        dr.onExitPressed(() -> {
            dialog.close();
        });
        dialog.show();
    }

    public void regMotivo() {
        MotRegister mr = new MotRegister();
        Stage dialog = DialogCreator.createDialog(mr);
        mr.onExitPressed(() -> {
            dialog.close();
        });
        dialog.show();
    }

    public void showConf() {
        if (cs == null) {
            cs = new ConfShower();
        }
//        cs.onRefresh(true);
        StaticReferences.getMainWindowReference().setCenterNode(cs);
    }

    public void showEntr() {
        if (em == null) {
            em = new EntMain();
        }
        em.onRefresh(true);
        StaticReferences.getMainWindowReference().setCenterNode(em);
    }

    public void showDon() {
        if (ds == null) {
            ds = new DonShower();
        }
//        ds.onRefresh(true);
        StaticReferences.getMainWindowReference().setCenterNode(ds);
    }

    public void showMot() {
        if (ms == null) {
            ms = new MotShower();
        }
//        ms.onRefresh(true);
        StaticReferences.getMainWindowReference().setCenterNode(ms);
    }

    public void showProd() {
        if (ps == null) {
            ps = new ProdShower();
        }
//        ps.onRefresh(true);
        StaticReferences.getMainWindowReference().setCenterNode(ps);
    }

    public void homeProd() {
        if (pm == null) {
            pm = new ProdMain();
        }
        StaticReferences.getMainWindowReference().setCenterNode(pm);
    }

    public void homeMag() {
        if (mm == null) {
            mm = new MagMain();
        }
        mm.onRefresh(true);
        StaticReferences.getMainWindowReference().setCenterNode(mm);
    }

}
