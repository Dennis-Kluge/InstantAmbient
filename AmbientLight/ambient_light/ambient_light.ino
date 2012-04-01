int single_led = 2;      
int red = 9;
int green = 10;
int blue = 11;

int value_leds[] = {0, 0, 0, 0}; // single, r, g, b

byte buffer[]  = {0, 0};
int counter = 0;


int incomingByte;


void setup() {
  Serial.begin(9600);
  
  pinMode(single_led, OUTPUT);   
  pinMode(red, OUTPUT);   
  pinMode(green, OUTPUT);   
  pinMode(blue, OUTPUT);   
}

int set_brightness(byte value) {
  switch(value) {
    case 48:
      return 0;
      break;
      
    case 49:
      return 1;
      break;
    
    case 50:
      return 2;
      break;
    
    case 51:
      return 3;
      break;
      
    case 52:
      return 4;
      break;
      
    case 53:
      return 5;
      break;
      
    case 54:
      return 6;
      break;
      
    case 55:
      return 7;
      break;
      
    case 56:
      return 8;
      break;
      
    case 57:
      return 9;
      break;
  } 
}

int set_led() {
  switch(buffer[0]) {
    case 48:      
      value_leds[0] = set_brightness(buffer[1]);
      break;
    case 49:
      value_leds[1] = set_brightness(buffer[1]);
      break;
    case 50:
      value_leds[2] = set_brightness(buffer[1]);
      break;
    case 51:
      value_leds[3] = set_brightness(buffer[1]);
      break;
  }  
}

int iterator_to_led(int iterator) {
 switch(iterator) {
  case 1:
    return 9;
    break;
   
  case 2:
    return 10;
   
  case 3: 
     return 11; 
 }
  
}

void loop() {
  
  if (Serial.available() > 0) {
    // read the incoming byte:
    incomingByte = Serial.read();
    if(counter < 1) {      
      buffer[counter] = incomingByte;
      counter++;
    } else {
      buffer[counter] = incomingByte;

      // finished reading
      set_led();
      Serial.println("\n");
      for(int i = 0; i < 4; i++) {
        Serial.println(value_leds[i]);  
      }
      counter = 0; 
    }      
      
  }

  
  for(int i = 0; i < 4; i++) {
    if(i == 0) {

       if(value_leds[i] > 0) {
         digitalWrite(single_led, HIGH);
       } else {
         digitalWrite(single_led, LOW);
       }
    } else {
        //Serial.println(i);
       if(value_leds[i] > 0) {
         analogWrite(iterator_to_led(i), value_leds[i] * 20);
       } else {

         analogWrite(iterator_to_led(i), 0);
       }
    }   
     
  }
  
}
