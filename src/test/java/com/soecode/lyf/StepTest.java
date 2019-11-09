package com.soecode.lyf;

public class StepTest {
    private final static int FIRST_STEP = 1;
    private final static int SECOND_STEP = 2;
    private final static int THIRD_STEP = 4;
    private final static int FOURTH_STEP = 8;

    public static void main(String[] args) {
        new StepTest().step(3);
        System.out.println();
    }

    public void step(int steps){
        if((steps & FIRST_STEP) == 0){
            System.out.println("execute first step");
            steps = steps | FIRST_STEP;
        }
        if((steps & SECOND_STEP) == 0){
            System.out.println("execute second step");
            steps = steps | SECOND_STEP;
        }
        if((steps & THIRD_STEP) == 0){
            System.out.println("execute third step");
            steps = steps | THIRD_STEP;
        }
        if((steps & FOURTH_STEP) == 0){
            System.out.println("execute fourth step");
            steps = steps | FOURTH_STEP;
        }
        System.out.println(steps);
    }
}
