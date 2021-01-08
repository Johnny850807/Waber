package tw.waterball.ddd.model.associations;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class AssociationViolationException extends IllegalStateException {

    public AssociationViolationException() {
    }

    public AssociationViolationException(String s) {
        super(s);
    }

    public AssociationViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssociationViolationException(Throwable cause) {
        super(cause);
    }
}
