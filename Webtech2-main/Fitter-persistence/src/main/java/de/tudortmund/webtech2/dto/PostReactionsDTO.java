package de.tudortmund.webtech2.dto;

import java.util.HashMap;
import java.util.Map;

public class PostReactionsDTO {

    private Boolean alreadyReacted;

    private int amount;

    private Map<String, Integer> reactions;

    public PostReactionsDTO(Map<String, String> reactions){
        this.reactions = new HashMap<>();
        for(String key : reactions.keySet()){
            this.reactions.compute(reactions.get(key), (k, v) -> v == null ? 1 : v + 1);   // Increments by one each
        }

        amount = reactions.size();
    }


    public Boolean isAlreadyReacted() {
        return alreadyReacted;
    }

    public void setAlreadyReacted(Boolean alreadyReacted) {
        this.alreadyReacted = alreadyReacted;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Map<String, Integer> getReactions() {
        return reactions;
    }

    public void setReactions(Map<String, Integer> reactions) {
        this.reactions = reactions;
    }
}
