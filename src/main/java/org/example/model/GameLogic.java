package org.example.model;
import java.util.HashMap;
import java.util.Map;

public class GameLogic {
    private final Map<String, String> questions = new HashMap<>(); // Содержит вопросы игры
    private final Map<String, Map<String, String>> choices = new HashMap<>(); // Содержит варианты ответов для каждого шага
    private final Map<String, String> results = new HashMap<>(); // Содержит результаты игры

    public GameLogic() {
        // Определяем вопросы и варианты ответов игры
        questions.put("start", "You have lost your memory. Will you accept the UFO's challenge? Accept or decline");
        questions.put("accept", "You accepted the challenge. Will you go to the bridge to meet the captain? GoToBridge or refuseBridge");
        questions.put("decline", "You declined the challenge. You lost.");
        questions.put("bridge", "You went to the bridge. Who are you? TellTruth or lie");

        // Определяем пути игры
        Map<String, String> startChoices = new HashMap<>();
        startChoices.put("Accept", "accept");
        startChoices.put("decline", "lose");
        choices.put("start", startChoices);

        Map<String, String> acceptChoices = new HashMap<>();
        acceptChoices.put("GoToBridge", "bridge");
        acceptChoices.put("refuseBridge", "lose");
        choices.put("accept", acceptChoices);

        Map<String, String> bridgeChoices = new HashMap<>();
        bridgeChoices.put("TellTruth", "win");
        bridgeChoices.put("lie", "lose");
        choices.put("bridge", bridgeChoices);

        // Определяем конечные результаты
        results.put("win", "You told the truth about yourself. You were returned home. Victory!");
        results.put("lose", "You lost. Try again.");
    }

    // Начинает игру, возвращая начальный шаг
    public String startGame() {
        return "start";
    }

    // Получает вопрос для указанного шага
    public String getQuestion(String step) {
        return questions.getOrDefault(step, "Неизвестный шаг");
    }

    // Определяет следующий шаг на основе текущего шага и ответа игрока
    public String nextStep(String currentStep, String answer) {
        return choices.getOrDefault(currentStep, new HashMap<>()).getOrDefault(answer, "start");
    }

    // Проверяет, завершена ли игра для указанного шага
    public boolean isGameOver(String step) {
        return results.containsKey(step);
    }

    // Получает сообщение о результате игры для конечного шага
    public String getGameResult(String step) {
        return results.getOrDefault(step, "Неизвестный результат");
    }
}
