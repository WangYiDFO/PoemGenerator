package org.memoryextension.nngm.web.customvalidators;

import org.memoryextension.nngm.core.RhymingPattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RhymePatternValidator implements ConstraintValidator<RhymePatternValidation, RhymingPattern> {

    @Override
    public boolean isValid(final RhymingPattern valueToValidate, final ConstraintValidatorContext context) {
        return false;
    }
}
