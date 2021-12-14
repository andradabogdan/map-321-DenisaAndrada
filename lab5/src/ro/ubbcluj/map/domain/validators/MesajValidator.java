package ro.ubbcluj.map.domain.validators;


import ro.ubbcluj.map.domain.Message;

import java.time.LocalDate;

public class MesajValidator implements Validator<Message> {
    public MesajValidator() {
    }

    public void validate(Message entity) throws ValidationException {
        String message = "";
        if (entity.getMessage().length() == 0) {
            message = message + "Mesajul nu trebuie sa fie gol";
        }

        if (entity.getData().getYear() < 1950 || entity.getData().getYear() > LocalDate.now().getYear()) {
            message = message + "Anul trebuie sa fie intre 1950 si anul curent";
        }

        if (message.length() > 0) {
            throw new ValidationException(message);
        }
    }
}
