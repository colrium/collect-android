package com.colrium.formbuilder.base;

import java.util.List;

public interface FormValidationListener {
    void onValidationPassed();

    void onValidationFailed(List<FormError> errors);
}
