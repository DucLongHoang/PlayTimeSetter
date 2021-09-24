package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class MarkEntry {
    static final Map<Integer, Integer> time = new HashMap<>();
    static {
        time.put(1, 30);
        time.put(2, 0);
        time.put(3, -60);
        time.put(4, -120);
        time.put(5, -180);
    }
    public static int getTime(int mark){
        return time.get(mark);
    }
    private LocalDate date;
    private String subject, topic;
    private int mark;
    private double weight;

    public MarkEntry(LocalDate date, String subject, String topic, int mark, double weight){
        this.date = date;
        this.subject = subject;
        this.topic = topic;
        this.mark = mark;
        this.weight = weight;
    }

    public LocalDate getDate() {return date;}
    public String getSubject() {return subject;}
    public String getTopic(){return getSubject() + " - " + topic;}
    public int getMark() {return mark;}
    public double getWeight() {return weight;}

    public void setDate(LocalDate date){
        this.date = date;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }
    public void setMark(int mark) {
        this.mark = mark;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Date: " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                " " + date.getDayOfWeek() +
                "\nSubject: " + subject + " - " + topic +
                "\nMark: " + mark +
                "\nWeight: " + weight +
                "\n--------------------\n";
    }
}
