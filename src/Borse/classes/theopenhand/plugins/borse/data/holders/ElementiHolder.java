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
import theopenhand.plugins.borse.data.ElementoBorsa;

/**
 *
 * @author gabri
 */
public class ElementiHolder extends ResultHolderImpl<ElementoBorsa> {

    @Query(queryID = 0, bindedClass = ElementoBorsa.class, hasBindedParams = false, hasResult = true)
    public static final String SELECT_QUERY = "SELECT * FROM getelementiborsef";

    @Query(queryID = 1, bindedClass = ElementoBorsa.class, hasBindedParams = true, isStoredProcedureCall = true, isUpdate = true)
    public static final String ADD_EDIT_PROCEDURE = "CALL setElementoBorsa(%N1, %N2, %N3, %N4, %N7, %N0);";

    @Query(queryID = 3, bindedClass = ElementoBorsa.class, hasBindedParams = false, hasResult = true)
    public static final String SELECT_QUERY_COMPLETE = "SELECT * FROM getelementiborsecompletef WHERE %N0 = %V0";

    @Query(queryID = 5, bindedClass = ElementoBorsa.class, hasBindedParams = false, isUpdate = true, hasResult = false, isStoredProcedureCall = true)
    public static final String DELETE_QUERY = "CALL removeElementoBorsa(%N0,%N2,%N8);";

    @Query(queryID = 6, bindedClass = ElementoBorsa.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY_COMPLETE_BORSA = "SELECT * FROM getelementiborsecompletef WHERE %N1 = %V1";

    @Query(queryID = 7, bindedClass = ElementoBorsa.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY_COMPLETE_ALL = "SELECT * FROM getelementiborsecompletef";

    public ElementiHolder() {
    }

}
