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
package theopenhand.plugins.famiglie.data;

import java.math.BigInteger;

/**
 *
 * @author gabri
 */
public class DataBuilder {

    private DataBuilder() {

    }

    public static Parentela generateParentelaByID(long id) {
        Parentela p = new Parentela();
        p.id = BigInteger.valueOf(id);
        return p;
    }

    public static Parentela generateParentelaByID(BigInteger id) {
        if (id != null) {
            Parentela p = new Parentela();
            p.id = id;
            return p;
        }
        return null;
    }

    public static Famiglia generateFamigliaByID(long id) {
        Famiglia f = new Famiglia();
        f.id = BigInteger.valueOf(id);
        return f;
    }

    public static Famiglia generateFamigliaByID(BigInteger id) {
        if (id != null) {
            Famiglia f = new Famiglia();
            f.id = id;
            return f;
        }
        return null;
    }
}
