package replies;

import java.util.List;

public class SearchReply {

    private final List<String> references;

    public SearchReply(List<String> references) {
        this.references = references;
    }

    public List<String> getReferences() {
        return references;
    }
}
