package ro.ubbcluj.map.domain.validators;

import ro.ubbcluj.map.domain.Prietenie;

import java.time.LocalDate;

public class PrietenieValidator implements Validator<Prietenie> {
    public PrietenieValidator() {
    }

    public void validate(Prietenie entity) throws ValidationException {
        String message = "";
        if (entity.getData().getYear() < 1950 || entity.getData().getYear() > LocalDate.now().getYear()) {
            message = message + "Anul trebuie sa fie intre 1950 si anul curent";
        }

        if (message.length() > 0) {
            throw new ValidationException(message);
        }
    }
}
