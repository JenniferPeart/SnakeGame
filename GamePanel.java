import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
// Inheritance
// -> GamePanel is the subclass (child)
// -> JPanel is the superclass (parent)
// -> GamePanel inherits attributes and methods from JPanel
// Action listener is an interface
// -> contains methods with empty bodies
// -> must override all it's methods?
public class GamePanel extends JPanel implements ActionListener {
    // static -> attribute belongs to the class rather than an object
    // final -> attribute can't be overwritte/modified
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20; // bigger number means less squares/bigger squares on the grid
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 80; // the higher the number slower the game
    final int x[] = new int[GAME_UNITS]; // snake cooridinates
    final int y[] = new int[GAME_UNITS]; // snake cooridinates
    int bodyParts = 6; // snake body
    int applesEaten;
    int appleX; // apple coordinates
    int appleY; // apple coordinates
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        if (running) {
            // draw grid to help with dev
            /* for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            } */
            // make apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            // make head and body of snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    // g.setColor(new Color(45, 180, 0)); // green snake
                    // make snake different colours
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            // score
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten,
                (SCREEN_WIDTH - metrics.stringWidth("Score "+applesEaten)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }
    public void newApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }
    public void move() {
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction) {
        case 'U':
            y[0] = y[0] - UNIT_SIZE;
            break;
        case 'D':
            y[0] = y[0] + UNIT_SIZE;
            break;
        case 'L':
            x[0] = x[0] - UNIT_SIZE;
            break;
        case 'R':
            x[0] = x[0] + UNIT_SIZE;
            break;
        }
    }
    public void checkApple() { // the snake eats apples
        if ((x[0] == appleX) && (y[0] == appleY)) { // if snakes head is on apple
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions() {
        // checks if head colides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // check if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        // check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        // check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        // check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }
    public void gameOver(Graphics g) {
        // Game Over writing
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metricsGameOver = getFontMetrics(g.getFont());
        g.drawString("Game Over",
            (SCREEN_WIDTH - metricsGameOver.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2); // centre
        // Score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metricsScore = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten,
            (SCREEN_WIDTH - metricsScore.stringWidth("Score "+applesEaten)) / 2, g.getFont().getSize());
    }
    @Override
    public void actionPerformed(ActionEvent e) { // method from Action Listener interface
        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    // MyKeyAdapter is an inner class
    // -> access by creating an object of the outer class (GamePanel)
    // -> and then creating an object of the inner class (MyKeyAdapter)
    public class MyKeyAdapter extends KeyAdapter {
        @Override public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') {
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') {
                    direction = 'R';
                }
                break;
            case KeyEvent.VK_UP:
                if (direction != 'D') {
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') {
                    direction = 'D';
                }
                break;
            }
        }
    }
}
