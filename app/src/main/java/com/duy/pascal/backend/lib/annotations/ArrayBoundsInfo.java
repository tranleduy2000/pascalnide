package com.duy.pascal.backend.lib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
// Make this annotation accessible at runtime via reflection.
@Target({ElementType.METHOD})
public @interface ArrayBoundsInfo {
    public int[] starts() default {};

    public int[] lengths() default {};
}
