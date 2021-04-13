package tw.waterball.waber.chaos.core;

import static java.util.Arrays.copyOfRange;

import tw.waterball.waber.chaos.api.FunValue;
import tw.waterball.waber.chaos.api.FunValuePacker;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class WaberFunValuePacker implements FunValuePacker {
    @Override
    public byte[] write(FunValue funValue) {
        WaberFunValue v = ((WaberFunValue) funValue);
        byte[] funValueBytes = v.getFunValue().getBytes(StandardCharsets.UTF_8);
        byte[] digest = v.getDigest();
        ByteBuffer buffer = ByteBuffer.allocate(4 + funValueBytes.length + digest.length);
        return buffer.putInt(funValueBytes.length)
                .put(funValueBytes)
                .put(digest).array();
    }

    @Override
    public WaberFunValue read(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int funValueContentLength = buffer.getInt();
        byte[] funValueBytes = new byte[funValueContentLength];
        buffer.get(funValueBytes);
        byte[] digest = copyOfRange(buffer.array(), buffer.position(), buffer.capacity());
        return new WaberFunValue(new String(funValueBytes), digest);
    }
}
