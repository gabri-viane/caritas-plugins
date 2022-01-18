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
import theopenhand.plugins.famiglie.data.Componente;

/**
 *
 * @author gabri
 */
public class ComponenteHolder extends ResultHolderImpl<Componente> {

    @Query(queryID = 0, bindedClass = Componente.class, hasBindedParams = true, hasResult = true)
    public static final String QUERY_SELECT = "SELECT * FROM getComponentiF WHERE %N2 = %V2";

    @Query(queryID = 1, bindedClass = Componente.class, hasBindedParams = true, isStoredProcedureCall = true, isUpdate = true)
    public static final String ADD_PROCEDURE = "CALL addComponente(%N2, %N3, %N4, %N5, %N6 , %N1);";

    @Query(queryID = 2, bindedClass = Componente.class, hasBindedParams = true, isStoredProcedureCall = true, isUpdate = true)
    public static final String UPDATE_PROCEDURE = "CALL editComponente(%N1, %N3, %N4, %N5, %N6);";

    @Query(queryID = 5, bindedClass = Componente.class, hasBindedParams = true, isUpdate = true, hasResult = false)
    public static final String DELETE_QUERY = "DELETE FROM Componenti WHERE %N1 = %V1;";

    public ComponenteHolder() {
    }

}
