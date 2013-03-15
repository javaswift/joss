package org.javaswift.joss.instructions;

import java.util.ArrayList;
import java.util.List;

public class QueryParameters {

    protected List<QueryParameter> queryParameters = new ArrayList<QueryParameter>();

    public QueryParameters(QueryParameter[] queryParameters) {
        for (QueryParameter queryParameter : queryParameters) {
            this.queryParameters.add(queryParameter);
        }
    }

    public String getQuery() {
        StringBuilder completeQuery = new StringBuilder("?");
        int position = 0;
        for (QueryParameter queryParameter : queryParameters) {
            String query = queryParameter.getQuery();
            if (query != null) {
                if (position > 0) {
                    completeQuery.append("&");
                }
                completeQuery.append(query);
                position++;
            }
        }
        return completeQuery.toString();
    }

    public String createUrl(String urlPath) {
        return urlPath + getQuery();
    }
}
