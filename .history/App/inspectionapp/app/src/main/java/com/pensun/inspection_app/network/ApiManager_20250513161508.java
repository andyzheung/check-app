package com.pensun.inspection_app.network;

public class ApiManager {
    private static ApiService apiService;

    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = ApiClient.getClient().create(ApiService.class);
        }
        return apiService;
    }

    public static void resetApiService() {
        apiService = null;
        ApiClient.resetClient();
    }
} 