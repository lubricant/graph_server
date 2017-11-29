package test;

import java.util.List;
import java.util.Map;

public interface SomeService {

    void echo();

    Integer call(Integer i);

    Map<String,String> call(List<Integer> i);
}
