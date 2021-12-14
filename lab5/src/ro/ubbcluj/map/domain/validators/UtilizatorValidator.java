package ro.ubbcluj.map.domain.validators;

import ro.ubbcluj.map.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    public UtilizatorValidator() {
    }

    public void validate(Utilizator entity) throws ValidationException {
        String message = "";

        if (entity.getFirstName().length() == 0) {
            message = message + "Prenumele trebuie sa existe!";
        }

        if (entity.getLastName().length() == 0) {
            message = message + "Numele trebuie sa existe!";
        }

        if (message.length() > 0) {
            throw new ValidationException(message);
        }
    }
}

