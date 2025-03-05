package backend.academy;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

@SuppressWarnings("UncommentedMain")
@State(Scope.Thread)
public class ReflectionBenchmark {
    private static final int WARM_UP_TIME = 30;
    private static final int MEASUREMENT_TIME = 90;
    private static final String TESTING_METHOD = "name";

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(ReflectionBenchmark.class.getSimpleName())
            .shouldFailOnError(true)
            .shouldDoGC(true)
            .mode(Mode.AverageTime)
            .timeUnit(TimeUnit.NANOSECONDS)
            .forks(1)
            .warmupForks(1)
            .warmupIterations(1)
            .warmupTime(TimeValue.seconds(WARM_UP_TIME))
            .measurementIterations(1)
            .measurementTime(TimeValue.seconds(MEASUREMENT_TIME))
            .build();

        new Runner(options).run();
    }

    private Student student;
    private Method reflectionMethod;
    private MethodHandle methodHandle;
    private CallSite callSite; // для LambdaMetafactory
    private Function<Student, String> func;

    @Setup
    public void setup() throws Throwable {
        student = new Student("Alexander", "Biryukov");

        // Method
        reflectionMethod = Student.class.getMethod(TESTING_METHOD);

        // MethodHandles
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType type = MethodType.methodType(String.class);
        methodHandle = lookup.findVirtual(Student.class, TESTING_METHOD, type);

        // LambdaMetafactory
        callSite = LambdaMetafactory.metafactory(
            lookup,
            "apply",
            MethodType.methodType(Function.class),
            MethodType.methodType(Object.class, Object.class),
            methodHandle,
            MethodType.methodType(String.class, Student.class)
        );

        func = (Function<Student, String>) callSite.getTarget().invoke();
    }

    // Тестирование производительности при прямом вызове
    @Benchmark
    public void directAccess(Blackhole bh) {
        String name = student.name();
        bh.consume(name);
    }

    // Тестирование производительности java.lang.reflect.Method
    @Benchmark
    public void reflectionMethod(Blackhole bh) throws InvocationTargetException, IllegalAccessException {
        Object o = reflectionMethod.invoke(student);
        bh.consume(o);
    }

    // Тестирование производительности java.lang.invoke.MethodHandles
    @Benchmark
    public void methodHandles(Blackhole bh) throws Throwable {
        Object o = methodHandle.invoke(student);
        bh.consume(o);
    }

    // Тестирование производительности java.lang.invoke.LambdaMetafactory
    // Лямбда создается заново при каждом вызове
    @Benchmark
    public void funcLambdaMetafactory(Blackhole bh) throws Throwable {
        Object o = ((Function<Student, String>) callSite.getTarget().invoke()).apply(student);
        bh.consume(o);
    }

    // Тестирование производительности java.lang.invoke.LambdaMetafactory
    // Используется заранее созданная лямбда
    @Benchmark
    public void lambdaMetafactory(Blackhole bh) throws Throwable {
        Object o = func.apply(student);
        bh.consume(o);
    }
}
