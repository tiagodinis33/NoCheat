package me.sxstore.nocheat.checks;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface CheckInfo {
    String name();
    
    String type();
}

