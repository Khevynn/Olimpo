package com.olimpo.Routes;

public class APIRoutes {
    
    //Auth routes
    public static final String REFRESH_TOKEN_ROUTE = "/auth/refresh";
    public static final String USER_LOGIN_ROUTE = "/auth/login";
    public static final String USER_REGISTER_ROUTE = "/auth/register";
    public static final String USER_LOGOUT_ROUTE = "/auth/logout";

    //Profile routes
    public static final String USER_GET_OWN_PROFILE_ROUTE = "/profile/";
    public static final String USER_GET_PROFILE_ROUTE = "/profile/{user}/{tag}";

    public static final String USER_UPDATE_PROFILE_ROUTE = "/profile/";
    public static final String USER_DELETE_ROUTE = "/profile/";

    //Friendship routes
    public static final String FRIENDS_SEND = "/friends/send";
    public static final String FRIENDS_RESPOND = "/friends/{requestId}/respond";
    public static final String FRIENDS_GET_ALL = "/friends/";
    public static final String FRIENDS_REMOVE = "/friends/remove";

    //Tournaments routes
    // public static final String TOURNAMENT_CREATE_ROUTE = "/tournaments/create";
    // public static final String TOURNAMENT_GET_ROUTE = "/tournaments/";
}
