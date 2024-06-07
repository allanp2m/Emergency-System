package com.mycompany.hospitalemergencysystem;

public class Patient implements Comparable<Patient> {
    private int classification;
    private int priority;
    private int senha;

    public Patient(int classification, int priority, int senha) {
        this.classification = classification;
        this.priority = priority;
        this.senha = senha;
    }

    public int getClassification() {
        return classification;
    }

    public int getPriority() {
        return priority;
    }

    public int getSenha() {
        return senha;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Patient other) {
        if (this.priority != other.priority) {
            return Integer.compare(other.priority, this.priority);
        }
        if (this.classification != other.classification) {
            return Integer.compare(other.classification, this.classification);
        }
        return Integer.compare(this.senha, other.senha);
    }
}
