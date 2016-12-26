/*
	Medidor de batimentos cardiacos 
        Rodrigo Feliciano - http://www.pakequis.com.br
	
	Com o código original: 
        Pulse Sensor Amped 1.4  by Joel Murphy and Yury Gitman   http://www.pulsesensor.com
	https://github.com/WorldFamousElectronics/PulseSensor_Amped_Arduino/blob/master/README.md  
*/

#include <LiquidCrystal.h>

//  Variables
int pulsePin = 1;                 // Pulse Sensor purple wire connected to analog pin 1

// Volatile Variables, used in the interrupt service routine!
volatile int BPM;                   // int that holds raw Analog in 0. updated every 2mS
volatile int Signal;                // holds the incoming raw data
volatile int IBI = 600;             // int that holds the time interval between beats! Must be seeded! 
volatile boolean Pulse = false;     // "True" when User's live heartbeat is detected. "False" when not a "live beat". 
volatile boolean QS = false;        // becomes true when Arduoino finds a beat.

//Para o shield de LCD e teclado
// select the pins used on the LCD panel
LiquidCrystal lcd(8, 9, 4, 5, 6, 7);

// define some values used by the panel and buttons
int lcd_key     = 0;
int adc_key_in  = 0;
#define btnRIGHT  0
#define btnUP     1
#define btnDOWN   2
#define btnLEFT   3
#define btnSELECT 4
#define btnNONE   5

void setup()
{
	//Inicia o LCD
	lcd.begin(16, 2); 
	lcd.setCursor(0,0);

	interruptSetup();                 // sets up to read Pulse Sensor signal every 2mS 
	
	lcd.setCursor(0,0);	      
	lcd.print(" Pulso = ");
}

//  Where the Magic Happens
void loop()
{
	if (QS == true)		// A Heartbeat Was Found
	{     
		lcd.setCursor(9,0);
		lcd.print(BPM);	//Mostra mensagem
                lcd.print(" BPM ");
		QS = false;     // reset the Quantified Self flag for next time    
    }
 
    delay(20);                             //  take a break
}

// Talvez eu use no futuro...
// read the buttons
int read_LCD_buttons()
{
	adc_key_in = analogRead(0);      // read the value from the sensor 

	if (adc_key_in > 1000) return btnNONE; // We make this the 1st option for speed reasons since it will be the most likely result
	// For V1.1 us this threshold
	//if (adc_key_in < 50)   return btnRIGHT;  
	//if (adc_key_in < 250)  return btnUP; 
	//if (adc_key_in < 450)  return btnDOWN; 
	//if (adc_key_in < 650)  return btnLEFT; 
	//if (adc_key_in < 850)  return btnSELECT;  

	//Meu Shield eh versao 1.0
	if (adc_key_in < 50)   return btnRIGHT;  
	if (adc_key_in < 195)  return btnUP; 
	if (adc_key_in < 380)  return btnDOWN; 
	if (adc_key_in < 555)  return btnLEFT; 
	if (adc_key_in < 790)  return btnSELECT;   

	return btnNONE;  // when all others fail, return this...
}
