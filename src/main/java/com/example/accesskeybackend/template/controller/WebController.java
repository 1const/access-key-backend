package com.example.accesskeybackend.template.controller;

import com.example.accesskeybackend.exception.IllegalArgumentException;
import com.example.accesskeybackend.template.util.ErrorMessage;
import com.example.accesskeybackend.template.util.Ipv6SupportResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Objects;


@RestController
@RequestMapping("/api/web")
public class WebController {
    private static final String CHECK_IPV6_SUPPORT_URL = "/checkIpv6Support";

    @GetMapping(CHECK_IPV6_SUPPORT_URL)
    public ResponseEntity<Ipv6SupportResponse> checkSupport(
            @RequestParam String siteUrl
    ) {
        Ipv6SupportResponse response = new Ipv6SupportResponse(false);
        String host = URI.create(siteUrl).getHost();
        if (Objects.isNull(host)) {
            throw new IllegalArgumentException("Invalid url form");
        }

        InetAddress[] inetAddress;
        try {
            inetAddress = InetAddress.getAllByName(host);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        for (InetAddress address : inetAddress) {
            if (address instanceof Inet6Address) {
                response.setSuccess(true);
                break;
            }
        }

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleException(IllegalArgumentException exception) {

        return new ResponseEntity<>(
                new ErrorMessage(exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}
