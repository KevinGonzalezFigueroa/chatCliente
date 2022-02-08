package com.cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        Scanner sc = new Scanner(System.in);

        Socket socket = new Socket("127.0.0.1", 8080);

        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        String historialDeMensajes = "";

        System.out.println("¿Cuál es su nombre de usuario?");
        String nombreUsuario = sc.nextLine();
        oos.writeObject(nombreUsuario);

        try {
            String returnValue = (String) ois.readObject();
            System.out.print(returnValue);

            historialDeMensajes = (String) ois.readObject();
            System.out.println(historialDeMensajes);

            int posicionMensajes = (int) ois.readObject();

            boolean finish = false;
            while (!finish) {
                System.out.println("¿Que desea del servidor?");
                String mensaje = sc.nextLine();
                oos.writeObject(mensaje);

                returnValue = (String) ois.readObject();
                System.out.println(returnValue);
                if (returnValue.equals("Goodbye")) {
                    finish = true;
                }else if(returnValue.equals("\n Historial de mensajes")){
                    oos.writeObject(posicionMensajes);
                    String nuevosMensajes = (String) ois.readObject();
                    posicionMensajes = (int) ois.readObject();
                    System.out.print(historialDeMensajes + "\n Nuevos mensajes \n" + nuevosMensajes);
                    historialDeMensajes += nuevosMensajes;
                }else if(returnValue.equals("Escriba su mensaje")){
                    String opcion = sc.nextLine();
                    oos.writeObject(opcion);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (oos != null)
                oos.close();
            if (ois != null)
                oos.close();
            if (socket != null)
                socket.close();
            System.out.println("Conexión cerrada.");
        }
    }
}
