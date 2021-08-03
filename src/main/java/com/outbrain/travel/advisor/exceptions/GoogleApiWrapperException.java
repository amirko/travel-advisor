package com.outbrain.travel.advisor.exceptions;

import com.google.maps.errors.ApiException;

public class GoogleApiWrapperException extends RuntimeException {

    public GoogleApiWrapperException(ApiException e) {
        super(e);
    }
}
