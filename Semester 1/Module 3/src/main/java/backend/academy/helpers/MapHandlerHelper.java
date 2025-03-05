package backend.academy.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import lombok.experimental.UtilityClass;

/**
 * Статический класс-хэлпер, осуществляющий обработку классов {@code Map<T, Integer>}
 */
@UtilityClass
public class MapHandlerHelper {
    private static final int NUMBER_OF_RETURNING_ELEMENTS = 3;
    private static List<Double> arrayOfValues;
    private static double curExtremumUpdate;

    /**
     * Осуществляет поиск ключей, хранящих максимальные или минимальные значения.
     * Поиск по максимальными или минимальным значениям определяется передаваемой в параметрах функцией
     * @param map  {@code Map<T, Integer>}
     * @param func  {@code BiFunction<Double, Double, Boolean>}
     * - функция, определяющая поиск минимальных или максимальных значений
     * @return {@code Map<T, Integer>} - мапа с ключами, хранящими максимальные или минимальные значения
     * @param <T> Тип ключа обрабатываемого {@code Map}
     * @see MapHandlerHelper#findMaxes(Map)
     * @see MapHandlerHelper#findMins(Map)
     */
    public static <T> Map<T, Integer> find(Map<T, Integer> map, BiFunction<Double, Double, Boolean> func) {
        List<T> arrayOfKeys = new ArrayList<>(
            Collections.nCopies(NUMBER_OF_RETURNING_ELEMENTS, null));

        for (T key : map.keySet()) {
            // Поиск минимального из текущих хранимых максимальных
            // или максимального из текущих хранимых минимальных
            int ind = -1;
            double curExtremum = curExtremumUpdate;
            for (int i = 0; i < arrayOfKeys.size(); ++i) {
                double curValue = arrayOfValues.get(i);
                if (func.apply(curExtremum, curValue)) {
                    curExtremum = curValue;
                    ind = i;
                }
            }

            // Подмена минимального (максимального) из текущих хранимых на новое значение, если оно больше (меньше)
            double curMapKey = map.get(key);
            if (!func.apply(curExtremum, curMapKey)) {
                arrayOfValues.set(ind, curMapKey);
                arrayOfKeys.set(ind, key);
            }
        }

        // Собираем мапу-результат
        Map<T, Integer> result = new HashMap<>();
        for (int i = 0; i < arrayOfValues.size(); ++i) {
            T curKey = arrayOfKeys.get(i);
            if (curKey != null) {
                result.put(curKey, (arrayOfValues.get(i).intValue()));
            }
        }

        return result;
    }

    /**
     * Возвращает мапу с ключами, хранящими максимальные значения, и сами значения
     * @param map  {@code Map<T, Integer>}
     * @return {@code Map<T, Integer>} - мапа с ключами, хранящими максимальные значения
     * @param <T> - Тип ключа обрабатываемого {@code Map}
     * @see MapHandlerHelper#find(Map, BiFunction)
     * @see MapHandlerHelper#findMins(Map)
     */
    public static <T> Map<T, Integer> findMaxes(Map<T, Integer> map) {
        arrayOfValues = new ArrayList<>(
            Collections.nCopies(NUMBER_OF_RETURNING_ELEMENTS, 0.0));
        curExtremumUpdate = Double.POSITIVE_INFINITY;
        return find(map, (first, second) -> first > second);
    }

    /**
     * Возвращает мапу с ключами, хранящими минимальные значения, и сами значения
     * @param map  {@code Map<T, Integer>}
     * @return {@code Map<T, Integer>} - мапа с ключами, хранящими минимальные значения
     * @param <T> Тип ключа обрабатываемого {@code Map}
     * @see MapHandlerHelper#find(Map, BiFunction)
     * @see MapHandlerHelper#findMaxes(Map)
     */
    public static <T> Map<T, Integer> findMins(Map<T, Integer> map) {
        arrayOfValues = new ArrayList<>(
            Collections.nCopies(NUMBER_OF_RETURNING_ELEMENTS, Double.POSITIVE_INFINITY));
        curExtremumUpdate = Double.NEGATIVE_INFINITY;
        return find(map, (first, second) -> first < second);
    }
}
