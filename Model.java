/*
 * CS151 - HW4
 * @author Thy Nguyen
 */
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Scanner;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Model {
public static String FILE_NAME = "src/events.txt";
public static MONTHS[] monthArr = MONTHS.values();
public static DAYS[] dayArr = DAYS.values();

public GregorianCalendar gc = new GregorianCalendar();
private ArrayList<Event> events;
private ArrayList<GregorianCalendar> datesOfEvent;
private ArrayList<ChangeListener> listeners;
public Model(){
	events = new ArrayList<>();
	listeners = new ArrayList<>();
	datesOfEvent = new ArrayList<>();
}
//accessor
public ArrayList<Event> getData(){
	return events;
}
public ArrayList<GregorianCalendar> getDatesOfEvent(){
	return datesOfEvent;
}
public void attach(ChangeListener l) {
	listeners.add(l);
}
//mutator
public void addEvent(Event e) {	
	//System.out.println(e.toString());
	events.add(e);
	sortEventList(events);
	writeToFile();
}

public void update() {
	for(ChangeListener l: listeners) {
		l.stateChanged(new ChangeEvent(this));
	}
}



private static void sortEventList(ArrayList<Event> list) {
	Comparator<Event> comp = new Comparator<Event>() {
		public int compare(Event e1, Event e2) {
			return e1.compareTo(e2);
		}
	};
	Collections.sort(list, comp);
}



public ArrayList<Event> searchEvent(int day, int mon, int year) {
	sortEventList(events);
	ArrayList<Event> foundArr = new ArrayList<>();
	for (int i = 0; i < events.size(); i++) {
		if (events.get(i).getDate().get(Calendar.MONTH) == mon && events.get(i).getDate().get(Calendar.DAY_OF_MONTH) == day
				&& events.get(i).getDate().get(Calendar.YEAR) == year)
			foundArr.add(events.get(i));
	}
	return foundArr;
}

/**
 * To load the events.txt and add all the events into array list
 */
public void loadFile() {
	ArrayList<String> arr = getStringFromFile();
	if (arr.size() == 0)
		System.out.println("Loadfile This is the first run");
	else {
		for (int i = 0; i < arr.size(); i++) {
			String line = arr.get(i);
			String[] column = line.split("\t");
			String month = String.valueOf(getIndex(column[1]));
			String day = column[2];
			String year = column[3];
			Event e = new Event(column[0], day, month, year);
			e.setStartTime(column[4]);
			if (column.length == 6) {
				e.setEndTime(column[5]);
			}
			events.add(e);
		} // end for
		sortEventList(events);
		System.out.println("Loadfile debug Successfully loading file");
	}
}

/**
 * To save all the events newly-created into the array list of events
 */
public void writeToFile() {
	FileOutputStream outFile = null;
	try {
		outFile = new FileOutputStream(FILE_NAME);
	} catch (FileNotFoundException fnf) {
		System.err.println("File cannot be opened!");
		return;
	}
	PrintWriter pW = new PrintWriter(outFile, false);
	sortEventList(events);
	for (int i = 0; i < events.size(); i++) {
		Event cur = events.get(i);
		String endtime = "";
		if (cur.getEndTime() != null)
			endtime = cur.getEndTime();
		pW.println(cur.getTitle() + "\t" + monthArr[cur.getDate().get(Calendar.MONTH)].toString() + "\t"
				+ cur.getDate().get(Calendar.DAY_OF_MONTH) + "\t" + cur.getDate().get(Calendar.YEAR) + "\t"
				+ cur.getStartTime() + "\t" + endtime);
	}

	pW.close();
}
//this will be used for the listener of the day button

private static ArrayList<String> getStringFromFile() {
	ArrayList<String> strings = new ArrayList<>();
	java.io.File file = new java.io.File(FILE_NAME);
	Scanner fileSc = null;
	try {
		fileSc = new Scanner(file);
	} catch (FileNotFoundException e) {
		System.out.println("File not found");
		strings = null;
	}
	String line = "";
	while (fileSc.hasNextLine()) {
		line = fileSc.nextLine();
		strings.add(line);
	}
	fileSc.close();
	return strings;
}

private int getIndex(String s) {
	int result = 0;
	for (int i = 0; i < monthArr.length; i++) {
		if (s.equals(monthArr[i].toString())) {
			result = i;
			break;
		}

	}
	return result;
}

public void previousDay() {
	gc.add(Calendar.DAY_OF_MONTH, -1);
	this.update();
}




}//end class
