package javaTest.joe;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.Arrays;
import java.util.List;

public class WhereClauseTest {

    private static final String FIND_ALL_APPS_STMT =
        " SELECT apps.*, s.scopeList "
            + "FROM apps "
            + "  INNER JOIN "
            + "    (SELECT app_id, group_concat(scope) as scopeList "
            + "       FROM app_scope __APP_SCOPE_WHERE_CLAUSE GROUP BY app_id) s "
            + "  ON apps.id = s.app_id "
            + "__APPS_WHERE_CLAUSE";

    public static void main(String[] args) {

        StringBuilder appScopeWhereClause = new StringBuilder("");
        StringBuilder appsWhereClause = new StringBuilder("");
        MapSqlParameterSource namedParams = new MapSqlParameterSource();

        List<String> scopeList = Arrays.asList("apex_r", "apex_w");
        List<String> statusList = Arrays.asList("created", "reviewed");
//        List<String> statusList = Arrays.asList();
        List<String> ownerGuidList = Arrays.asList("joe321", "joejoe321321");

        for (int i = 0; i < scopeList.size(); i++) {
            if (appScopeWhereClause.length() > 0) {
                appScopeWhereClause.append(" OR ");
            }
            appScopeWhereClause.append("scope = :scope");
            appScopeWhereClause.append(i);

            namedParams.addValue("scope" + i, scopeList.get(i));
        }
        if (appScopeWhereClause.length() > 0) {
            appScopeWhereClause.insert(0, "WHERE ");
        }

        for (int i = 0; i < statusList.size(); i++) {
            if (appsWhereClause.length() > 0) {
                appsWhereClause.append(" OR ");
            }
            appsWhereClause.append("status = :status");
            appsWhereClause.append(i);

            namedParams.addValue("status" + i, statusList.get(i));
        }

        for (int i = 0; i < ownerGuidList.size(); i++) {
            if (appsWhereClause.length() > 0) {
                appsWhereClause.append(" OR ");
            }
            appsWhereClause.append("rId = :rId");
            appsWhereClause.append(i);

            namedParams.addValue("rId" + i, ownerGuidList.get(i));
        }
        if (appsWhereClause.length() > 0) {
            appsWhereClause.insert(0, "WHERE ");
        }

        String stmt = FIND_ALL_APPS_STMT
            .replace("__APP_SCOPE_WHERE_CLAUSE", appScopeWhereClause)
            .replace("__APPS_WHERE_CLAUSE", appsWhereClause);

//        System.out.println(appScopeWhereClause.toString());
//        System.out.println(appsWhereClause.toString());
        System.out.println(stmt);

        System.out.println(namedParams.getValues());
    }
}
