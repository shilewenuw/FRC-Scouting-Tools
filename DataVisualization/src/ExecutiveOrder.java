import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Created by pickl on 1/11/2018.
 */
public class ExecutiveOrder extends JFrame {
    public static void main(String[] args){
        ExecutiveOrder ex = new ExecutiveOrder();
        ex.setVisible(true);

    }
    ExecutiveOrder(){
        super("Data");
        setSize(1200,800);
        GetArrayFromDB DBVars = new GetArrayFromDB(1);
        JPanel jpanel = new JPanel();
        jpanel.setPreferredSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 3000));
        OptionPanel optionPanel = new OptionPanel(DBVars.teams, Arrays.copyOfRange(DBVars.variables,
                2, DBVars.variables.length), jpanel);


        add(optionPanel, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(jpanel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setMinimumSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 3000));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}

