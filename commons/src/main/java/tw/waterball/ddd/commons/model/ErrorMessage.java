package tw.waterball.ddd.commons.model;

import lombok.*;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Getter @Setter
@NoArgsConstructor
public class ErrorMessage {
    private Class<? extends RuntimeException> errorType;
    private String errorTypeName;
    private String message;

    public ErrorMessage(Class<? extends RuntimeException> errorType, String message) {
        setErrorType(errorType);
        this.message = message;
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public void setErrorTypeName(String errorTypeName) {
        this.errorTypeName = errorTypeName;
        this.errorType = (Class<? extends RuntimeException>) Class.forName(errorTypeName);
    }

    public void setErrorType(Class<? extends RuntimeException> errorType) {
        this.errorType = errorType;
        this.errorTypeName = errorType.getName();
    }

    public boolean isOriginatedFromExceptionType(Class<? extends RuntimeException> exceptionType) {
        return errorType.isAssignableFrom(exceptionType);
    }

    public static ErrorMessage fromException(RuntimeException err) {
        return new ErrorMessage(err.getClass(), err.getMessage());
    }

    public void throwException() throws RuntimeException {
        try {
            throw errorType.getDeclaredConstructor(String.class).newInstance(message);
        } catch (NoSuchMethodException| IllegalAccessException| InvocationTargetException| InstantiationException err) {
            try {
                throw errorType.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(err);
            }
        }
    }
}
