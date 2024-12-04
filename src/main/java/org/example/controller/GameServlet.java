package org.example.controller;

import org.example.model.GameLogic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/game")
public class GameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Получаем имя игрока и количество сыгранных игр из сессии
        String playerName = (String) session.getAttribute("playerName");

        // Перенаправляем на страницу приветствия, если имя игрока или количество игр не установлены
        if (playerName == null) {
            response.sendRedirect("welcome.jsp");
            return;
        }

        GameLogic gameLogic = new GameLogic();

        // Получаем текущий шаг игры; если его нет, начинаем игру
        String currentStep = (String) session.getAttribute("currentStep");
        if (currentStep == null) {
            currentStep = gameLogic.startGame();
            session.setAttribute("currentStep", currentStep);
        }

        // Устанавливаем вопрос для текущего шага
        request.setAttribute("question", gameLogic.getQuestion(currentStep));
        // Перенаправляем запрос на JSP-страницу игры
        request.getRequestDispatcher("WEB-INF/jsp/game.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Получаем имя игрока из сессии
        String playerName = request.getParameter("playerName") == null ?
                (String) session.getAttribute("playerName"): request.getParameter("playerName");
        GameLogic gameLogic = new GameLogic();

        // Перенаправляем на страницу приветствия, если имя игрока не установлено
        if (playerName == null) {
            response.sendRedirect("welcome.jsp");
            return;
        }

        // Получаем текущий шаг и ответ, предоставленный игроком
        String currentStep = (String) session.getAttribute("currentStep");
        String answer = request.getParameter("answer");

        // Проверяем, завершена ли игра; если да, перенаправляем на страницу результата
        if (gameLogic.isGameOver(currentStep)) {
            response.sendRedirect("result.jsp");
            return;
        }

        // Определяем следующий шаг на основе ответа игрока
        String nextStep = gameLogic.nextStep(currentStep, answer);
        session.setAttribute("currentStep", nextStep);

        // Если игра завершается, сохраняем результат и перенаправляем на страницу результата
        if (gameLogic.isGameOver(nextStep)) {
            session.setAttribute("gameResult", gameLogic.getGameResult(nextStep));
            response.sendRedirect("result.jsp");
        } else {
            // Продолжаем игру, перенаправляя на страницу игры
            session.setAttribute("playerName", playerName);
            response.sendRedirect("game");
        }
    }
}