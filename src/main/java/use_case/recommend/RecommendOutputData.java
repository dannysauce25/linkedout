package use_case.recommend;

import entity.SideQuest;
import java.util.List;

public class RecommendOutputData {
    private final List<SideQuest> rankedQuests;   // best match first

    public RecommendOutputData(List<SideQuest> rankedQuests) {
        this.rankedQuests = List.copyOf(rankedQuests);
    }

    public List<SideQuest> getRankedQuests() { return rankedQuests; }
}