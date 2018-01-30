package zhangyi.training.school.fp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Java8FPTest {
    @Test
    public void should_sum_all_numbers_in_list() {
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            l.add(i);
        }

        assertThat(l.stream().reduce(0, (x, y) -> x + y)).isEqualTo(45);
    }
}
