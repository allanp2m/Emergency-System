package com.mycompany.hospitalemergencysystem;

import java.util.Arrays;
import java.util.Comparator;

public class TriageQueue {
    private Patient[] queue;
    private int size;

    public TriageQueue() {
        queue = new Patient[100];
        size = 0;
    }

    public void addPatient(Patient patient) {
        if (size < queue.length) {
            queue[size++] = patient;
            sortQueue();
        } else {
            System.err.println("Fila de triagem cheia.");
        }
    }

    public void removePatientBySenha(int senha) {
        for (int i = 0; i < size; i++) {
            if (queue[i].getSenha() == senha) {
                System.arraycopy(queue, i + 1, queue, i, size - i - 1);
                size--;
                break;
            }
        }
    }

    public Patient[] getAllPatients() {
        return Arrays.copyOf(queue, size);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clearQueue() {
        size = 0;
    }

    public void sortQueue() {
        Arrays.sort(queue, 0, size, Comparator.naturalOrder());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append("Senha: ").append(queue[i].getSenha())
              .append(", Classificação: ").append(queue[i].getClassification() == 1 ? "Preferencial" : "Comum")
              .append(", Prioridade: ").append(queue[i].getPriority()).append("\n");
        }
        return sb.toString();
    }
}