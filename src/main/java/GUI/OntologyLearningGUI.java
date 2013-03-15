package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import ontologyLearning.ontologyExtraction.searchEngine.DBConstructor;
import ontologyLearning.ontologyExtraction.searchEngine.OntologySearch;
import ontologyLearning.ontologyExtraction.searchEngine.QueryBuildingException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;

public class OntologyLearningGUI extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JTable table1;
	private JTextField conceptTextField;
	private JComboBox attributesComboBox1;
	private JTabbedPane tabbedPane1;

	/*
	 * buttons
	 */
	private JButton matchButton;
	private JButton removeButton;
	private JButton addButton;
	private JButton removeButton2;
	private JButton addButton2;
	private JTextField addAttributeTextField;
	private JTextField addAttributeTextField2;

	private JComboBox sourceOntoComboBox;
	private JList listAttributs;
	private JList listAttributs2;
	private JComboBox targetOntoComboBox;
	/*
	 * tabs
	 */
	private JPanel attributesExtractor;
	private JPanel attributesMatching;
	private JTable matchTable;
	private DefaultTableModel instancesModelTable;
	DefaultTableModel matchTableModel;

	private final ArrayList<String> column = new ArrayList<String>();
	JScrollPane optionPane;

	/**
	 * View params.
	 */
	private Map<String, OntProperty> matchMap = new HashMap<String, OntProperty>();
	private DefaultListModel listModel;
	private DefaultListModel listModel2;

	private void addMatchTableRow(String a, String b) {
		Vector<String> vector = new Vector<String>();
		vector.add(a);
		vector.add(b);
		matchTableModel.addRow(vector);
	}

	/**
	 * Creates all components for the view.
	 */
	private void init() {
		// main container.
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		/*
		 * Building tabs
		 */
		tabbedPane1 = new JTabbedPane();

		attributesExtractor = new JPanel();
		attributesExtractor.setLayout(new BoxLayout(attributesExtractor, BoxLayout.Y_AXIS));

		attributesMatching = new JPanel();

		tabbedPane1.addTab("Attributes Extraction", attributesExtractor);
		tabbedPane1.addTab("Attributes Matching", attributesMatching);

		contentPane.add(tabbedPane1);
		/*
		 * instanciating components.
		 */
		matchTable = new JTable();
		buttonOK = new JButton("OK");

		table1 = new JTable();
		conceptTextField = new JTextField(15);
		attributesComboBox1 = new JComboBox();
		makeNotResizable(attributesComboBox1);
		matchButton = new JButton("Create Onto");
		removeButton = new JButton("Remove");
		addButton = new JButton("Add");

		removeButton2 = new JButton("Remove");
		addButton2 = new JButton("Add");
		sourceOntoComboBox = new JComboBox();
		targetOntoComboBox = new JComboBox();

		instancesModelTable = new DefaultTableModel();
		instancesModelTable.addColumn("Instance");
		instancesModelTable.addColumn("Value");
		addAttributeTextField = new JTextField(10);
		addAttributeTextField2 = new JTextField(10);

		/*
		 * adding components to tabs
		 */

		/*
		 * tab1
		 */
		Box box = Box.createHorizontalBox();
		box.add(conceptTextField);
		conceptTextField.setMaximumSize(conceptTextField.getPreferredSize());
		box.add(buttonOK);
		attributesExtractor.add(box);
		attributesExtractor.add(attributesComboBox1);
		JScrollPane scrollPane = new JScrollPane(table1);
		attributesExtractor.add(scrollPane);

		/*
		 * tab2
		 */
		attributesMatching.setLayout(new BoxLayout(attributesMatching, BoxLayout.Y_AXIS));
		box = Box.createHorizontalBox();
		box.add(Box.createRigidArea(new Dimension(10, 0)));
		box.add(sourceOntoComboBox);
		box.add(Box.createRigidArea(new Dimension(10, 0)));

		// box.add(matchButton);

		makeNotResizable(sourceOntoComboBox);
		box.add(Box.createRigidArea(new Dimension(10, 0)));
		box.add(targetOntoComboBox);
		box.add(Box.createRigidArea(new Dimension(10, 0)));

		makeNotResizable(targetOntoComboBox);
		attributesMatching.add(box);

		// attributesMatching.add(matchTable);
		// attributesMatching.add(Box.createVerticalGlue());
		/*
		 * table components.
		 */
		// JTable matchTable = new JTable();
		//
		// DefaultTableModel modelTable;
		// DefaultTableModel matchTableModel;

		// for (String p : DBConstructor.getSourceProperties()) {
		//
		// dbCombo.addItem(p);
		// }

		listModel = new DefaultListModel();
		listModel2 = new DefaultListModel();
		Box boxAttributs = Box.createHorizontalBox();
		Box subBoxLeft = Box.createVerticalBox();
		Box buttonBox = Box.createHorizontalBox();
		Box buttonBox2 = Box.createHorizontalBox();
		Box subBoxRight = Box.createVerticalBox();

		listAttributs = new JList(listModel);
		listAttributs2 = new JList(listModel2);

		JScrollPane listAttributsScrollPane = new JScrollPane(listAttributs);
		JScrollPane listAttributsScrollPane2 = new JScrollPane(listAttributs2);

		HireListener hireListener = new HireListener(addButton);

		addButton.setActionCommand("Hire");
		addButton.addActionListener(hireListener);
		addButton.setEnabled(false);

		addAttributeTextField.addActionListener(hireListener);
		addAttributeTextField.getDocument().addDocumentListener(hireListener);

		addAttributeTextField2.addActionListener(hireListener);
		addAttributeTextField2.getDocument().addDocumentListener(hireListener);
		// String name =
		// listModel.getElementAt(listAttributs.getSelectedIndex()).toString();

		buttonBox.add(removeButton);
		buttonBox.add(addButton);
		buttonBox.add(addAttributeTextField);
		buttonBox2.add(removeButton2);
		buttonBox2.add(addButton2);
		buttonBox2.add(addAttributeTextField2);

		subBoxLeft.add(listAttributsScrollPane);
		subBoxLeft.add(buttonBox);

		subBoxRight.add(listAttributsScrollPane2);
		subBoxRight.add(buttonBox2);

		boxAttributs.add(subBoxLeft);
		boxAttributs.add(subBoxRight);

		attributesMatching.add(boxAttributs);

		initComboBox2();
		initDbBox();
		attributesMatching.add(matchButton);

	}

	private void makeNotResizable(Component comp) {
		comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, comp.getPreferredSize().height));
	}

	private void initComboBox2() {
		matchMap = DBConstructor.createIsObjectPropertyMap();

		sourceOntoComboBox.removeAllItems();
		for (Map.Entry<String, OntProperty> p : matchMap.entrySet()) {
			// comboBox2.addItem(p.getValue());

			listModel.addElement(p.getValue());

		}
	}

	private void initDbBox() {
		for (String p : DBConstructor.getSourceProperties()) {
			// dbCombo.addItem(p);
			listModel2.addElement(p);
		}

	}

	public OntologyLearningGUI() {
		init();
		// initialise la map de correspondances.

		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		matchTableModel = new DefaultTableModel();
		matchTableModel.addColumn("Source");
		matchTableModel.addColumn("Target");
		matchTable.setModel(matchTableModel);
		/*
		 * ################ Listeners ################
		 */
		/*
		 * OK
		 */
		buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					onOK();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		matchButton.addActionListener(new ActionListener() {

			OntModel baseOntologyModel = null;
			Map<String, OntProperty> isObjectPropertyMap = null;

			@Override
			public void actionPerformed(ActionEvent actionEvent) {

				// DBContructor.addMatch(dbCombo.getSelectedItem().toString(),comboBox2.getSelectedItem().toString());

			}
		});
	}

	private void initDBConstructor() {
		DBConstructor.init();
	}

	private void onOK() throws ParserConfigurationException, IOException, SAXException, TransformerException, QueryBuildingException {
		attributesComboBox1.removeAllItems();
		String text = conceptTextField.getText();

		char c = text.toUpperCase().charAt(0);
		String text2 = c + text.substring(1);
		System.out.println(text2);
		OntologySearch os = new OntologySearch(text2);
		os.getInstanceSet();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;

		docBuilder = dbf.newDocumentBuilder();

		final Document doc

		= docBuilder.parse(new File("docs/InstancesFor" + text + ".xml"));

		NodeList children = doc.getElementsByTagName(text.toLowerCase());

		for (int i = 0; i < children.getLength(); i++) {

			NodeList attributes = children.item(i).getChildNodes();
			for (int j = 0; j < attributes.getLength(); j++) {

				if (!column.contains(attributes.item(j).getNodeName())) {
					column.add(attributes.item(j).getNodeName());
				}

			}

		}

		column.remove("#text");

		System.out.println(column);

		attributesComboBox1.addItem("Select an attribute...");
		for (String s : column) {

			attributesComboBox1.addItem(s);
		}

		attributesComboBox1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				instancesModelTable.removeTableModelListener(table1);
				// table1.removeAll();
				table1.setModel(instancesModelTable);

				if (column.contains(attributesComboBox1.getSelectedItem())) {
					while (instancesModelTable.getRowCount() > 0) {
						instancesModelTable.removeRow(0);
					}

					NodeList n = doc.getElementsByTagName(attributesComboBox1.getSelectedItem().toString());
					for (int i = 0; i < n.getLength(); i++) {
						Vector<String> vector = new Vector<String>();

						vector.add(n.item(i).getParentNode().getAttributes().getNamedItem("name").getTextContent());
						vector.add(n.item(i).getTextContent());
						instancesModelTable.addRow(vector);

					}

				}
			}
		});

		// dispose();
	}

	private void onCancel() {
		// add your code here if necessary
		dispose();
	}

	public static void main(String[] args) {
		OntologyLearningGUI dialog = new OntologyLearningGUI();
		dialog.pack();
		dialog.setVisible(true);
		System.exit(0);
	}

	// This listener is shared by the text field and the hire button.
	class HireListener implements ActionListener, DocumentListener {
		private boolean alreadyEnabled = false;
		private JButton button;

		public HireListener(JButton button) {
			this.button = button;
		}

		// Required by ActionListener.
		@Override
		public void actionPerformed(ActionEvent e) {
			String name = addAttributeTextField.getText();

			// User didn't type in a unique name...
			if (name.equals("") || alreadyInList(name)) {
				Toolkit.getDefaultToolkit().beep();
				addAttributeTextField.requestFocusInWindow();
				addAttributeTextField.selectAll();
				return;
			}

			int index = listAttributs.getSelectedIndex(); // get selected index
			if (index == -1) { // no selection, so insert at beginning
				index = 0;
			} else { // add after the selected item
				index++;
			}

			listModel.insertElementAt(addAttributeTextField.getText(), index);
			// If we just wanted to add to the end, we'd do this:
			// listModel.addElement(employeeName.getText());

			// Reset the text field.
			addAttributeTextField.requestFocusInWindow();
			addAttributeTextField.setText("");

			// Select the new item and make it visible.
			listAttributs.setSelectedIndex(index);
			listAttributs.ensureIndexIsVisible(index);
		}

		class FireListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				// This method can be called only if
				// there's a valid selection
				// so go ahead and remove whatever's selected.
				int index = listAttributs.getSelectedIndex();
				listModel.remove(index);

				int size = listModel.getSize();

				if (size == 0) { // Nobody's left, disable firing.
					removeButton.setEnabled(false);

				} else { // Select an index.
					if (index == listModel.getSize()) {
						// removed item in last position
						index--;
					}

					listAttributs.setSelectedIndex(index);
					listAttributs.ensureIndexIsVisible(index);
				}
			}
		}

		// This method tests for string equality. You could certainly
		// get more sophisticated about the algorithm. For example,
		// you might want to ignore white space and capitalization.
		protected boolean alreadyInList(String name) {
			return listModel.contains(name);
		}

		// Required by DocumentListener.
		@Override
		public void insertUpdate(DocumentEvent e) {
			enableButton();
		}

		// Required by DocumentListener.
		@Override
		public void removeUpdate(DocumentEvent e) {
			handleEmptyTextField(e);
		}

		// Required by DocumentListener.
		@Override
		public void changedUpdate(DocumentEvent e) {
			if (!handleEmptyTextField(e)) {
				enableButton();
			}
		}

		private void enableButton() {
			if (!alreadyEnabled) {
				button.setEnabled(true);
			}
		}

		private boolean handleEmptyTextField(DocumentEvent e) {
			if (e.getDocument().getLength() <= 0) {
				button.setEnabled(false);
				alreadyEnabled = false;
				return true;
			}
			return false;
		}
	}

}
