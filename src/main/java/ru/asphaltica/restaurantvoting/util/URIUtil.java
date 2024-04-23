package ru.asphaltica.restaurantvoting.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class URIUtil {
    private URIUtil() {
    }
    public static URI getCreatedUri(String path, int id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path)
                .buildAndExpand(id).toUri();
    }
    public static URI getCreatedUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path)
                .build().toUri();
    }
}
