package com.example.codelearningpuzzle;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.codelearningpuzzle.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult, tvStatus;
    private CodeAdapter sourceAdapter, workspaceAdapter;

    // Списки для отображения в RecyclerView
    private final List<String> sourceLines = new ArrayList<>();
    private final List<String> workspaceLines = new ArrayList<>();

    // Переменные для логики игры
    private AppDatabase db;
    private List<TaskEntity> allTasksFromDb;
    private int currentTaskIndex = 0;
    private TaskEntity currentTask;

    private int attempts = 3;
    private int score = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализируем базу данных Room
        db = AppDatabase.getInstance(this);

        initViews();
        setupDatabaseTasks(); // Загружаем или создаем задания
        loadTaskByIndex();    // Показываем текущее задание
    }

    private void initViews() {
        tvResult = findViewById(R.id.tvResult);
        tvStatus = findViewById(R.id.tvStatus);
        Button btnCheck = findViewById(R.id.btnCheck);

        RecyclerView rvSource = findViewById(R.id.rvSourceCode);
        RecyclerView rvWorkspace = findViewById(R.id.rvWorkspace);

        rvSource.setLayoutManager(new LinearLayoutManager(this));
        rvWorkspace.setLayoutManager(new LinearLayoutManager(this));

        sourceAdapter = new CodeAdapter(sourceLines, position -> {
            String line = sourceLines.remove(position);
            workspaceLines.add(line);
            sourceAdapter.notifyItemRemoved(position);
            workspaceAdapter.notifyItemInserted(workspaceLines.size() - 1);
        });

        workspaceAdapter = new CodeAdapter(workspaceLines, position -> {
            String line = workspaceLines.remove(position);
            sourceLines.add(line);
            workspaceAdapter.notifyItemRemoved(position);
            sourceAdapter.notifyItemInserted(sourceLines.size() - 1);
        });

        rvSource.setAdapter(sourceAdapter);
        rvWorkspace.setAdapter(workspaceAdapter);

        btnCheck.setOnClickListener(v -> checkSolution());
        updateStatus();
    }

    private void setupDatabaseTasks() {
        allTasksFromDb = db.taskDao().getAllTasks();

        // Если база пустая (первый запуск), наполняем её уровнями
        if (allTasksFromDb.isEmpty()) {
            db.taskDao().insertAll(
                    new TaskEntity("Большое", Arrays.asList(
                            "int x = 5;",
                            "if (x > 3) {",
                            "    System.out.print(\"Большое\");",
                            "}"
                    )),
                    new TaskEntity("0 1 2 ", Arrays.asList(
                            "for (int i = 0; i < 3; i++) {",
                            "    System.out.print(i + \" \");",
                            "}"
                    )),
                    new TaskEntity("Результат: 10", Arrays.asList(
                            "int a = 4;",
                            "int b = 6;",
                            "int sum = a + b;",
                            "System.out.print(\"Результат: \" + sum);"
                    ))
            );
            // Перечитываем базу после заполнения
            allTasksFromDb = db.taskDao().getAllTasks();
        }
    }

    private void loadTaskByIndex() {
        if (allTasksFromDb == null || allTasksFromDb.isEmpty()) return;

        // Если прошли все задания, возвращаемся на первое
        if (currentTaskIndex >= allTasksFromDb.size()) {
            currentTaskIndex = 0;
            Toast.makeText(this, "Вы прошли все уровни! Начинаем заново.", Toast.LENGTH_LONG).show();
        }

        currentTask = allTasksFromDb.get(currentTaskIndex);
        tvResult.setText("Результат выполнения программы:\n\n" + currentTask.resultText);

        // Перемешиваем строки перед показом
        List<String> shuffled = new ArrayList<>(currentTask.correctOrder);
        java.util.Collections.shuffle(shuffled);

        sourceLines.clear();
        sourceLines.addAll(shuffled);
        workspaceLines.clear();

        sourceAdapter.notifyDataSetChanged();
        workspaceAdapter.notifyDataSetChanged();
    }

    private void checkSolution() {
        if (workspaceLines.equals(currentTask.correctOrder)) {
            Toast.makeText(this, "Верно! Переходим к следующему уровню.", Toast.LENGTH_SHORT).show();
            score += 20;
            attempts = 3;
            currentTaskIndex++; // Переключаем индекс на следующую задачу
            loadTaskByIndex();
        } else {
            attempts--;
            if (attempts <= 0) {
                Toast.makeText(this, "Попытки исчерпаны! Снято 15 баллов.", Toast.LENGTH_LONG).show();
                score = Math.max(0, score - 15);
                attempts = 3;
                loadTaskByIndex(); // Перезапускаем текущий уровень
            } else {
                Toast.makeText(this, "Неверно! Попробуйте переставить строки.", Toast.LENGTH_SHORT).show();
            }
        }
        updateStatus();
    }

    private void updateStatus() {
        tvStatus.setText(String.format("Попыток: %d | Уровень: %d | Баллы: %d",
                attempts, (currentTaskIndex + 1), score));
    }
}
