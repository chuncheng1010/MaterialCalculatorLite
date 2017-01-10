package com.project.material.helper;

import java.util.regex.Pattern;

public class Fraction {

    public int main;
    public int up;
    public int down;

    public Fraction(int main, int up, int down) {
        this.main = main;
        this.up = up;
        this.down = down;
    }

    public double getValue() {
        return ((main * down) + up) / down;
    }

    public boolean checkDigit(String str) {
        Pattern pat = Pattern.compile("[0-9/]");

        if (!pat.matcher(str).find()) return false;

        String[] array = str.split(" ");
        if (!array[1].equals("")) {
            String[] array1 = array[1].split("/");
            int up, down;
            try {
                up = Integer.parseInt(array1[0]);
                down = Integer.parseInt(array1[1]);
                if (up == 0 || down == 0 || down != 16)
                    return false;
                else
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

}
