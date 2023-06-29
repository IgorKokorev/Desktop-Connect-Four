package four;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectFour extends JFrame implements ActionListener {
    private final int numberOfRows = 6;
    private final int numberOfColumns = 7;
    private final int numberInARow = 4;
    private boolean isFinished = false;
    private JButton[][] fields = new JButton[numberOfRows][numberOfColumns];
    private String nextMove = "X";
    private Color defaultColor = Color.LIGHT_GRAY;
    private Color winColor = Color.ORANGE;

    public ConnectFour() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setVisible(true);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setTitle("Connect Four");

        JPanel gamePanel = setGamePanel();

        JButton button = new JButton("Reset");
        button.setName("ButtonReset");
//        button.setBounds(0, 250, 300, 50);
        button.setBackground(winColor);
        button.addActionListener(this);
        add(gamePanel);
        add(button);

    }

    private JPanel setGamePanel() {
        JPanel gamePanel = new JPanel();
//        rowPanel.setPreferredSize(new Dimension(600, 400));
//        rowPanel.setMaximumSize(rowPanel.getPreferredSize());
        gamePanel.setPreferredSize(new Dimension(300, 250));

        gamePanel.setLayout(new GridLayout(numberOfRows, numberOfColumns, 0, 0));
        gamePanel.setBackground(winColor);
        addButtons(gamePanel);
        return gamePanel;
    }

    private void addButtons(JPanel gamePanel) {
        for (int r = 0; r < numberOfRows; r++) {
            for (int c = 0; c < numberOfColumns; c++) {
                addOneButton(gamePanel, r, c);
            }
        }
    }

    private void addOneButton(JPanel gamePanel, int r, int c) {
        StringBuilder name = new StringBuilder();
        name.append((char) ('A' + c));
        name.append((char) ('0' + numberOfRows - r));
        JButton button = new JButton(" ");
        button.setName("Button" + name);
        button.setBackground(defaultColor);
        button.addActionListener(this);
        fields[r][c] = button;
        gamePanel.add(button);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object object = e.getSource();
        if (!(object instanceof JButton)) return;
        JButton button = (JButton) object;

        if (button.getName().equals("ButtonReset")) {
            resetGame();
            return;
        }

        if(!isFinished && button.getText().equals(" ")) {
            makeMove(button);
        }
    }

    private void makeMove(JButton button) {
        button = getLowestFreeButton(button);
        button.setText(nextMove);
        Line line = checkBoard();
        if (line != null) {
            // and of game
            paintLine(line, winColor);
            isFinished = true;
        }
        changeMove();
    }

    private void paintLine(Line line, Color color) {
        for (int i = 0; i < numberInARow; i++) {
            fields[line.startRow + i * line.moveRow][line.startColumn + i * line.moveColumn].setBackground(color);
        }
    }

    private Line checkBoard() {
        Line line = new Line();

        // check for win
        // set 3 standard directions
        for (int dir = 1; dir <= 3; dir ++) {

            line.moveRow = dir / 2;
            line.moveColumn = dir % 2;

            for (line.startRow = 0;
                 line.startRow + line.moveRow * (numberInARow - 1) < numberOfRows;
                 line.startRow++) {

                for (line.startColumn = 0;
                     line.startColumn + line.moveColumn * (numberInARow - 1) < numberOfColumns;
                     line.startColumn++) {
                    if (checkOneLine(line)) return line;
                }
            }
        }

        // check 4th direction
        line.moveRow = -1;
        line.moveColumn = 1;

        for (line.startRow = numberInARow - 1;
             line.startRow < numberOfRows;
             line.startRow++) {

            for (line.startColumn = 0;
                 line.startColumn + numberInARow - 1 < numberOfColumns;
                 line.startColumn++) {
                if (checkOneLine(line)) return line;
            }
        }

        return null;
    }

    private boolean checkOneLine(Line line) {
        String text = fields[line.startRow][line.startColumn].getText();
        if (text.equals(" ")) return false;
        for (int i = 1; i < numberInARow; i++) {
            String next = fields[line.startRow + i * line.moveRow][line.startColumn + i * line.moveColumn].getText();
            if (!text.equals(next)) return false;
        }
        return true;
    }

    private void resetGame() {
        isFinished = false;
        nextMove = "X";
        for (int r = 0; r < numberOfRows; r++) {
            for (int c = 0; c < numberOfColumns; c++) {
                fields[r][c].setText(" ");
                fields[r][c].setBackground(defaultColor);
            }
        }
    }

    private JButton getLowestFreeButton(JButton button) {
        String buttonName = button.getName();
        int r = '0' + numberOfRows - buttonName.charAt(7);
        int c = buttonName.charAt(6) - 'A';

        int nextRow = r + 1;
        while (nextRow < numberOfRows) {
            if (fields[nextRow][c].getText().equals(" ")) nextRow++;
            else break;
        }

        return fields[nextRow - 1][c];
    }

    private void changeMove() {
        nextMove = nextMove.equals("X") ? "O" : "X";
    }
}

class Line {
    int startRow;
    int startColumn;
    int moveRow;
    int moveColumn;
}