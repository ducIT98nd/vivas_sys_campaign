package com.vivas.campaignxsync.utils;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
public class AppUtils {

    public Throwable getrootcause(Exception e) {
        Optional<Throwable> rootCause =
                Stream.iterate(e, Throwable::getCause).filter(element -> element.getCause() == null).findFirst();
        return rootCause.orElse(null);
    }
}
