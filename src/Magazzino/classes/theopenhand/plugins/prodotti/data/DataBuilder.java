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
package theopenhand.plugins.prodotti.data;

import java.math.BigInteger;

/**
 *
 * @author gabri
 */
public class DataBuilder {

    private DataBuilder() {

    }

    public static Confezione generateConfezioneByID(long id) {
        Confezione c = new Confezione();
        c.id = BigInteger.valueOf(id);
        return c;
    }

    public static Confezione generateConfezioneByID(BigInteger id) {
        if (id != null) {
            Confezione c = new Confezione();
            c.id = id;
            return c;
        }
        return null;
    }

    public static Donatore generateDonatoreByID(long id) {
        Donatore d = new Donatore();
        d.id = BigInteger.valueOf(id);
        return d;
    }

    public static Donatore generateDonatoreByID(BigInteger id) {
        if (id != null) {
            Donatore d = new Donatore();
            d.id = id;
            return d;
        }
        return null;
    }

    public static Prodotto generateProdottoByID(long id) {
        Prodotto p = new Prodotto();
        p.id = BigInteger.valueOf(id);
        return p;
    }

    public static Prodotto generateProdottoByID(BigInteger id) {
        if (id != null) {
            Prodotto p = new Prodotto();
            p.id = id;
            return p;
        }
        return null;
    }

}
