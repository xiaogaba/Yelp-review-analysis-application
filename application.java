package hw3_scource;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import oracle.net.aso.e;

public class hw3 {

	private Connection mConnection = null;
	
	private boolean mSqlDebug = true;
	
	// GUI objects
	private JFrame mFrame = null;
	Font mDefaultFont = new Font("Consolas", Font.PLAIN, 24);

	JButton mStartButton = new JButton("Start search");
	
	// The search logic drop list.
	JLabel mSearchLogicLabel = new JLabel("The search logic:");
	JComboBox mSearchLogicList = new JComboBox();
	
	// The main category list.
	JLabel mMainCategoryLabel = new JLabel("Main category:");
	JList<CheckboxListItem> mMainCategoryList = null;
	JScrollPane mMainCategoryScroller = new JScrollPane();
	
	// The sub category list.
	JLabel mSubCategoryLabel = new JLabel("Sub category:");
	JList<CheckboxListItem> mSubCategoryList = null;
	JScrollPane mSubCategoryScroller = new JScrollPane();
	
	// The attribute list.
	JLabel mAttributeLabel = new JLabel("Attribute:");
	JList<CheckboxListItem> mAttributeList = null;
	JScrollPane mAttributeScroller = new JScrollPane();
	
	// The open time list.
	JCheckBox mUseTimeInSearchBox = new JCheckBox("Use time in search");
	
	JLabel mDayOfWeekLabel = new JLabel("Day of weeks:");
	JComboBox mDayOfWeekList = new JComboBox();
	JLabel mOpenTimeLabel = new JLabel("Open time:");
	JComboBox mOpenTimeList = new JComboBox();
	JLabel mCloseTimeLabel = new JLabel("Close time:");
	JComboBox mCloseTimeList = new JComboBox();
	
	// The search result table.
	JLabel mSearchResultLabel = new JLabel("Search result:");
	JTable mSearchResultTable = null;
	JScrollPane mSearchResultTableScroller = new JScrollPane();

	// The review dialog
	JDialog mReviewDialog = null;
	JTable mReviewTable = null;
	JScrollPane mReviewTableScroller = new JScrollPane();
	
	// All the current selected data.
	String[] mMainCategorySelectedItems = new String[0];
	String[] mSubCategorySelectedItems = new String[0];
	String[] mAttributeSelectedItems = new String[0];
		
	// The check box
	class CheckboxListItem {
		private String label;
		private boolean isSelected = false;
 
		public CheckboxListItem(String label) {
			this.label = label;
		}
 
		public boolean isSelected() {
			return isSelected;
		}
 
		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}

