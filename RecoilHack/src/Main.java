import java.util.logging.LogManager;
import com.fazecast.jSerialComm.*
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

public class Main implements NativeMouseInputListener {
	public void nativeMouseClicked(NativeMouseEvent e) {
		System.out.println("Mouse Clicked: " + e.getClickCount());
	}

	public void nativeMousePressed(NativeMouseEvent e) {
		sendByte((byte) 97);
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