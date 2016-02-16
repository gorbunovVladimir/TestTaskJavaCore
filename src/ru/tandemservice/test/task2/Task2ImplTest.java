package ru.tandemservice.test.task2;

/**
 * Created by Vl on 20.01.2016.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Task2ImplTest {


    public static void main(String[] args) throws Exception {
        final ElementExampleImpl.Context context = new ElementExampleImpl.Context();
        List<IElement> elems = new ArrayList<>();
        Collections.addAll(elems, new ElementExampleImpl(context, -20),
                new ElementExampleImpl(context, -1),new ElementExampleImpl(context, 3),new ElementExampleImpl(context, 0),new ElementExampleImpl(context, 38),new ElementExampleImpl(context, 131),new ElementExampleImpl(context, -45),new ElementExampleImpl(context, 17));
/*addAll(Collection<? super T> c, T... elements) - Adds all of the specified elements to the specified collection.*/
        Task2Impl.INSTANCE.assignNumbers(elems);
        System.out.println(elems);
    }
}


//[Element{number=1, id=1174361318}, Element{number=2, id=589873731}, Element{number=3, id=200006406}, Element{number=4, id=2052001577}]
//operationCount=2
