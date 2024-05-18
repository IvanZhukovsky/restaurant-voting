package ru.asphaltica.restaurantvoting.common.error;

public class AccessDeniedToChangeVoteException extends AppException{
    public AccessDeniedToChangeVoteException(String msg) {
        super(msg, ErrorType.FORBIDDEN);
    }
}
