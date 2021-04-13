package tw.waterball.waber.chaos.core;

import lombok.SneakyThrows;
import tw.waterball.waber.chaos.api.FunValue;

import java.security.MessageDigest;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class WaberFunValue implements FunValue {
    private final String funValue;
    private final byte[] digest;

    @SneakyThrows
    public WaberFunValue(String funValue) {
        this.funValue = funValue;

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(funValue.getBytes());
        digest = md.digest();
    }

    public WaberFunValue(String funValue, byte[] digest) {
        this.funValue = funValue;
        this.digest = digest;
    }

    public byte[] getDigest() {
        return digest;
    }

    public String getFunValue() {
        return funValue;
    }
}
