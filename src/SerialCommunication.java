import com.fazecast.jSerialComm.*;
import java.util.*;

public class SerialCommunication {
    private static SerialPort serialPort;

    private static StringBuilder concatenatedString = new StringBuilder();

    public static StringBuilder getConcatenatedString() {
        if (concatenatedString != null) {
            String data = concatenatedString.toString().replaceAll("\\r\\n|\\r|\\n", "");
            return new StringBuilder(data);
        } else {
            return new StringBuilder();
        }
    }
    static String contraseña = "";

    private static void enviarDato(int dato) {
        String mensaje = Integer.toString(dato);
        byte[] buffer = mensaje.getBytes();
        serialPort.writeBytes(buffer, buffer.length);
        System.out.println("Dato enviado: " + dato);
    }

    public static void main(String[] args) {
        // Obtén una instancia de la librería jSerialComm
            serialPort = SerialPort.getCommPort("COM3"); // Reemplaza "COM3" con el puerto serial correcto

        // Configura los parámetros de la conexión serial (baud rate, bits de datos, paridad, etc.)
            serialPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);

            // Abre el puerto serial
            if (serialPort.openPort()) {
                System.out.println("Puerto serial abierto correctamente.");

                // Variable para almacenar el string concatenado

                // Crea un hilo para recibir datos continuamente
                Thread thread = new Thread(() -> {
                    while (serialPort.isOpen()) {
                        // Verifica si hay datos disponibles para leer
                        if (serialPort.bytesAvailable() > 0) {
                            // Lee los datos del puerto serial
                            byte[] buffer = new byte[serialPort.bytesAvailable()];
                            int numRead = serialPort.readBytes(buffer, buffer.length);

                            // Convierte los datos leídos en una cadena de texto
                            String data = new String(buffer, 0, numRead);

                            // Concatena el string recibido al string acumulado
                            concatenatedString.append(data);

                            //contraseña
                            contraseña = String.valueOf(getConcatenatedString());
                            System.out.println(contraseña);
                            //

                            //huffman
                            Map<Character, String> huffmanCodes = HuffmanEncoder.compress(contraseña);
                            System.out.println("Huffman Codes:");
                            for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
                                System.out.println(entry.getKey() + ": " + entry.getValue());
                            }
                            //
                        }
                    }
                });

                // Inicia el hilo de recepción de datos
                thread.start();

                // Espera a que el usuario decida terminar el programa (opcional)
                try {
                    System.in.read();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Detiene el hilo de recepción de datos
                thread.interrupt();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Puerto serial cerrado.");
            } else {
                System.out.println("No se pudo abrir el puerto serial.");
            }
    }
}
