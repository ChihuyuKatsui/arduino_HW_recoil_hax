import java.util.logging.LogManager;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import com.fazecast.jSerialComm.SerialPort;


public class Main implements NativeMouseInputListener {
	public void nativeMouseClicked(NativeMouseEvent e) {
		System.out.println("Mouse Clicked: " + e.getClickCount());
	}

	public void nativeMousePressed(NativeMouseEvent e) {
		sendByte((byte) 97);
		System.out.println("Mouse Clicked: " + e.getClickCount());
	}

	public void nativeMouseReleased(NativeMouseEvent e) {
		System.out.println("Mouse Released: " + e.getButton());
	}

	public void nativeMouseMoved(NativeMouseEvent e) {
		System.out.println("Mouse Moved: " + e.getX() + ", " + e.getY());
	}

	public void nativeMouseDragged(NativeMouseEvent e) {
		System.out.println("Mouse Dragged: " + e.getX() + ", " + e.getY());
	}

	SerialPort serialPort;
	boolean portFound = false;

	void sendByte(byte b) {
		byte[] buff = new byte[1];
		buff[0] = b;
		serialPort.writeBytes(buff, 1);
	}

	  void startSerial() {

		    if (!portFound || serialPort != null) {
		      return;
		    }

		    // ポート名
		    String portName = "COM6";

		    for (SerialPort sp : SerialPort.getCommPorts()) {
		      if (sp.getSystemPortName().equals(portName)) {
		        serialPort = sp;
		        break;
		      }
		    }
		    // ボーレート
		    serialPort.setBaudRate(9600);
		    serialPort.openPort();
	  }



	public static void main(String[] args) {
		try {
			GlobalScreen.registerNativeHook();
			LogManager.getLogManager().reset();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}

		// Construct the example object.
		Main example = new Main();

		// Add the appropriate listeners.
		GlobalScreen.addNativeMouseListener(example);
//		GlobalScreen.addNativeMouseMotionListener(example);

	}
}