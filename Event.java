	/*
 * CS151 - HW4
 * @author Thy Nguyen
 */
import java.util.Calendar;
	import java.text.ParseException;
	import java.util.GregorianCalendar;
	enum MONTHS {
		Jan, Feb, Mar, Apr, May, June, July, Aug, Sep, Oct, Nov, Dec;
	}

	enum DAYS {
		Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
	}


	public class Event {
		public static MONTHS[] monthArr = MONTHS.values();
		public static DAYS[] dayArr = DAYS.values();
		private String title;
		private GregorianCalendar date;
		private String startTime;
		private String endTime;

		/**
		 * To construct an Event object with String
		 * 
		 * @param t
		 *            title of the event
		 */
		public Event(String t) {
			this.title = t;
		}

		/**
		 * To construct an Event object with String
		 * 
		 * @param t
		 *            the title of the event
		 * @param day
		 *            the day of month when the event occurs
		 * @param mon
		 *            the month when the event occurs
		 * @param year
		 *            the year when the event occurs
		 */
		public Event(String t, String day, String mon, String year) {
			this.title = t;
			date = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(mon), Integer.parseInt(day));
		}

		/**
		 * @return the title of the event
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * To get the date when the event occurs
		 * 
		 * @return the date when the event occurs
		 */
		public GregorianCalendar getDate() {
			return date;
		}

		/**
		 * @return the start time of the event
		 */
		public String getStartTime() {
			return startTime;
		}

		/**
		 * @return the end time of the event
		 */
		public String getEndTime() {
			return endTime;
		}

		/**
		 * To set the start time of the event
		 * 
		 * @param s
		 *            start time string in format hh:mm
		 */
		public void setStartTime(String s) {
			String[] time = s.split(":");
			date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
			date.set(Calendar.MINUTE, Integer.parseInt(time[1]));
			startTime = s;
		}

		/**
		 * To set the start time of the event
		 * 
		 * @param s
		 *            end time string in format hh:mm
		 */
		public void setEndTime(String s) {
			if(s!=null) {
			try {
				String[] time = s.split(":");
			date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
			date.set(Calendar.MINUTE, Integer.parseInt(time[1]));
				
			}catch (NullPointerException npx) {
				
			}
			}
		}

		/**
		 * To get the string date in desired format similar to Saturday, Mar 24,
		 * 2018
		 * 
		 * @return string of date in desired format
		 */
//		public String eventDate() {
//			return dayArr[date.get(Calendar.DAY_OF_WEEK) - 1].toString() + ", "
//					+ monthArr[date.get(Calendar.MONTH)].toString() + " " + date.get(Calendar.DAY_OF_MONTH) + ", "
//					+ date.get(Calendar.YEAR);
//		}

		/**
		 * To get the string of title, start time and end time of the event
		 */
		public String toString() {
			if (endTime != null)
				return startTime + " - " + endTime + ": " + title;
			else
				return startTime + ": " + title  ;
		}

		/**
		 * @return The string in desired format used for printing list of event
		 */
//		public String toStringForList() {
//			String printEnd;
//			if (endTime == null)
//				printEnd = " ";
//			else
//				printEnd = " - " + endTime;
//			return dayArr[date.get(Calendar.DAY_OF_WEEK) - 1].toString() + " "
//					+ monthArr[date.get(Calendar.MONTH)].toString() + " " + date.get(Calendar.DAY_OF_MONTH) + " "
//					+ startTime + printEnd + " " + title;
//		}

		/**
		 * To check if two events are in the same date
		 * 
		 * @param e
		 *            event that needs to be checked
		 * @return true if two events occur in the same date, false otherwise
		 */
		public boolean sameDate(Event e) {
			return this.date.get(Calendar.YEAR) == e.getDate().get(Calendar.YEAR)
					&& this.date.get(Calendar.MONTH) == e.getDate().get(Calendar.MONTH)
					&& this.date.get(Calendar.DAY_OF_MONTH) == e.getDate().get(Calendar.DAY_OF_MONTH);
		}

		/**
		 * To check if the event conflicts time with another
		 * 
		 * @param e
		 *            event that needs to be checked
		 * @return true if they are conflict, false otherwise
		 */
//		public boolean isConflict(Event e) {
//			boolean isConflict = false;
//			if (this.sameDate(e)) {
//				if (endTime != null) {
//				if (e.getStartTime().compareTo(this.getStartTime()) >= 0
//						&& e.getStartTime().compareTo(this.getEndTime()) < 0)
//					isConflict =  true;
//				else
//				isConflict =  e.getStartTime().compareTo(this.getStartTime()) == 0;
//			} 
//			
//		}
//			System.out.println(isConflict);
//			return isConflict;
//		}
		public boolean isConflict(Event e) {
			if (!this.sameDate(e))
				return false;
			if (endTime != null) {
				if (e.getStartTime().compareTo(this.getStartTime()) > 0
						&& e.getStartTime().compareTo(this.getEndTime()) < 0)
					return true;
			} else
				return e.getStartTime().compareTo(this.getStartTime()) == 0;
			if (e.getStartTime().compareTo(this.getEndTime())==0)
				return true;
			else
				return false;
		}

		/**
		 * To compare two events in term of starting time
		 * 
		 * @param e
		 *            event that needs to be checked
		 * @return -1, 0, 1 for this event start time is smaller, equal or greater
		 *         than those of e respectively
		 */
		public int compareTo(Event e) {
			if (this.sameDate(e))
				return this.getStartTime().compareTo(e.getStartTime());
			// not the same date
			return this.getDate().compareTo(e.getDate());
		}

	}// end class


