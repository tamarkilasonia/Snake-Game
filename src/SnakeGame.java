import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {
    private final int TILE_SIZE = 25;
    private final int GAME_WIDTH = 800;
    private final int GAME_HEIGHT = 600;
    private final int TOTAL_TILES = (GAME_WIDTH * GAME_HEIGHT) / (TILE_SIZE * TILE_SIZE);

    private int[] snakeX = new int[TOTAL_TILES];
    private int[] snakeY = new int[TOTAL_TILES];
    private int snakeLength = 3;

    private int foodX, foodY;

    private char direction = 'R'; // L, R, U, D
    private boolean running = true;

    private Timer timer;
    private final int DELAY = 100;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Initialize snake
        for (int i = 0; i < snakeLength; i++) {
            snakeX[i] = 50 - i * TILE_SIZE;
            snakeY[i] = 50;
        }

        spawnFood();

        timer = new Timer(DELAY, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw game area
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(foodX, foodY, TILE_SIZE, TILE_SIZE);

        // Draw snake
        for (int i = 0; i < snakeLength; i++) {
            g.setColor(i == 0 ? Color.GREEN : Color.WHITE); // Head is green, body is white
            g.fillRect(snakeX[i], snakeY[i], TILE_SIZE, TILE_SIZE);
        }

        // Draw Game Over message
        if (!running) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over!", GAME_WIDTH / 2 - 120, GAME_HEIGHT / 2);
            g.drawString("Press R to Restart", GAME_WIDTH / 2 - 150, GAME_HEIGHT / 2 + 50);
        }
    }

    private void spawnFood() {
        Random random = new Random();
        foodX = random.nextInt(GAME_WIDTH / TILE_SIZE) * TILE_SIZE;
        foodY = random.nextInt(GAME_HEIGHT / TILE_SIZE) * TILE_SIZE;
    }

    private void move() {
        for (int i = snakeLength - 1; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        switch (direction) {
            case 'L' -> snakeX[0] -= TILE_SIZE;
            case 'R' -> snakeX[0] += TILE_SIZE;
            case 'U' -> snakeY[0] -= TILE_SIZE;
            case 'D' -> snakeY[0] += TILE_SIZE;
        }
    }

    private void checkCollision() {
        // Check wall collision
        if (snakeX[0] < 0 || snakeX[0] >= GAME_WIDTH || snakeY[0] < 0 || snakeY[0] >= GAME_HEIGHT) {
            running = false;
            timer.stop();
        }

        // Check self-collision
        for (int i = 1; i < snakeLength; i++) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                running = false;
                timer.stop();
                break;
            }
        }

        // Check food collision
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            snakeLength++;
            spawnFood();
        }
    }

    private void restart() {
        snakeLength = 3;
        direction = 'R';
        running = true;

        for (int i = 0; i < snakeLength; i++) {
            snakeX[i] = 50 - i * TILE_SIZE;
            snakeY[i] = 50;
        }

        spawnFood();
        timer.start();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (running) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            restart();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new SnakeGame();
    }
}
