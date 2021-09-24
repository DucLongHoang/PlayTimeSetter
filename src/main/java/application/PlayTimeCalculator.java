package application;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class PlayTimeCalculator {
    private final LinkedList<MarkEntry> marks;
    private LinkedList<MarkEntry> weekMarks;
    private LocalDate aFriday;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public PlayTimeCalculator(LinkedList<MarkEntry> marks){
        // TODO - change to dynamic fridays
        aFriday = LocalDate.now();
        while(!aFriday.getDayOfWeek().equals(DayOfWeek.FRIDAY)){
            aFriday = aFriday.minusDays(1);
        }
        //aFriday = LocalDate.parse("2021-06-18");
        this.marks = marks;
        this.keepThisWeeksMarks();
    }

    public LinkedList<MarkEntry> getAllMarks() {
        return marks;
    }

    public LinkedList<MarkEntry> getWeekMarks() {
        return weekMarks;
    }

    public int getFinalPlayTime(){
        return (int) (Math.round(this.getPlayTime() / 15.0) * 15.0);
    }
    public double getPlayTime(){
        double result = 0;
        for(MarkEntry entry: weekMarks){
            result += MarkEntry.getTime(entry.getMark()) * entry.getWeight();
        }
        return result;
    }
    public String getWeek(){
        return aFriday.minusDays(4).format(dtf) + " - " + aFriday.format(dtf);
    }

    private void keepThisWeeksMarks() {
        LinkedList<MarkEntry> newList = new LinkedList<>();
        LocalDate monday = aFriday.minusDays(4);
        LocalDate temp;
        int before, after;

        for(MarkEntry entry: marks){
            temp = entry.getDate();
            before = monday.compareTo(temp);
            after = temp.compareTo(aFriday);
            if(!(before > 0 || after > 0)){
                newList.add(entry);
            }
        }
        this.weekMarks = newList;
    }

    public String listWeekMarks() {
        LocalDate date = this.aFriday;
        String sb = "This week: \n" + "Monday -> Friday\n" + date.minusDays(4).format(dtf) +
                " -> " + date.format(dtf) + "\n\n" +
                //for(MarkEntry entry: marks){sb.append(entry.toString());}
                "Filip can play: " + getPlayTime() +
                " minutes";

        return sb;
    }
}
