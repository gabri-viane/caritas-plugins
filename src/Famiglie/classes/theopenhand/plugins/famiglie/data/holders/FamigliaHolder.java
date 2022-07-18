/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.plugins.famiglie.data.holders;

import theopenhand.commons.connection.runtime.annotations.Query;
import theopenhand.commons.connection.runtime.impls.ResultHolderImpl;
import theopenhand.plugins.famiglie.data.Famiglia;

/**
 *
 * @author gabri
 */
public class FamigliaHolder extends ResultHolderImpl<Famiglia> {

    @Query(queryID = 0, bindedClass = Famiglia.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY = "SELECT * FROM getFamiglieF";

    @Query(queryID = 1, bindedClass = Famiglia.class, hasBindedParams = true, isStoredProcedureCall = true, isUpdate = true)
    public static final String ADD_PROCEDURE = "CALL addFamiglia(%N2, %N4, %N5, %N6, %N7, %N8, %N9, %N10, %N13, %N1, %N11, %N12);";

    @Query(queryID = 3, bindedClass = Famiglia.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY_COMPLETE = "SELECT * FROM getFamiglieCompleteF WHERE %N2 = %V2";

    @Query(queryID = 4, bindedClass = Famiglia.class, hasBindedParams = true, isStoredProcedureCall = true, isUpdate = true)
    public static final String EDIT_PROCEDURE = "CALL editFamiglia(%N2, %N4, %N5, %N6, %N7, %N13);";

    @Query(queryID = 5, bindedClass = Famiglia.class, hasBindedParams = true, isUpdate = true, hasResult = false)
    public static final String DELETE_QUERY = "DELETE FROM Famiglie WHERE %N2 = %V2;";

    @Query(queryID = 7, bindedClass = Famiglia.class, hasBindedParams = true, hasResult = false)
    public static final String SELECT_QUERY_COMPLETE_ALL = "SELECT * FROM getFamiglieCompleteF";

    @Query(queryID = 8, bindedClass = Famiglia.class, hasBindedParams = true, hasResult = false)
    public static final String SELECT_QUERY_COMPLETE_ALL_ORDERED = "SELECT * FROM getFamiglieCompleteF ORDER BY %N1";

    @Query(queryID = 9, bindedClass = Famiglia.class, hasBindedParams = true, hasResult = true)
    public static final String SELECT_QUERY_ORDERED = "SELECT * FROM getFamiglieF ORDER BY %N1";

    public FamigliaHolder() {
    }

}
