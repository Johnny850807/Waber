package tw.waterball.chaos.core.md5;

import tw.waterball.chaos.api.FunValuePacker;
import tw.waterball.chaos.api.ChaosFactory;
import tw.waterball.chaos.api.FunValue;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class Md5ChaosFactory implements ChaosFactory {
    @Override
    public FunValue createFunValue(String funValue) {
        return new Md5FunValue(funValue);
    }

    @Override
    public FunValuePacker createFunValuePacker() {
        return new Md5FunValuePacker();
    }
}
