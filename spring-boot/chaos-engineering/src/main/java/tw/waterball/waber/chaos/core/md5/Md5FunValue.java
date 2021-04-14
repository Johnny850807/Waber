package tw.waterball.waber.chaos.core.md5;

import lombok.SneakyThrows;
import tw.waterball.waber.chaos.api.FunValue;

import java.security.MessageDigest;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class Md5FunValue implements FunValue {
    private final String funValue;
    private final byte[] digest;

    @SneakyThrows
    public Md5FunValue(String funValue) {
        this.funValue = funValue;

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(funValue.getBytes());
        digest = md.digest();
    }

    public Md5FunValue(String funValue, byte[] digest) {
        this.funValue = funValue;
        this.digest = digest;
    }

    public byte[] getDigest() {
        return digest;
    }

    public String getFunValue() {
        return funValue;
    }

    @Override
    public String toString() {
        return funValue;
    }
}
