import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;


import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by pickl on 2/1/2018.
 */
public class OptionPanel extends JPanel {
    JComboBox comboTeamListForSoloPanel, comboVariablesForComparePanel, comboVariablesForSoloPanel, comboVariablesForSoloPanel2 = null;

    public OptionPanel(String[] teams, String[] variables, JPanel panel){

        JPanel topContainer = new JPanel();
        JPanel createGraphPanel = new JPanel();
        JPanel soloPanel = new JPanel(new GridLayout(0, 1));
        JPanel averagesPanel = new JPanel(new GridLayout(0, 1));
        JPanel comparePanel = new JPanel(new GridLayout(0, 1));
        createGraphPanel.setLayout(new GridLayout(0,3));
        /*createGraphPanel.add(noEditText("Solo"));
        createGraphPanel.add(noEditText("Averages"));
        createGraphPanel.add(noEditText("Compare"));*/

        comboTeamListForSoloPanel = new JComboBox();
        comboVariablesForComparePanel = new JComboBox();
        comboVariablesForSoloPanel = new JComboBox();
        comboVariablesForSoloPanel2 = new JComboBox();
        //createGraphPanel.add(comboTeamListForSoloPanel);
        JTextField teamAverages = new JTextField("");
        teamAverages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] teams = teamAverages.getText().toString().split(",");
                panel.add((new AveragesPanel(teams, panel)).chartPanel);
                panel.validate();
                repaint();
            }
        });
        /*createGraphPanel.add(jTextField);//todo
        createGraphPanel.add(comboVariablesForComparePanel);
        createGraphPanel.add(comboVariablesForSoloPanel);
        createGraphPanel.add(noEditText(""));
        createGraphPanel.add(noEditText(""));
        createGraphPanel.add(comboVariablesForSoloPanel2);*/

        JButton deleteButton = new JButton("Delete Disabled");
        deleteButton.setBackground(Color.RED);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Global.delete){
                    deleteButton.setText("Delete Disabled");
                    deleteButton.setBackground(Color.RED);
                    Global.delete = !Global.delete;
                }
                else {
                    deleteButton.setText("Delete Activated");
                    deleteButton.setBackground(Color.GREEN);
                    Global.delete = !Global.delete;
                }
            }
        });
        deleteButton.setPreferredSize(new Dimension(140, 80));
        soloPanel.add(new JLabel("Solo"));
        soloPanel.add(comboTeamListForSoloPanel);
        soloPanel.add(comboVariablesForSoloPanel);
        soloPanel.add(comboVariablesForSoloPanel2);
        averagesPanel.add(new JLabel("Averages"));
        averagesPanel.add(teamAverages);
        averagesPanel.add(new JLabel());
        averagesPanel.add(new JLabel());
        comparePanel.add(new JLabel("Compare"));
        comparePanel.add(comboVariablesForComparePanel);
        comparePanel.add(new JLabel());
        comparePanel.add(new JLabel());
        createGraphPanel.add(soloPanel);
        createGraphPanel.add(averagesPanel);
        createGraphPanel.add(comparePanel);

        topContainer.add(createGraphPanel);
        topContainer.add(deleteButton);
        add(topContainer);
        SwingUtilities.invokeLater(new Runnable() {//must use with autocomplete
            public void run() {
                for(int i=0;i<teams.length;i++)
                    teams[i] = teams[i].split("m")[1];
                AutoCompleteSupport.install(comboTeamListForSoloPanel, GlazedLists.eventListOf(teams));
                AutoCompleteSupport.install(comboVariablesForComparePanel, GlazedLists.eventListOf(variables));
                AutoCompleteSupport.install(comboVariablesForSoloPanel, GlazedLists.eventListOf(variables));
                AutoCompleteSupport.install(comboVariablesForSoloPanel2, GlazedLists.eventListOf(variables));
                comboVariablesForComparePanel.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent event) {
                        if (event.getKeyChar() == KeyEvent.VK_ENTER) {
                            if (!((JTextComponent) ((JComboBox) ((Component) event
                                    .getSource()).getParent()).getEditor()
                                    .getEditorComponent()).getText().isEmpty()){
                                try{
                                    panel.add((new NickCromparePanel((String) comboVariablesForComparePanel.getSelectedItem(), panel)
                                            .chartPanel));
                                    panel.validate();
                                    repaint();

                                }catch (Exception c){
                                    c.printStackTrace();
                                    JOptionPane pane = new JOptionPane();
                                    pane.showMessageDialog(null, "Fill in the variables correctly",
                                            "Error", JOptionPane.ERROR_MESSAGE);}
                            }
                        }
                    }
                });
                comboVariablesForSoloPanel.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent event) {
                        if (event.getKeyChar() == KeyEvent.VK_ENTER) {
                            if (!((JTextComponent) ((JComboBox) ((Component) event
                                    .getSource()).getParent()).getEditor()
                                    .getEditorComponent()).getText().isEmpty()){
                                try{
                                    panel.add((new EveryDayBro((String) comboTeamListForSoloPanel.getSelectedItem(),
                                            (String) comboVariablesForSoloPanel.getSelectedItem(), panel)).chartPanel);
                                    panel.validate();
                                    repaint();

                                }catch (Exception o){
                                    o.printStackTrace();
                                    JOptionPane pane = new JOptionPane();
                                    pane.showMessageDialog(null, "Fill in the variables correctly",
                                            "Error", JOptionPane.ERROR_MESSAGE);}
                            }
                        }
                    }
                });
                comboVariablesForSoloPanel2.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent event) {
                        if (event.getKeyChar() == KeyEvent.VK_ENTER) {
                            if (!((JTextComponent) ((JComboBox) ((Component) event
                                    .getSource()).getParent()).getEditor()
                                    .getEditorComponent()).getText().isEmpty() && !((JTextComponent)comboVariablesForSoloPanel.getEditor().
                                    getEditorComponent()).getText().isEmpty()){
                                try{
                                    panel.add((new EveryDayBro((String) comboTeamListForSoloPanel.getSelectedItem(),
                                            (String) comboVariablesForSoloPanel.getSelectedItem(), (String) comboVariablesForSoloPanel2.getSelectedItem(),
                                            panel)).chartPanel);
                                    panel.validate();
                                    repaint();

                                }catch (Exception o){
                                    o.printStackTrace();
                                    JOptionPane pane = new JOptionPane();
                                    pane.showMessageDialog(null, "Fill in the variables correctly",
                                            "Error", JOptionPane.ERROR_MESSAGE);}
                            }
                        }
                    }
                });

                comboTeamListForSoloPanel.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent event) {
                        if (event.getKeyChar() == KeyEvent.VK_ENTER) {
                            if (!((JTextComponent) ((JComboBox) ((Component) event
                                    .getSource()).getParent()).getEditor()
                                    .getEditorComponent()).getText().isEmpty() &&
                                    ! ((JTextComponent) comboVariablesForSoloPanel.getEditor()
                                            .getEditorComponent()).getText().isEmpty()){
                                try{
                                    panel.add((new EveryDayBro((String) comboTeamListForSoloPanel.getSelectedItem(),
                                            (String) comboVariablesForSoloPanel.getSelectedItem(), panel)).chartPanel);
                                    panel.validate();
                                    repaint();

                                }catch (Exception o){
                                    o.printStackTrace();
                                    JOptionPane pane = new JOptionPane();
                                    pane.showMessageDialog(null, "Fill in the variables correctly",
                                            "Error", JOptionPane.ERROR_MESSAGE);}
                            }
                        }
                    }
                });

            }
        });
    }
    JTextField noEditText(String str){
        JTextField field = new JTextField(str);
        field.setEditable(false);
        return field;
    }

}
