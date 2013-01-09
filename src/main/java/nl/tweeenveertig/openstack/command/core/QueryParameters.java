package nl.tweeenveertig.openstack.command.core;

public class QueryParameters {

    private QueryParameter[] queryParameters;

    public QueryParameters(QueryParameter[] queryParameters) {
        this.queryParameters = queryParameters;
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
}
