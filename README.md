# EMV_PayWave_Reader
Android project to read an EMV PayWave card using emvtools (https://sourceforge.net/projects/emvtools/).

Assumptions:
1.	All devices this will run on must have NFC support. The application will not start if the phone does not.
2.	A physical device is required. Emulators will crash on run due to being unable to get an instance of NfcAdapter without hardware. 
3. 	I presume all PayWave cards use NfcA and IsoDep technology as the three cards I tested on did.

Ian Babington
	
