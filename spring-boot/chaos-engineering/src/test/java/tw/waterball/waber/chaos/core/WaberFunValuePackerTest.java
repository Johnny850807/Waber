package tw.waterball.waber.chaos.core;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class WaberFunValuePackerTest {
    WaberFunValuePacker packer = new WaberFunValuePacker();

    @Test
    void test() {
        WaberFunValue expected = new WaberFunValue("Hello World");
        byte[] packet = packer.write(expected);
        WaberFunValue actual = packer.read(packet);

        assertEquals(expected.getFunValue(), actual.getFunValue());
        assertArrayEquals(expected.getDigest(), actual.getDigest());
    }

}