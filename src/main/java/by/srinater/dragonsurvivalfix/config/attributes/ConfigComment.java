package by.srinater.dragonsurvivalfix.config.attributes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigComment {
    String value() default "";
}
