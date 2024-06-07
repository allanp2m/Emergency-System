package com.mycompany.hospitalemergencysystem;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HospitalEmergencySystem {
    private TriageQueue triageQueue;
    private EmergencyQueue emergencyQueue;
    private int senhaCounter = 1;
    private String triageFilePath = "C:\\Users\\Allan\\Desktop\\A3\\pacientes_triagem.txt";
    private String emergencyFilePath = "C:\\Users\\Allan\\Desktop\\A3\\pacientes_atendimento.txt";

    public HospitalEmergencySystem() {
        triageQueue = new TriageQueue();
        emergencyQueue = new EmergencyQueue();
        loadPatientsFromFile();
    }

    public void addPatientToTriage(int classification) {
        int senha = senhaCounter++;
        Patient patient = new Patient(classification, 0, senha);
        triageQueue.addPatient(patient);
        savePatientToFile(triageFilePath, patient);
    }

    private void savePatientToFile(String filePath, Patient patient) {
        try (FileWriter fw = new FileWriter(filePath, true)) {
            fw.write(patient.getSenha() + "," + patient.getClassification() + "," + patient.getPriority() + "\n");
            fw.flush();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public void transferPatientsToEmergency() {
        Patient[] patients = triageQueue.getAllPatients();
        for (Patient patient : patients) {
            if (patient != null) {
                String priorityStr = JOptionPane.showInputDialog(null,
                        "Informe a prioridade para o paciente com senha " + patient.getSenha() + " (Classificação: " + (patient.getClassification() == 1 ? "Preferencial" : "Comum") + "):",
                        "Definir Prioridade", JOptionPane.QUESTION_MESSAGE);
                if (priorityStr != null && !priorityStr.trim().isEmpty()) {
                    int priority = Integer.parseInt(priorityStr.trim());
                    patient.setPriority(priority);
                    emergencyQueue.addPatient(patient);
                    savePatientToFile(emergencyFilePath, patient);
                }
            }
        }
        triageQueue.clearQueue();
        saveQueueToFile(triageFilePath, triageQueue.getAllPatients());
        saveQueueToFile(emergencyFilePath, emergencyQueue.getAllPatients());
    }

    private void saveQueueToFile(String filePath, Patient[] patients) {
        try (FileWriter fw = new FileWriter(filePath, false)) {
            for (Patient patient : patients) {
                if (patient != null) {
                    fw.write(patient.getSenha() + "," + patient.getClassification() + "," + patient.getPriority() + "\n");
                }
            }
            fw.flush();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public void attendPatients() {
        while (!emergencyQueue.isEmpty()) {
            Patient nextPatient = emergencyQueue.getNextPatient();
            System.out.println("Atendendo paciente: " +
                "Senha (" + nextPatient.getSenha() + ") " +
                "(Classificação: " + (nextPatient.getClassification() == 1 ? "Preferencial" : "Comum") + ") " +
                "(Prioridade: " + nextPatient.getPriority() + ")");
        }
    }

    public void showGUI() {
        JFrame frame = new JFrame("Hospital Emergency System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1));

        JButton commonButton = new JButton("Retirar Senha Comum");
        commonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatientToTriage(0);
                JOptionPane.showMessageDialog(frame, "Senha comum retirada com sucesso!");
            }
        });

        JButton preferentialButton = new JButton("Retirar Senha Preferencial");
        preferentialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatientToTriage(1);
                JOptionPane.showMessageDialog(frame, "Senha preferencial retirada com sucesso!");
            }
        });

        JButton transferButton = new JButton("Transferir para Atendimento");
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transferPatientsToEmergency();
                JOptionPane.showMessageDialog(frame, "Pacientes transferidos para atendimento!");
            }
        });

        JButton viewTriageButton = new JButton("Visualizar Triagem");
        viewTriageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String triageData = triageQueue.toString();
                JOptionPane.showMessageDialog(frame, triageData.isEmpty() ? "Nenhum paciente em triagem." : triageData, "Pacientes em Triagem", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton viewEmergencyButton = new JButton("Visualizar Atendimento");
        viewEmergencyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emergencyData = emergencyQueue.toString();
                JOptionPane.showMessageDialog(frame, emergencyData.isEmpty() ? "Nenhum paciente em atendimento." : emergencyData, "Pacientes em Atendimento", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton deleteTriageButton = new JButton("Excluir Dados de Triagem");
        deleteTriageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String senhaStr = JOptionPane.showInputDialog(frame, "Informe a senha do paciente a ser excluído da triagem:", "Excluir Paciente de Triagem", JOptionPane.QUESTION_MESSAGE);
                if (senhaStr != null && !senhaStr.trim().isEmpty()) {
                    int senha = Integer.parseInt(senhaStr.trim());
                    triageQueue.removePatientBySenha(senha);
                    saveQueueToFile(triageFilePath, triageQueue.getAllPatients());
                    JOptionPane.showMessageDialog(frame, "Paciente excluído da triagem!");
                }
            }
        });

        JButton deleteEmergencyButton = new JButton("Excluir Dados de Atendimento");
        deleteEmergencyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String senhaStr = JOptionPane.showInputDialog(frame, "Informe a senha do paciente a ser excluído do atendimento:", "Excluir Paciente de Atendimento", JOptionPane.QUESTION_MESSAGE);
                if (senhaStr != null && !senhaStr.trim().isEmpty()) {
                    int senha = Integer.parseInt(senhaStr.trim());
                    emergencyQueue.removePatientBySenha(senha);
                    saveQueueToFile(emergencyFilePath, emergencyQueue.getAllPatients());
                    JOptionPane.showMessageDialog(frame, "Paciente excluído do atendimento!");
                }
            }
        });

        panel.add(commonButton);
        panel.add(preferentialButton);
        panel.add(transferButton);
        panel.add(viewTriageButton);
        panel.add(viewEmergencyButton);
        panel.add(deleteTriageButton);
        panel.add(deleteEmergencyButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private void loadPatientsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(triageFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int senha = Integer.parseInt(parts[0]);
                int classification = Integer.parseInt(parts[1]);
                int priority = Integer.parseInt(parts[2]);
                Patient patient = new Patient(classification, priority, senha);
                triageQueue.addPatient(patient);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de triagem: " + e.getMessage());
        }

        try (BufferedReader br = new BufferedReader(new FileReader(emergencyFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int senha = Integer.parseInt(parts[0]);
                int classification = Integer.parseInt(parts[1]);
                int priority = Integer.parseInt(parts[2]);
                Patient patient = new Patient(classification, priority, senha);
                emergencyQueue.addPatient(patient);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de atendimento: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        HospitalEmergencySystem hospitalSystem = new HospitalEmergencySystem();
        hospitalSystem.showGUI();
    }
}