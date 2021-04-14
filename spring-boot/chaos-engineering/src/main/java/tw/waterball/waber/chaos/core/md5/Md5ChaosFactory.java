package tw.waterball.waber.chaos.core.md5;

import tw.waterball.waber.chaos.api.ChaosFactory;
import tw.waterball.waber.chaos.api.FunValue;
import tw.waterball.waber.chaos.api.FunValuePacker;

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
