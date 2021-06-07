//package tw.waterball.ddd.waber.springboot.match.chaos;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//import tw.waterball.chaos.annotations.ChaosEngineering;
//import tw.waterball.chaos.api.ChaosTriggeredException;
//import tw.waterball.chaos.core.md5.Md5Chaos;
//import tw.waterball.ddd.model.match.Match;
//import tw.waterball.ddd.model.match.MatchPreferences;
//import tw.waterball.ddd.model.user.Driver;
//
///**
// * @author Waterball (johnny850807@gmail.com)
// */
//@ChaosEngineering
//@Aspect
//@Component
//public class MisMatchToBusinessCar extends Md5Chaos {
//    @Override
//    public String getName() {
//        return "Match.MisMatchToBusinessCar";
//    }
//
//    @Override
//    protected Criteria criteria() {
//        return or(positiveNumberAtPositions(1, 2, 3), positiveNumberAtPositions(12, 13, 14),
//                negativeNumberAtPositions(1, 5, 12, 13), areNotZeros(5, 10, 15));
//    }
//
//    @Around("execution(* tw.waterball.ddd.model.match.Match.start(..))")
//    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
//        if (isAlive()) {
//            MatchPreferences preferences = (MatchPreferences) joinPoint.getArgs()[1];
//            preferences.setCarType(Driver.CarType.Business);
//        }
//        return joinPoint.proceed();
//    }
//}
