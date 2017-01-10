package com.project.material.util;

public class CalculateVolumeUtils {

    public static final int MAX_PARAMETER_COUNT = 5;

    public static final int ANGLE = 0;
    public static final int CHANNEL = 1;
    public static final int CONE = 2;
    public static final int CUBE = 3;
    public static final int CYLINDER = 4;
    public static final int HEXAGONAL_PRISM = 5;
    public static final int I_BEAM = 6;
    public static final int RECTANGLE_TUBE = 7;
    public static final int ROUND_TUBE = 8;
    public static final int SPHERE = 9;
    public static final int SQUARE_BASED_PYRAMID = 10;
    public static final int SQUARE_TUBE = 11;
    public static final int TEE_BEAM = 12;

    public static double calculateAngle(double height, double width, double length, double gauge) {
        return (height + width - gauge) * gauge * length;
    }

    public static double calculateChannel(double height, double width1, double width2, double length, double gauge) {
        return (height + width1 + width2 - 2 * gauge) * gauge * length;
    }

    public static double calculateCone(double radius, double height) {
        return Math.PI * Math.pow(radius, 2) * height / 3;
    }

    public static double calculateCube(double width, double length, double height) {
        return width * length * height;
    }

    public static double calculateCylinder(double radius, double height) {
        return Math.PI * Math.pow(radius, 2) * height;
    }

    public static double calculateHexagonalPrism(double side, double height) {
        return 3 * Math.sqrt(3) * Math.pow(side, 2) * height / 2;
    }

    public static double calculateI_Beam(double height, double width1, double width2, double length, double gauge) {
        return (width1 + width2 + height - gauge * 2) * gauge * length;
    }

    public static double calculateRectangularTube(double height, double width, double length, double gauge) {
        return (width * height - (width - gauge) * (height - gauge)) * length;
    }

    public static double calculateRoundTube(double diameter, double gauge, double length) {
        return Math.PI * (Math.pow(diameter / 2, 2) - Math.pow((diameter - gauge) / 2, 2)) * length;
    }

    public static double calculateSphere(double radius) {
        return 4 * Math.PI * Math.pow(radius, 3) / 3;
    }

    public static double calculateSquaredPyramid(double height, double length, double width) {
        return width * length * height / 3;
    }

    public static double calculateSquareTube(double height, double width, double length, double gauge) {
        return (width * height - (width - gauge) * (height - gauge)) * length;
    }

    public static double calculateTee_Beam(double height, double width, double length, double gauge) {
        return (width + height - gauge) * gauge * length;
    }

    public static double calculateVolume(int shape, double param1, double param2, double param3, double param4, double param5) {
        switch (shape) {
            case ANGLE:
                return calculateAngle(param1, param2, param3, param4);

            case CHANNEL:
                return calculateChannel(param1, param2, param3, param4, param5);

            case CONE:
                return calculateCone(param1, param2);

            case CUBE:
                return calculateCube(param1, param2, param3);

            case CYLINDER:
                return calculateCylinder(param1, param2);

            case HEXAGONAL_PRISM:
                return calculateHexagonalPrism(param1, param2);

            case I_BEAM:
                return calculateI_Beam(param1, param2, param3, param4, param5);

            case RECTANGLE_TUBE:
                return calculateRectangularTube(param1, param2, param3, param4);

            case ROUND_TUBE:
                return calculateRoundTube(param1, param2, param3);

            case SPHERE:
                return calculateSphere(param1);

            case SQUARE_BASED_PYRAMID:
                return calculateSquaredPyramid(param1, param2, param3);

            case SQUARE_TUBE:
                return calculateSquareTube(param1, param2, param3, param4);

            case TEE_BEAM:
                return calculateTee_Beam(param1, param2, param3, param4);

            default:
                return 0;
        }
    }

}
