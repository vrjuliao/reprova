package br.ufmg.engsoft.reprova.model;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class QuestionCalculations {
    public static double calculateGradeAverage(Map<Semester, Map<String, Map<String, Float>>> record) {
        double acc = firstLevelAccumulation(record);
        return acc / record.size();
    }

    public static double sum(Map<Semester, Map<String, Map<String, Float>>> record) {
        double average = calculateGradeAverage(record);
        double sum = 0.0;
        for (Map.Entry<Semester, Map<String, Map<String, Float>>> entry : record.entrySet()) {
            for (Map.Entry<String, Map<String, Float>> innerEntry : entry.getValue().entrySet()) {
                for (var grades : innerEntry.getValue().values()) {
                    sum += Math.pow(grades - average, 2);
                }
            }
        }
        return sum;
    }

    public static double secondLevelAccumulation(Map.Entry<Semester, Map<String, Map<String, Float>>> entry) {
        double acc2 = 0;
        for (Map.Entry<String, Map<String, Float>> innerEntry : entry.getValue().entrySet()) {
            acc2 += innerEntry.getValue().values().stream().mapToDouble(Float::doubleValue).average().orElse(0);
        }
        return acc2;
    }

    public static double firstLevelAccumulation(Map<Semester, Map<String, Map<String, Float>>> record) {
        double acc = 0;
        for (Map.Entry<Semester, Map<String, Map<String, Float>>> entry : record.entrySet()) {
            double acc2 = secondLevelAccumulation(entry);
            acc += acc2 / entry.getValue().entrySet().size();
        }
        return acc;
    }

    public static int quantityOfGrades(Map<Semester, Map<String, Map<String, Float>>> record) {
        int qtt = 0;
        for (Map.Entry<Semester, Map<String, Map<String, Float>>> entry : record.entrySet()) {
            for (Map.Entry<String, Map<String, Float>> innerEntry : entry.getValue().entrySet()) {
                qtt += innerEntry.getValue().size();
            }
        }
        return qtt;
    }

    public static double calculateGradeStandardDeviation(Map<Semester, Map<String, Map<String, Float>>> record) {
        double sum = sum(record);
        int qtt = quantityOfGrades(record);
        double stdDev = Math.sqrt(sum / (qtt - 1));
        return stdDev;
    }

    public static double calculateGradeMedian(Map<Semester, Map<String, Map<String, Float>>> record) {
        List<Float> gradeList = new ArrayList<Float>();
        for (Map.Entry<Semester, Map<String, Map<String, Float>>> entry : record.entrySet()) {
            for (Map.Entry<String, Map<String, Float>> innerEntry : entry.getValue().entrySet()) {
                for (var grades : innerEntry.getValue().values()) {
                    gradeList.add(grades);
                }
            }
        }
        Collections.sort(gradeList);
        if (gradeList.size() == 0) {
            return 0.0;
        }
        int i = gradeList.size() / 2;
        if (gradeList.size() % 2 == 0) {
            return (gradeList.get(i - 1) + gradeList.get(i)) / 2;
        } else {
            return gradeList.get(i);
        }
    }
}