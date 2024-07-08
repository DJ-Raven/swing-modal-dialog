package test.alerts.animator;

public abstract class Evaluator<T> {

    public abstract T evaluate(T from, T target, float fraction);
}
