package replies;

import com.google.common.base.Objects;

import java.util.List;
import java.util.Map;

public class AggregationReply {

    private final Map<String, List<String>> answer;

    public AggregationReply(Map<String, List<String>> answer) {
        this.answer = answer;
    }

    public Map<String, List<String>> getAnswer() {
        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggregationReply that = (AggregationReply) o;
        return Objects.equal(answer, that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(answer);
    }

    @Override
    public String toString() {
        return "AggregationReply{" +
                "answer=" + answer +
                '}';
    }
}
