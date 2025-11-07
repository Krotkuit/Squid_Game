package fr.salut.squidgame.utils.log;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogWriter {
    private static final long MAX_SIZE_BYTES = 1024 * 1024; // 1 MB
    private static final String LOG_FOLDER = "deadlogs/";
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private BufferedWriter writer;
    private String currentDate;
    private int index = 1;

    public LogWriter() throws IOException {
        Files.createDirectories(Paths.get(LOG_FOLDER));
        currentDate = getToday();
        openWriter();
    }

    private String getToday() {
        return new SimpleDateFormat(DATE_FORMAT).format(new Date());
    }

    private void openWriter() throws IOException {
        while (true) {
            String fileName = LOG_FOLDER + currentDate + "-" + String.format("%02d", index) + ".txt";
            File file = new File(fileName);
            if (!file.exists() || file.length() < MAX_SIZE_BYTES) {
                writer = new BufferedWriter(new FileWriter(file, true));
                break;
            }
            index++;
        }
    }

    public void log(String message) throws IOException {
        String today = getToday();
        if (!today.equals(currentDate)) {
            // Nouveau jour : reset
            writer.close();
            currentDate = today;
            index = 1;
            openWriter();
        }

        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        writer.write("[" + timestamp + "] " + message);
        writer.newLine();
        writer.flush();

        File currentFile = new File(LOG_FOLDER + currentDate + "-" + String.format("%02d", index) + ".txt");
        if (currentFile.length() >= MAX_SIZE_BYTES) {
            writer.close();
            index++;
            openWriter();
        }
    }

    public void close() throws IOException {
        if (writer != null) writer.close();
    }
}