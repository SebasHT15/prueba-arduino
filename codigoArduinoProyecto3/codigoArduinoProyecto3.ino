int pinBotonPunto = 2;
int pinBotonLinea = 3;
const int buzzerPin = 4;
int led1 = 5;
int led2 = 6;


int estadoBotonPunto = 0;
int estadoBotonLinea = 0;

// Variable para almacenar la palabra en código Morse
String palabraMorse = "";

// Tiempo de espera en milisegundos para considerar que se ha completado una pulsación
const unsigned long tiempoEspera = 500;

// Tabla de traducción de código Morse a caracteres
const char* morseTable[36][2] = {
  {".-", "A"},
  {"-...", "B"},
  {"-.-.", "C"},
  {"-..", "D"},
  {".", "E"},
  {"..-.", "F"},
  {"--.", "G"},
  {"....", "H"},
  {"..", "I"},
  {".---", "J"},
  {"-.-", "K"},
  {".-..", "L"},
  {"--", "M"},
  {"-.", "N"},
  {"---", "O"},
  {".--.", "P"},
  {"--.-", "Q"},
  {".-.", "R"},
  {"...", "S"},
  {"-", "T"},
  {"..-", "U"},
  {"...-", "V"},
  {".--", "W"},
  {"-..-", "X"},
  {"-.--", "Y"},
  {"--..", "Z"},
  {"-----", "0"},
  {".----", "1"},
  {"..---", "2"},
  {"...--", "3"},
  {"....-", "4"},
  {".....", "5"},
  {"-....", "6"},
  {"--...", "7"},
  {"---..", "8"},
  {"----.", "9"},
};

// Variables para controlar el tiempo
unsigned long tiempoUltimaPulsacion = 0;
boolean esperandoFinPulsacion = false;

void setup() {
  pinMode(pinBotonPunto, INPUT);
  pinMode(pinBotonLinea, INPUT);
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);
  pinMode(buzzerPin, OUTPUT);
  Serial.begin(9600);
}

void loop() {
  escribirMorse();
  if (Serial.available() > 0) {
    int valor = Serial.parseInt();  // Leer el número enviado desde Java

    // Realizar acciones según el valor recibido
    switch (valor) {
      case 1:
        emitirSonido();
        break;
      case 2:
        encenderLed1();
        break;
      case 3:
        encenderLed2();
        break;
      default:
        // Valor inválido, no se realiza ninguna acción
        break;
    }
  }
}

void escribirMorse(){
  // Verificar si se ha presionado el botón del punto
  estadoBotonPunto = digitalRead(pinBotonPunto);
  if (estadoBotonPunto == HIGH) {
    palabraMorse += ".";
    esperandoFinPulsacion = true;
    tiempoUltimaPulsacion = millis();
    delay(200); 
  }

  // Verificar si se ha presionado el botón de la línea
  estadoBotonLinea = digitalRead(pinBotonLinea);
  if (estadoBotonLinea == HIGH) {
    palabraMorse += "-";
    esperandoFinPulsacion = true;
    tiempoUltimaPulsacion = millis();
    delay(200); 
  }

  // Verificar si se ha completado una pulsación
  if (esperandoFinPulsacion && (millis() - tiempoUltimaPulsacion) > tiempoEspera) {
    if (palabraMorse.length() > 0) {
      String textoTraducido = traducirMorse(palabraMorse);
      Serial.println(textoTraducido);
      palabraMorse = "";
    }
    esperandoFinPulsacion = false;
  }
}

//Funcion que me traduce la palabra
String traducirMorse(String morse) {
  int totalCaracteres = sizeof(morseTable) / sizeof(morseTable[0]);

  for (int i = 0; i < totalCaracteres; i++) {
    if (morse == morseTable[i][0]) {
      return morseTable[i][1];
    }
  }

  return ""; // Carácter no encontrado en la tabla
}

void emitirSonido() {
  // Generar un sonido con el buzzer
  tone(buzzerPin, 1000, 500);  // Frecuencia de 1000 Hz durante 500 ms
}

void encenderLed1() {
  // Encender el LED 1
  digitalWrite(led1, HIGH);
  delay(3000);
  digitalWrite(led1, LOW);
  
}

void encenderLed2() {
  // Encender el LED 2
  digitalWrite(led2, HIGH);
  delay(3000);
  digitalWrite(led2, LOW);
  
}