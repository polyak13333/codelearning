package com.example.codelearningpuzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodeTask {
    private final String resultText;
    private final List<String> correctOrder;

    public CodeTask(String resultText, List<String> correctOrder) {
        this.resultText = resultText;
        this.correctOrder = correctOrder;
    }

    public String getResultText() { return resultText; }
    public List<String> getCorrectOrder() { return correctOrder; }

    public List<String> getShuffledLines() {
        List<String> shuffled = new ArrayList<>(correctOrder);
        while (shuffled.equals(correctOrder) && shuffled.size() > 1) {
            Collections.shuffle(shuffled);
        }
        return shuffled;
    }
}
