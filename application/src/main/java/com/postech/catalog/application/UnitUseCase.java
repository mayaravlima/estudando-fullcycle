package com.postech.catalog.application;

public abstract class UnitUseCase<IN> {

    public abstract void execute(IN input);
}
