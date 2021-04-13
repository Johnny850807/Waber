package tw.waterball.waber.chaos.api;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface FunValuePacker {
    byte[] write(FunValue funValue);
    FunValue read(byte[] bytes);
}
