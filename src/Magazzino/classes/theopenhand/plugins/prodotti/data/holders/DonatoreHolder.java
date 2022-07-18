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
import theopenhand.plugins.prodotti.data.Donatore;

/**
 *
 * @author gabri
 */
public class DonatoreHolder extends ResultHolderImpl<Donatore> {
    
    @Query(queryID = 0, bindedClass = Donatore.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY = "SELECT * FROM getDonatori";
    
    @Query(queryID = 1, bindedClass = Donatore.class, hasBindedParams = true, isStoredProcedureCall = true, isUpdate = true)
    public static final String ADD_PROCEDURE = "CALL addDonatore(%N1, %N2, %N0);";
    
    @Query(queryID = 2, bindedClass = Donatore.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY_COMPLETE = "SELECT * FROM getDonatori WHERE %N0 = %V0";
    
    @Query(queryID = 3, bindedClass = Donatore.class, hasBindedParams = true, isStoredProcedureCall = true, isUpdate = true)
    public static final String EDIT_PROCEDURE = "CALL editDonatore(%N0, %N1, %N2);";
    
    @Query(queryID = 4, bindedClass = Donatore.class, hasBindedParams = true, isUpdate = true, hasResult = false)
    public static final String DELETE_QUERY = "DELETE FROM Donatori WHERE %N0 = %V0;";

    @Query(queryID = 5, bindedClass = Donatore.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY_ORDERED = "SELECT * FROM getDonatori ORDER BY %N1";
    
    public DonatoreHolder() {
    }
    
}
