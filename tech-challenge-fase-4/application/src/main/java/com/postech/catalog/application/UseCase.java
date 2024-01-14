package com.postech.catalog.application;

import com.postech.catalog.domain.catagory.Category;

public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(IN input);
}