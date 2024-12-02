package services;

public class Poisson{
    public static long generate(double tasa) {
        return (long) (-Math.log(1 - Math.random()) / tasa * 1000);
    }
}
