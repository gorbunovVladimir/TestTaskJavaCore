package ru.tandemservice.test.task2;

import java.util.List;
import java.util.TreeMap;

/**
 * <h1>Задание №2</h1>
 * Реализуйте интерфейс {@link IElementNumberAssigner}.
 *
 * <p>Помимо качества кода, мы будем обращать внимание на оптимальность предложенного алгоритма по времени работы
 * с учетом скорости выполнения операции присвоения номера:
 * большим плюсом (хотя это и не обязательно) будет оценка числа операций, доказательство оптимальности
 * или указание области, в которой алгоритм будет оптимальным.</p>
 */
public class Task2Impl implements IElementNumberAssigner {

    // ваша реализация должна работать, как singleton. даже при использовании из нескольких потоков.
    public static final IElementNumberAssigner INSTANCE = new Task2Impl();

    // sequenceOrderIElements хранит пары ключ-значения (<element.getNumber(),element> pairs) для быстрого поиска
    // элемента по его номеру следования элементов в коллекции List<IElement> elements
    private final TreeMap<Integer, IElement> sequenceOrderIElements = new TreeMap<>();
    private int freeNumber = 1;

   /* Алгоритм находит свободный номер (номер, который не присвоен ни одному элементу). Последовательно присваивает нужный номер элементам, следуя следующему алгоритму:
      - В случае, если необходимый номер (который необходимо присвоить текущему элементу) свободен, то присвоить и обновить свободный номер.
      - Свободным номером становится номер, который был у текущего элемента.
        Иначе освободить номер и сделать тоже самое.
        Освобождение номера n происходит при помощи присваивания свободного номера элементу, чей номер является n.
    */
    @Override
    public void assignNumbers(final List<IElement> elements) {
        // напишите здесь свою реализацию. Мы ждем от вас хорошо структурированного, документированного и понятного кода, работающего за разумное время.
        for (IElement e : elements)
            sequenceOrderIElements.put(e.getNumber(), e);
        // Найти свободный номер (номер, который не присвоен ни одному элементу) используя sequenceOrderIElements
        while (sequenceOrderIElements.keySet().contains(freeNumber)) {
            freeNumber++;
        }
        //Выставление номеров для элементов коллекции в порядке следования элементов в коллекции
        int index = 1; //указатель на высталяемый номер
        for (IElement current : elements) {
            int currentNumber = current.getNumber();
            if (currentNumber != index) {
                //elementHoldsIndex хранит элемент с номером, устанавливаемым в текущий момент
                IElement elementHoldsIndex = sequenceOrderIElements.get(index);
                if (elementHoldsIndex != null)
                    repositionNumbers(current, elementHoldsIndex);
                else // если element не найден в sequenceOrderIElements (выставляемому номеру не соответствует ни один элемент),
                     // тогда просто устанавливаем ему необходимый номер
                    setupNumberAndUpdateSequenceOrderIElements(current, index);
            }
            index++;
        }
    }
    /**Переставляет номера элементов
     * @param tobeSetup element which takes number of the second argument
     * @param tobeFree element which number will be set to free number
     */
    private void repositionNumbers(final IElement tobeSetup, final IElement tobeFree) {
        //Освобождаем требуемый номер выставляя элементу у которого он был свободный номер. Обновляем sequenceOrderIElements и freeNumber
        int tobeSetupNumber = tobeFree.getNumber();
        setupNumberAndUpdateSequenceOrderIElements(tobeFree, freeNumber);//вызов {@code element.setNumber(i)}в данном случае разрешен т.к. ∀ e ∊ {@code elements} (e.number ≠ i)
        //но т.к. набор методов, которые можно вызвать у переменной (в данном случае tobeFree), определяется типом переменной(IElement), то я не могу воспользоваться setNumber(i))
        freeNumber = tobeSetupNumber;

        //Выставляем элементу требуемый номер. Обновляем sequenceOrderIElements и freeNumber
        setupNumberAndUpdateSequenceOrderIElements(tobeSetup, tobeSetupNumber);
        freeNumber = tobeSetup.getNumber();
    }

    private void setupNumberAndUpdateSequenceOrderIElements(final IElement element, int newNumber) {
        //System.out.println(newNumber);
        sequenceOrderIElements.remove(element.getNumber());
        element.setupNumber(newNumber);
        sequenceOrderIElements.put(element.getNumber(), element);
    }
}


/**
 * Метод выставляет номера {@link IElement#setupNumber(int)}
 * для элементов коллекции {@code elements}
 * в порядке следования элементов в коллекции.
 *
 * Изначально коллекция не содержит элементов, номера которых повторяются.
 * При этом обеспечиваются слеюущие условия:<ul>
 *  -метод работает только с существующими элементами (не создает новых),
 *  -на протяжении всей работы метода обеспечивается уникальность номеров элементов:
 *     -вызов {@code element.setNumber(i)} разрешен ⇔   ∀ e ∊ {@code elements} (e.number ≠ i),</p>
 *      вызов {@code element.setNumber(i)} разрешен тогда и только тогда, когда для всех е принадлежащих
 *      {@code elements} (e.number ≠ i)
 *      -метод устойчив к передаче в него в качестве параметра {@link java.util.Collections#unmodifiableList(List)} и
 *      любой другой реализации immutable-list,</p>
 *     -метод должен работать за «приемлемое» время ({@link IElement#setupNumber(int)} - трудоемкая операция и пользоваться ей надо рационально)</p>
 * @param elements элементы, которым нужно выставить номера
 */