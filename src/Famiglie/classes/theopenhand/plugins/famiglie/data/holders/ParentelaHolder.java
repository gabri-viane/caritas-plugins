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
package theopenhand.plugins.famiglie.data.holders;

import theopenhand.commons.connection.runtime.annotations.Query;
import theopenhand.commons.connection.runtime.impls.ResultHolderImpl;
import theopenhand.plugins.famiglie.data.Parentela;

/**
 *
 * @author gabri
 */
public class ParentelaHolder extends ResultHolderImpl<Parentela> {

    @Query(queryID = 0, bindedClass = Parentela.class, hasBindedParams = false, hasResult = true)
    public static final String SELECT_QUERY = "SELECT * FROM getParentele";

    public ParentelaHolder() {
    }

}
