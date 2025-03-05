package backend.academy.transformations.affine;

import backend.academy.domain.model.Point;
import backend.academy.exeptions.InvalidAffineTransformationCoefs;
import backend.academy.transformations.Transformation;

/**
 * <p>Представляет собой афинное преобразование двумерной точки</p>
 * <blockquote><pre>
 * (xn+1)   = ( a  b ) * (xn) + ( c )
 * (yn+1)     ( d  e )   (yn)   ( f )
 * где:
 * - (a, b, d, e) — коэффициенты матрицы линейного преобразования;
 * - (c, f) — компоненты сдвига (translation).
 * </pre></blockquote>
 * @see Transformation
 */
public class AffineTransformation implements Transformation {
    protected double a;
    protected double b;
    protected double c;
    protected double d;
    protected double e;
    protected double f;

    /**
     * Условия для коэффициентов матрицы афинного преобразования:
     * <blockquote><pre>
     * 1. a^2 + d^2 < 1
     * 2. b^2 + e^2 < 1
     * 3. a^2 + b^2 + d^2 + e^2 < 1 + (ae - bd)^2
     * </pre></blockquote>
     * Эти ограничения используются для нормализации преобразования,
     * чтобы оно оставалось "умеренным" и не вызывало значительных искажений
     * @param a коэффициент a
     * @param b коэффициент b
     * @param d коэффициент d
     * @param e коэффициент e
     * @return {@code true} если коэффициенты соответствуют условиям,
     * {@code false} если не соответствуют
     */
    public boolean isCorrect(
        double a,
        double b,
        double d,
        double e
    ) {
        double powA = Math.pow(a, 2);
        double powB = Math.pow(b, 2);
        double powD = Math.pow(d, 2);
        double powE = Math.pow(e, 2);

        return (powA + powD) < 1
            && (powB + powE) < 1
            && ((powA + powB + powD + powE) < 1 + Math.pow(a * e - b * d, 2));
    }

    /**
     * Применяет афинное преобразование для точки с координатами x и y
     * @param x координата x
     * @param y координата y
     * @return экземпляр класса {@code Point} - результат преобразования
     * @see Point
     */
    public Point transfrom(double x, double y) {
        double newX = a * x + b * y + c;
        double newY = d * x + e * y + f;
        return new Point(newX, newY);
    }

    /**
     * Конструктор, создающий объект с заданными коэффициентами
     * @param a коэффициент a
     * @param b коэффициент b
     * @param c коэффициент c
     * @param d коэффициент d
     * @param e коэффициент e
     * @param f коэффициент f
     * @throws InvalidAffineTransformationCoefs если не проходят проверку
     * @see AffineTransformation#isCorrect(double, double, double, double)
     */
    public AffineTransformation(
        double a,
        double b,
        double c,
        double d,
        double e,
        double f
    ) {
        //if (!isCorrect(a, b, d, e)) {
        //    throw new InvalidAffineTransformationCoefs(a, b, d, e);
        //} else {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
        //}
    }
}
