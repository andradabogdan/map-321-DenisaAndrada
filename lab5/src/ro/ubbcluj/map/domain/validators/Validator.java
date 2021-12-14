package ro.ubbcluj.map.domain.validators;

public interface Validator<T> {
    void validate(T var1) throws ValidationException;
}
