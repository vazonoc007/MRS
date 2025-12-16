import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class SnakeGame extends JFrame {
    public SnakeGame() {
        this.add(new GamePanel());
        this.setTitle("Snake 🐍");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    static void main() {
        SwingUtilities.invokeLater(SnakeGame::new);
    }

    public enum SnakeGameMode {
        CLASSIC, NO_WALLS, OBSTACLES, WALLS_OBSTACLES
    }

    static class GamePanel extends JPanel implements ActionListener {

        static final int SCREEN_WIDTH = 600;
        static final int SCREEN_HEIGHT = 600;
        static final int UNIT_SIZE = 25;
        static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
        static final int DELAY = 75;

        final int[] x = new int[GAME_UNITS];
        final int[] y = new int[GAME_UNITS];
        int bodyParts = 6;
        int applesEaten;
        int appleX, appleY;
        char direction = 'R';

        boolean running = false;
        boolean inMenu = true;
        boolean paused = false;   // ← нове

        SnakeGameMode mode = SnakeGameMode.CLASSIC;
        final List<Point> obstacles = new ArrayList<>();

        Timer timer;
        final Random random = new Random();

        public GamePanel() {
            this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            this.setBackground(Color.black);
            this.setFocusable(true);
            this.addKeyListener(new MyKeyAdapter());
        }

        private void resetGame() {
            bodyParts = 6;
            applesEaten = 0;
            direction = 'R';
            paused = false;

            int startX = SCREEN_WIDTH / 2;
            int startY = SCREEN_HEIGHT / 2;

            for (int i = 0; i < bodyParts; i++) {
                x[i] = startX - i * UNIT_SIZE;
                y[i] = startY;
            }
        }

        private void startGame() {
            inMenu = false;
            running = true;
            paused = false;

            resetGame();
            createObstacles();
            newApple();

            timer = new Timer(DELAY, this);
            timer.start();
        }

        private void drawMenu(Graphics g) {
            g.setColor(Color.green);
            g.setFont(new Font("Ink Free", Font.BOLD, 55));
            FontMetrics fm = getFontMetrics(g.getFont());
            g.drawString("Snake", (SCREEN_WIDTH - fm.stringWidth("Snake")) / 2, 120);

            g.setFont(new Font("Ink Free", Font.PLAIN, 30));
            String[] lines = {
                    "1 — Classic mod (with walls)",
                    "2 — Without walls",
                    "3 — Obstacle",
                    "4 — Walls + Obstacle",
                    "",
                    "Press 1–4, to start game"
            };

            int y = 220;
            for (String s : lines) {
                fm = getFontMetrics(g.getFont());
                g.drawString(s, (SCREEN_WIDTH - fm.stringWidth(s)) / 2, y);
                y += 45;
            }
        }

        private void drawPause(Graphics g) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            g.setColor(Color.yellow);
            g.setFont(new Font("Ink Free", Font.BOLD, 60));
            FontMetrics fm = getFontMetrics(g.getFont());
            g.drawString("Pause", (SCREEN_WIDTH - fm.stringWidth("Pause")) / 2, 200);

            g.setFont(new Font("Ink Free", Font.PLAIN, 30));
            fm = getFontMetrics(g.getFont());

            String[] lines = {
                    "R — Continue",
                    "M — Start menu",
                    "Q — Quit the game"
            };

            int y = 300;
            for (String s : lines) {
                g.drawString(s, (SCREEN_WIDTH - fm.stringWidth(s)) / 2, y);
                y += 50;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (inMenu) {
                drawMenu(g);
                return;
            }

            if (paused) {
                drawPause(g);
                return;
            }

            if (!running) {
                drawGameOver(g);
                return;
            }

            drawGame(g);
        }

        private void drawGame(Graphics g) {

            g.setColor(Color.gray);
            for (Point p : obstacles)
                g.fillRect(p.x, p.y, UNIT_SIZE, UNIT_SIZE);

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                g.setColor(i == 0 ? Color.green : new Color(45, 180, 0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 35));
            FontMetrics fm = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten,
                    (SCREEN_WIDTH - fm.stringWidth("Score: " + applesEaten)) / 2,
                    g.getFont().getSize());
        }

        private void drawGameOver(Graphics g) {
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics m1 = getFontMetrics(g.getFont());
            g.drawString("Game Over",
                    (SCREEN_WIDTH - m1.stringWidth("Game Over")) / 2,
                    SCREEN_HEIGHT / 2);

            g.setFont(new Font("Ink Free", Font.BOLD, 35));
            FontMetrics m2 = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten,
                    (SCREEN_WIDTH - m2.stringWidth("Score: " + applesEaten)) / 2,
                    60);

            g.setFont(new Font("Ink Free", Font.PLAIN, 30));
            g.drawString("Press any button",
                    120, SCREEN_HEIGHT - 80);
        }


        private void newApple() {
            boolean valid;
            do {
                valid = true;
                appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
                appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;

                for (int i = 0; i < bodyParts; i++)
                    if (x[i] == appleX && y[i] == appleY) {
                        valid = false;
                        break;
                    }

                for (Point p : obstacles)
                    if (p.x == appleX && p.y == appleY) {
                        valid = false;
                        break;
                    }

            } while (!valid);
        }

        private void createObstacles() {
            obstacles.clear();
            if (mode == SnakeGameMode.OBSTACLES || mode == SnakeGameMode.WALLS_OBSTACLES) {
                for (int i = 0; i < 10; i++) {
                    obstacles.add(new Point(
                            random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE,
                            random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE
                    ));
                }
            }
        }

        private void move() {
            for (int i = bodyParts; i > 0; i--) {
                x[i] = x[i - 1];
                y[i] = y[i - 1];
            }

            switch (direction) {
                case 'U': y[0] -= UNIT_SIZE; break;
                case 'D': y[0] += UNIT_SIZE; break;
                case 'L': x[0] -= UNIT_SIZE; break;
                case 'R': x[0] += UNIT_SIZE; break;
            }

            if (mode == SnakeGameMode.NO_WALLS || mode == SnakeGameMode.OBSTACLES) {
                if (x[0] < 0) x[0] = SCREEN_WIDTH - UNIT_SIZE;
                if (x[0] >= SCREEN_WIDTH) x[0] = 0;
                if (y[0] < 0) y[0] = SCREEN_HEIGHT - UNIT_SIZE;
                if (y[0] >= SCREEN_HEIGHT) y[0] = 0;
            }
        }

        private void checkCollisions() {
            for (int i = bodyParts; i > 0; i--)
                if (x[0] == x[i] && y[0] == y[i]) {
                    running = false;
                    break;
                }

            if (mode == SnakeGameMode.CLASSIC || mode == SnakeGameMode.WALLS_OBSTACLES)
                if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT)
                    running = false;

            if (mode == SnakeGameMode.OBSTACLES || mode == SnakeGameMode.WALLS_OBSTACLES)
                for (Point p : obstacles)
                    if (x[0] == p.x && y[0] == p.y) {
                        running = false;
                        break;
                    }

            if (!running && timer != null) timer.stop();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (running && !paused) {
                move();
                if (x[0] == appleX && y[0] == appleY) {
                    bodyParts++;
                    applesEaten++;
                    newApple();
                }
                checkCollisions();
            }
            repaint();
        }

        private class MyKeyAdapter extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {

                int key = e.getKeyCode();

                if (inMenu) {
                    switch (key) {
                        case KeyEvent.VK_1: mode = SnakeGameMode.CLASSIC; startGame(); break;
                        case KeyEvent.VK_2: mode = SnakeGameMode.NO_WALLS; startGame(); break;
                        case KeyEvent.VK_3: mode = SnakeGameMode.OBSTACLES; startGame(); break;
                        case KeyEvent.VK_4: mode = SnakeGameMode.WALLS_OBSTACLES; startGame(); break;
                    }
                    return;
                }

                if (key == KeyEvent.VK_ESCAPE && running) {
                    paused = !paused;
                    repaint();
                    return;
                }

                if (paused) {
                    switch (key) {
                        case KeyEvent.VK_R: // продовжити
                        case KeyEvent.VK_ESCAPE:
                            paused = false;
                            break;

                        case KeyEvent.VK_M: // вихід до меню
                            paused = false;
                            running = false;
                            inMenu = true;
                            break;

                        case KeyEvent.VK_Q: // закрити
                            System.exit(0);
                            break;
                    }
                    repaint();
                    return;
                }

                if (!running) {
                    inMenu = true;
                    repaint();
                    return;
                }

                switch (key) {
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
            }
        }
    }

}
