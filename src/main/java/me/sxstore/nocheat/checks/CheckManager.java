package me.sxstore.nocheat.checks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.sxstore.nocheat.checks.impl.movement.Fly;
import me.sxstore.nocheat.checks.impl.movement.Speed;

public class CheckManager {

    private static final Class[] checks = new Class[]{
            Fly.class,
            Speed.class
    };

    public static List<Check> loadChecks() {
        List<Check> checklist = new ArrayList<>();
        Arrays.asList(checks).forEach(check -> {
            try {
                checklist.add((Check) check.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return checklist;
    }
}
