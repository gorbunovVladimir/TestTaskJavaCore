package ru.tandemservice.test.task1;

import com.sun.istack.internal.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>Задание №1</h1>
 * Реализуйте интерфейс {@link IStringRowsListSorter}.
 *
 * <p>Мы будем обращать внимание в первую очередь на структуру кода и владение стандартными средствами java.</p>
 */
public class Task1Impl implements IStringRowsListSorter
{

    // ваша реализация должна работать, как singleton. даже при использовании из нескольких потоков.
    public static final IStringRowsListSorter INSTANCE = new Task1Impl();

    @Override
    public void sort(final List<String[]> rows, final int columnIndex)
    {
        // напишите здесь свою реализацию. Мы ждем от вас хорошо структурированного, документированного и понятного кода.
        Collections.sort(rows, new ColumnComparator(columnIndex));
    }
    /**
     * Компаратор, сравнивающий строки по заданому порядку: null > "" > все остальные строки (null = null, ""="").
     * Разбивает строку на числовую и нечисловую последовательность символов, вначале пытаясь сравнивать численно.
     * В случае, если "доступные" подстроки одинаковые сравнивает количество подстрок.
     */
    class ColumnComparator implements Comparator<String[]>
    {
        private final int columnIndex;

        ColumnComparator(final int columnIndex)
        {
            this.columnIndex = columnIndex;
        }

        @Override
        public int compare(final String[] row1, final String[] row2)
        {
            try
            {
                String str1=row1[columnIndex], str2=row2[columnIndex];
                //Если одна из строк равняется null
                if (str1 == null || str2 == null)
                {
                    if (str1 == null)
                    {
                        return str2 == null ? 0 : -1;
                    }
                    else if (str2 == null)
                    {
                        return 1;
                    }
                }
                //Если одна из строк является строкой с пустым значением
                if (str1.equals("") || str2.equals(""))
                {
                    if (str1.equals(""))
                    {
                        return str2.equals("") ? 0 : -1;
                    }
                    else if (str2.equals(""))
                    {
                        return 1;
                    }
                }
                //Сравнение строк по подстрокам
                return splitIntoSequenceAndTryCompareNumericalFirst(str1, str2);
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                System.err.format("Список записей не может быть отсортирован по указанной колонке - %d %n", columnIndex);
                throw new ArrayIndexOutOfBoundsException(e.getMessage());
            }
        }
        /**
         * Разбитие строки на числовую и нечисловую последовательность символов
         * Пример: "823hddf.5g7.45332" разделяет на ["823", "hddf.", "5", "g", "7", ".", "45332"]
         */
        private List<String> splitIntoSequence(String str) {
            Matcher match = Pattern.compile("\\d+|\\D+").matcher(str);
            List<String> strings = new ArrayList<String>();
            while (match.find()) {
                strings.add(match.group());
            }
            return strings;
        }
        //Сравнение строк по подстрокам
        private int splitIntoSequenceAndTryCompareNumericalFirst(@NotNull final String str1, @NotNull final String str2) {
            Iterator<String> it1 = splitIntoSequence(str1).iterator();
            Iterator<String> it2 = splitIntoSequence(str2).iterator();

            while (it1.hasNext() && it2.hasNext()) {
                String substring1 = it1.next();
                String substring2 = it2.next();

                //Сравнение подстрок как числа
                Integer comparedAsIntegers;
                Integer int1 = tryParseAsInteger(substring1);
                Integer int2 = tryParseAsInteger(substring2);
                if (int1 != null && int2 != null) {
                    comparedAsIntegers=int1.compareTo(int2);
                } else {
                    comparedAsIntegers=null;
                }
                if (comparedAsIntegers != null && comparedAsIntegers != 0) {
                    return comparedAsIntegers;
                }

                //Сравнение подстрок как строки (В случае если не получилось сравнить как числа)
                int comparedAsSubstrings = substring1.compareTo(substring2);
                if (comparedAsSubstrings != 0) {
                    return comparedAsSubstrings;
                }
            }
            //В случае, если "доступные" подстроки одинаковые сравниваем количество подстрок.
            return Integer.compare(str1.length(), str2.length());
        }

        /**
         * Treat given text as Integer
         * @param substring Integer in string format
         * @return parsed Integer otherwise null
         */
        private Integer tryParseAsInteger(String substring) {
            try {
                return new Integer(substring);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }
}
/**
 * Сортирует переданный список записей (каждая запись - набор колонок) таблицы по указанной колонке по следующим правилам:
 * <ul>
 *  <li>в колонке могут быть null и пустые значения - строки с null-значениями должны быть первыми,
 *  затем строки с пустым значением, затем все остальные,</li>
 *  <li>строка бьется на подстроки следующим образом: выделяем непрерывные максимальные фрагменты строки,
 *  состоящие только из цифр, и считаем набором подстрок эти фрагменты и все оставшиеся от такого разбиения фрагменты строки</li>
 *  <li>при сравнении строк осуществляется последовательное сравнение их подстрок до первого несовпадения,</li>
 *  <li>если обе подстроки состоят из цифр - то при сравнении они интерпретируются как целые числа
 *  (вначале должно идти меньшее число), в противном случае - как строки,</li>
 *  <li>сортировка должна быть устойчива к исходной сортировке списка - т.е., если строки
 *  (в контексте указанных правил сравнения) неразличимы, то сортировка не должна менять их местами.</li>
 * </ul>
 *
 * @param rows список записей таблицы (например, результат sql select), которые нужно отсортировать по указанной колонке
 * @param columnIndex индекс колонки, по которой нужно провести сортировку
 */

  //отсортированный список: отсортированная запись
  // "1", "1a", "2", "3", "4", "11", "11x", "20", "101", "a", "ab", "a1", "a11x"

