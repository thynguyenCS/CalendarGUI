/*
 * CS151 - HW4
 * @author Thy Nguyen
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumnModel;

public class ViewController {
	public static int NUM_DAYS = 7;
	public static String FILE_NAME = "src/events.txt";
	public static MONTHS[] monthArr = MONTHS.values();
	public static DAYS[] dayArr = DAYS.values();

	public GregorianCalendar gc;
	private Model model;
	private ArrayList<Event> events;
	private final JFrame fr, createFrame;
	private final JPanel calendarGrid, topButtonsPanel, calendarBoard;
	private final JTextArea eventsList;
	private JTextField eventTitle, eventDate, start, end;
	private JLabel monYearLabel, dateLabel;
	public int currentDay, currentMon, currentYear;
/**
 * Construct a view model object
 * @param m model
 */
	public ViewController(Model m) {
		gc = new GregorianCalendar();
		this.model = m;
		events = model.getData();
		currentDay = gc.get(Calendar.DAY_OF_MONTH);
		currentMon = gc.get(Calendar.MONTH);// less than 1 compare to real appearance
		currentYear = gc.get(Calendar.YEAR);

		fr = new JFrame();

		fr.setLayout(new BorderLayout());
		createFrame = new JFrame();

		// top panel for create button, previous button, next button and quit button

		topButtonsPanel = new JPanel();
		JButton createBtn = createButton(currentDay, currentMon, currentYear);
		JButton prevBtn = previousButton();
		JButton nextBtn = nextButton();
		JButton quitBtn = quitButton();
		topButtonsPanel.add(createBtn);
		topButtonsPanel.add(prevBtn);
		topButtonsPanel.add(nextBtn);
		topButtonsPanel.add(quitBtn);
		fr.add(topButtonsPanel, BorderLayout.NORTH);

		// calendarBoard including calendargrid
		calendarBoard = new JPanel();

		monYearLabel = new JLabel(monthArr[currentMon].toString() + " " + currentYear);

		calendarGrid = new JPanel();

		calendarBoard.add(monYearLabel);
		calendarBoard.add(calendarGrid());

		fr.add(calendarBoard, BorderLayout.WEST);////// temporary, need to add the panel of month and year
		JPanel rightPanel = new JPanel();
		dateLabel = new JLabel(currentDay + "/" + (currentMon + 1) + "/" + currentYear);
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(dateLabel, BorderLayout.NORTH);

		//eventsList = eventsList();
		// eventsList.setText("hi");
		eventsList = new JTextArea();

		rightPanel.add(eventsList, BorderLayout.CENTER);
		fr.add(rightPanel, BorderLayout.CENTER);

		// attach listeners
		model.attach(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				calendarGrid();
				fr.validate();
				fr.repaint();

			}
		});
		model.attach(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				monYearLabel.setText(monthArr[currentMon].toString() + " " + currentYear);
				monYearLabel.repaint();

			}
		});
		model.attach(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				eventsList.setFont(new Font("Serif", Font.ITALIC, 13));
				eventsList.setBackground(Color.PINK);
				ArrayList<Event> found = model.searchEvent(currentDay, currentMon, currentYear);
				String display = "";
				if (found == null) {
					display = "No event";
				} else {
					for (Event e : found) {
						display += e.toString() + "\n";
					}
				}
				System.out.println(display);
				eventsList.setText(display);
				eventsList.repaint();
			}
		});
		model.attach(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				dateLabel.setText(currentDay + "/" + (currentMon + 1) + "/" + currentYear);
				dateLabel.repaint();
			}
		});

		// Design the look of the frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		fr.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
		fr.pack();
		fr.setLocationRelativeTo(null);
		;
		fr.setVisible(true);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
/**
 * 
 * @return a JtextArea displaying events list
 */
	public JTextArea eventsList() {
		//System.out.println("here");
	//	 eventsList = new JTextArea();
		ArrayList<Event> found = model.searchEvent(currentDay, currentMon, currentYear);
		String display = "";
		if (found == null) {
			display = "No event";
			System.out.println("here");
		} else {
			for (Event e : found) {
				// display += e.toString() + "\n";
				display += "hi ";
				System.out.println("here1");
			}
		}
		System.out.println(display);
		eventsList.setText(display);
		eventsList.setFont(new Font("Serif", Font.ITALIC, 13));
		eventsList.setBackground(Color.pink);
		return eventsList;
	}
/**
 * 
 * @return calendar grid - month view
 */
	public JPanel calendarGrid() {
		calendarGrid.removeAll();
		// Calendar c = (Calendar) gc.clone();
		gc.set(Calendar.DAY_OF_MONTH, 1);
		int firstDay = gc.get(Calendar.DAY_OF_WEEK) - 1;
		int daysInMon = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendarGrid.setLayout(new GridLayout(0, 7));
		String[] header = { "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" };
		for (int ct = 0; ct < header.length; ct++) {
			JButton headerBtn = new JButton(header[ct]);
			// headerBtn.setSize(7, 7);
			headerBtn.setEnabled(false);
			headerBtn.setBorderPainted(false);

			calendarGrid.add(headerBtn);
		}
		// create individual days in month by buttons
		if (firstDay != 0) {
			for (int i = 0; i < firstDay; i++) {
				JButton nullBtn = new JButton(" ");
				nullBtn.setBorderPainted(false);
				nullBtn.setEnabled(false);
				calendarGrid.add(nullBtn);
			}

		}
		JButton dateBtn;
		int counter = 1;
		while (counter <= daysInMon) {
			dateBtn = new JButton(String.valueOf(counter));
			dateBtn.addActionListener(changeDateListener(counter));
			if (counter == currentDay) {
				dateBtn.setBackground(Color.GRAY);
				dateBtn.setOpaque(true);
			}

			dateBtn.setBorderPainted(false);
			calendarGrid.add(dateBtn);
			counter++;
		} // end while
		return calendarGrid;
	}

	private ActionListener changeDateListener(int dateOnLabel) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton dateBtn = (JButton) e.getSource();
				currentDay = Integer.parseInt(dateBtn.getActionCommand());
				model.update();
			}
		};
	}

	private JButton createButton(int day, int mon, int year) {
		JButton createBtn = new JButton("Create");
		createBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFrame(createFrame, day, mon, year);
			}
		});
		return createBtn;
	}
