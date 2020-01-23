import java.util.logging.LogManager;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

public class Main extends Thread implements NativeMouseInputListener, SerialPortMessageListener{


	static boolean RMB;
	static boolean LMB;
	static boolean EMB;
	static boolean flag = false;
	static byte[] B0 = { 100 };
	static byte[] B1 = { 111 };
	static byte[] newData= {1};

	@Override
	public void nativeMouseClicked(NativeMouseEvent e) {

	}

	@Override
	public void nativeMousePressed(NativeMouseEvent e) {
		if (e.getButton() == 1) {
			LMB = true;
		} else if (e.getButton() == 2) {
			RMB = true;
		}
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
		if (e.getButton() == 1) {
			LMB = false;
		} else if (e.getButton() == 2) {
			RMB = false;
		}
	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent e) {

	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent e) {

	}


	@Override
	public int getListeningEvents() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public boolean delimiterIndicatesEndOfMessage() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public byte[] getMessageDelimiter() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}



/////////----------↓↓ためし↓↓-------------/////////
////	SerialPort serialPort;
////	boolean portFound = false;
//
////	void sendByte(byte b) {
////		byte[] buff = new byte[0];
////		buff[0] = b;
////		serialPort.writeBytes(buff, 1);
////	}
//
////	void startSerial() {
////		String portName = "COM6";
////		for (SerialPort sp : SerialPort.getCommPorts()) {
////			if (sp.getSystemPortName().equals(portName)) {
////				serialPort = sp;
////				break;
////			}
////		}
////		serialPort.openPort();
////	}
/////////--------------ためし-------------/////////


	public static void main(String[] args) {
		SerialPort comPort = SerialPort.getCommPorts()[1];
		comPort.openPort();
		System.out.println(comPort.getSystemPortName());
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);

		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			System.exit(1);
		}

		// Construct the example object.
		Main main = new Main() {
			@Override
			public void run() {
				System.out.println(flag);
				while (true) {
					//while(newData[0]==-1){}
					System.out.print("");
					if (RMB && LMB&&!flag) {
						comPort.writeBytes(B0, 1);
						flag = true;
						System.out.println(flag);
					}else if (!LMB &&flag) {
						comPort.writeBytes(B1, 1);
						flag = false;
						System.out.println(flag);
					}else if (!RMB &&flag) {
						comPort.writeBytes(B1, 1);
						flag = false;
						System.out.println(flag);
					}
				}
			}
		};
		LogManager.getLogManager().reset();
		main.start();

		// Add the appropriate listeners.
		GlobalScreen.addNativeMouseListener(main);

		//GlobalScreen.addNativeMouseMotionListener(example);
//		comPort.addDataListener(new SerialPortDataListener() {
//			@Override
//			public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }
//			@Override
//			public void serialEvent(SerialPortEvent event){
//				newData = event.getReceivedData();
//				System.out.println("Received data of size: " + newData.length);
//				System.out.print(newData[0]);
//				if(newData[0]==-1) {
////					try {
////						Thread.sleep(50);
////					} catch (InterruptedException e) {
////						e.printStackTrace();
////					}
//					comPort.closePort();
//					try {
//						Thread.sleep(11000);
//					} catch (InterruptedException e) {
//					e.printStackTrace();
//					}
//					System.out.print(SerialPort.getCommPorts()[1]);
//					comPort.openPort();
//				}
//			}
//		});



	}


}