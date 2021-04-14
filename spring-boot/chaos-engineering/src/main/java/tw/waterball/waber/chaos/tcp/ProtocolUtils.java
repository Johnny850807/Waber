package tw.waterball.waber.chaos.tcp;

import static java.util.Arrays.copyOfRange;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class ProtocolUtils {

    public static ByteBuffer writeStringByContentLength(ByteBuffer buffer, String str) {
        byte[] raw = str.getBytes(StandardCharsets.UTF_8);
        return buffer.putInt(raw.length)
                .put(raw);
    }

    public static String readStringByContentLength(ByteBuffer buffer) {
        int contentLength = buffer.getInt();
        byte[] raw = copyOfRange(buffer.array(), buffer.position(), buffer.position() + contentLength);
        return new String(raw, StandardCharsets.UTF_8);
    }
}
