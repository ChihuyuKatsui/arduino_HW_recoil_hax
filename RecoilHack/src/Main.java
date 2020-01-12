import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import com.fazecast.jSerialComm.SerialPort;

public class Main implements NativeMouseInputListener {

	static boolean RMB;
	static boolean LMB;
	static boolean EMB;
public void nativeMouseClicked(NativeMouseEvent e) {
System.out.println("Mouse Clicked: " + e.getClickCount());
}

public void nativeMousePressed(NativeMouseEvent e) {
	if(e.getButton()==1) {
		LMB=true;
	}else {
		RMB=true;
	}
	if(LMB&&RMB)System.out.println("かかかかっかか");
}

public void nativeMouseReleased(NativeMouseEvent e) {
	if(e.getButton()==1) {
		LMB=false;
	}else {
		RMB=false;
	}
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

		    // ポート名
		    String portName = "COM6";

		    for (SerialPort sp : SerialPort.getCommPorts()) {
		      if (sp.getSystemPortName().equals(portName)) {
		        serialPort = sp;
		        break;
		      }
		    }
		    // ボーレート
		    //setComPortParameters​(9600, int newDataBits, int newStopBits, int newParity);
		    serialPort.openPort();
	  }






	public static void main(String[] args) {
		SerialPort comPort = SerialPort.getCommPorts()[1];
		comPort.openPort();
		System.out.println(comPort.getSystemPortName());
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
		      byte[] B0 = {100};
		      comPort.writeBytes(B0,1);
		      System.out.println(B0[0]);
		      byte[] B1 = {111};
		      comPort.writeBytes(B1,1);
		      System.out.println(B1[0]);

		try {
			GlobalScreen.registerNativeHook();
		}catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			System.exit(1);
		}

			// Construct the example object.
			System.out.println("aaaa");
			Main example = new Main();
			System.out.println("aaaa");
			// Add the appropriate listeners.
			GlobalScreen.addNativeMouseListener(example);
			System.out.println("bbbb");
			GlobalScreen.addNativeMouseMotionListener(example);
			System.out.println("cccc");

	}
}