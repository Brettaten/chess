package chess.Logic;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import chess.Logic.Colors;
import chess.Logic.Game;
import chess.Logic.Settings;

public class GameTimer implements Runnable {
    private JLabel timerLabel;
    private Thread thread;
    private long lastTime;
    private int increment;
    private boolean isRunning;
    private boolean isPaused;
    private boolean isAlive;
    private boolean isUnlimited;
    private Object lock;
    private long timeDiff;
    private boolean isFirstTime;
    private long savedTime;

    public GameTimer(JLabel timerLabel) {
        this.timerLabel = timerLabel;
        increment = Settings.increment;
        this.lock = new Object();
    }

    public void run() {
        while (isAlive) {
            synchronized (lock) {
                if (isPaused == true) {
                    savedTime =  (System.currentTimeMillis()-lastTime);
                    try {
                        lock.wait();
                        lastTime = System.currentTimeMillis();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
           long currentTime = System.currentTimeMillis();


            if(isFirstTime){
                updateLabel();
                lastTime = System.currentTimeMillis();
                isFirstTime = false;
            }
            else if(currentTime-lastTime  >= 1000-savedTime){
//                System.out.println("Currentime: "+currentTime);
//                System.out.println("lastTIme: "+lastTime);
//                System.out.println("savcetiem: "+savedTime);

                updateLabel();
                long extraTime = 0;

                if(currentTime-lastTime > 1000){
                    extraTime = currentTime - lastTime -1000;
                }
                lastTime = System.currentTimeMillis()+extraTime;
                savedTime = 0;
            }

        }
    }

    private void updateLabel() {
        String s = timerLabel.getText();
        int[] time = { Character.getNumericValue(s.charAt(4)),
                Character.getNumericValue(s.charAt(3)),
                Character.getNumericValue(s.charAt(1)),
                Character.getNumericValue(s.charAt(0)) };

        for (int i = 0; i < 4; i++) {
            if (time[i] != 0) {
                time[i]--;
                if (checkEnd(time)) {
                    Game.gameUpdate.endTime();
                }
                break;
            } else {
                if (i == 0 || i == 2) {
                    time[i] = 9;
                } else if (i == 1 || i == 3) {
                    time[i] = 5;
                }
            }
        }
        timerLabel.setText(String.valueOf(time[3]) + String.valueOf(time[2]) + ":" + String.valueOf(time[1])
                + String.valueOf(time[0]));
    }

    private boolean checkEnd(int[] time) {
        for (int i : time) {
            if (i != 0) {
                return false;
            }
        }
        return true;
    }

    public void pause() {
        isPaused = true;
        isRunning = false;

        setIncrement();
    }

    private void setIncrement() {
        String s = timerLabel.getText();
        int[] time = { Character.getNumericValue(s.charAt(0)),
                Character.getNumericValue(s.charAt(1)),
                Character.getNumericValue(s.charAt(3)),
                Character.getNumericValue(s.charAt(4)) };
        String rawTime = "";
        for (int i : time) {
            rawTime += i;
        }
        rawTime.strip();
        int newTime = Integer.parseInt(rawTime) + increment;
        if (String.valueOf(newTime).length() == 3) {
            time[0] = 0;
            time[1] = Character.getNumericValue(String.valueOf(newTime).charAt(0));
            time[2] = Character.getNumericValue(String.valueOf(newTime).charAt(1));
            time[3] = Character.getNumericValue(String.valueOf(newTime).charAt(2));
        } else if (String.valueOf(newTime).length() == 2) {
            time[0] = 0;
            time[1] = 0;
            time[2] = Character.getNumericValue(String.valueOf(newTime).charAt(0));
            time[3] = Character.getNumericValue(String.valueOf(newTime).charAt(1));
        } else if (String.valueOf(newTime).length() == 1) {
            time[0] = 0;
            time[1] = 0;
            time[2] = 0;
            time[3] = Character.getNumericValue(String.valueOf(newTime).charAt(0));
        } else {
            time[0] = Character.getNumericValue(String.valueOf(newTime).charAt(0));
            time[1] = Character.getNumericValue(String.valueOf(newTime).charAt(1));
            time[2] = Character.getNumericValue(String.valueOf(newTime).charAt(2));
            time[3] = Character.getNumericValue(String.valueOf(newTime).charAt(3));
        }

        for (int i = 2; i >= 0; i--) {
            if (i == 2 && time[i] >= 6) {
                int temp = time[i];
                time[i] = 0;
                for (int j = temp; j >= 7; j--) {
                    time[i]++;
                }
            } else if (i == 1) {
                if(time[i] == 9){
                    time[i] = 0;
                }
                else{
                    time[i]++;
                    if (time[i] != 0) {
                        break;
                    }
                }
            } else if (i == 0) {
                if(time[i] == 9){
                    break;
                }
                time[i]++;
                if (time[i] != 0) {
                    break;
                } else {
                    time[i]--;
                    break;
                }
            } else {
                break;
            }
        }
        timerLabel.setText(String.valueOf(time[0]) + String.valueOf(time[1]) + ":" + String.valueOf(time[2])
                + String.valueOf(time[3]));
    }

    public void resume() {
        synchronized (lock) {
            isRunning = true;
            isPaused = false;
            lock.notify();
        }
    }

    public void start() {
        if (isUnlimited) {
            return;
        }
        isFirstTime = true;
        isRunning = true;
        isPaused = false;
        isAlive = true;
        thread = new Thread(this);
        thread.start();
    }

    public boolean hasStarted() {
        return thread != null;
    }

    public void resetTimer() {
        isAlive = false;
        if (Settings.time == "unlimited") {
            timerLabel.setText("0:00");
            isUnlimited = true;
        } else {
            if (Settings.time.length() == 4) {
                timerLabel.setText("0" + Settings.time);
            } else {
                timerLabel.setText(Settings.time);
            }
            isUnlimited = false;
        }
        increment = Settings.increment;
    }

    public void setNull() {
        isAlive = false;
        isRunning = false;
        thread = null;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
