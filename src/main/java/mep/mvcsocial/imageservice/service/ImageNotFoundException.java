package mep.mvcsocial.imageservice.service;

class ImageNotFoundException extends RuntimeException {

    ImageNotFoundException(String message) {
        super(message);
    }
}
