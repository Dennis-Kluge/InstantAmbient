package android.ambientclient.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;

public class BluetoothConnectedThread extends Thread {
    private static final String MESSAGE_READ = null;
	private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    public BluetoothConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final.
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        System.out.println("Ausgabe: bluetoothconnectedthread 2");

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                // Send the obtained bytes to the UI Activity.
//                mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
//                        .sendToTarget();

                String bufferString = new String(buffer);
                System.out.println("Ausgabe: buffer: " + bufferString);

            } catch (IOException e) {
                break;
            }
        }
    }

    /* Call this from the main Activity to send data to the remote device. */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);

            mmOutStream.flush();

        } catch (IOException e) { }
    }

    /* Call this from the main Activity to shutdown the connection. */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}