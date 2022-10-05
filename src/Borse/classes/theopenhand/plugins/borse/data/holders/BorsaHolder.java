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
package theopenhand.plugins.borse.data.holders;

import theopenhand.commons.connection.runtime.annotations.Query;
import theopenhand.commons.connection.runtime.impls.ResultHolderImpl;
import theopenhand.plugins.borse.data.Borsa;

/**
 *
 * @author gabri
 */
public class BorsaHolder extends ResultHolderImpl<Borsa> {

    @Query(queryID = 0, bindedClass = Borsa.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY = "SELECT * FROM getborsef";

    @Query(queryID = 1, bindedClass = Borsa.class, hasBindedParams = true, isStoredProcedureCall = true, isUpdate = true)
    public static final String ADD_PROCEDURE = "CALL addBorsa(%N1, %N2, %N3, %N0);";

    @Query(queryID = 3, bindedClass = Borsa.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY_COMPLETE = "SELECT * FROM getborsecompletef WHERE %N0 = %V0";

    @Query(queryID = 4, bindedClass = Borsa.class, hasBindedParams = false, isStoredProcedureCall = true, isUpdate = true)
    public static final String EDIT_PROCEDURE = "CALL editBorsa(%N0, %N1, %N2, %N3, %N4);";

    @Query(queryID = 5, bindedClass = Borsa.class, hasBindedParams = false, isStoredProcedureCall = true, isUpdate = true)
    public static final String DELETE_PROCEDURE = "CALL removeBorsa(%N0);";

    @Query(queryID = 6, bindedClass = Borsa.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY_COMPLETE_FAMIGLIA = "SELECT * FROM getborsecompletef WHERE %N1 = %V1";

    @Query(queryID = 7, bindedClass = Borsa.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY_COMPLETE_ALL = "SELECT * FROM getborsecompleteF";

    public BorsaHolder() {
    }

}
