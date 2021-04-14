package tw.waterball.waber.chaos.core.md5;

import static java.util.Arrays.copyOfRange;

import tw.waterball.waber.chaos.api.FunValue;
import tw.waterball.waber.chaos.api.FunValuePacker;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class Md5FunValuePacker implements FunValuePacker {
    @Override
    public byte[] write(FunValue funValue) {
        Md5FunValue v = ((Md5FunValue) funValue);
        byte[] funValueBytes = v.getFunValue().getBytes(StandardCharsets.UTF_8);
        byte[] digest = v.getDigest();
        ByteBuffer buffer = ByteBuffer.allocate(4 + funValueBytes.length + digest.length);
        return buffer.putInt(funValueBytes.length)
                .put(funValueBytes)
                .put(digest).array();
    }

    @Override
    public Md5FunValue read(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int funValueContentLength = buffer.getInt();
        byte[] funValueBytes = new byte[funValueContentLength];
        buffer.get(funValueBytes);
        byte[] digest = copyOfRange(buffer.array(), buffer.position(), buffer.capacity());
        return new Md5FunValue(new String(funValueBytes), digest);
    }
}
