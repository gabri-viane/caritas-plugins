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
package theopenhand.plugins.prodotti.data.holders;

import theopenhand.commons.connection.runtime.annotations.Query;
import theopenhand.commons.connection.runtime.impls.ResultHolderImpl;
import theopenhand.plugins.prodotti.data.Entrata;

/**
 *
 * @author gabri
 */
public class EntrataHolder extends ResultHolderImpl<Entrata> {

    @Query(queryID = 0, bindedClass = Entrata.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY_FORMATTED = "SELECT * FROM getEntrateF";

    @Query(queryID = 1, bindedClass = Entrata.class, hasBindedParams = true, isStoredProcedureCall = true, isUpdate = true)
    public static final String ADD_PROCEDURE = "CALL addEntrata(%N5, %N6, %N3, %N4, %N0);";

    @Query(queryID = 2, bindedClass = Entrata.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY = "SELECT * FROM getEntrate";

    @Query(queryID = 3, bindedClass = Entrata.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY_COMPLETE = "SELECT * FROM getEntrateCompleteF WHERE %N0 = %V0";

    /**
     * Se nel caso in futuro quando proverò a modificare un'entrata e non va
     * togliere dall'annotazione "outParams" e mettere INOUT la variabile _ID
     * nel database nella stored procedure.
     */
    @Query(queryID = 4, bindedClass = Entrata.class, hasBindedParams = true, isStoredProcedureCall = true, isUpdate = true, outPrams = {}, hasResult = false)
    public static final String EDIT_PROCEDURE = "CALL editEntrata(%N0, %N1, %N2, %N3, %N4, %N5, %N6, %N7);";

    @Query(queryID = 5, bindedClass = Entrata.class, hasBindedParams = true, isUpdate = true, hasResult = false)
    public static final String DELETE_QUERY = "DELETE FROM Entrate WHERE %N0 = %V0;";

    @Query(queryID = 7, bindedClass = Entrata.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY_COMPLETE_ALL = "SELECT * FROM getEntrateCompleteF";

    public EntrataHolder() {
    }

}
