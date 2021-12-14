package ro.ubbcluj.map.domain.validators;
import ro.ubbcluj.map.domain.FriendRequest;

import java.time.LocalDate;
import java.util.Objects;

public class CerereValidator implements Validator<FriendRequest> {

    public CerereValidator() {

    }
    @Override
    public void validate(FriendRequest entity) throws ValidationException {
        String message = "";
        if (!Objects.equals(entity.getStatus(), "approved") && !Objects.equals(entity.getStatus(), "pending")
                && !Objects.equals(entity.getStatus(), "rejected")) {
            message += "Statusul trebuie sa fie pending, rejected sau approved!";
        }
        if (message.length() > 0) {
            throw new ValidationException(message);
        }
    }
}

