package in.appnow.astrobuddy.utils;

import java.util.Random;

/**
 * Created by sonu on 16:24, 17/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class RandomUtils {
    public RandomUtils() {
    }

    public int getRandomValue(int min, int max) {
        Random r = new Random();
        int random = r.nextInt(max - min) + min;//return random between (min...max-1) i.e. if max =10 and min =6 then it will give between 6,7,8,9.
        return random;
    }
}
