import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DynamicBranchDashboard extends JFrame {

    private JTree tree;
    private JPanel cardContainer;
    private CardLayout cardLayout;
    private JTabbedPane topTabBar;

    public DynamicBranchDashboard() {
        super("Report Branch Tracker & Dynamic Navigation");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Top Bar / Tab Bar for Quick Branch Switching
        topTabBar = new JTabbedPane();
        topTabBar.addTab("Sales Reports", null);
        topTabBar.addTab("Finance Reports", null);
        topTabBar.addTab("HR Reports", null);

        topTabBar.addChangeListener(e -> {
            int selectedIndex = topTabBar.getSelectedIndex();
            String title = topTabBar.getTitleAt(selectedIndex);
            cardLayout.show(cardContainer, title);
        });
        add(topTabBar, BorderLayout.NORTH);

        // 2. Build JTree for Branch Hierarchy
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Main Reports");

        DefaultMutableTreeNode salesNode = new DefaultMutableTreeNode("Sales Reports");
        salesNode.add(new DefaultMutableTreeNode("Daily Sales"));
        salesNode.add(new DefaultMutableTreeNode("Monthly Sales"));

        DefaultMutableTreeNode financeNode = new DefaultMutableTreeNode("Finance Reports");
        financeNode.add(new DefaultMutableTreeNode("Expense Tracker"));
        financeNode.add(new DefaultMutableTreeNode("Profit & Loss"));

        DefaultMutableTreeNode hrNode = new DefaultMutableTreeNode("HR Reports");
        hrNode.add(new DefaultMutableTreeNode("Attendance"));
        hrNode.add(new DefaultMutableTreeNode("Payroll"));

        root.add(salesNode);
        root.add(financeNode);
        root.add(hrNode);

        tree = new JTree(root);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        // 3. Right Container with CardLayout for dynamic buttons & views
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);

        // Add sub-panels for each branch / node
        cardContainer.add(createBranchPanel("Sales Reports", new String[]{"Export Daily CSV", "View Sales Chart", "Print Sales Summary"}), "Sales Reports");
        cardContainer.add(createBranchPanel("Daily Sales", new String[]{"Download Today Report", "Filter by Region"}), "Daily Sales");
        cardContainer.add(createBranchPanel("Monthly Sales", new String[]{"Compare Months", "Export PDF"}), "Monthly Sales");

        cardContainer.add(createBranchPanel("Finance Reports", new String[]{"Generate Balance Sheet", "Audit Log", "Tax Summary"}), "Finance Reports");
        cardContainer.add(createBranchPanel("Expense Tracker", new String[]{"Add Expense", "Approve Claims"}), "Expense Tracker");
        cardContainer.add(createBranchPanel("Profit & Loss", new String[]{"Q1 Report", "Annual Projection"}), "Profit & Loss");

        cardContainer.add(createBranchPanel("HR Reports", new String[]{"Daily Punch Log", "Leave Requests", "Generate Payslips"}), "HR Reports");
        cardContainer.add(createBranchPanel("Attendance", new String[]{"Biometric Sync", "Mark Absent"}), "Attendance");
        cardContainer.add(createBranchPanel("Payroll", new String[]{"Process Salary", "Bank Transfer Sheet"}), "Payroll");

        // Default Welcome / Root Panel
        cardContainer.add(createBranchPanel("Main Reports", new String[]{"Refresh All Data", "System Settings"}), "Main Reports");

        // 4. Tree Selection Listener: Switch Panel on click
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode == null) return;

            String nodeName = selectedNode.getUserObject().toString();
            cardLayout.show(cardContainer, nodeName);

            // Sync top tab if matching main branch
            for (int i = 0; i < topTabBar.getTabCount(); i++) {
                if (topTabBar.getTitleAt(i).equalsIgnoreCase(nodeName)) {
                    topTabBar.setSelectedIndex(i);
                    break;
                }
            }
        });

        // 5. Left & Right Split Layout
        JScrollPane treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setPreferredSize(new Dimension(250, 500));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, cardContainer);
        splitPane.setDividerLocation(250);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createBranchPanel(String branchTitle, String[] actionButtons) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("Current Branch: " + branchTitle, JLabel.LEFT);
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(header, BorderLayout.NORTH);

        JPanel buttonGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        for (String btnText : actionButtons) {
            JButton btn = new JButton(btnText);
            btn.setPreferredSize(new Dimension(180, 40));
            btn.addActionListener((ActionEvent e) -> {
                JOptionPane.showMessageDialog(this, "Executed: " + btnText + " (" + branchTitle + ")");
            });
            buttonGrid.add(btn);
        }

        panel.add(buttonGrid, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DynamicBranchDashboard().setVisible(true);
        });
    }
}