		public String toString() {
			return label;
		}
	}
	
	class CheckboxListRenderer extends JCheckBox implements
	ListCellRenderer<CheckboxListItem> {
		@Override
		public Component getListCellRendererComponent(
			JList<? extends CheckboxListItem> list, CheckboxListItem value,
			int index, boolean isSelected, boolean cellHasFocus) {
			
			setEnabled(list.isEnabled());
			setSelected(value.isSelected());
			setFont(list.getFont());
			setBackground(list.getBackground());
			setForeground(list.getForeground());
			setText(value.toString());

			return this;
		}
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					hw3 window = new hw3();
					window.mFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	class MainCategoryListMouseAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent event) {
			JList<CheckboxListItem> list = (JList<CheckboxListItem>) event.getSource();
			
			int index = list.locationToIndex(event.getPoint());
			CheckboxListItem clickItem = (CheckboxListItem) list.getModel().getElementAt(index);
			clickItem.setSelected(!clickItem.isSelected());
            list.repaint(list.getCellBounds(index, index));
            
            int selectedCount = 0;
            for (int i = 0; i < mMainCategoryList.getModel().getSize(); ++i) {
            	if (mMainCategoryList.getModel().getElementAt(i).isSelected()) {
            		selectedCount++;
            	}
            }

            mMainCategorySelectedItems = new String[selectedCount]; 
            
            int mainCategoryIndex = 0;
            for (int i = 0; i < mMainCategoryList.getModel().getSize(); ++i) {
            	if (mMainCategoryList.getModel().getElementAt(i).isSelected()) {
            		mMainCategorySelectedItems[mainCategoryIndex] = new String(mMainCategoryList.getModel().getElementAt(i).toString());
            		mainCategoryIndex++;
            	}
            }
            
            refreshAll();
		}
	}
	
	class SubCategoryListMouseAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent event) {
			JList<CheckboxListItem> list = (JList<CheckboxListItem>) event.getSource();
			
			int index = list.locationToIndex(event.getPoint());
			CheckboxListItem clickItem = (CheckboxListItem) list.getModel().getElementAt(index);
			clickItem.setSelected(!clickItem.isSelected());
            list.repaint(list.getCellBounds(index, index));
            
            int selectedCount = 0;
            for (int i = 0; i < mSubCategoryList.getModel().getSize(); ++i) {
            	if (mSubCategoryList.getModel().getElementAt(i).isSelected()) {
            		selectedCount++;
            	}
            }

            mSubCategorySelectedItems = new String[selectedCount]; 
            
            int subCategoryIndex = 0;
            for (int i = 0; i < mSubCategoryList.getModel().getSize(); ++i) {
            	if (mSubCategoryList.getModel().getElementAt(i).isSelected()) {
            		mSubCategorySelectedItems[subCategoryIndex] = new String(mSubCategoryList.getModel().getElementAt(i).toString());
            		subCategoryIndex++;
            	}
            }

            refreshAll();
		}
	}
	
	class AttributeListMouseAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent event) {
			JList<CheckboxListItem> list = (JList<CheckboxListItem>) event.getSource();
			
			int index = list.locationToIndex(event.getPoint());
			CheckboxListItem clickItem = (CheckboxListItem) list.getModel().getElementAt(index);
			clickItem.setSelected(!clickItem.isSelected());
            list.repaint(list.getCellBounds(index, index));
            
            int selectedCount = 0;
            for (int i = 0; i < mAttributeList.getModel().getSize(); ++i) {
            	if (mAttributeList.getModel().getElementAt(i).isSelected()) {
            		selectedCount++;
            	}
            }

            mAttributeSelectedItems = new String[selectedCount]; 
            
            int attributeIndex = 0;
            for (int i = 0; i < mAttributeList.getModel().getSize(); ++i) {
            	if (mAttributeList.getModel().getElementAt(i).isSelected()) {
            		mAttributeSelectedItems[attributeIndex] = new String(mAttributeList.getModel().getElementAt(i).toString());
            		attributeIndex++;
            	}
            }

            refreshAll();
		}
	}
	
	class ResultTableMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent evt) {
			int row = mSearchResultTable.getSelectedRow();
			if (row < 0) {
				return;
			}
			
			String bid = (String)mSearchResultTable.getValueAt(row, 0);
			
			mReviewDialog = new JDialog(mFrame, "Review", true);
			mReviewDialog.setBounds(30, 30, 1800, 1000);
			String[] columnNames = {"Review Date",
					"Stars",
					"Review Text",
					"User Name",
					"Useful votes"};
			
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("SELECT REVIEWDATE, STARS, TEXT, YELP_USER.NAME, USEFUL FROM REVIEW, YELP_USER WHERE BID='")
				.append(bid).append("' AND REVIEW.USER_ID = YELP_USER.USER_ID");
			
			if (mSqlDebug) {
				System.out.println(sqlBuilder.toString());
			}
			
			String[][] tableContents = null;
			int rowCount = 0;
			try {
				PreparedStatement preparedStatement = mConnection.prepareStatement(sqlBuilder.toString(),
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = preparedStatement.executeQuery();
			
				if (rs.last()) {
					rowCount = rs.getRow();
					rs.beforeFirst();
				}
				
				tableContents = new String[rowCount][5];
				for (int i = 0; i < rowCount; ++i) {
					rs.next();
					tableContents[i][0] = rs.getString("REVIEWDATE");
					tableContents[i][1] = rs.getString("STARS");
					tableContents[i][2] = rs.getString("TEXT");
					tableContents[i][3] = rs.getString("NAME");
					tableContents[i][4] = rs.getString("USEFUL");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}

			mReviewTable = new JTable(tableContents, columnNames);
			mReviewTable.setBounds(50,100,1740,940);
			mReviewTable.setFont(mDefaultFont);
			mReviewTable.getTableHeader().setPreferredSize(new Dimension(30,30));
			mReviewTable.getTableHeader().setFont(new Font("Consolas", Font.PLAIN, 12));
			mReviewTable.setRowHeight(30);

			mReviewTableScroller.setViewportView(mReviewTable);
			
			mReviewDialog.getContentPane().add(mReviewTableScroller);
			
			mReviewDialog.setVisible(true);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}		
	}

	class StartButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Object[][] searchResult = doSearch();
			String[] columnNames = {"Business id",
					"address",
					"City",
					"State",
					"Stars",
					"Review #",
					"Check ins #"};
			mSearchResultTable = new JTable(searchResult, columnNames);
			
			mSearchResultTable.setFont(mDefaultFont);
			mSearchResultTable.getTableHeader().setPreferredSize(new Dimension(30,30));
			mSearchResultTable.getTableHeader().setFont(new Font("Consolas", Font.PLAIN, 12));
			mSearchResultTable.setRowHeight(30);
			mSearchResultTable.addMouseListener(new ResultTableMouseListener());

			mSearchResultTableScroller.setViewportView(mSearchResultTable);
		}
	}
	
	/**
	 * Create the application.
	 */
	public hw3() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		connectDB();
		initializeFrame();
		initSearchLogic();
		initStartButton();
		initMainCategoryList();
		initSubCategoryList();
		initAttributeList();
		initTimeCombo();
		initSearchResultTable();
	}

	private void connectDB() {
		// Load the Oracle database driver
		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			
			// Information needed when connecting to a database
			String host = "localhost";
			String port = "1521";
			String dbName = "orcl";
			String userName = "Scott";
			String password = "tiger";

			// JDBC URL construction
			String dbURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
			mConnection = DriverManager.getConnection(dbURL, userName, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void initializeFrame() {
		mFrame = new JFrame("YelpTool");
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mFrame.getContentPane().setLayout(null);
		mFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		mFrame.setVisible(true);
		mFrame.setFont(mDefaultFont);
	}

	private void initSearchLogic() {
		mSearchLogicLabel.setBounds(20, 10, 250, 30);
		mSearchLogicLabel.setFont(mDefaultFont);
		mFrame.getContentPane().add(mSearchLogicLabel);
		
		mSearchLogicList.setBounds(40, 40, 160, 30);
		mSearchLogicList.setFont(mDefaultFont);
		mSearchLogicList.addItem("AND");
		mSearchLogicList.addItem("OR");
		mSearchLogicList.addActionListener (new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				refreshAll();
			}
		});
		mFrame.getContentPane().add(mSearchLogicList);
	}
	
	private void initStartButton() {
		mStartButton.setBounds(300, 20, 1500, 40);
		mStartButton.setFont(mDefaultFont);
		mStartButton.addActionListener(new StartButtonActionListener());
		
		mFrame.getContentPane().add(mStartButton);
	}
	
	private void initMainCategoryList() {
		mMainCategoryLabel.setBounds(20, 80, 400, 20);
		mMainCategoryLabel.setFont(mDefaultFont);
		mFrame.getContentPane().add(mMainCategoryLabel);
		
		String[] mainCategories = QueryMainCategories();
		
		CheckboxListItem[] checkboxListItems = new CheckboxListItem[mainCategories.length];
		
		for (int i = 0; i < mainCategories.length; ++i) {
			checkboxListItems[i] = new CheckboxListItem(mainCategories[i]);
		}
		mMainCategoryList = new JList<CheckboxListItem>(checkboxListItems);
		mMainCategoryList.setCellRenderer(new CheckboxListRenderer());
		mMainCategoryList.setBounds(20, 110, 400, 750);
		mMainCategoryList.addMouseListener(new MainCategoryListMouseAdapter());
		mMainCategoryList.setFont(mDefaultFont);
		mMainCategoryList.setBackground(new Color(177, 196,221));

		mMainCategoryScroller.setBounds(20, 110, 430, 750);
		mMainCategoryScroller.setViewportView(mMainCategoryList);
		
		mFrame.getContentPane().add(mMainCategoryScroller);
	}
	
	private void initSubCategoryList() {
		mSubCategoryLabel.setBounds(470, 80, 400, 20);
		mSubCategoryLabel.setFont(mDefaultFont);
		mFrame.getContentPane().add(mSubCategoryLabel);

		mSubCategoryScroller.setBounds(470, 110, 430, 750);
		mSubCategoryScroller.setBackground(new Color(136, 206, 234));
		mFrame.getContentPane().add(mSubCategoryScroller);
	}
	
	private void initAttributeList() {
		mAttributeLabel.setBounds(920, 80, 400, 20);
		mAttributeLabel.setFont(mDefaultFont);
		mFrame.getContentPane().add(mAttributeLabel);

		mAttributeScroller.setBounds(920, 110, 430, 750);
		mFrame.getContentPane().add(mAttributeScroller);
	}
	
	private void initTimeCombo() {
		// Whether using time in search or not.
		mUseTimeInSearchBox.setBounds(20, 890, 400, 30);
		mUseTimeInSearchBox.setFont(mDefaultFont);
		mFrame.getContentPane().add(mUseTimeInSearchBox);
		
		// Day of week.
		mDayOfWeekLabel.setBounds(20, 940, 200, 30);
		mDayOfWeekLabel.setFont(mDefaultFont);
		mFrame.getContentPane().add(mDayOfWeekLabel);
		
		mDayOfWeekList.setBounds(220, 940, 160, 30);
		mDayOfWeekList.setFont(mDefaultFont);
		mFrame.getContentPane().add(mDayOfWeekList);
		
		// Open time
		mOpenTimeLabel.setBounds(470, 940, 200, 30);
		mOpenTimeLabel.setFont(mDefaultFont);
		mFrame.getContentPane().add(mOpenTimeLabel);

		mOpenTimeList.setBounds(670, 940, 160, 30);
		mOpenTimeList.setFont(mDefaultFont);
		mFrame.getContentPane().add(mOpenTimeList);
		
		// Close time
		mCloseTimeLabel.setBounds(920, 940, 200, 30);
		mCloseTimeLabel.setFont(mDefaultFont);
		mFrame.getContentPane().add(mCloseTimeLabel);

		mCloseTimeList.setBounds(1120, 940, 160, 30);
		mCloseTimeList.setFont(mDefaultFont);
		mFrame.getContentPane().add(mCloseTimeList);
	}
	
	private void initSearchResultTable() {
		mSearchResultLabel.setBounds(1380, 80, 500, 20);
		mSearchResultLabel.setFont(mDefaultFont);
		mFrame.getContentPane().add(mSearchResultLabel);
		
		String[] columnNames = {"Business id",
			"address",
			"City",
			"State",
			"Stars",
			"Review #",
			"Check ins #"};
		Object[][] data = {{"", "", "", "", "", "", ""}};

		mSearchResultTable = new JTable(data, columnNames);
		mSearchResultTable.setBounds(1380, 110, 500, 750);
		mSearchResultTable.setFont(mDefaultFont);
		mSearchResultTable.getTableHeader().setPreferredSize(new Dimension(30,30));
		mSearchResultTable.getTableHeader().setFont(new Font("Consolas", Font.PLAIN, 12));
		mSearchResultTable.setRowHeight(30);
		
		mSearchResultTableScroller.setBounds(1380, 110, 530, 750);
		mSearchResultTableScroller.setViewportView(mSearchResultTable);
		
		mFrame.getContentPane().add(mSearchResultTableScroller);
	}
	
	private String[] QueryMainCategories() {
		StringBuilder sqlBuilder = new StringBuilder();
		PreparedStatement preparedStatement;
		ResultSet rs;
		String[] mainCategories = null;
		int rowCount = 0;
 
		// SQL command
		sqlBuilder.append("SELECT DISTINCT mainCategory").append("\n")
			.append("FROM MainCategory").append("\n")
			.append("ORDER BY mainCategory");

		try {
			preparedStatement = mConnection.prepareStatement(sqlBuilder.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = preparedStatement.executeQuery();

			if (rs.last()) {
				rowCount = rs.getRow();
				rs.beforeFirst();
			}
			mainCategories = new String[rowCount];
			for (int i = 0; i < rowCount; ++i) {
				rs.next();
				mainCategories[i] = new String(rs.getString(rs.findColumn("mainCategory")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return mainCategories;
	}
	
	private void refreshAll() {
		recreateSubCategoryList();
		recreateAttributeList();
		recreateOpenDayOfWeek();
		recreateOpenTime();
		recreateCloseTime();
	}
	
	public void recreateSubCategoryList() {
		if (mMainCategorySelectedItems.length == 0) {
			mSubCategoryScroller.setViewportView(null);
        	return;
		}
		String[] subCategories = querySubCategories();

		// Select the items that were selected and are still available
		int remainSelectedItemsCount = 0;

		CheckboxListItem[] checkboxListItems = new CheckboxListItem[subCategories.length];
		for (int i = 0; i < subCategories.length; ++i) {
			checkboxListItems[i] = new CheckboxListItem(subCategories[i]);
			for (String s : mSubCategorySelectedItems) {
				if (s.equals(subCategories[i])) {
					checkboxListItems[i].setSelected(true);
					remainSelectedItemsCount++;
				}
			}
		}

		String[] newSubCategorySelectedItems = new String[remainSelectedItemsCount];
		int i = 0;
		if (mSubCategorySelectedItems != null && subCategories != null) {
			for (String subCategory : subCategories) {
				for (String selectedItem: mSubCategorySelectedItems) {
					if (subCategory.equals(selectedItem)) {
						newSubCategorySelectedItems[i] = new String(subCategory);
						++i;
					}
				}
			}
		}
		mSubCategorySelectedItems = newSubCategorySelectedItems;
		
		mSubCategoryList = new JList<CheckboxListItem>(checkboxListItems);
		
		mSubCategoryList.setCellRenderer(new CheckboxListRenderer());
		mSubCategoryList.addMouseListener(new SubCategoryListMouseAdapter());
		mSubCategoryList.setFont(mDefaultFont);
		mSubCategoryList.setBackground(new Color(136, 206, 234));

		mSubCategoryScroller.setViewportView(mSubCategoryList);

		mFrame.revalidate();
		mFrame.repaint();
	}
	
	private String[] querySubCategories() {
		StringBuilder sqlBuilder = new StringBuilder();
		PreparedStatement preparedStatement;
		ResultSet rs;
		String[] subCategories = null;
		int rowCount = 0;
		
		sqlBuilder.append("SELECT DISTINCT subCategory").append("\n")
			.append("FROM MainCategory main,SubCategory sub").append("\n")
			.append("WHERE (");
	
		for (int i = 0; i < mMainCategorySelectedItems.length; ++i) {
			sqlBuilder.append("main.mainCategory='")
				.append(mMainCategorySelectedItems[i])
				.append("'");
			if (i < mMainCategorySelectedItems.length - 1) {
				sqlBuilder.append(" ").append(mSearchLogicList.getSelectedItem())
					.append(" ");
			}
		}

		sqlBuilder.append(") AND main.bid=sub.bid").append("\n")
			.append("ORDER BY subCategory");

		try {
			preparedStatement = mConnection.prepareStatement(sqlBuilder.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
			rs = preparedStatement.executeQuery();
			
			if (rs.last()) {
				rowCount = rs.getRow();
				rs.beforeFirst();
			}
			
			subCategories = new String[rowCount];
			for (int i = 0; i < rowCount; ++i) {
				rs.next();
				subCategories[i] = new String(rs.getString(rs.findColumn("subCategory")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return subCategories;
	}
	
	public void recreateAttributeList() {
		if (mSubCategorySelectedItems == null || mSubCategorySelectedItems.length == 0) {
			mAttributeScroller.setViewportView(null);
        	return;
		}
		String[] attributes = queryAttributes();
		
		int remainSelectedItemsCount = 0;

		CheckboxListItem[] checkboxListItems = new CheckboxListItem[attributes.length];
		for (int i = 0; i < attributes.length; ++i) {
			checkboxListItems[i] = new CheckboxListItem(attributes[i]);
			if (mAttributeSelectedItems != null) {
				for (String s : mAttributeSelectedItems) {
					if (s.equals(attributes[i])) {
						checkboxListItems[i].setSelected(true);
						remainSelectedItemsCount++;
					}
				}
			}
		}
		
		String[] newAttributeSelectedItems = new String[remainSelectedItemsCount];
		int i = 0;
		if (mAttributeSelectedItems != null && attributes != null) {
			for (String attr : attributes) {
				for (String selectedItem: mAttributeSelectedItems) {
					if (attr.equals(selectedItem)) {
						newAttributeSelectedItems[i] = new String(attr);
						++i;
					}
				}
			}
		}
		mAttributeSelectedItems = newAttributeSelectedItems;
		
		mAttributeList = new JList<CheckboxListItem>(checkboxListItems);
		
		mAttributeList.setCellRenderer(new CheckboxListRenderer());
		mAttributeList.addMouseListener(new AttributeListMouseAdapter());
		mAttributeList.setFont(mDefaultFont);
		mAttributeList.setBackground(new Color(176, 239, 240));

		mAttributeScroller.setViewportView(mAttributeList);

		mFrame.revalidate();
		mFrame.repaint();
	}
	
	private String[] queryAttributes() {
		StringBuilder sqlBuilder = new StringBuilder();
		PreparedStatement preparedStatement;
		ResultSet rs;
		String[] attributes = null;
		int rowCount = 0;
		
		sqlBuilder.append("SELECT DISTINCT attribute").append("\n")
			.append("FROM SubCategory sub,attribute attr").append("\n")
			.append("WHERE (");
	
		for (int i = 0; i < mSubCategorySelectedItems.length; ++i) {
			sqlBuilder.append("sub.subCategory='")
				.append(mSubCategorySelectedItems[i])
				.append("'");
			if (i < mSubCategorySelectedItems.length - 1) {
				sqlBuilder.append(" ").append(mSearchLogicList.getSelectedItem())
					.append(" ");
			}
		}
		
		sqlBuilder.append(") AND sub.bid=attr.bid").append("\n")
			.append("ORDER BY attribute");

		try {
			preparedStatement = mConnection.prepareStatement(sqlBuilder.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
			rs = preparedStatement.executeQuery();
			
			if (rs.last()) {
				rowCount = rs.getRow();
				rs.beforeFirst();
			}
			
			attributes = new String[rowCount];
			for (int i = 0; i < rowCount; ++i) {
				rs.next();
				attributes[i] = new String(rs.getString(rs.findColumn("attribute")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return attributes;
	}
	
	private void recreateOpenDayOfWeek() {
		String[] dayOfWeeks = queryDayOfWeeks();
		
		String curSelectedItem = (String)mDayOfWeekList.getSelectedItem();
		
		mDayOfWeekList.removeAllItems();
		
		for (String day : dayOfWeeks) {
			mDayOfWeekList.addItem(day);
		}
		
		if (curSelectedItem != null) {
			mDayOfWeekList.setSelectedItem(curSelectedItem);
		}
	}
	
	private void recreateOpenTime() {
		String[] openTimes = queryTimes(true);
		
		String curSelectedItem = (String)mOpenTimeList.getSelectedItem();

		mOpenTimeList.removeAllItems();

		for (String time : openTimes) {
			mOpenTimeList.addItem(time);
		}

		if (curSelectedItem != null) {
			mOpenTimeList.setSelectedItem(curSelectedItem);
		}
	}
	
	private void recreateCloseTime() {
		String[] closeTimes = queryTimes(false);
		
		String curSelectedItem = (String)mCloseTimeList.getSelectedItem();

		mCloseTimeList.removeAllItems();

		for (String time : closeTimes) {
			mCloseTimeList.addItem(time);
		}

		if (curSelectedItem != null) {
			mCloseTimeList.setSelectedItem(curSelectedItem);
		}
	}
	
	private String[] queryDayOfWeeks() {
		if (mMainCategorySelectedItems.length == 0) {
			return new String[0];
		}
		PreparedStatement preparedStatement;
		ResultSet rs;

		String[] fieldNames = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
		Boolean[] openOnDayOfWeek = new Boolean[7];
		int count = 0;
		
		for (int i = 0; i < fieldNames.length; ++i) {
			StringBuilder sqlBuilder = new StringBuilder();
			
			if (mAttributeSelectedItems.length != 0) {
				sqlBuilder.append("SELECT COUNT(Business.bid)").append("\n")
					.append("FROM Business,attribute").append("\n")
					.append("WHERE (");
				for (int j = 0; j < mAttributeSelectedItems.length; ++j) {
					sqlBuilder.append("attribute.attribute = '")
						.append(mAttributeSelectedItems[j])
						.append("'");
					if (j < mAttributeSelectedItems.length - 1) {
						sqlBuilder.append(" ").append(mSearchLogicList.getSelectedItem())
							.append(" ");
					}
				}
				sqlBuilder.append(") AND Business.BID = attribute.BID AND Business.")
					.append(fieldNames[i]).append("_OPEN != -1");
			} else if (mSubCategorySelectedItems.length != 0) {
				sqlBuilder.append("SELECT COUNT(Business.bid)").append("\n")
					.append("FROM Business,SubCategory").append("\n")
					.append("WHERE (");
				for (int j = 0; j < mSubCategorySelectedItems.length; ++j) {
					sqlBuilder.append("SubCategory.subCategory = '")
						.append(mSubCategorySelectedItems[j])
						.append("'");
					if (j < mSubCategorySelectedItems.length - 1) {
						sqlBuilder.append(" ").append(mSearchLogicList.getSelectedItem())
							.append(" ");
					}
				}
				sqlBuilder.append(") AND Business.BID = SubCategory.BID AND Business.")
					.append(fieldNames[i]).append("_OPEN != -1");
			} else {
				sqlBuilder.append("SELECT COUNT(Business.bid)").append("\n")
					.append("FROM Business,MainCategory").append("\n")
					.append("WHERE (");
				for (int j = 0; j < mMainCategorySelectedItems.length; ++j) {
					sqlBuilder.append("MainCategory.mainCategory = '")
						.append(mMainCategorySelectedItems[j])
						.append("'");
					if (j < mMainCategorySelectedItems.length - 1) {
						sqlBuilder.append(" ").append(mSearchLogicList.getSelectedItem())
							.append(" ");
					}
				}
				sqlBuilder.append(") AND Business.BID = MainCategory.BID AND Business.")
					.append(fieldNames[i]).append("_OPEN != -1");
			}
			
			try {
				preparedStatement = mConnection.prepareStatement(sqlBuilder.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = preparedStatement.executeQuery();
				if (rs.next()) {
					if (rs.getInt(rs.findColumn("COUNT(Business.bid)")) != 0) {
						openOnDayOfWeek[i] = true;
						++count;
					} else {
						openOnDayOfWeek[i] = false;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		String[] openDayOfWeeks = new String[count];
		String[] weekDayName = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		int idx = 0;
		for (int i = 0; i < 7; ++i) {
			if (openOnDayOfWeek[i]) {
				openDayOfWeeks[idx] = new String(weekDayName[i]);
				idx++;
			}
		}
		return openDayOfWeeks;
	}
	
	private String[] queryTimes(boolean isOpenTime) {
		String selectedDayOfWeek = (String)mDayOfWeekList.getSelectedItem();
		if (selectedDayOfWeek == null) {
			return new String[0];
		}

		String[] dayOfWeekToField = {
				"Sunday", "SUN_OPEN", "SUN_CLOSE",
				"Monday", "MON_OPEN", "MON_CLOSE",
				"Tuesday", "TUE_OPEN", "TUE_CLOSE",
				"Wednesday", "WED_OPEN", "WED_CLOSE",
				"Thursday", "THU_OPEN", "THU_CLOSE",
				"Friday", "FRI_OPEN", "FRI_CLOSE",
				"Saturday", "SAT_OPEN", "SAT_CLOSE"};
		String fieldName = null;
		for (int i = 0; i < dayOfWeekToField.length; i += 3) {
			if (selectedDayOfWeek.equals(dayOfWeekToField[i])) {
				if (isOpenTime) {
					fieldName = dayOfWeekToField[i + 1];
				} else {
					fieldName = dayOfWeekToField[i + 2];
				}
				break;
			}
		}
		
		StringBuilder sqlBuilder = new StringBuilder();
		
		if (mAttributeSelectedItems.length != 0) {
			sqlBuilder.append("SELECT DISTINCT ").append(fieldName).append("\n")
				.append("FROM Business,attribute").append("\n")
				.append("WHERE (");
			for (int j = 0; j < mAttributeSelectedItems.length; ++j) {
				sqlBuilder.append("attribute.attribute = '")
					.append(mAttributeSelectedItems[j])
					.append("'");
				if (j < mAttributeSelectedItems.length - 1) {
					sqlBuilder.append(" ").append(mSearchLogicList.getSelectedItem())
						.append(" ");
				}
			}
			sqlBuilder.append(") AND Business.BID = attribute.BID AND Business.")
				.append(fieldName).append(" != -1 ORDER BY ").append(fieldName);
		} else if (mSubCategorySelectedItems.length != 0) {
			sqlBuilder.append("SELECT DISTINCT ").append(fieldName).append("\n")
				.append("FROM Business,SubCategory").append("\n")
				.append("WHERE (");
			for (int j = 0; j < mSubCategorySelectedItems.length; ++j) {
				sqlBuilder.append("SubCategory.SubCategory = '")
					.append(mSubCategorySelectedItems[j])
					.append("'");
				if (j < mSubCategorySelectedItems.length - 1) {
					sqlBuilder.append(" ").append(mSearchLogicList.getSelectedItem())
						.append(" ");
				}
			}
			sqlBuilder.append(") AND Business.BID = SubCategory.BID AND Business.")
				.append(fieldName).append(" != -1 ORDER BY ").append(fieldName);
		} else if (mMainCategorySelectedItems.length != 0) {
			sqlBuilder.append("SELECT DISTINCT ").append(fieldName).append("\n")
				.append("FROM Business,mainCategory").append("\n")
				.append("WHERE (");
			for (int j = 0; j < mMainCategorySelectedItems.length; ++j) {
				sqlBuilder.append("mainCategory.mainCategory = '")
					.append(mMainCategorySelectedItems[j])
					.append("'");
				if (j < mMainCategorySelectedItems.length - 1) {
					sqlBuilder.append(" ").append(mSearchLogicList.getSelectedItem())
						.append(" ");
				}
			}
			sqlBuilder.append(") AND Business.BID = mainCategory.BID AND Business.")
				.append(fieldName).append(" != -1 ORDER BY ").append(fieldName);
		}
		
		System.out.println("queryTimes.sql: " + sqlBuilder.toString());
		
		String[] times = null;
		
		try {
			PreparedStatement preparedStatement = mConnection.prepareStatement(sqlBuilder.toString(),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = preparedStatement.executeQuery();
			int rowCount = 0;
			if (rs.last()) {
				rowCount = rs.getRow();
				rs.beforeFirst();
			}
			
			times = new String[rowCount];
			for (int i = 0; i < rowCount; ++i) {
				rs.next();
				int openTimeHour = rs.getInt(rs.findColumn(fieldName)) / 100;
				int openTimeMin = rs.getInt(rs.findColumn(fieldName)) % 100;
				StringBuilder timeBuilder = new StringBuilder();
				if (openTimeHour < 10) {
					timeBuilder.append('0');
				}
				timeBuilder.append(openTimeHour).append(":");
				
				if (openTimeMin < 10) {
					timeBuilder.append("0");
				}
				timeBuilder.append(openTimeMin);

				times[i] = new String(timeBuilder.toString());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return times;
	}
	
	private String[][] doSearch() {
		if (mMainCategorySelectedItems.length == 0) {
			String[][] emptyResult = new String[1][7];
			return emptyResult;
		}
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT DISTINCT Business.bid, Business.ADDRESS, Business.City, Business.State, Business.Rate, Business.Review_Count, YELP_CHECKIN.CHECKIN").append("\n")
			.append("FROM BUSINESS,YELP_CHECKIN");
		if (mMainCategorySelectedItems.length != 0) {
			sqlBuilder.append(",MainCategory");
		}
		if (mSubCategorySelectedItems.length != 0) {
			sqlBuilder.append(",SubCategory");
		}
		if (mAttributeSelectedItems.length != 0) {
			sqlBuilder.append(",Attribute");
		}

		sqlBuilder.append(" WHERE").append("\n");
		
		boolean needAnd = false;
		if (mMainCategorySelectedItems.length != 0) {
			sqlBuilder.append("(");
			for (int i = 0; i < mMainCategorySelectedItems.length; ++i) {
				sqlBuilder.append("mainCategory.mainCategory = '")
					.append(mMainCategorySelectedItems[i])
					.append("'");
				if (i < mMainCategorySelectedItems.length - 1) {
					sqlBuilder.append(" ").append(mSearchLogicList.getSelectedItem())
						.append(" ");
				}
			}
			sqlBuilder.append(") AND Business.BID = mainCategory.BID").append("\n");
			needAnd = true;
		}
		
		if (mSubCategorySelectedItems.length != 0) {
			if (needAnd) {
				sqlBuilder.append(" AND ");
			}
			sqlBuilder.append("(");
			for (int i = 0; i < mSubCategorySelectedItems.length; ++i) {
				sqlBuilder.append("subCategory.subCategory = '")
					.append(mSubCategorySelectedItems[i])
					.append("'");
				if (i < mSubCategorySelectedItems.length - 1) {
					sqlBuilder.append(" ").append(mSearchLogicList.getSelectedItem())
						.append(" ");
				}
			}
			sqlBuilder.append(") AND Business.BID = subCategory.BID").append("\n");
			needAnd = true;
		}
		
		if (mAttributeSelectedItems.length != 0) {
			if (needAnd) {
				sqlBuilder.append(" AND ");
			}
			sqlBuilder.append("(");
			for (int i = 0; i < mAttributeSelectedItems.length; ++i) {
				sqlBuilder.append("Attribute.Attribute = '")
					.append(mAttributeSelectedItems[i])
					.append("'");
				if (i < mAttributeSelectedItems.length - 1) {
					sqlBuilder.append(" ").append(mSearchLogicList.getSelectedItem())
						.append(" ");
				}
			}
			sqlBuilder.append(") AND Business.BID = Attribute.BID").append("\n");
			needAnd = true;
		}
		
		String weekDayPrefix = null;
		if (mDayOfWeekList.getSelectedItem() != null && mUseTimeInSearchBox.isSelected()) {
			String[] prefixList = {
					"Sunday", "SUN_",
					"Monday", "MON_",
					"Tuesday", "TUE_",
					"Wednesday", "WED_",
					"Thursday", "THU_",
					"Friday", "FRI_",
					"Saturday", "SAT_"
			};
			for (int i = 0; i < prefixList.length; i += 2) {
				if (mDayOfWeekList.getSelectedItem().toString().equals(prefixList[i])) {
					weekDayPrefix = new String(prefixList[i + 1]);
				}
			}
		}

		if (mOpenTimeList.getSelectedItem() != null && mUseTimeInSearchBox.isSelected()) {
			if (needAnd) {
				sqlBuilder.append(" AND (");
			}
			
			String minOpenTime = mOpenTimeList.getSelectedItem().toString();
			sqlBuilder.append("BUSINESS.").append(weekDayPrefix).append("OPEN > ")
				.append(minOpenTime.charAt(0)).append(minOpenTime.charAt(1))
				.append(minOpenTime.charAt(3)).append(minOpenTime.charAt(4))
				.append(")");

			needAnd = true;
		}
		
		if (mCloseTimeList.getSelectedItem() != null && mUseTimeInSearchBox.isSelected()) {
			if (needAnd) {
				sqlBuilder.append(" AND (");
			}
			
			String maxCloseTime = mCloseTimeList.getSelectedItem().toString();
			sqlBuilder.append("BUSINESS.").append(weekDayPrefix).append("CLOSE < ")
				.append(maxCloseTime.charAt(0)).append(maxCloseTime.charAt(1))
				.append(maxCloseTime.charAt(3)).append(maxCloseTime.charAt(4))
				.append(")");

			needAnd = true;
		}
		
		if (needAnd) {
			sqlBuilder.append(" AND ");
		}
		sqlBuilder.append(" Business.BID = YELP_CHECKIN.BID");

		System.out.println("sql: " + sqlBuilder.toString());
		
		String[][] resultTable = null;
		
		try {
			PreparedStatement preparedStatement = mConnection.prepareStatement(sqlBuilder.toString(),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = preparedStatement.executeQuery();
			int rowCount = 0;
			if (rs.last()) {
				rowCount = rs.getRow();
				rs.beforeFirst();
			}
			
			resultTable = new String[rowCount][7];
			for (int i = 0; i < rowCount; ++i) {
				rs.next();
				resultTable[i][0] = rs.getString("BID");
				resultTable[i][1] = rs.getString("Address");
				resultTable[i][2] = rs.getString("CITY");
				resultTable[i][3] = rs.getString("STATE");
				resultTable[i][4] = rs.getString("Rate");
				resultTable[i][5] = rs.getString("Review_Count");
				resultTable[i][6] = rs.getString("CHECKIN");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resultTable;
	}
	
}

