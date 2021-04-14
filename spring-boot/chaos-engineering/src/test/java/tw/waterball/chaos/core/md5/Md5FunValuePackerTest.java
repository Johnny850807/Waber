package tw.waterball.chaos.core.md5;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Md5FunValuePackerTest {
    Md5FunValuePacker packer = new Md5FunValuePacker();

    @Test
    void test() {
        Md5FunValue expected = new Md5FunValue("Hello World");
        byte[] packet = packer.write(expected);
        Md5FunValue actual = packer.read(packet);

        assertEquals(expected.getFunValue(), actual.getFunValue());
        assertArrayEquals(expected.getDigest(), actual.getDigest());
    }

}