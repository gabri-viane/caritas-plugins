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
package theopenhand.plugins.borse.data.savable;

import java.math.BigInteger;
import theopenhand.commons.events.programm.FutureCallable;
import theopenhand.runtime.data.components.ListDataElement;
import theopenhand.runtime.data.components.MapDataElement;
import theopenhand.runtime.data.components.PairDataElement;
import theopenhand.window.graphics.commons.BasePickerDialogCNTRL;
import theopenhand.window.graphics.creators.DialogCreator;

/**
 *
 * @author gabri
 */
public class SavedBorseHolder {

    private static final SavedBorseHolder instance = new SavedBorseHolder();
    private MapDataElement<String, ListDataElement<PairDataElement<BigInteger, Integer>>> scb;

    private SavedBorseHolder() {

    }

    public void addBorsa(ListDataElement<PairDataElement<BigInteger, Integer>> borsa) {

    }

    public static ListDataElement<PairDataElement<BigInteger, Integer>> createBorsa(String name) {
        ListDataElement<PairDataElement<BigInteger, Integer>> borsa = new ListDataElement<>(name);
        instance.scb.addElement(name, borsa);
        return borsa;
    }

    public static void addElementToBorsa(ListDataElement<PairDataElement<BigInteger, Integer>> borsa, BigInteger id_elem, Integer amount, String name) {
        borsa.addElement(new PairDataElement<>(name, id_elem, amount));
    }

    public static SavedBorseHolder getInstance() {
        return instance;
    }

    public void setContainerFile(MapDataElement<String, ListDataElement<PairDataElement<BigInteger, Integer>>> scb) {
        this.scb = scb;
    }

    public MapDataElement<String, ListDataElement<PairDataElement<BigInteger, Integer>>> getContainerFile() {
        return scb;
    }

    public void selectFromAvailable(FutureCallable<ListDataElement<PairDataElement<BigInteger, Integer>>> sb) {
        if (scb != null) {
            BasePickerDialogCNTRL<ListDataElement<PairDataElement<BigInteger, Integer>>, MapDataElement<String, ListDataElement<PairDataElement<BigInteger, Integer>>>> picker = new BasePickerDialogCNTRL<>("Seleziona elemento", scb);
            picker.setActionButton("Rimuovi", (args) -> {
                var arg = args[0];
                if (arg != null) {
                    var b = (ListDataElement<PairDataElement<BigInteger, Integer>>) arg;
                    scb.removeElement(b.getName());
                    picker.onRefresh(true);
                }
                return null;
            });
            DialogCreator.showDialog(picker, () -> {
                sb.execute(picker.getValue());
            }, null);
        }
    }

}
