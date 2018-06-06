import jssc.*;

public class SRTT{

	public static void main(String[] args) {
		try{
			String[] list = SerialPortList.getPortNames();
			for (String s: list) {
				// System.out.println(s);
				// String abc = "Q@n";
				// byte[] biting = abc.getBytes(); 
				// for (byte b: biting) {
				// 	System.out.println(b);
				// }

				SerialPort serialPort = new SerialPort(s);
				serialPort.openPort();
				serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				// System.out.println(serialPort.purgePort(SerialPort.PURGE_RXCLEAR));
				// System.out.println(serialPort.purgePort(SerialPort.PURGE_TXCLEAR));
				
				// System.out.println(serialPort.purgePort(SerialPort.PURGE_TXABORT));
				// System.out.println(serialPort.purgePort(SerialPort.PURGE_TXABORT));

				// byte[] a = {81,64,110};
				// byte[] b = {69,72,114};

				// serialPort.writeBytes(a);
				//serialPort.writeBytes(b);
				
				serialPort.writeString("Q@n");//remote control
				//serialPort.writeString("EBx");//arm
				serialPort.writeString("EHr");//stim
				

				//System.out.println(serialPort.writeString("Q@n")); //remote control
				//System.out.println(serialPort.writeString("EBx")); //arm
				//System.out.println(serialPort.writeString("EHr")); //stim
				System.out.println(serialPort.closePort());
				
			}

		} catch (SerialPortException e) {
			System.out.println(e.getPortName() + " " + e.getMethodName() + " " + e.getExceptionType());
		} catch (Exception e) {
			System.out.println(e);
		}
		//System.out.println("Hello World");
		
	}
}
