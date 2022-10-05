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
import theopenhand.plugins.famiglie.data.InformazioniFamiglia;

/**
 *
 * @author gabri
 */
public class InformazioniFamigliaHolder extends ResultHolderImpl<InformazioniFamiglia> {

    @Query(queryID = 0, bindedClass = InformazioniFamiglia.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY = "SELECT * FROM getinformazionifamiglie";

    /*@Query(queryID = 1, bindedClass = InformazioniFamiglia.class, hasBindedParams = true, isStoredProcedureCall = true, isUpdate = true)
    public static final String ADD_PROCEDURE = "CALL addFamiglia(%N2, %N4, %N5, %N6,%N7, %N8, %N9,%N10, %N1, %N11, %N12);";
     */
    public InformazioniFamigliaHolder() {
    }

}
