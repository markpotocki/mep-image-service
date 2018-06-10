package mep.mvcsocial.imageservice.service;


import org.springframework.web.bind.annotation.ResponseStatus;


class FileTooLargeException extends RuntimeException {

    FileTooLargeException(String message) {
        super(message);
    }
}
