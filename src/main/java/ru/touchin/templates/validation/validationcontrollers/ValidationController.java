/*
 *  Copyright (c) 2016 Touch Instinct
 *
 *  This file is part of RoboSwag library.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package ru.touchin.templates.validation.validationcontrollers;

import android.support.annotation.NonNull;

import ru.touchin.templates.validation.validators.Validator;

/**
 * Created by Ilia Kurtov on 24/01/2017.
 * TODO: fill
 */
public class ValidationController<TValidator extends Validator> {

    @NonNull
    private final TValidator validator;

    public ValidationController(@NonNull final TValidator validator) {
        this.validator = validator;
    }

    @NonNull
    public TValidator getValidator() {
        return validator;
    }

}