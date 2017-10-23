package com.pum.tomasz.mobileengineerassignment1.injector.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by tomasz on 23.10.2017.
 */

@Scope
@Retention(RUNTIME) public @interface PerActivity {}