/**
 * 
 * @return button to go back 
 */
	public JButton previousButton() {
		JButton prevBtn = new JButton("<<");
		prevBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gc.set(Calendar.DAY_OF_MONTH, currentDay);// reset the calendar to current day
				gc.add(Calendar.DAY_OF_MONTH, -1);
				currentMon= gc.get(Calendar.MONTH);
				currentYear = gc.get(Calendar.YEAR);
				currentDay = gc.get(Calendar.DAY_OF_MONTH);
				System.out.println("previous" + currentDay);
				model.update();

				// so it works with test, but does not show on the calendar
			}
		});
		return prevBtn;
	}
/**
 * 
 * @return button to go next day
 */
	public JButton nextButton() {
		JButton nextBtn = new JButton(">>");
		nextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gc.set(Calendar.DAY_OF_MONTH, currentDay);// reset the calendar to current day
				gc.add(Calendar.DAY_OF_MONTH, 1);
				currentMon= gc.get(Calendar.MONTH);
				currentYear = gc.get(Calendar.YEAR);
				currentDay = gc.get(Calendar.DAY_OF_MONTH);
				System.out.println("next" + currentDay);
				model.update();

			}
		});
		return nextBtn;
	}

	private JButton quitButton() {
		JButton quitBtn = new JButton("Quit");
		quitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.writeToFile();
				System.exit(0);

			}
		});
		return quitBtn;
	}
/**
 * frame for create an event
 * @param createFrame
 * @param day
 * @param mon
 * @param year
 */
	public void createFrame(JFrame createFrame, int day, int mon, int year) {

		final int FIELD_WIDTH = 20;
		// create this so the cursor not go to untitled textfield
		JTextField title = new JTextField(FIELD_WIDTH / 2);
		title.setText("CREATE EVENT");
		title.setFont(new Font("Serif", Font.BOLD, 14));
		title.setEditable(false);
		title.setBorder(null);

		// create required text fields and button

		eventTitle = new JTextField(5 * FIELD_WIDTH);
		eventDate = new JTextField(2 * FIELD_WIDTH);
		start = new JTextField(FIELD_WIDTH);
		JLabel to = new JLabel("to");
		end = new JTextField(FIELD_WIDTH);
		eventTitle = formatText(eventTitle, "Untitled Event");
		start = formatText(start, "hh:mm");
		end = formatText(end, "hh:mm");
		eventDate.setText((String.valueOf(currentMon + 1)) + "/" + String.valueOf(currentDay) + "/" + String.valueOf(currentYear));

		JButton saveBtn = new JButton("SAVE");
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Event event = new Event(eventTitle.getText(), String.valueOf(currentDay), String.valueOf(currentMon +1),
						String.valueOf(year));
				event.setStartTime(start.getText());
				String endtime;
				if (end.getText().equals("hh:mm"))
					endtime = null;
				else
					endtime = end.getText();
				event.setEndTime(endtime);
				model.loadFile();
				boolean isConflict = false;
				for (int i = 0; i < events.size(); i++) {
					if (events.get(i).isConflict(event) ==true) {
						isConflict = true;
					}
				}

				if (isConflict ==true)
					JOptionPane.showMessageDialog(createFrame, "Please create another event", "Time conflict",
							JOptionPane.WARNING_MESSAGE);
				else {
				model.addEvent(event);
				JOptionPane.showMessageDialog(createFrame, "Event added" , "Success message", JOptionPane.INFORMATION_MESSAGE);
				model.writeToFile();
				
				}
				createFrame.dispose();
				
			}
		});

		// create panels to set up layout
		JPanel panel = new JPanel();
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		panel.add(title);
		panel1.add(eventTitle);
		panel2.add(eventDate);
		panel2.add(start);
		panel2.add(to);
		panel2.add(end);
		panel2.add(saveBtn);

		// add panels to frame
		createFrame.setLayout(new BorderLayout());
		createFrame.add(panel, BorderLayout.NORTH);
		createFrame.add(panel1, BorderLayout.CENTER);
		createFrame.add(panel2, BorderLayout.SOUTH);
		createFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createFrame.setVisible(true);
		createFrame.setSize(200, 100);
		createFrame.pack();

		// return e;
	}

	public JTextField formatText(JTextField tf, final String s) {
		Font defaultFont = new Font("Monospaced", Font.ITALIC, 12);
		Font textFont = new Font("Serif", Font.PLAIN, 13);
		tf.setText(s);
		tf.setFont(defaultFont);
		tf.setForeground(Color.GRAY);
		tf.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (tf.getText().equals(s)) {
					tf.setText("");
					tf.setFont(textFont);
					tf.setForeground(Color.black);
				} else {
					tf.setText(tf.getText());
					tf.setFont(textFont);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (tf.getText().equals(s) || tf.getText().length() == 0) {
					tf.setText(s);
					tf.setFont(defaultFont);
					tf.setForeground(Color.GRAY);
					// tf.setText(null);
				} else {
					tf.setText(tf.getText());
					tf.setFont(textFont);
					tf.setForeground(Color.BLACK);
				}
			}
		});
		return tf;
	}
}// end class
